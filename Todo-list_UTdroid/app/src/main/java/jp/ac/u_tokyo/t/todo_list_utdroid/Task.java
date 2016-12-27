package jp.ac.u_tokyo.t.todo_list_utdroid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by shimada on 2016/12/22.
 */

public class Task {
    /* メンバ変数 */
    private String taskName;
    private String taskText;
    private Calendar deadlineTime;
    private boolean isImportant;

    /* コンストラクタ(set) */
    public Task(String taskName, String taskText, int year, int month, int day, int hour, int minute, boolean isImportant) {
        this.set(taskName, taskText, year, month, day, hour, minute, isImportant);
    }

    private void set(String taskName, String taskText, int year, int month, int day, int hour, int minute, boolean isImportant) {
        this.taskName = taskName;
        this.taskText = taskText;
        this.deadlineTime = new GregorianCalendar(year, month-1, day, hour, minute);
        this.isImportant = isImportant;
    }

    public String getName() {
        return taskName;
    }
    public String getText() { return taskText; }
    public Calendar getDeadlineTime() {
        return deadlineTime;
    }
    public long remainDay() {
        return (deadlineTime.getTimeInMillis() - System.currentTimeMillis()) / TimeUnit.DAYS.toMillis(1);
    }
    public boolean isImportant() {
        return isImportant;
    }
}
