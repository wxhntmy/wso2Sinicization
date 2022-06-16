package com.chenwc.wso2am.publisher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private final static String en_json = "D:\\WSO2_ESB\\wso2am-4.0.0\\repository\\deployment\\server\\jaggeryapps\\publisher\\site\\public\\locales\\en.json";
	private final static String raw_en_json = "D:\\WSO2_ESB\\wso2am-4.0.0\\repository\\deployment\\server\\jaggeryapps\\publisher\\site\\public\\locales\\raw.en.json";

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		LoadLog4jProperties.init(false, LogLevel.info);
		try {
			startEn();
			//startRawEn();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 翻译 raw.en.json
	 * @throws Exception 异常信息
	 */
	public static void startRawEn() throws Exception {
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		JSONObject rawEnJsonObject = JSONObject.parseObject(Utils.getJsonFile(raw_en_json));
		JSONObject tempJsonObject = new JSONObject();
		for (String key:rawEnJsonObject.keySet()) {
			JSONObject testJson = rawEnJsonObject.getJSONObject(key);
			JSONObject item = new JSONObject();
			for (String keyItem : testJson.keySet()){
				String value = testJson.getString(keyItem);
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
							Thread.sleep(20000);
							cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
						}
					}
				}
				if (Utils.isEmpty(cntext)) {
					item.put(keyItem, "");
				} else {
					item.put(keyItem, cntext);
				}
			}
			tempJsonObject.put(key, item);
		}
		log.info("raw.zh.json :{}", tempJsonObject.toJSONString());
		writeFile(tempJsonObject.toJSONString(), "raw.zh.json");
	}

	/**
	 * 翻译 en.json
	 * @throws Exception 异常信息
	 */
	public static void startEn() throws Exception {
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		JSONObject enJsonObject = JSONObject.parseObject(Utils.getJsonFile(en_json));
		
		JSONObject cnJsonObject = new JSONObject();
		for (String key : enJsonObject.keySet()) {
			JSONArray tempJsonArray = enJsonObject.getJSONArray(key);
			JSONArray itemJsonArray = new JSONArray();
			for (int i = 0; i < tempJsonArray.size(); i++) {
				JSONObject tempJsonObject = tempJsonArray.getJSONObject(i);
				JSONObject item = new JSONObject();
				for (String keyItem : tempJsonObject.keySet()) {
					String cntext = null;
					String value = tempJsonObject.getString(keyItem);
					
					if (!"type".equals(keyItem)) {
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
									Thread.sleep(20000);
									cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
								}
							}
						}
					}
					
					if (Utils.isEmpty(cntext)) {
						item.put(keyItem, value);
					} else {
						item.put(keyItem, cntext);
					}
				}
				itemJsonArray.add(item);
			}
			cnJsonObject.put(key, itemJsonArray);
		}
		
		log.info(cnJsonObject.toString());
		writeFile(cnJsonObject.toString(), "zh.json");
	}
	
	/**
	 * 写入文件
	 */
	public static void writeFile(String content, String fileName) {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		File file = new File(path + "dist\\wso2 am 4.0.0\\publisher\\" + fileName);
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
