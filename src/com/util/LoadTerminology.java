package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 术语翻译及常用词语翻译
 * @author wxhntmy
 *
 */
public class LoadTerminology {

	private static Logger log = LoggerFactory.getLogger(LoadTerminology.class);
	
	public static HashMap<String, String> getTerminology(){
		HashMap<String, String> terminology = new HashMap<>();
		
		JSONObject tempJson = JSONObject.parseObject(getJsonFile());
		for (String key: tempJson.keySet()) {
			terminology.put(key.toLowerCase(), tempJson.getString(key));
		}
		return terminology;
	}
	
	private static String getJsonFile() {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		String result = "";
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(path + "src\\com\\util\\terminology.json");
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
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		LoadLog4jProperties.init(false, LogLevel.info);
		log.info(JSONObject.parseObject(getJsonFile()).toJSONString());
		log.info(getTerminology().toString());
		
	}
}
