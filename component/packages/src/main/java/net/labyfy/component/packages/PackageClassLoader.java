package net.labyfy.component.packages;

import com.google.common.base.Preconditions;

import java.net.URL;
import java.net.URLClassLoader;

public class PackageClassLoader  extends URLClassLoader {

    private PackageClassLoader(URL jarFile) {
        super(new URL[] {jarFile});
    }

    protected static PackageClassLoader create(URL jarFile) {
        Preconditions.checkNotNull(jarFile);
        return new PackageClassLoader(jarFile);
    }
}
