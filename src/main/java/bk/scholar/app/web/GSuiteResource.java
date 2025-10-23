package bk.scholar.app.web;

import bk.scholar.app.dto.GEvent;
import bk.scholar.app.dto.GFileFolder;
import bk.scholar.app.dto.GMessage;
import bk.scholar.app.service.GSuiteService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GSuiteResource {
  private final GSuiteService gSuiteService;

  public GSuiteResource(GSuiteService gSuiteService) {
    this.gSuiteService = gSuiteService;
  }

  @GetMapping("/events")
  public List<GEvent> getCalendarEvents() throws Exception {
    return gSuiteService.getCalendarEvents();
  }

  @GetMapping("/events/new/{dateString}/{summary}")
  public GEvent createCalendarEvent(@PathVariable String dateString,
      @PathVariable String summary) throws Exception {
    return gSuiteService.createCalendarEvent(dateString, summary);
  }

  @GetMapping("/events/delete/{eventId}")
  public GMessage deleteCalendarEvent(@PathVariable String eventId) throws Exception {
    return gSuiteService.deleteCalendarEvent(eventId);
  }

  @GetMapping("/events/{eventId}/to/{userEmail}")
  public GMessage addCalendarUser(@PathVariable String eventId,
      @PathVariable String userEmail) throws Exception {
    return gSuiteService.addCalendarUser(eventId, userEmail);
  }

  @GetMapping("/events/{eventId}/remove/{userEmail}")
  public GMessage removeCalendarUser(@PathVariable String eventId,
      @PathVariable String userEmail) throws Exception {
    return gSuiteService.removeCalendarUser(eventId, userEmail);
  }
  //gSuiteService.getCalendarUser(); // not used yet

  // FOLDERS STARTED
  @GetMapping("/drive/folders")
  public String driveFolders() throws IOException {
    gSuiteService.listFolders();
    return "GMessage";
  }

  @GetMapping("/drive/folders/{folderId}")
  public List<GFileFolder> driveFoldersById(@PathVariable String folderId) throws Exception {
    return gSuiteService.getFoldersById(folderId);
  }

  @GetMapping("/drive/folders/new/{parentFolderId}/{folderName}")
  public GMessage createFolder(@PathVariable String parentFolderId,
      @PathVariable String folderName) throws Exception {
    return gSuiteService.createFolder(parentFolderId, folderName);
  }

  @GetMapping("/drive/folders/new-file/{parentFolderId}/{fileName}/{fileContent}")
  public GMessage createFileWithContent(@PathVariable String parentFolderId,
      @PathVariable String fileName, @PathVariable String fileContent) throws Exception {
    return gSuiteService.createFile(parentFolderId,fileName,fileContent);
  }

  @GetMapping("/drive/folders/delete/{parentFolderId}/{fileName}")
  public GMessage deleteFileOrFolderByName(@PathVariable String parentFolderId,
      @PathVariable String fileName) throws Exception {
    return gSuiteService.deleteItemByName(parentFolderId,fileName);
  }
}
