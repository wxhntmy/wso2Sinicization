package com.chenwc.wso2am.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.util.TranslateByBaiDuUtil;
import com.util.LoadLog4jProperties;
import com.util.LoadTerminology;
import com.util.LogLevel;

/**
 * WSO2 AM Admin
 * @author wxhntmy
 */
public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	private final static String file_path = "D:\\WSO2_ESB\\wso2am-4.0.0\\repository\\deployment\\server\\jaggeryapps\\admin\\site\\public\\locales\\en.json";
	
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

	
	public static void start() throws Exception {
		HashMap<String, String> terminology = LoadTerminology.getTerminology();
		JSONObject enJsonObject = JSONObject.parseObject(getJsonFile(file_path));

		JSONObject tempJsonObject = new JSONObject();
		for (String key:enJsonObject.keySet()) {
			String value = enJsonObject.getString(key);
			String cntext = null;
			if (terminology.containsKey(value.toLowerCase())) {
				cntext = terminology.get(value.toLowerCase());
			}
			else {
				if (!isEmpty(value)) {
					try {
						cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}
			if (isEmpty(cntext)) {
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
		File file = new File(path + "dist\\wso2 am 4.0.0\\admin\\" + fileName);
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
	
	/**
	 * 判断字符串是否为空
	 * @param string 输入字符串
	 * @return 为空返回true，不为空返回false
	 */
	public static boolean isEmpty(String string) {
		if (null == string || "".equals(string) || string.isEmpty() || string.length() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 读取文件
	 * @param pathString 文件路径
	 * @return 文件内容
	 */
	private static String getJsonFile(String pathString) {
		log.info("file path: {}",pathString);
		String result = "";
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(pathString);
			inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
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
				br.close();
				inputStreamReader.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		result = sb.toString();
		return result;	
	}
}
