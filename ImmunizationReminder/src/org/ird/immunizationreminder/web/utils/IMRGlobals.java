package org.ird.immunizationreminder.web.utils;

public class IMRGlobals {
public static final String WEB_LOG_FILE_NAME="/ImmuRemLogs/IRWeb/IRWeblog.log";
public static final String MODEM_LOG_FILE_NAME="/ImmuRemLogs/IRModem/IRModemlog.log";
public static final String FAILED_FLAG_FILE_NAME="/ImmuRemLogs/UnsentFlagEmails/unsentFlagEmails.txt";
public static final String RECEIVED_RESPONSE_FILE_NAME="/ImmuRemLogs/RecievedResponse/recievedResponse.txt";
public static final String UNSAVED_RESPONSE_FILE_NAME="/ImmuRemLogs/UnsavedResponse/unsavedresponse.txt";
public static final String UNSAVED_REMINDER_FILE_NAME="/ImmuRemLogs/UnsavedReminder/unsavedreminder.txt";
public static final String UNSENT_REMINDER_FILE_NAME="/ImmuRemLogs/UnsentReminder/unsentreminder.txt";
public static final String DAILY_DETAILED_LOG_FILE_NAME="/ImmuRemLogs/DailyDetailedLog/dailyDetailedLog.txt";
public static final String FILE_SEPAREATOR = System.getProperty("file.separator");
public static final String WEB_LOG_FOLDER_PATH="/ImmuRemLogs/IRWeb";
public static final String MODEM_LOG_FOLDER_PATH="/ImmuRemLogs/IRModem";
public static final String FAILED_FLAG_FOLDER_PATH="/ImmuRemLogs/UnsentFlagEmails";
public static final String RECEIVED_RESPONSE_FOLDER_PATH="/ImmuRemLogs/RecievedResponse";
public static final String UNSAVED_RESPONSE_FOLDER_PATH="/ImmuRemLogs/UnsavedResponse";
public static final String UNSAVED_REMINDER_FOLDER_PATH="/ImmuRemLogs/UnsavedReminder";
public static final String UNSENT_REMINDER_FOLDER_PATH="/ImmuRemLogs/UnsentReminder";
public static final String DAILY_DETAILED_LOG_FOLDER_PATH="/ImmuRemLogs/DailyDetailedLog";
public static final String UPLOAD_TEMP_DIR="/ImmuRemLogs/UploadTemp";
public static final String CSV_DELIMITER_CHARACTER="\t";
public static final String[] EXPECTED_CSV_DATE_FORMATS=new String[]{"MM/dd/yyyy HH:mm:ss","MM/dd/yyyy"};
//public static final String ALL_LOGS_FOLDER_PATH=System.getProperty("user.home")+"/InteractiveRemLogs";

//donot change the format without seeing effect in java , javascript scw, date.js codes used in application
public static final String GLOBAL_DATE_FORMAT="dd-MM-yyyy";

public static final int MAX_REMINDER_1_7_VALIDITY_DAYS = 6;

public static Integer REMINDER_PUSHER_TIMER_INTERVAL_MIN;
public static Integer REMINDER_UPDATER_TIMER_INTERVAL_MIN;
public static Integer RESPONSE_READER_TIMER_INTERVAL_MIN;

public static Integer SMS_TARSEEL_PROJECT_ID;
}
