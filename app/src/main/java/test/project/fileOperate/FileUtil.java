package test.project.fileOperate;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import test.project.LoginActivity;

/**
 * Created by yushilong on 2015/10/16.
 */
public class FileUtil {
    private static int BUFFER_SIZE = 16 * 1024;
    public static final String FOLDER_PREFIX = "shihuo";

    public static void unzip(final Context context, Handler handler, String zipFile, String location) throws IOException {
        int size;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            if (!location.endsWith("/")) {
                location += "/";
            }
            File f = FileUtil.getFileDir(location);
            if (!f.exists())
                f.mkdirs();
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = FileUtil.getFileDir(path);
                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }
                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
            String fileDir = FOLDER_PREFIX + File.separator + LoginActivity.fileDirName;
            final String targetDir = fileDir + File.separator + "unzip";
            final File file = new File(FileUtil.getFileDir(targetDir), "version.txt");
            if (!file.exists())
                file.createNewFile();
            Files.write("3.2.0".getBytes(), file);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "解压文件完成", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (final Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "解压文件失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("test", "解压文件失败" + e.getMessage());
                }
            });
            e.printStackTrace();
        }
    }

    public static File getFileDir(String dir) {
        return Environment.getExternalStoragePublicDirectory(dir);
    }

    public static boolean deleteDir(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDir(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (file.delete());
    }
}
