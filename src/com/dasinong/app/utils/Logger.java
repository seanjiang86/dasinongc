package com.dasinong.app.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.dasinong.app.DsnApplication;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class Logger {

	private static final boolean isLogCatEnabled = true;
	private static final boolean isFileEnabled = true;
	private static final int mPrintLogLevel = Log.VERBOSE;

	private static final long FILE_LENGTH = 500 * 1024;
	private static final int mLogCount = 20;
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat mDataFormat = new SimpleDateFormat("MM/dd_HH:mm:ss");
	private static HashMap<String, File> mLogFileMap = null;

	public static void d(Object tag, Object content) {
		debug(String.valueOf(tag), String.valueOf(content), Log.DEBUG, false, true);
	}

	public static void i(Object tag, Object content) {
		debug(String.valueOf(tag), String.valueOf(content), Log.INFO, false, true);
	}

	public static void e(Object tag, Object content) {
		debug(String.valueOf(tag), String.valueOf(content), Log.ERROR, false, true);
	}

	public static void w(Object tag, Object content) {
		debug(String.valueOf(tag), String.valueOf(content), Log.WARN, false, true);
	}

	public static void v(Object tag, Object content) {
		debug(String.valueOf(tag), String.valueOf(content), Log.VERBOSE, false, true);
	}

	public static void d(Object tag, Object content, boolean isToFile) {
		debug(String.valueOf(tag), String.valueOf(content), Log.DEBUG, isToFile, true);
	}

	public static void i(Object tag, Object content, boolean isToFile) {
		debug(String.valueOf(tag), String.valueOf(content), Log.INFO, isToFile, true);
	}

	public static void e(Object tag, Object content, boolean isToFile) {
		debug(String.valueOf(tag), String.valueOf(content), Log.ERROR, isToFile, true);
	}

	public static void w(Object tag, Object content, boolean isToFile) {
		debug(String.valueOf(tag), String.valueOf(content), Log.WARN, isToFile, true);
	}

	public static void v(Object tag, Object content, boolean isToFile) {
		debug(String.valueOf(tag), String.valueOf(content), Log.VERBOSE, isToFile, true);
	}

	private static void debug(String tag, String content, int level, boolean isToFile, boolean isToLagcat) {


		if (!isLogCatEnabled) {
			if (!isFileEnabled) {
				return;
			}
		}

		if (mPrintLogLevel > level) {
			return;
		}

		switch (level) {
		case Log.VERBOSE:
			if (isToLagcat && isLogCatEnabled) {
				Log.v(String.valueOf(tag), String.valueOf(content));
			}
			if (isToFile && isFileEnabled) {
				debugFile(tag, content, "__VERBOSE");
			}
			break;
		case Log.DEBUG:
			if (isToLagcat && isLogCatEnabled) {
				Log.d(String.valueOf(tag), String.valueOf(content));
			}
			if (isToFile && isFileEnabled) {
				debugFile(tag, content, "__DEBUG");
			}
			break;
		case Log.INFO:
			if (isToLagcat && isLogCatEnabled) {
				Log.i(String.valueOf(tag), String.valueOf(content));
			}
			if (isToFile && isFileEnabled) {
				debugFile(tag, content, "__INFO");
			}
			break;
		case Log.WARN:
			if (isToLagcat && isLogCatEnabled) {
				Log.w(String.valueOf(tag), String.valueOf(content));
			}
			if (isToFile && isFileEnabled) {
				debugFile(tag, content, "__WARN");
			}
			break;
		case Log.ERROR:
			if (isToLagcat && isLogCatEnabled) {
				Log.e(String.valueOf(tag), String.valueOf(content));
			}
			if (isToFile && isFileEnabled) {
				debugFile(tag, content, "__ERROR");
			}

			break;
		}
	}

	private static void debugFile(String tag, String content, String levelName) {

		try {

			StringBuffer sb = new StringBuffer();
			sb.append(mDataFormat.format(new Date(System.currentTimeMillis())));
			// long time = System.currentTimeMillis();

			File mLogFile = null;

			if (mLogFileMap == null) {
				mLogFileMap = new HashMap<String, File>();
			}

			if (mLogFileMap.containsKey(tag)) {
				mLogFile = mLogFileMap.get(tag);
			}

			if (mLogFile == null || mLogFile.length() > FILE_LENGTH || !mLogFile.exists()) {
				mLogFile = getLogfile(tag);
				if (!mLogFile.exists()) {
					mLogFile.createNewFile();
				}
				mLogFileMap.put(tag, mLogFile);
			}

			// Log.d("YSL", "time:" + (System.currentTimeMillis() - time));
			writeToFile(mLogFile, sb.toString() + levelName + "____" + tag + " :", String.valueOf(content));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean writeToFile(File file, String tag, String message) {
		if (file == null) {
			return false;
		}
		try {
			FileWriter fw = new FileWriter(file, true);
			fw.append(tag);
			fw.append("\n");
			fw.append("        " + message);
			fw.append("\n\n");
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static File getLogfile(String tag) {

		File file = new File(getAppLogDirectory() + tag + File.separator);

		if (!file.exists()) {
			file.mkdirs();
		}

		File[] listFiles = file.listFiles();

		for (int i = 0; i < mLogCount; i++) {
			File f = new File(getLogFilePath(tag, i));
			long length = f.length();
			if (f.exists() && length < FILE_LENGTH) {
				return f;
			} else {
				if (!f.exists()) {
					// StringBuffer sb = new StringBuffer();
					// sb.append(mDataFormat.format(new
					// Date(System.currentTimeMillis())));
					// writeToFile(f, sb.toString(), "");
					return f;
				}
			}
		}

		for (int i = 0; i < mLogCount - 1; i++) {
			File preFile = new File(getLogFilePath(tag, i));
			File nextFile = new File(getLogFilePath(tag, i + 1));

			if (nextFile.lastModified() - preFile.lastModified() < 0) {
				nextFile.delete();
				return nextFile;
			}

			if (i == (mLogCount - 2)) {
				File f = new File(getLogFilePath(tag, 0));
				f.delete();
				return f;
			}
		}

		return null;
	}

	private static String getLogFilePath(String tag, int i) {
		return getAppLogDirectory() + tag + File.separator + "dasinongLog_" + i + ".log";
	}

	private static String getAppLogDirectory() {
		String logPath = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ? DsnApplication.getContext()
				.getExternalFilesDir("") + File.separator + "log" + File.separator : "dasinong" + File.separator + "log" + File.separator;
		if (!isDirExist(logPath)) {
			File file = new File(logPath);
			file.mkdirs();
		}
		return logPath;
	}

	public static boolean isDirExist(String path) {
		if (path == null) {
			return false;
		}
		File file = new File(path);
		return file.exists() && file.isDirectory();
	}

	public static void d1(String tag, Object content) {
		debug("YSL", String.valueOf(tag + "__" + content), Log.DEBUG, true, true);
	}

	public static void e1(String tag, Throwable e) {
		debug("YSL", Log.getStackTraceString(e), Log.ERROR, true, true);
	}

	public static void e1(String tag, String e) {
		debug("YSL", e, Log.ERROR, true, true);
	}
}
