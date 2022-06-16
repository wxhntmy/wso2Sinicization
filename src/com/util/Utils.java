package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 工具类
 */
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    /**
     * 判断字符串是否为空
     * @param string 输入字符串
     * @return 为空返回true，不为空返回false
     */
    public static boolean isEmpty(String string) {
        return null == string || "".equals(string) || string.length() == 0;
    }

    /**
     * 读取文件
     * @param pathString 文件路径
     * @return 文件内容
     */
    public static String getJsonFile(String pathString) {
        log.info("file path: {}",pathString);
        String result = "";
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(pathString);
            inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            br = new BufferedReader(inputStreamReader);
            String string = null;
            while((string = br.readLine()) != null){
                sb.append(string);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally {
            try {
                if (null != br){
                    br.close();
                }
                if (null != inputStreamReader){
                    inputStreamReader.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        result = sb.toString();
        return result;
    }

}
