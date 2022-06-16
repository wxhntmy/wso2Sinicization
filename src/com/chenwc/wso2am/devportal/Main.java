package com.chenwc.wso2am.devportal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private final static String file_path = "D:\\WSO2_ESB\\wso2am-4.0.0\\repository\\deployment\\server\\jaggeryapps\\devportal\\site\\public\\locales\\en.json";
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		LoadLog4jProperties.init(false, LogLevel.info);
		try {
			start();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 翻译 en.json
	 * @throws Exception 异常信息
	 */
	public static void start() throws Exception {
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		JSONObject enJsonObject = JSONObject.parseObject(Utils.getJsonFile(file_path));

		JSONObject tempJsonObject = new JSONObject();
		for (String key:enJsonObject.keySet()) {
			String value = enJsonObject.getString(key);
			String cntext = null;
			if (terminology.containsKey(value.toLowerCase())) {
				cntext = terminology.get(value.toLowerCase());
			}
			else {
				if (!Utils.isEmpty(value)) {
					try {
						cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
			if (Utils.isEmpty(cntext)) {
				tempJsonObject.put(key, "");
			} else {
				tempJsonObject.put(key, cntext);
			}
		}

		writeFile(tempJsonObject.toJSONString(), "zh.json");
	}
	
	/**
	 * 写入文件
	 */
	public static void writeFile(String content, String fileName) {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		File file = new File(path + "dist\\wso2 am 4.0.0\\devportal\\" + fileName);
		Writer writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			writer.write(content + "\r\n");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
			try {
				if (null != writer) {
					writer.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
	}
	

	

}
