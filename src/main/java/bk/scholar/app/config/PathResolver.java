package bk.scholar.app.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class PathResolver {

  private static final Logger log = LoggerFactory.getLogger(GoogleConfig.class);

  private final ResourceLoader resourceLoader;

  public PathResolver(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public Properties loadGoogleToolsProperties() {
    try {
      Resource resource = resourceLoader.getResource("classpath:googletools.txt");
      Properties props = new Properties();
      props.load(resource.getInputStream());
      return props;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load googletools.txt", e);
    }
  }

  public File resolveClasspathResourceAsFile(String classpathResource) {
    try {
      // Remove "classpath:" prefix if present
      String resourcePath = classpathResource.startsWith("classpath:")
          ? classpathResource.substring(10)
          : classpathResource;

      Resource resource = resourceLoader.getResource("classpath:" + resourcePath);

      if (!resource.exists()) {
        throw new FileNotFoundException("Resource not found: " + resourcePath);
      }

      // For directories (like token store), create temp directory
      if (resourcePath.endsWith("/")) {
        return createTempDirectoryFromResource(resource, resourcePath);
      } else {
        // For files (like credentials.json), create temp file
        return createTempFileFromResource(resource, resourcePath);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to resolve resource: " + classpathResource, e);
    }
  }

  private File createTempFileFromResource(Resource resource, String resourcePath)
      throws IOException {
    String fileName = Paths.get(resourcePath).getFileName().toString();
    String prefix = fileName.substring(0, Math.min(fileName.indexOf('.'), fileName.length()));
    String suffix = fileName.substring(fileName.indexOf('.'));

    File tempFile = File.createTempFile(prefix, suffix);
    tempFile.deleteOnExit();

    try (InputStream is = resource.getInputStream()) {
      Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return tempFile;
  }

  private File createTempDirectoryFromResource(Resource resource, String resourcePath)
      throws IOException {
    // For token directory, we just need an empty directory
    // Google's FileDataStoreFactory will create files inside it
    File tempDir = Files.createTempDirectory("google-token-").toFile();
    tempDir.deleteOnExit();
    return tempDir;
  }

  public File getTokenDirectory(String filePath) {
    // Use a consistent location that works everywhere
    String userHome = System.getProperty("user.home");
    //File tokenDir = new File(userHome, "salmanrepos/mygithub/agilebd/token");
    File tokenDir = new File(userHome, filePath);
    if (!tokenDir.exists()) {
      tokenDir.mkdirs();
      log.info("There was no directory called '{}', now created", tokenDir.getAbsolutePath());
    }
    return tokenDir;
  }
}
