package jp.ac.u_tokyo.t.todo_list_utdroid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by shimada on 2016/12/22.
 */

public class Task {
    /* メンバ変数 */
    private int taskID;
    private String taskName;
    private String taskText;
    private Calendar deadlineTime;
    /* 0 - 3 */
    private int taskImportance;
    private String folderName;
    private Calendar completeTime;

    /* コンストラクタ(set) */
/*    public Task(String taskName, String taskText, int year, int month, int day, int hour, int minute, int taskImportance) {
        this.set(taskName, taskText, year, month, day, hour, minute, taskImportance);
    }*/
    // for Database
    public Task(int taskID, String taskName, String taskText, long deadlineTime,
                int taskImportance, String folderName, long completeTime) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskText = taskText;
        this.deadlineTime = new GregorianCalendar();
        this.deadlineTime.setTimeInMillis(deadlineTime);
        this.taskImportance = taskImportance;
        this.folderName = folderName;
        this.completeTime = new GregorianCalendar();
        this.completeTime.setTimeInMillis(completeTime);
    }

/*    private void set(String taskName, String taskText, int year, int month, int day, int hour, int minute, int taskImportance) {
        this.taskName = taskName;
        this.taskText = taskText;
        this.deadlineTime = new GregorianCalendar(year, month-1, day, hour, minute);
        this.taskImportance = taskImportance;
    }*/

    public int getTaskID() {
        return taskID;
    }
    public String getName() {
        return taskName;
    }
    public String getText() {
        return taskText;
    }
    public Calendar getDeadlineTime() {
        return deadlineTime;
    }
    public long remainDay() {
        return (deadlineTime.getTimeInMillis() - System.currentTimeMillis()) / TimeUnit.DAYS.toMillis(1);
    }
    public int getTaskImportance() {
        return taskImportance;
    }
    public String getFolderName() {
        return folderName;
    }
    public Calendar getCompleteTime() {
        return completeTime;
    }
}
