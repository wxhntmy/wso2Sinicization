package com.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除单一文件、文件夹；删除文件夹及其文件夹下所有文件
 * 
 * @author mrwang
 */
public class DeleteDirectory {

	private static Logger log = LoggerFactory.getLogger(DeleteDirectory.class);

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	private static void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			log.debug("Successfully deleted empty directory: " + dir);
		} else {
			log.debug("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		doDeleteEmptyDir("");
		String newDir2 = "E:\\org.wso2.carbon.service.mgt.ui_4.7.0";
		deleteDir(new File(newDir2));
	}
}
