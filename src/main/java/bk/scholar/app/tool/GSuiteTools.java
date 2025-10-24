package bk.scholar.app.tool;

import bk.scholar.app.config.Utility;
import bk.scholar.app.dto.CalendarEvent;
import bk.scholar.app.dto.FileItem;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.ToolResponseMessage.ToolResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class GSuiteTools {

  private static final Logger log = LoggerFactory.getLogger(GSuiteTools.class);
  private final Drive gDrive;
  private final Calendar gCalendar;
  private final String calendarId = "primary";

  public GSuiteTools(Drive gDrive, Calendar gCalendar) {
    this.gDrive = gDrive;
    this.gCalendar = gCalendar;
  }

  // --- CALENDAR TOOLS ---

  @Tool(name = "find_all_events_of_a_calendar", description = "Get all calendar events from the primary calendar")
  public List<CalendarEvent> getCalendarEvents() throws IOException {
    log.info("find_all_events_of_a_calendar | Called");

    //getCalendarEvents
    Events events = gCalendar.events().list(calendarId).execute();
    return events.getItems() == null ? Collections.emptyList() :
        events.getItems().stream()
            .map(e -> new CalendarEvent(e.getId(), e.getSummary(), e.getEtag()))
            .collect(Collectors.toList());
  }

  @Tool(name = "create_calendar_event_on_date", description = "Create a new calendar event on a given date (format: yyyy-MM-dd) with a summary/title")
  public CalendarEvent createCalendarEvent(String dateString, String summary) throws IOException {
    //createCalendarEvent
    log.info("create_calendar_event_on_date | dateString: {}, summary: {}", dateString, summary);
    var timeMap = Utility.createEventTime(dateString);
    Event event = new Event()
        .setSummary(summary)
        .setLocation("Dhaka/Bangladesh")
        .setStart(timeMap.get("start"))
        .setEnd(timeMap.get("end"));

    Event created = gCalendar.events().insert(calendarId, event).execute();
    return new CalendarEvent(created.getId(), created.getSummary(), created.getEtag());
  }

  @Tool(name = "delete_calendar_event", description = "Delete a calendar event by its event ID")
  public ToolResponse deleteCalendarEvent(String eventId) throws IOException {
    //deleteCalendarEvent
    log.info("delete_calendar_event | eventIdy: {}", eventId);
    gCalendar.events().delete(calendarId, eventId).execute();
    return new ToolResponse(eventId, "Event", "Event deleted successfully");
  }

  @Tool(name = "attendee_to_a_calendar_event", description = "Add a user (by email) as an attendee to a calendar event")
  public ToolResponse addCalendarUser(String eventId, String email) throws IOException {
    //addCalendarUser
    log.info("attendee_to_a_calendar_event | eventId: {}, email: {}", eventId, email);
    Event event = gCalendar.events().get(calendarId, eventId).execute();
    List<EventAttendee> attendees = Optional.ofNullable(event.getAttendees()).orElse(new ArrayList<>());

    boolean alreadyExists = attendees.stream()
        .anyMatch(a -> a.getEmail().equalsIgnoreCase(email));

    if (!alreadyExists) {
      attendees.add(new EventAttendee().setEmail(email));
      event.setAttendees(attendees);
      gCalendar.events().update(calendarId, eventId, event).execute();
      return new ToolResponse(eventId, email, "User added to event");
    }
    return new ToolResponse(eventId, email, "User already in event");
  }

  @Tool(name = "remove_attendee_from_a_calendar_event", description = "Remove a user (by email) from a calendar event's attendees")
  public ToolResponse removeCalendarUser(String eventId, String email) throws IOException {
    log.info("remove_attendee_from_a_calendar_event | eventId: {}, email: {}", eventId, email);
    //removeCalendarUser
    Event event = gCalendar.events().get(calendarId, eventId).execute();
    List<EventAttendee> attendees = event.getAttendees();

    if (attendees != null) {
      boolean removed = attendees.removeIf(a -> a.getEmail().equalsIgnoreCase(email));
      if (removed) {
        event.setAttendees(attendees);
        gCalendar.events().update(calendarId, eventId, event).execute();
        return new ToolResponse(eventId, email, "User removed from event");
      }
    }
    return new ToolResponse(eventId, email, "User not found in event");
  }

  // --- DRIVE TOOLS ---

  @Tool(name = "list_all_files_and_folders", description = "List all files and folders (not just folders) inside a Google Drive folder by its ID")
  public List<FileItem> getFoldersById(String folderId) throws IOException {
    log.info("list_all_files_and_folders | folderId: {}", folderId);
    //getFoldersById
    String query = "'" + folderId + "' in parents and trashed = false";
    FileList result = gDrive.files().list()
        .setQ(query)
        .setFields("files(id, name, mimeType, parents)")
        .execute();

    return result.getFiles().stream()
        .map(f -> new FileItem(
            f.getId(),
            f.getName(),
            f.getMimeType(),
            String.valueOf(f.getParents() != null ? f.getParents().size() : 0)
        ))
        .collect(Collectors.toList());
  }

  @Tool(name = "create_new_folder", description = "Create a new folder in Google Drive under a parent folder ID with a given name")
  public ToolResponse createFolder(String parentFolderId, String folderName) throws IOException {
    log.info("create_new_folder | parentFolderId: {}, folderName: {}", parentFolderId, folderName);
    //createFolder
    File metadata = new File()
        .setName(folderName)
        .setMimeType("application/vnd.google-apps.folder")
        .setParents(Collections.singletonList(parentFolderId));

    File created = gDrive.files().create(metadata).setFields("id, name").execute();
    return new ToolResponse(created.getId(), created.getName(), "Folder created");
  }

  @Tool(name = "create_new_file", description = "Create a new text file in Google Drive under a parent folder ID with given name and content")
  public ToolResponse createFile(String parentFolderId, String fileName, String fileContent) throws IOException {
    log.info("create_new_file | parentFolderId: {}, fileName: {}, fileContent: {}", parentFolderId, fileName, fileContent);
    //createFile
    ByteArrayContent content = ByteArrayContent.fromString("text/plain", fileContent);
    File metadata = new File()
        .setName(fileName)
        .setParents(Collections.singletonList(parentFolderId));

    File created = gDrive.files().create(metadata, content).setFields("id, name").execute();
    return new ToolResponse(created.getId(), created.getName(), "File created");
  }

  @Tool(name = "delete_folder_file_by_name", description = "Delete a file or folder from Google Drive by its name under a specific parent folder ID")
  public ToolResponse deleteItemByName(String parentId, String itemName) throws IOException {
    log.info("delete_folder_file_by_name | parentId: {}, itemName: {}", parentId, itemName);
    //deleteItemByName
    String safeName = itemName.replace("'", "\\'");
    String query = String.format("'%s' in parents and name = '%s' and trashed = false", parentId, safeName);

    FileList result = gDrive.files().list().setQ(query).setFields("files(id, name, mimeType)").execute();
    List<File> files = result.getFiles();

    if (files.isEmpty()) {
      return new ToolResponse("", itemName, "Item not found");
    }

    String fileId = files.get(0).getId();
    gDrive.files().delete(fileId).execute();
    return new ToolResponse(fileId, itemName, "Item deleted");
  }
}
