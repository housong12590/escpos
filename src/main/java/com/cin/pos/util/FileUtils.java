package com.cin.pos.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
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
        FileOutputStream fos = null;
        FileChannel channel = null;
        try {
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.safeClose(fos, channel);
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

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
