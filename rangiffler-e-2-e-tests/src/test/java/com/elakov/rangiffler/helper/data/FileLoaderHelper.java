package com.elakov.rangiffler.helper.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileLoaderHelper {

    private static final ClassLoader classLoader = FileLoaderHelper.class.getClassLoader();

    public static String getFileByClasspath(String fileClasspath) {

        try (InputStream is = classLoader.getResourceAsStream(fileClasspath)) {
            assert is != null;
            String fileExtension = fileClasspath.substring(fileClasspath.lastIndexOf(".") + 1);
            byte[] base64Image = Base64.getEncoder().encode(is.readAllBytes());
            return "data:image/" + fileExtension + ";base64," + new String(base64Image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
