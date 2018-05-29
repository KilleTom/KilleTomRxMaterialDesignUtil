package cn.ypz.com.killetomrxmateria.rxwidget.seekbar;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class AnneUtils {

    private static final File BUILD_PROP_FILE = new File(Environment.getRootDirectory(), "build.prop");
    private static Properties sBuildProperties;
    private static final Object sBuildPropertiesLock = new Object();

    private static Properties getBuildProperties() {
        synchronized (sBuildPropertiesLock) {
            if (sBuildProperties == null) {
                sBuildProperties = new Properties();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(BUILD_PROP_FILE);
                    sBuildProperties.load(fis);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return sBuildProperties;
    }

    static boolean isMIUI() {
        return getBuildProperties().containsKey("ro.miui.ui.version.name");
    }

}