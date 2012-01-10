package com.xtremelabs.robolectric.shadows;

import android.util.Log;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Shadow {@link Log} class that redirects Android logging statements to console and/or log file.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
@Implements(Log.class)
public class ShadowLog {
    @RealObject
    private Log realLog;

    private static List<LogItem> logs = new ArrayList<LogItem>();

    private static final String FLAG_VERBOSE = "V";
    private static final String FLAG_DEBUG = "D";
    private static final String FLAG_INFO = "I";
    private static final String FLAG_WARN = "W";
    private static final String FLAG_ERROR = "E";

    private static final String DEFAULT_FILENAME = "logcat.txt";

    private static boolean isConsoleEnabled = false;
    private static boolean isFileEnabled = false;
    private static BufferedWriter buffer = null;
    private static String filename = null;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static String lastLogMessage = null;

    @Implementation
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    @Implementation
    public static void e(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.error, tag, msg, throwable));
        log(FLAG_ERROR, tag, msg);
    }

    @Implementation
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    @Implementation
    public static void d(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.debug, tag, msg, throwable));
        log(FLAG_DEBUG, tag, msg);
    }

    @Implementation
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    @Implementation
    public static void i(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.info, tag, msg, throwable));
        log(FLAG_INFO, tag, msg);
    }

    @Implementation
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    @Implementation
    public static void v(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.verbose, tag, msg, throwable));
        log(FLAG_VERBOSE, tag, msg);
    }

    @Implementation
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    @Implementation
    public static void w(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.warning, tag, msg, throwable));
        log(FLAG_WARN, tag, msg);
    }

    @Implementation
    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    @Implementation
    public static void wtf(String tag, String msg, Throwable throwable) {
        logs.add(new LogItem(LogType.wtf, tag, msg, throwable));
    }

    public static List<LogItem> getLogs() {
        return logs;
    }

    public static void reset() {
        logs.clear();
    }

    public static enum LogType {
        debug, error, info, verbose, warning, wtf
    }

    public static class LogItem {
        public final LogType type;
        public final String tag;
        public final String msg;
        public final Throwable throwable;

        public LogItem(LogType type, String tag, String msg, Throwable throwable) {
            this.type = type;
            this.tag = tag;
            this.msg = msg;
            this.throwable = throwable;
        }
    }

    /**
     * Enables console logging.
     */
    public static void enableConsoleLogging() {
        isConsoleEnabled = true;
    }

    /**
     * Disables console logging.
     */
    public static void disableConsoleLogging() {
        isConsoleEnabled = false;
    }

    /**
     * Enables file logging.
     */
    public static void enableFileLogging() {
        enableFileLogging(DEFAULT_FILENAME);
    }

    /**
     * Enables file logging to specified file.
     *
     * @param filename Target file to write logact output.
     */
    public static void enableFileLogging(String filename) {
        ShadowLog.filename = filename;
        isFileEnabled = true;
    }

    /**
     * Disables file logging.
     */
    public static void disableFileLogging() {
        isFileEnabled = false;
    }

    /**
     * Log statement in standard Android logcat format.
     *
     * @param level Android logging level flag.
     * @param tag Android logging tag.
     * @param msg Android logging message.
     */
    private static void log(String level, String tag, String msg) {
        log(sdf.format(new Date()) + ": " + level + "/" + tag + ": " + msg);
    }

    /**
     * Log system line separator to all available output streams.
     */
    private static void log() {
        log("");
    }

    /**
     * Log statement to all available output streams.
     *
     * @param s The string to print to the target stream.
     */
    private static void log(String s) {
        lastLogMessage = s;

        if (isConsoleEnabled) {
            println(s);
        }

        if (isFileEnabled) {
            writeBuffer(s);
        }
    }

    /**
     * Wrapper for System.out.println(String).
     *
     * @param s The string to print to the target stream.
     */
    private static void println(String s) {
        System.out.println(s);
    }

    /**
     * Write a string to file output.
     *
     * @param s The string to print to the target stream.
     */
    private static void writeBuffer(String s) {
        if (buffer == null) {
            initBuffer();
        }

        try {
            buffer.append(s);
            buffer.newLine();
            buffer.flush();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Open {@link BufferedWriter} for file output.
     */
    private static void initBuffer() {
        try {
            buffer = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Get most recent message logged by the system.
     *
     * @return Most recent log message.
     */
    public static String getLastLogMessage() {
        return lastLogMessage;
    }

    /**
     * Segments the log by test method.
     *
     * @param method Test method.
     */
    public static void logTest(Method method) {
        log();
        log("Test Log: " + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
        log("-----------------------------------------------------------------------------");
    }

}
