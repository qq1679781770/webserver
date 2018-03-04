package com.jsxnh.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static void writeTofile(File file,byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    public static void deleteFile(File file){
        if(file.exists()){
            file.delete();
        }
    }

}
