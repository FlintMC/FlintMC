package net.flintmc.mcapi.world.storage.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a service to backup worlds.
 */
public interface WorldBackupService {

  /**
   * Creates a backup of the world with the {@code fileName}.
   *
   * @param fileName The world's file name.
   * @return The file size of the backup.
   * @throws IOException      Thrown when an I/O error occurs.
   * @throws RuntimeException Thrown when an I/O error occurs.
   */
  long createBackup(String fileName) throws IOException;

  /**
   * Retrieves the backup folder.
   *
   * @return The backup folder.
   */
  Path getBackupFolder();

}
