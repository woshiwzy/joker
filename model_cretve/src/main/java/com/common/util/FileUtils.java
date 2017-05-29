package com.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by wangzy on 15/11/10.
 */
public class FileUtils {

    public static byte[] readFile2Byte(String path) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedInputStream bfi = new BufferedInputStream(new FileInputStream(path));

        byte[] buffer = new byte[512];
        int ret = -1;
        while ((ret=bfi.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, ret);
        }
        bfi.close();

        return byteArrayOutputStream.toByteArray();
    }


    public static byte[] readFile2ByteUrl(String urlText) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        URL url=new URL(urlText);
        InputStream ins = url.openStream();
        BufferedInputStream bfi = new BufferedInputStream(ins);

        byte[] buffer = new byte[512];
        int ret = -1;
        while ((ret=bfi.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, ret);
        }
        bfi.close();
        return byteArrayOutputStream.toByteArray();
    }

}
