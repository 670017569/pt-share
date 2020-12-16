package com.pt.share.core.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @ClassName HttpRequestUtil
 * @Description TODO
 * @Author potato
 * @Date 2020/12/15 下午11:20
 **/
public class HttpRequestUtil {


    /**
     * 读取请求体中字符串
     * 请求体只能被读取一次!!!
     */
    public static String readBodyToString(HttpServletRequest request) throws IOException {
        String str;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        br = request.getReader();                   // 获取请求体reader
        while ((str = br.readLine()) != null) {
            sb.append(str);                         // 逐行读取字符串
        }
        br.close();
        return sb.toString();
    }

}
