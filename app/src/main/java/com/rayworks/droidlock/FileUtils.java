package com.rayworks.droidlock;

import android.os.Environment;
import android.text.TextUtils;

import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import timber.log.Timber;

/**
 * Created by Sean on 10/12/17.
 */

public final class FileUtils {
    static File getExternalFolder(String folderName) {
        File rootDir = Environment.getExternalStorageDirectory();
        File appDir = new File(rootDir, "droid_lock");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        if (TextUtils.isEmpty(folderName)) {
            return appDir;
        }

        File subDir = new File(appDir, folderName);
        if (!subDir.exists()) {
            subDir.mkdir();
        }

        return subDir;
    }

    public static boolean applyFileLocking() {
        File lock = new File(getExternalFolder(null), "test.lock");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(lock.getAbsolutePath());
            FileLock fileLock = outputStream.getChannel().tryLock();
            if (fileLock != null) {
                Timber.d(">>> Locked now");
                Thread.sleep(8000); // simulate the critical area access

                fileLock.release();
                Timber.d(">>> Lock released");

                return true;
            }
            return false;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        return false;
    }
}
