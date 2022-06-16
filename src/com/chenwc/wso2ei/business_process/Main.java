package com.chenwc.wso2ei.business_process;

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
	
	private final static String bpmn_explorer = "D:\\WSO2_ESB\\wso2ei-6.6.0\\wso2\\business-process\\repository\\deployment\\server\\jaggeryapps\\bpmn-explorer\\config\\locales\\locale_en.json";
	private final static String humantask_explorer = "D:\\WSO2_ESB\\wso2ei-6.6.0\\wso2\\business-process\\repository\\deployment\\server\\jaggeryapps\\humantask-explorer\\config\\locales\\locale_en.json";
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		LoadLog4jProperties.init(false, LogLevel.info);
		
		JSONObject bpmnExplorerJsonObject = JSONObject.parseObject(Utils.getJsonFile(bpmn_explorer));
		JSONObject humantaskExplorerJsonObject = JSONObject.parseObject(Utils.getJsonFile(humantask_explorer));
		
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		
		JSONObject tempJsonObject = new JSONObject();
		for (String key:bpmnExplorerJsonObject.keySet()) {
			String value = bpmnExplorerJsonObject.getString(key);
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
		writeFile(tempJsonObject.toJSONString(), "bpmn_explorer_zh.json");
		
		
		tempJsonObject = new JSONObject();
		for (String key:humantaskExplorerJsonObject.keySet()) {
			String value = humantaskExplorerJsonObject.getString(key);
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
		writeFile(tempJsonObject.toJSONString(), "humantask_explorer_zh.json");
		
		log.info(bpmnExplorerJsonObject.toJSONString());
		log.info(humantaskExplorerJsonObject.toJSONString());
	}

	/**
	 * 写入文件
	 */
	public static void writeFile(String content, String fileName) {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		File file = new File(path + "dist\\wso2 ei 6.6.0\\business-process\\" + fileName);
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

