package com.chenwc.wso2am;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.util.LoadLog4jProperties;
import com.util.LogLevel;

public class Test {
	
	private static Logger log = LoggerFactory.getLogger(Test.class);

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		LoadLog4jProperties.init(false, LogLevel.info);

		File file = new File("1.txt");
		Writer writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			JSONObject tJsonObject = JSONObject.parseObject(getJsonFile());
			for(String key: tJsonObject.keySet()) {
				log.info("key: {}\t\tvalue:{}", key, tJsonObject.getString(key));
				String valueString = tJsonObject.getString(key);
				valueString = valueString.replace("（", "(").replace("）", ")").replace("。", ".").replace("“", "\"").replace("”", "\"")
						.replace("！", "!").replace("？", "?").replace("：", ":").replace("，", ",").replace("\\", "\\\\").replace("\"", "\\\"")
						.replace("{", " {").replace("}", "} ");
				
				String keyString = key.replace("\\", "\\\\").replace("\"", "\\\"").replace("	", " ").replace("	", " ").replace("\r", "")
						.replace("\n", "").replace("\r\n", "");
				writer.write("\"" + keyString + "\":\""+ valueString +"\",\r\n");
			}
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
	
	private static String getJsonFile() {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		String result = "";
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(path + "src\\com\\chenwc\\wso2am\\baiduApiTranlate.json");
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
