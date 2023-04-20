package com.skyband.ecr.sdk.logger;

import java.util.Calendar;

public abstract class Logger {

    private static final boolean ENABLE = true;

    protected static final int ERROR = 0;
    protected static final int WARN = 1;
    protected static final int DEBUG = 2;
    protected static final int INFO = 3;
    protected static final int DIAGNOSE = 4;
    protected static final int TRACE = 5;

    protected static int level = TRACE;
    protected static Logger logger = null;

    protected static final String CONSOLE_LOG = "console";
    protected static final String FILE_LOG = "file";

    private static final String LOGGER_TYPE = CONSOLE_LOG;

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final String BUILD_VERSION = "1.0";
    private String[] prefixes = {"SEVERE", "WARN", "DEBUG", "INFO", "DIAGNOSE", "TRACE"};
    private String name = "DEFAULT";

    protected Logger() {
    }

    protected Logger(String name) {
        this.name = name;
    }

    public abstract void init();

    protected abstract Logger newLogger(String name);

    protected abstract void writeLog(int logType, String msg);

    public abstract void close();

    /**
     * Use this method to get the logger when it is required to preserve the
     * class and method names in the logs when the library is obfuscated.
     *
     * @param name Fully qualified name of the class
     * @return Logger object
     */
    public static Logger getNewLogger(String name) {
        if (logger == null) {
            init(LOGGER_TYPE);
        }
        return logger;
    }

    /**
     * Use this method to get logger when the library would not be obfuscated.
     *
     * @param obj Object specific logger
     * @return Logger object
     */
    public static Logger getNewLogger(Object obj) {
        String name = obj.getClass().getName();
        return logger.newLogger(name);
    }

    public static void init(String logger) {
        if (CONSOLE_LOG.equalsIgnoreCase(logger)) {
            Logger.logger = new ConsoleLogger(logger);
        } else if (FILE_LOG.equalsIgnoreCase(logger)) {
            FileLogger fileLogger = new FileLogger(logger);
            fileLogger.init();
            Logger.logger = fileLogger;
        }
    }

    public static void init(String logger, String fileName) {
        if (CONSOLE_LOG.equalsIgnoreCase(logger)) {
            Logger.logger = new ConsoleLogger();
        } else if (FILE_LOG.equalsIgnoreCase(logger)) {
            FileLogger fileLogger = new FileLogger(logger);
            fileLogger.initWithFile(fileName);
            Logger.logger = fileLogger;
        }
    }

    /**
     * <ul>
     * <li>When log level is set to ERROR, only error conditions that are errors
     * are logged</li>
     * <li>
     * When log level is set to WARN, the following are logged
     * <ul>
     * <li>ERROR</li>
     * <li>WARN</li>
     * </ul>
     * </li>
     * <li>
     * When log level is set to DEBUG, the following are logged
     * <ul>
     * <li>ERROR</li>
     * <li>WARN</li>
     * <li>DEBUG</li>
     * </ul>
     * </li>
     * <li>When log level is set to INFO, everything is logged</li>
     * </ul>
     *
     * @param level Set Level for the entire logging system
     */
    public static void setLevel(int level) {
        Logger.level = level;
    }

    private void log(int level, String message) {
        String logMsg = "[" + now() + "] " + "[" + Thread.currentThread().getName() + "] " + "[" + BUILD_VERSION + "] " + " [" + prefixes[level] + "] " + " : "
                + message;

        if (ENABLE) {
            writeLog(level, logMsg);
        }
    }

    public void severe(String message, Exception e) {
        log(ERROR, message + "::" + e.getMessage() + "::" + e.getClass() + "::" + e.getCause() + "::" + e.toString());
    }

    public void warn(String message) {
        log(WARN, message);
    }

    public void debug(String message) {
        log(DEBUG, message);
    }

    public void info(String message) {
        log(INFO, message);
    }

    public void diagnose(String message) {
        log(DIAGNOSE, message);
    }

    public void trace(String message) {
        log(TRACE, message);
    }

    public static void closeLog() {
        if (logger != null) {
            logger.close();
        }
    }

    protected String now() {

        StringBuilder f = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        String mon = MONTHS[cal.get(Calendar.MONTH)];
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        f.append(mon);
        f.append("-");
        f.append(day);
        f.append("-");
        f.append(year);
        f.append(" ");
        f.append(hr);
        f.append(":");
        f.append(min);
        f.append(":");
        f.append(sec);
        return f.toString();
    }
}
