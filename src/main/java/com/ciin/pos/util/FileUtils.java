package com.ciin.pos.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileUtils {

    public static String fileRead(String filePath) {
        return fileRead(new File(filePath));
    }


    public static String fileRead(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.safeClose(reader);
        }
        return null;
    }

    public static void fileWrite(String filePath, String content) {
        fileWrite(new File(filePath), content);
    }


    public static void fileWrite(File file, String content) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes());
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.safeClose(bos, fos);
        }
    }

    public static void fileWrite(File file, byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        fileWrite(file, is);
    }

    public static void fileWrite(File file, InputStream is) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            byte[] buf = new byte[2048];
            int len;
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.safeClose(is, fos);
        }
    }

    public static void mkdir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void copyFile(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        copyFile(srcFile, dstFile);
    }

    public static void copyFile(File srcFile, File dstFile) {
        try {
            File parentFile = srcFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            Files.copy(srcFile.toPath(), dstFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDir(String srcDirPath, String dstDirPath) {
        File srcDir = new File(srcDirPath);
        File dstDir = new File(dstDirPath);
        copyDir(srcDir, dstDir);
    }

    public static void copyDir(File srcDir, File dstDir) {
        File[] srcFiles = srcDir.listFiles();
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        if (srcFiles == null) {
            return;
        }
        for (File srcFile : srcFiles) {
            if (srcFile.isFile()) {
                copyFile(srcFile, new File(dstDir, srcFile.getName()));
            } else {
                copyDir(srcFile, new File(dstDir, srcFile.getName()));
            }
        }
    }

    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        deleteFile(f);
                    }
                }
            }
            file.delete();
        }
    }
}
