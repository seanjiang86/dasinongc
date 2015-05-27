package com.dasinong.app.utils;

import java.io.File;

import android.os.Environment;

public class FileHelper {

	/**
	 * @Description: 删除文件夹
	 * @author YSL
	 * @param rootFolderPath
	 * @return boolean
	 */
	public static boolean deleteFolder(String rootFolderPath) {
		if (!sdCardExist()) {
			return false;
		}
		File f = new File(rootFolderPath);
		if (f == null || !f.exists()) {
			return true;
		}
		if (!f.delete()) {
			File[] fs = f.listFiles();
			for (int index = 0; index < fs.length; index++) {
				if (fs[index].isDirectory()) {
					deleteFolder(fs[index].getAbsolutePath());
				} else {
					fs[index].delete();
				}
			}
			f.delete();
			return true;
		}
		return false;
	}

	public static boolean deleteFolder(File f) {
		if (!sdCardExist()) {
			return false;
		}
		// File f = file;
		if (f == null || !f.exists()) {
			return true;
		}
		if (!f.delete()) {
			File[] fs = f.listFiles();
			for (int index = 0; index < fs.length; index++) {
				if (fs[index].isDirectory()) {
					deleteFolder(fs[index].getAbsolutePath());
				} else {
					fs[index].delete();
				}
			}
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * @Description: 检查设备是否存在sd卡
	 * @author YSL
	 * @return
	 * @return boolean
	 */
	public static boolean sdCardExist() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		return sdCardExist;
	}

}
