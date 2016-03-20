package com.vui.bluelight.utils;

import android.text.TextUtils;
import android.util.Log;
 
public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = VERBOSE;
    public static final String SEPARATOR = ",";
 
    public static void v(String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getLogLocation(stackTraceElement);
            Log.v(tag,  message);
        }
    }
 
    public static void d(String message) {
        if (LEVEL <= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getLogLocation(stackTraceElement);
            Log.d(tag, message);
        }
    }
    public static void i(String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getLogLocation(stackTraceElement);
            Log.i(tag,message);
        }
    }
 
 
    public static void w(String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getLogLocation(stackTraceElement);
            Log.w(tag, message);
        }
    }
    
    public static void e(String message) {
    	String tag="";
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getLogLocation(stackTraceElement);
            }
            Log.e(tag, message);
        }
    }
    
    public static String getLogLocation(StackTraceElement stackTraceElement){
    	// 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取输出行数
        int lineNumber = stackTraceElement.getLineNumber();
    	return className+"."+methodName+"():"+lineNumber;
    }
    
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取输出行数
        int lineNumber = stackTraceElement.getLineNumber();
        logInfoStringBuilder.append("[ ");
        logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
        logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }
 
}