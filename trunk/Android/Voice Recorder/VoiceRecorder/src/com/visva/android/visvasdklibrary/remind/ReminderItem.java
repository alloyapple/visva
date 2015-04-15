package com.visva.android.visvasdklibrary.remind;

/**
 * 
 * @author kane
 *
 */
public class ReminderItem {

    public String className = null;
    public String id = null;
    public long triggerAtMillis;
    public long intervalMillis;
    public int repeatCount;
    public boolean isRepeatMode;

    public ReminderItem(String className, String id, long triggerAtMillis, long intervalMillis, int repeatCount, boolean isRepeatMode) {
        super();
        this.className = className;
        this.id = id;
        this.triggerAtMillis = triggerAtMillis;
        this.intervalMillis = intervalMillis;
        this.repeatCount = repeatCount;
        this.isRepeatMode = isRepeatMode;
    }

}
