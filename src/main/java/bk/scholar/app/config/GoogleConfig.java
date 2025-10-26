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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class GoogleConfig {

  private static final Logger log = LoggerFactory.getLogger(GoogleConfig.class);
  private final ResourceLoader resourceLoader;
  private final PathResolver pathResolver;

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

  public GoogleConfig(ResourceLoader resourceLoader, PathResolver pathResolver) {
    try {
      this.resourceLoader = resourceLoader;
      this.pathResolver = pathResolver;
      this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      this.properties = pathResolver.loadGoogleToolsProperties();
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
    try {
      // Resolve credentials file from classpath
      String credentialsPath = properties.getProperty(CREDENTIALS_PATHNAME);
      log.info("credentialsPath : {}", credentialsPath);
      // Remove "classpath:" prefix
      Resource credentialsResource = resourceLoader.getResource("classpath:" + credentialsPath);
      log.info("credentialsResource : {}", credentialsResource.getURI().getPath());

      InputStream in = credentialsResource.getInputStream();
      GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
          new InputStreamReader(in, StandardCharsets.UTF_8));

      // Resolve token directory from classpath
      String tokenPath = properties.getProperty(TOKEN_PATHNAME);
      log.info("tokenPath : {}", tokenPath);
      File tokenDirectory = pathResolver.getTokenDirectory(tokenPath);
      log.info("tokenDirectory : {}", tokenDirectory.getAbsolutePath());

      GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
          this.httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
          .setDataStoreFactory(new FileDataStoreFactory(tokenDirectory))
          .setAccessType("offline")
          .build();

      return new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(
          flow,
          new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver()).authorize(
          "user");

    } catch (Exception e) {
      throw new RuntimeException("Failed to create Google Authorization Flow", e);
    }
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
