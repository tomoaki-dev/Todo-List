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

    /*
     * 参考
     * http://qiita.com/kurukurupapa@github/items/b52dee4935a6434d006b
     */

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
