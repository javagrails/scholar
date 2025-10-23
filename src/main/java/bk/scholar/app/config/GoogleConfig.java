package bk.scholar.app.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.drive.Drive;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GoogleConfig {

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String APPLICATION_NAME = "gtools";
  private static final List<String> SCOPES = List.of(
      com.google.api.services.calendar.CalendarScopes.CALENDAR,
      com.google.api.services.drive.DriveScopes.DRIVE);
  public static final String CREDENTIALS_PATHNAME = "credentialsPathname";
  public static final String TOKEN_PATHNAME = "tokenPathname";
  private final NetHttpTransport httpTransport;
  private final Properties properties;
  private final Credential credential;
  private final Calendar calendarService;
  private final Drive driveService;

  public GoogleConfig() {
    try {
      this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      this.properties = Utility.loadProperties(new File("googletools.txt"));
      this.credential = this.getCredentials();
      this.calendarService = this.buildCalendarService();
      this.driveService = this.buildDriveService();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @return com.google.api.client.auth.oauth2.Credential credentialObject
   * @throws Exception
   */
  private Credential getCredentials() throws Exception {
    InputStream in = new FileInputStream(String.valueOf(properties.get(CREDENTIALS_PATHNAME)));
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        this.httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new File(
            String.valueOf(properties.get(TOKEN_PATHNAME)))))
        .setAccessType("offline")
        .build();

    return new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(flow,
        new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver()).authorize("user");
  }

  /**
   *
   * @return object of com.google.api.services.calendar.Calendar;
   */
  private Calendar buildCalendarService() {
    return new Calendar.Builder(this.httpTransport, JSON_FACTORY, this.credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  /**
   *
   * @return object of com.google.api.services.drive.Drive
   */
  private Drive buildDriveService() {
    return new Drive.Builder(httpTransport, JSON_FACTORY, this.credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  @Bean
  public Drive gDrive() {
    return this.driveService;
  }

  @Bean
  public Calendar gCalendar() {
    return this.calendarService;
  }
}
