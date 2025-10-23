package bk.scholar.app.service;

import bk.scholar.app.config.Utility;
import bk.scholar.app.dto.GEvent;
import bk.scholar.app.dto.GFileFolder;
import bk.scholar.app.dto.GMessage;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GSuiteService {
  private final Drive gDrive;
  private final Calendar gCalendar;
  private final String calendarId = "primary"; // or a specific calendar ID

  public GSuiteService(Drive gDrive, Calendar gCalendar) {
    this.gDrive = gDrive;
    this.gCalendar = gCalendar;
  }

  private Event getEvent(String eventId) throws IOException {
    return gCalendar.events().get(calendarId, eventId).execute();
  }

  // @TOOL 1
  public List<GEvent> getCalendarEvents() throws Exception {
    List<GEvent> gEvents = new ArrayList<>();
    Events events = gCalendar.events().list(calendarId).execute();
    if(events.getItems() != null && !events.getItems().isEmpty()) {
      gEvents = events.getItems().stream()
          .map(it -> new GEvent(it.getId(), it.getSummary(), it.getEtag())).toList();
    }
    return gEvents;
  }

  // @TOOL 2
  public GEvent createCalendarEvent(String dateString, String summary) throws IOException {
    var timeMap = Utility.createEventTime(dateString);
    Event event = new Event()
        .setSummary(summary)
        .setLocation("Dhaka/Bangladesh");
    event.setStart(timeMap.get("start"));
    event.setEnd(timeMap.get("end"));

    // Insert the event into the primary calendar
    Event createdEvent = gCalendar.events()
        .insert(calendarId, event)
        .execute();
    //System.out.println("Event created: " + createdEvent.getHtmlLink());
    return new GEvent(createdEvent.getId(), createdEvent.getSummary(), createdEvent.getEtag());
  }

  //@TOOL 3
  public GMessage deleteCalendarEvent(String eventId) {
    GMessage message = new GMessage(eventId, "", "Event Deleted");
    try {
      gCalendar.events().delete(calendarId, eventId).execute();
      //System.out.println("Event deleted successfully.");
    } catch (IOException e) {
      //System.err.println("Error deleting event: " + e.getMessage());
      throw new  RuntimeException("Error deleting event: " + e.getMessage());
    }
    return message;
  }

  // @TOOL 4
  public GMessage addCalendarUser(String eventId, String email) throws Exception {
    GMessage message = new GMessage(eventId, email, "This user already in the Event");
    Event event = this.getEvent(eventId);
    List<EventAttendee> attendees = event.getAttendees() != null ? event.getAttendees() : new ArrayList<>();
    if (attendees.stream().noneMatch(attendee -> attendee.getEmail().equalsIgnoreCase(email))) {
      attendees.add(new EventAttendee().setEmail(email));
      event.setAttendees(attendees);
      gCalendar.events().update(calendarId, eventId, event).execute();
      message = message.withComment("User just added in the Event");
    }
    return message;
  }

  // @TOOL 5
  public GMessage removeCalendarUser(String eventId, String email) throws Exception {
    GMessage message = new GMessage(eventId, email, "This user not in this Event");
    Event event = this.getEvent(eventId);
    List<EventAttendee> attendees = event.getAttendees();
    if (attendees != null && attendees.removeIf(attendee -> attendee.getEmail().equalsIgnoreCase(email))) {
      event.setAttendees(attendees);
      gCalendar.events().update(calendarId, eventId, event).execute();
      message = message.withComment("User just removed from the Event");
    }
    return message;
  }

  // NOT USED YET
  public boolean getCalendarUser(String eventId, String email) throws Exception {
    Event event = this.getEvent(eventId);
    if(event.getAttendees() != null && !event.getAttendees().isEmpty()) {
      return event.getAttendees().stream().anyMatch(attendee -> attendee.getEmail().equalsIgnoreCase(email));
    }
    return false;
  }

  // FOLDERS STARTED
  public void listFolders() throws IOException {
    String query = "mimeType = 'application/vnd.google-apps.folder'";

    FileList result = gDrive.files().list()
        .setQ(query)
        .setFields("files(id, name)")
        .execute();

    for (com.google.api.services.drive.model.File file : result.getFiles()) {
      System.out.printf("Folder Name(%s), ID(%s)%n", file.getName(), file.getId());
    }
  }

  // D1 - tools
  public List<GFileFolder> getFoldersById(String folderId) throws Exception {
    final List<GFileFolder> list = new ArrayList<>();
    //String query = "'" + folderId + "' in parents and mimeType='application/vnd.google-apps.folder'";
    String query = "'" + folderId + "' in parents and trashed = false";
    FileList result = gDrive.files().list()
        .setQ(query)
        //.setFields("files(id, name)")
        .setFields("files(id, name, mimeType, parents)")
        .execute();
    for (com.google.api.services.drive.model.File file : result.getFiles()) {
      list.add(new GFileFolder(file.getId(), file.getName(), file.getMimeType(), file.getParents().size()+""));
    }
    return list;
  }

  // D2 -- tools
  public GMessage createFolder(String parentFolderId, String folderName) throws IOException {
    File folderMetadata = new File();
    folderMetadata.setName(folderName);
    folderMetadata.setMimeType("application/vnd.google-apps.folder");
    folderMetadata.setParents(java.util.Collections.singletonList(parentFolderId));

    File createdFolder = gDrive.files().create(folderMetadata)
        .setFields("id, name")
        .execute();
    System.out.println("Created folder: " + createdFolder.getName() + " (ID: " + createdFolder.getId() + ")");
    return new GMessage(createdFolder.getId(), createdFolder.getName(), "Folder created");
  }
  // D3 -- tools
  public GMessage createFile(String parentFolderId, String fileName, String fileContent) throws IOException {
    ByteArrayContent content = ByteArrayContent.fromString("text/plain", fileContent);
    File fileMetadata = new File();
    fileMetadata.setName(fileName);
    fileMetadata.setParents(java.util.Collections.singletonList(parentFolderId));

    File createdFile = gDrive.files().create(fileMetadata, content)
        .setFields("id, name")
        .execute();
    System.out.println("Created file: " + createdFile.getName() + " (ID: " + createdFile.getId() + ")");
    return new GMessage(createdFile.getId(), createdFile.getName(), "File created");
  }

  // D4 -- tools
  public GMessage deleteItemByName(String parentId, String itemName) throws IOException {
    // Build query: item must be in the parent AND have the exact name AND not trashed
    String query = String.format(
        "'%s' in parents and name = '%s' and trashed = false",
        parentId,
        itemName.replace("'", "\\'") // Escape single quotes in name
    );

    FileList result = gDrive.files().list()
        .setQ(query)
        .setFields("files(id, name, mimeType)")
        .execute();

    List<File> files = result.getFiles();

    if (files.isEmpty()) {
      return new GMessage("", "No item named " + itemName + " found in parent folder", "File NOT FOUND");
    }

    if (files.size() > 1) {
      System.out.println("Warning: Multiple items found with name '" + itemName + "'. Deleting the first one.");
    }

    String fileId = files.get(0).getId();
    gDrive.files().delete(fileId).execute();
    System.out.println("Deleted: '" + itemName + "' (ID: " + fileId + ")");
    return new GMessage(fileId, "File name " + itemName , "File Deleted");
  }
}
