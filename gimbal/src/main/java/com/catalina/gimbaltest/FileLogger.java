package com.catalina.gimbaltest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class for logging events in the phone's external memory. <br>
 * Get an instance by calling FileLogger logger = FileLogger.getInstance(); <br>
 * The first time it gets called it creates a Log.txt file for appending. <br>
 * To log a message call FileLogger.getInstance().write(String msg); <br>
 * Remember to call FileLogger.getInstance().close(); on a proper place (such as onDestroy()) <br> 
 * Need to add to the manifest: <br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */
public class FileLogger {

	private static FileLogger instance;
	private static BufferedWriter out;
	private static final String TAG = "FileLogger";

	private FileLogger() {
		try {
			createFileOnDevice(true);
		} catch(IOException e) {
			e.printStackTrace();	
		}
	} 

	public static FileLogger getInstance(){
		if (instance == null) {
			return instance = new FileLogger();
		} else {
			return instance;
		}		
	}

	private void createFileOnDevice(Boolean append) throws IOException {
		File Root = Environment.getExternalStorageDirectory();
		Log.i(TAG, "External storage path: " + Root.getAbsolutePath().toString());
		
		if (Root.canWrite()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
			String currentDate = dateFormat.format(new Date()); 
			
			File  LogFile = new File(Root, "GimbalTestLog_" + currentDate + ".txt");
			FileWriter LogWriter = new FileWriter(LogFile, append);
			out = new BufferedWriter(LogWriter);

			String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
			out.write("Started logging at " + currentDateTime + "\n");
		}
	}

	public void write(String message) {
	    Log.i(TAG, message);
		
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

		try {
			out.write(currentDateTimeString + " --- " + message + "\n" );
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
