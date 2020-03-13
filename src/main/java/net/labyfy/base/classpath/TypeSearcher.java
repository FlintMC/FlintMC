package net.labyfy.base.classpath;

import com.google.common.reflect.ClassPath;
import com.google.inject.Inject;
import net.labyfy.version.VersionProvider;

import java.io.IOException;
import java.util.Collection;

public class TypeSearcher {

  private final VersionProvider versionProvider;
  private final ClassPath classPath;

  @Inject
  private TypeSearcher(VersionProvider versionProvider) throws IOException {
    this.versionProvider = versionProvider;
    this.classPath = ClassPath.from(getClass().getClassLoader());
  }

  public Result filter(TypeFilter... typeFilters){
    synchronized (this.classPath){
      return null;
    }
  }

  public class Result {

    public Result filter(TypeFilter... typeFilters){
      return null;
    }
  }

}
