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
    private String Importance;

    /* コンストラクタ(set) */
    public Task(String taskName, String taskText, int year, int month, int day, int hour, int minute, String Importance) {
        this.set(taskName, taskText, year, month, day, hour, minute, Importance);
    }
    // for Database
    public Task(String taskName, String taskText, long deadlineTime, boolean isImportant) {
        this.taskName = taskName;
        this.taskText = taskText;
        this.deadlineTime = new GregorianCalendar();
        this.deadlineTime.setTimeInMillis(deadlineTime);
        this.isImportant = isImportant;
    }

    private void set(String taskName, String taskText, int year, int month, int day, int hour, int minute, String Importance) {
        this.taskName = taskName;
        this.taskText = taskText;
        this.deadlineTime = new GregorianCalendar(year, month-1, day, hour, minute);
        this.Importance = Importance;
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
}
