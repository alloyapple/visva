package com.visva.android.visvasdklibrary.remind;

import android.content.Context;
/**
 * 
 * @author kane
 *
 */
public interface IReminder {
    public boolean onRemind(Context context, ReminderItem reminderItem);
}
