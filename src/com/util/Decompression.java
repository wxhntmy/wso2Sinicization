package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解压工具
 * 
 * @author mrwang
 */
public class Decompression {

	protected static Logger log = LoggerFactory.getLogger(Decompression.class);

	@SuppressWarnings("resource")
	public static void uncompress(File jarFile, File tarDir) throws IOException {
		JarFile jfInst = new JarFile(jarFile);
		Enumeration<JarEntry> enumEntry = jfInst.entries();
		while (enumEntry.hasMoreElements()) {
			JarEntry jarEntry = enumEntry.nextElement();
			File tarFile = new File(tarDir, jarEntry.getName());
			/*
			 * if(jarEntry.getName().contains("META-INF")){ File miFile = new File(tarDir,
			 * "META-INF"); if(!miFile.exists()){ miFile.mkdirs(); }
			 * 
			 * }
			 */
			log.info("原文件路径 -->" + tarFile.getPath());
			String path = tarFile.getPath().substring(0, tarFile.getPath().lastIndexOf("\\") + 1);
			log.info("原文件所属目录 -->" + tarFile.getPath());
			File file = new File(path);
			if ((!file.exists() && !file.isDirectory())) {
				file.mkdirs();
			}
			makeFile(jarEntry, tarFile);
			if (jarEntry.isDirectory()) {
				continue;
			}
			FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();
			InputStream ins = jfInst.getInputStream(jarEntry);
			transferStream(ins, fileChannel);
		}
	}

	/**
	 * 流交换操作
	 * 
	 * @param ins
	 *            输入流
	 * @param channel
	 *            输出流
	 */
	private static void transferStream(InputStream ins, FileChannel channel) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
		ReadableByteChannel rbcInst = Channels.newChannel(ins);
		try {
			while (-1 != (rbcInst.read(byteBuffer))) {
				byteBuffer.flip();
				channel.write(byteBuffer);
				byteBuffer.clear();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (null != rbcInst) {
				try {
					rbcInst.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != channel) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 打印jar文件内容信息
	 * 
	 * @param file
	 *            jar文件
	 */
	public static void printJarEntry(File file) {
		JarFile jfInst = null;
		;
		try {
			jfInst = new JarFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<JarEntry> enumEntry = jfInst.entries();
		while (enumEntry.hasMoreElements()) {
			log.info(enumEntry.nextElement().toString());
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param jarEntry
	 *            jar实体
	 * @param fileInst
	 *            文件实体
	 * @throws IOException
	 *             抛出异常
	 */
	public static void makeFile(JarEntry jarEntry, File fileInst) {
		if (!fileInst.exists()) {
			if (jarEntry.isDirectory()) {
				fileInst.mkdirs();
			} else {
				try {
					fileInst.createNewFile();
				} catch (IOException e) {
					log.error("创建文件失败>>>".concat(fileInst.getPath()));
				}
			}
		}
	}

	public static void main(String[] args) {
		File jarFile = new File(
				"D:\\WSO2 EI 6.6.0\\wso2ei-6.6.0\\wso2\\components\\plugins\\org.wso2.carbon.service.mgt.ui_4.9.15.jar");
		File targetDir = new File("D:\\WSO2 EI 6.6.0\\org.wso2.carbon.service.mgt.ui_4.9.15");
		try {
			Decompression.uncompress(jarFile, targetDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
