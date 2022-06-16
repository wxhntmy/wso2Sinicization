package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 解压jar包，读取jar包里的Resource.properties文件，读取到配置文件内容，请求百度翻译API，获取翻译后的内容
 * 把翻译后的内容更新到Resource.properties文件，然后再把解压后的文件夹压缩成jar包
 * 
 * @author mrwang
 *
 */
public class UpdJarContent {

	private static Logger log = LoggerFactory.getLogger(UpdJarContent.class);

	public JSONObject updJarContent(SignUtils sign, FileOutputStream logfile, String... proName) throws Exception {
		JSONObject tempJsonObject = new JSONObject();
		if (!isExists(sign.getJarNewPath())) {
			List<String> filePathlist = new ArrayList<String>();
			HashMap<String, String> terminology = LoadTerminology.getTerminology();
			for (int i = 0; i < proName.length; i++) {
				Decompression.uncompress(new File(sign.getJarPath()), new File(sign.getFilePath()));
				ArrayList<String> filelist = new ArrayList<String>();
				getFiles(sign.getFilePath(), filelist);
				for (String string : filelist) {
					if (string.contains(proName[i])) {
						logfile.write((string).getBytes());
						logfile.write("\r\n".getBytes());
						filePathlist.add(string);
						Properties pro = new Properties();
						InputStream in = new BufferedInputStream(new FileInputStream(string));
						pro.load(in);
						Iterator<String> it = pro.stringPropertyNames().iterator();
						FileOutputStream yfile = new FileOutputStream(string);
						FileOutputStream originalfile = new FileOutputStream(
								string.replace(proName[i], "original_file"));
						pro.store(originalfile, "file update...");
						while (it.hasNext()) {
							String key = it.next();
							String value = pro.getProperty(key);
							log.info("key-->" + key + "    value-->" + value);
							String cntext = null;
							
							if (terminology.containsKey(value.toLowerCase())) {
								cntext = terminology.get(value.toLowerCase());
							}
							else {
								try {
									if (!Utils.isEmpty(value)) {
										cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
									}
									// Thread.sleep(1000);
								} catch (Exception e) {
									// TODO: handle exception
									log.error(e.getMessage());
									Thread.sleep(20000);
									if (!Utils.isEmpty(value)) {
										cntext = TranslateByBaiDuUtil.translate(value, TranslateByBaiDuUtil.ENGLISH, TranslateByBaiDuUtil.CHINA);
									}
								}
							}
							log.info("key-->" + key + "    value-->" + cntext);
							if (Utils.isEmpty(cntext)) {
								pro.put(key, "");
							} else {
								pro.put(key, cntext);
							}
							if(!Utils.isEmpty(value) && !Utils.isEmpty(cntext)) {
								tempJsonObject.put(value, cntext);
							}
							Thread.sleep(10);
						}
						pro.store(yfile, "file update...");
						yfile.close();
						originalfile.close();
						in.close();
					}

				}
				Compressor zc = new Compressor(sign.getJarNewPath());
				zc.setOriginalUrl(sign.getOriginalUrl());
				zc.compress(sign.getFilePath());
				log.info("恭喜修改压缩文件成功！！！！，修改文件如下：");
				for (String string : filePathlist) {
					log.info("update file path is [" + string + "]");
				}
				log.info("翻译后的值：" + tempJsonObject.toJSONString());
			}

		} else {
			log.info("jar包已经存在--->" + sign.getJarNewPath());
		}
		return tempJsonObject;
	}
	
	/**
	 * 将文件夹下所有文件存储
	 * 
	 * @param filePath
	 * @param filelist
	 */
	public static ArrayList<String> getFiles(String filePath, ArrayList<String> filelist) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getFiles(file.getAbsolutePath(), filelist);
			} else {
				filelist.add(file.getAbsolutePath());
			}
		}
		return filelist;
	}

	/**
	 * 将文件夹下所有jar存储
	 * 
	 * @param filePath
	 */
	public static ArrayList<File> searchJarFiles(String filePath) {
		ArrayList<File> filelist = new ArrayList<File>();
		File f = new File(filePath);
		if (!f.exists()) {
			log.info(filePath + " not exists");
			return null;
		}
		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File file = fa[i];
			if (file.isDirectory()) {
				log.info(file.getName() + " [目录]");
			} else {
				String path = file.getAbsolutePath();
				if (path.contains("ui_") && path.contains(".jar") && !path.contains("_1.jar")) {
					filelist.add(file);
				}
				if (path.indexOf("org.wso2.carbon.i18n") != -1) {
					filelist.add(file);
				}
			}
		}
		return filelist;
	}

	public static void createFile(String path, String fileName) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		File file = new File(f, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isExists(String jarNewPath) {
		File file = new File(jarNewPath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
