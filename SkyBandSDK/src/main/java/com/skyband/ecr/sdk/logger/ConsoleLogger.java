package com.skyband.ecr.sdk.logger;

import android.util.Log;

public class ConsoleLogger extends Logger {

    private String tag;

    protected ConsoleLogger() {
    }

    protected ConsoleLogger(String name) {
        super(name);
        tag = name;
    }

    public void init() {
        //Required for future implementation
    }

    public void close() {
        //Required for future implementation
    }

    protected void writeLog(String mesg) {
        //Required for future implementation
    }

    protected void writeLog(int logType, String logMessage) {

        switch (logType) {
            case ERROR:
                Log.e(tag, logMessage);
                break;
            case WARN:
                Log.w(tag, logMessage);
                break;
            case DEBUG:
                Log.d(tag, logMessage);
                break;
            case INFO:
            case TRACE:
                Log.i(tag, logMessage);
                break;
            case DIAGNOSE:
                Log.v(tag, logMessage);
                break;
                default:
                    break;
        }
    }

    public Logger newLogger(String name) {
        return new ConsoleLogger(name);
    }
}
