package com.chenwc.wso2am;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.util.SignUtils;
import com.util.UpdJarContent;
import com.util.LoadLog4jProperties;
import com.util.LogLevel;

public class Main {
	
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		LoadLog4jProperties.init(false, LogLevel.info);
		Long t1 = System.currentTimeMillis();
		try {
			start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Long t2 = System.currentTimeMillis();
		log.info("汉化成功，耗时：{} 秒", (t2-t1)/1000.00);
	}
	public static void start() throws Exception {
		String infoPath = "D:\\WSO2_ESB\\wso2am-4.0.0-chinese";
		String logFilePath = "info.log";
		UpdJarContent.createFile(infoPath, logFilePath);
		FileOutputStream logfile = new FileOutputStream(infoPath + logFilePath);
		String proName = "Resources";
		ArrayList<File> filelist = UpdJarContent
				.searchJarFiles("D:\\WSO2_ESB\\wso2am-4.0.0\\repository\\components\\plugins");
		log.info("文件列表大小：" + filelist.size());
		
		JSONArray baiduJsonArray = new JSONArray();
		
		for (File file : filelist) {
			UpdJarContent ut = new UpdJarContent();
			SignUtils ss = new SignUtils();
			String path = file.getAbsolutePath();
			ss.setJarPath(path);
			logfile.write(("------" + path + "------").getBytes());
			logfile.write("\r\n".getBytes());
			log.info("需要解压的压缩包路径-->" + path);
			String fileName = file.getName().replace(".jar", "");
			ss.setOriginalUrl(fileName);
			log.info("用于压缩包删除前缀目录的名称-->" + fileName);
			String filePath = infoPath + "\\file\\" + fileName;
			ss.setFilePath(filePath);
			log.info("解压后的文件夹目录路径-->" + filePath);
			String newFilePath = infoPath + "\\jar\\" + file.getName();
			ss.setJarNewPath(newFilePath);
			log.info("新打包后的压缩包路径-->" + newFilePath);
			JSONObject tempJsonObject = ut.updJarContent(ss, logfile, proName);
			logfile.write(("------" + path + "------").getBytes());
			logfile.write("\r\n".getBytes());
			// DeleteDirectory.deleteDir(new File(filePath));
			// log.info("删除临时解压文件 -->" + filePath + "<-- 成功！");
			
			baiduJsonArray.add(tempJsonObject);
		}
		logfile.close();
		JSONObject testJsonObject = new JSONObject();
		for (int i = 0; i < baiduJsonArray.size(); i++) {
			JSONObject indexJsonObject = baiduJsonArray.getJSONObject(i);
			for (String key:indexJsonObject.keySet()) {
				testJsonObject.put(key, indexJsonObject.getString(key));
			}
		}
		writeFile(testJsonObject.toJSONString());
	}
	
	/**
	 * 写入文件
	 */
	public static void writeFile(String content) {
		File pathFile = new File("1.txt");
		String path = pathFile.getAbsolutePath().replace("1.txt", "");
		log.info(path);
		File file = new File(path + "src\\com\\chenwc\\wso2am\\baiduApiTranlate.json");
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
