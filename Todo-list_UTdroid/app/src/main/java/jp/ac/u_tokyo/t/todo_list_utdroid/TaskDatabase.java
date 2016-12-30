package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shimada on 2016/12/28.
 */

public class TaskDatabase {
    /* 定数 */
    private final static String DATABASE_NAME = "TaskList.db";
    private final static String ACTIVE_TASK_TABLE = "activeTaskTable";
    private final static String ARCHIVE_TASK_TABLE = "archiveTaskTable";
    private final static int DATABASE_VERSION = 1;

//    private final static String taskID = "taskID"
//    private final static String taskName = "taskName";
//    private final static String taskText = "taskText";
//    private final static String deadlineTime = "deadlineTime";
//    private final static String isImportant = "isImportant";

    /* 内部に保持 */
    private SQLiteDatabase database;
    private TaskDatabaseHelper taskHelper;

    public TaskDatabase(Context context) {
        taskHelper = new TaskDatabaseHelper(context);
    }

    /* デーダベースに追加 */
    public void add(String taskName, String taskText, long deadlineTime, int isImportant) {
        database = taskHelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("taskName", taskName);
        content.put("taskText", taskText);
        content.put("deadlineTime", deadlineTime);
        // 0 - 3
        content.put("taskImportance", isImportant);
        database.insert(ACTIVE_TASK_TABLE, null, content);
        database.close();
    }

    /* 配列に読み込み */
    public List<Task> read() {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = database.query(ACTIVE_TASK_TABLE, null, null, null, null, null, null);
        /* 一つ目に移動しつつ存在を確認 */
        if (cursor.moveToFirst()) {
            do {
                int taskID = cursor.getInt(cursor.getColumnIndex("taskID"));
                String taskName = cursor.getString(cursor.getColumnIndex("taskName"));
                String taskText = cursor.getString(cursor.getColumnIndex("taskText"));
                long deadlineTime = cursor.getLong(cursor.getColumnIndex("deadlineTime"));
                int taskImportance = cursor.getInt(cursor.getColumnIndex("taskImportance"));
                Task task = new Task(taskID, taskName, taskText, deadlineTime, taskImportance);
                taskList.add(task);
                Log.d("read", "size = " + taskList.size());
            } while (cursor.moveToNext());
        } // else 0件
        cursor.close();
        database.close();
        return taskList;
    }

    void delete(Task task) {
        // アーカイブ時の処理
        database = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskID", task.getTaskID());
        contentValues.put("taskName", task.getName());
        contentValues.put("taskText", task.getText());
        contentValues.put("deadlineTime", task.getDeadlineTime().getTimeInMillis());
        contentValues.put("taskImportance", task.getTaskImportance());
        database.insert(ARCHIVE_TASK_TABLE, null, contentValues);
        database.delete(ACTIVE_TASK_TABLE, "TaskID=?", new String[]{String.valueOf(task.getTaskID())});
        database.close();
    }

    /* Helper (内部クラス) */
    private class TaskDatabaseHelper extends SQLiteOpenHelper {
        TaskDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            String createTable = " ( "
                    + "taskID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "taskName TEXT NOT NULL, "
                    + "taskText TEXT, "
                    + "deadlineTime INTEGER NOT NULL, "
                    + "taskImportance INTEGER NOT NULL );";
            Log.d("onCreate", createTable);
            database.execSQL("CREATE TABLE " + ACTIVE_TASK_TABLE + createTable);
            database.execSQL("CREATE TABLE " + ARCHIVE_TASK_TABLE + createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase taskDatabase, int oldVersion, int newVersion) {
            // do nothing
        }
    }
}
