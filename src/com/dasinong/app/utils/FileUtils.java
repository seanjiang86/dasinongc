package com.dasinong.app.utils;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by liuningning on 15/6/13.
 */
public class FileUtils {


    public static <T> T readEntity(Class<T> clazz, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (clazz == String.class) {
                return (T) FileUtils.readFile(file);
            } else {
                Gson gson = new Gson();
                try {
                    String json = new String(FileUtils.readFile(file));
                    return gson.fromJson(json, clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            return null;
        }
    }


    public static byte[] readFile(File file) {
        byte[] result = null;
        try {
            int fileSize = (int) file.length();
            result = new byte[fileSize];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(result, 0, fileSize);
            bis.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static boolean writeFile(String path, String content) {
        if(null == content){
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(content);
            writer.close();
            fos.close();
            return true;
        } catch (IOException e) {

            return false;
        }
    }


    public static boolean writeEntity(Object entity,String filePath){
        Gson gson = new Gson();
        try{
            if(entity instanceof String){
                return FileUtils.writeFile(filePath, (String) entity);
            }else{
                String json = gson.toJson(entity);
                return FileUtils.writeFile(filePath, json);
            }
        }catch (Exception e){
            return false;
        }
    }
}
