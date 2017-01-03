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
    private final static String TASK_TABLE = "taskTable";
    private final static int DATABASE_VERSION = 1;

    /* SQL 列の名前 */
    private final static String TASK_ID = "taskID";
    private final static String TASK_NAME = "taskName";
    private final static String TASK_TEXT = "taskText";
    private final static String DEADLINE_TIME = "deadlineTime";
    private final static String TASK_IMPORTANCE = "taskImportance";

    /* メンバ変数 */
    private SQLiteDatabase database;
    private TaskDatabaseHelper taskHelper;

    TaskDatabase(Context context) {
        taskHelper = new TaskDatabaseHelper(context);
    }

    /* デーダベースに追加 */
    void add(String taskName, String taskText, long deadlineTime, int taskImportance) {
        database = taskHelper.getWritableDatabase();
        database.insert(TASK_TABLE, null, getContentValues(taskName, taskText, deadlineTime, taskImportance));
        database.close();
    }

    /* IDを指定して追加 */
    void add(int taskID, String taskName, String taskText, long deadlineTime, int taskImportance) {
        database = taskHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(taskName, taskText, deadlineTime, taskImportance);
        contentValues.put(TASK_ID, taskID);
        database.update(TASK_TABLE, contentValues, TASK_ID + "=?", new String[]{String.valueOf(taskID)});
        database.close();
    }

    /* 配列に読み込み */
    List<Task> read() {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = database.query(TASK_TABLE, null, null, null, null, null, null);
        /* 一つ目に移動しつつ存在を確認 */
        if (cursor.moveToFirst()) {
            int taskIDColumnNumber = cursor.getColumnIndex(TASK_ID);
            int taskNameColumnNumber = cursor.getColumnIndex(TASK_NAME);
            int taskTextColumnNumber = cursor.getColumnIndex(TASK_TEXT);
            int deadlineTimeColumnNumber = cursor.getColumnIndex(DEADLINE_TIME);
            int taskImportanceColumnNumber = cursor.getColumnIndex(TASK_IMPORTANCE);

            do {
                int taskID = cursor.getInt(taskIDColumnNumber);
                String taskName = cursor.getString(taskNameColumnNumber);
                String taskText = cursor.getString(taskTextColumnNumber);
                long deadlineTime = cursor.getLong(deadlineTimeColumnNumber);
                int taskImportance = cursor.getInt(taskImportanceColumnNumber);
                taskList.add(new Task(taskID, taskName, taskText, deadlineTime, taskImportance));
            } while (cursor.moveToNext());
        } // else 0件
        cursor.close();
        database.close();
        return taskList;
    }

    void delete(Task task) {
        // 一時的な処理
        database = taskHelper.getWritableDatabase();
        database.delete(TASK_TABLE, TASK_ID + "=?", new String[]{String.valueOf(task.getTaskID())});
        database.close();
    }

    /* IDからタスクを検索 */
    Task getTaskById(int taskID) {
        database = taskHelper.getReadableDatabase();
        Cursor cursor = database.query(TASK_TABLE, null, TASK_ID + "=?", new String[]{String.valueOf(taskID)}, null, null, null);
        if (!cursor.moveToFirst()) {
            Log.d("getTaskById", "error");
        }
        String taskName = cursor.getString(cursor.getColumnIndex(TASK_NAME));
        String taskText = cursor.getString(cursor.getColumnIndex(TASK_TEXT));
        long deadlineTime = cursor.getLong(cursor.getColumnIndex(DEADLINE_TIME));
        int taskImportance = cursor.getInt(cursor.getColumnIndex(TASK_IMPORTANCE));
        cursor.close();
        database.close();
        return new Task(taskID, taskName, taskText, deadlineTime, taskImportance);
    }

    /* Helper (内部クラス) */
    private class TaskDatabaseHelper extends SQLiteOpenHelper {
        TaskDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            String createTaskTable = "CREATE TABLE " + TASK_TABLE + " ("
                    + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TASK_NAME + " TEXT NOT NULL, "
                    + TASK_TEXT + " TEXT, "
                    + DEADLINE_TIME + " INTEGER NOT NULL, "
                    + TASK_IMPORTANCE + " INTEGER NOT NULL);";
            Log.d("onCreate", createTaskTable);
            database.execSQL(createTaskTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase taskDatabase, int oldVersion, int newVersion) {
            // do nothing
        }
    }

    private ContentValues getContentValues(String taskName, String taskText, long deadlineTime, int taskImportance) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, taskName);
        contentValues.put(TASK_TEXT, taskText);
        contentValues.put(DEADLINE_TIME, deadlineTime);
        contentValues.put(TASK_IMPORTANCE, taskImportance);
        return contentValues;
    }
}
