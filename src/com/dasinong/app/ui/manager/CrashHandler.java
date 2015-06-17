package com.dasinong.app.ui.manager;

import java.io.File;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.dasinong.app.utils.Logger;

public class CrashHandler implements UncaughtExceptionHandler {

	private static String mLogDir;
	private Context mContext;

	public CrashHandler(Context context, String logDir) {
		mLogDir = logDir;
		this.mContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		Logger.e("DasinongException", "dasinong_" + Log.getStackTraceString(ex));

		PrintStream printStream = null;
		try {
			printStream = new PrintStream(getCrashFile());
			ex.printStackTrace(printStream);
			printStream.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (printStream != null) {
				printStream.close();
			}
		}

		if (ex instanceof OutOfMemoryError) {
			Logger.e("OutOfMemoryError", "OutOfMemoryError:" + Log.getStackTraceString(ex));
			return;
		}

		exit();
	}

	private static File getCrashFile() {
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH.mm.ss", Locale.getDefault()).format(new Date());
		return new File(mLogDir + File.separator + timeStamp  + "_crash.txt");
	}

	private static void exit() {
		// android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
