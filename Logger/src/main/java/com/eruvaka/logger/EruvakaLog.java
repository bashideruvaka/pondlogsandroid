package com.eruvaka.logger;

import org.apache.log4j.Logger;

import android.content.Context;

public class EruvakaLog {

	private static Logger uilog = null;
	private static Context context;

	//class intialization with default log file name
	public EruvakaLog(Context context) {

		this(context, "rapidAppLog.txt");

	}

	//class intialization with desired log file name
	public EruvakaLog(Context context, String logfileName) {

		this.context = context;
		if (null == uilog) {
			
			ConfigureLog4J.configure(context, logfileName);
			
			uilog = Logger.getLogger(context.getClass());
			
			uilog.debug("Class Name : "+context.getClass());
			

		}
	}

	//write the debug log to the file and log aswell 
	public static void d(String tag, String msg) {

		uilog.debug(tag + "-" + msg);
	}

	//write the info log to the file and log aswell
	public static void i(String tag, String msg) {

		uilog.info(tag + "-" + msg);
	}

	//write the error log to the file with exception and log aswell
	public static void e(String tag, String msg, Exception e) {

		uilog.error(tag + "-" + msg, e);
	}

	//write the verbose log to the file and log aswell
	public static void v(String tag, String msg) {

		uilog.info(tag + "-" + msg);
	}

	//write the warn log to the file and log aswell
	public static void w(String tag, String msg) {

		uilog.warn(tag + "-" + msg);
	}

}
