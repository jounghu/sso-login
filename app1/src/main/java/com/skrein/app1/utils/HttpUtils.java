package com.skrein.app1.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 2020/5/7 11:10
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public class HttpUtils {
    public static String doGet(String urlParam) {

        try {
            URL url = new URL(urlParam);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();

            ByteArrayOutputStream b = new ByteArrayOutputStream();

            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                b.write(bytes, 0, len);
            }

            b.close();
            is.close();
            return new String(b.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }
}
