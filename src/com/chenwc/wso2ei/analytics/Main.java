package com.chenwc.wso2ei.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import com.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class Main {
	
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	private final static String business_rules = "D:\\WSO2_ESB\\wso2ei-6.6.0\\wso2\\analytics\\wso2\\dashboard\\deployment\\web-ui-apps\\business-rules\\public\\locales\\en.json";
	private final static String monitoring = "D:\\WSO2_ESB\\wso2ei-6.6.0\\wso2\\analytics\\wso2\\dashboard\\deployment\\web-ui-apps\\monitoring\\public\\locales\\en.json";
	private final static String portal = "D:\\WSO2_ESB\\wso2ei-6.6.0\\wso2\\analytics\\wso2\\dashboard\\deployment\\web-ui-apps\\portal\\public\\locales\\en.json";
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		LoadLog4jProperties.init(false, LogLevel.info);
		
		JSONObject businessRulesJsonObject = JSONObject.parseObject(Utils.getJsonFile(business_rules));
		JSONObject monitoringJsonObject = JSONObject.parseObject(Utils.getJsonFile(monitoring));
		JSONObject portalJsonObject = JSONObject.parseObject(Utils.getJsonFile(portal));
		
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		
		JSONObject tempJsonObject = new JSONObject();
		for (String key:businessRulesJsonObject.keySet()) {
			String value = businessRulesJsonObject.getString(key);
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
		writeFile(tempJsonObject.toJSONString(), "business_rules_zh.json");
		
		
		tempJsonObject = new JSONObject();
		for (String key:monitoringJsonObject.keySet()) {
			String value = monitoringJsonObject.getString(key);
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
		writeFile(tempJsonObject.toJSONString(), "monitoring_zh.json");
		
		tempJsonObject = new JSONObject();
		for (String key:portalJsonObject.keySet()) {
			String value = portalJsonObject.getString(key);
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
		writeFile(tempJsonObject.toJSONString(), "portal_zh.json");
		
		log.info(businessRulesJsonObject.toJSONString());
		log.info(monitoringJsonObject.toJSONString());
		log.info(portalJsonObject.toJSONString());
	}

	
	/**
	 * 写入文件
	 */
	public static void writeFile(String content, String fileName) {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		File file = new File(path + "dist\\wso2 ei 6.6.0\\analytics\\" + fileName);
		Writer writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
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
