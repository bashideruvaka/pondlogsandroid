package com.eruvaka.logger;

import java.io.File;

import org.apache.log4j.Level;

import android.content.Context;
import android.os.Environment;


import de.mindpipe.android.logging.log4j.LogConfigurator;

public class ConfigureLog4J {
	
	private static final String TAG = "ConfigureLog4J";
	private static final int MAX_BACKUP_SIZE = 50;
	private static final int MAX_FILE_SIZE = 1024 * 1024;
	
	
	/**
	 * for more file patterns refer http://www.tutorialspoint.com/log4j/log4j_patternlayout.htm
	 * example the below file pattern will log in this pattern - 
	 * 24 Jul 2013 17:50:43,431-DEBUG-TestRapidLog-Class Name : TestRapidLog
	 * date-type-className-msg
	 */
	//pattern in which data has to be written in the file
	private static final String FILE_PATTERN = "%d{DATE}-%-5p-%c-%m%n";
	
	 
	
	public enum Levels {
		ALL,
	    ERROR,
	    DEBUG,
	    INFO,
	    WARN,
	    NONE
	    
	  }
	
	static LogConfigurator logConfigurator = null;
	
	public static void configure(Context context, String logFileName) {
		
		logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "Android/data/"+context.getPackageName()+"/Logs" +File.separator + logFileName);
		
		logConfigurator.setMaxBackupSize(MAX_BACKUP_SIZE);
		
		logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
		
		logConfigurator.setFilePattern(FILE_PATTERN);
				
		logConfigurator.setRootLevel(Level.ALL);
		
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ALL);
		
		logConfigurator.configure();
	}
	
	public static void setLevel(String type){
		
		Levels level = Levels.valueOf(type.toUpperCase());
		
		switch(level){
		
		case ALL:
			EruvakaLog.i(TAG, "LogConfigurator root level set to All");
			logConfigurator.setRootLevel(Level.ALL);
			logConfigurator.setLevel("org.apache", Level.ALL);
			logConfigurator.configure();
			break;
			
		case ERROR:
			EruvakaLog.i(TAG, "LogConfigurator root level set to Error");
			logConfigurator.setRootLevel(Level.ERROR);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
			break;
			
		case DEBUG:
			EruvakaLog.i(TAG, "LogConfigurator root level set to Debug");
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.DEBUG);
			logConfigurator.configure();
			break;
			
		case INFO:
			EruvakaLog.i(TAG, "LogConfigurator root level set to Info");
			logConfigurator.setRootLevel(Level.INFO);
			logConfigurator.setLevel("org.apache", Level.INFO);
			logConfigurator.configure();
			break;
			
		case WARN:
			EruvakaLog.i(TAG, "LogConfigurator root level set to None");
			logConfigurator.setRootLevel(Level.WARN);
			logConfigurator.setLevel("org.apache", Level.WARN);
			logConfigurator.configure();
			break;
			
		case NONE:
			EruvakaLog.i(TAG, "LogConfigurator root level set to Warn");
			logConfigurator.setRootLevel(Level.ERROR);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
			break;
		
			
		default:
			EruvakaLog.i(TAG, "LogConfigurator root level set to Debug");
			logConfigurator.setRootLevel(Level.DEBUG);
			break;
		}
	}
	
	

}
