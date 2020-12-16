package net.flintmc.mcapi.v1_15_2.world.storage;

import com.google.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.storage.service.WorldBackupService;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = WorldBackupService.class, version = "1.15.2")
public class VersionedWorldBackupService implements WorldBackupService {

  /**
   * {@inheritDoc}
   */
  @Override
  public long createBackup(String fileName) throws IOException {
    return Minecraft.getInstance().getSaveLoader().createBackup(fileName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path getBackupFolder() {
    return Minecraft.getInstance().getSaveLoader().getBackupsFolder();
  }
}
