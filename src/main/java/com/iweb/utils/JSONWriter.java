package com.iweb.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JSONWriter {
    public static void writeJson(Object data, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println(JSON.toJSONString(data));
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    public static void writeJson(Object data, HttpServletResponse response, String dataFormat) {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println(JSON.toJSONStringWithDateFormat(data, dataFormat, SerializerFeature.WriteDateUseDateFormat));
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
