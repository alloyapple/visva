package com.visva.android.visvasdklibrary.log;

import java.util.Locale;

import com.visva.android.visvasdklibrary.constant.AIOConstant;

import android.util.Log;

/** Logging helper class. Refer to VolleyLog */
public class AIOLog {
    public static String  TAG    = AIOLog.class.getSimpleName();

    public static boolean ASSERT = false;

    // private static final String LOG_FILE_PATH = Environment.getDataDirectory() + File.separator + "/data/com.visva.android.visvasdklibrary/log";
    // public static boolean LOG4J = true;
    // private static Logger logger = null;

    // static {
    // if(LOG4J){
    // logger = Logger.getLogger(AllInOneLog.class);
    // try {
    // configureLogger(logger);
    // }catch (Exception e){
    // AllInOneLog.asserting(e, "SA file logger is not configure");
    // }
    // }
    // }

    // static public void configureLogger(Logger logger) {
    // final String filePattern = "%m%n";
    // final RollingFileAppender rollingFileAppender;
    // final Layout fileLayout = new PatternLayout(filePattern);
    // try {
    // rollingFileAppender = new RollingFileAppender(fileLayout, LOG_FILE_PATH + File.separator + "AllinOne.log");
    // } catch (final IOException e) {
    // throw new RuntimeException("Exception configuring log system", e);
    // }
    // rollingFileAppender.setMaxBackupIndex(1);
    // rollingFileAppender.setMaximumFileSize(1024*512);
    // rollingFileAppender.setImmediateFlush(true);
    //
    // logger.addAppender(rollingFileAppender);
    // logger.setLevel(Level.DEBUG);
    // }

    /**
     * Customize the log tag for your application, so that other apps
     * using Volley don't mix their logs with yours. <br />
     * Enable the log property for your tag before starting your app: <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
        TAG = tag;
    }

    public static void v(String format, String args) {
        if (AIOConstant.DEBUG_MODE) {
            String message = buildMessage(format, args);
            // if(LOG4J)
            // logger.info("[V] " + message);
            Log.v(TAG, message);
        }
    }

    public static void d(String format, String args) {

        String message = buildMessage(format, args);
        // if(LOG4J)
        // logger.debug("[D] " + message);
        Log.d(TAG, message);
    }

    public static void e(String format, String args) {

        String message = buildMessage(format, args);
        // if(LOG4J)
        // logger.error("[E] " + message);
        Log.e(TAG, message);
    }

    public static void e(Throwable tr, String format, String args) {

        String message = buildMessage(format, args);
        // if(LOG4J)
        // logger.error("[E] " + message);
        Log.e(TAG, message, tr);
    }

    public static void asserting(Exception ex, String message) {
        if (AIOConstant.DEBUG_MODE) {
            String msg = buildMessage(TAG, message);
            Exception testEx = ex;
            if (testEx == null)
                testEx = new Exception(message);

            e(testEx.toString(), msg);
            // if(LOG4J){
            // logger.fatal("[E] " + msg, testEx);
            // }
        }

        if (ASSERT) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, String args) {
        String msg = args;
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(AIOLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        // if(LOG4J)
        return String.format(Locale.US, "%d:[%d] %s: %s", System.currentTimeMillis(), Thread.currentThread().getId(), caller, msg);
        // else
        // return String.format(Locale.US, "[%d] %s: %s",
        // Thread.currentThread().getId(), caller, msg);
    }
}
