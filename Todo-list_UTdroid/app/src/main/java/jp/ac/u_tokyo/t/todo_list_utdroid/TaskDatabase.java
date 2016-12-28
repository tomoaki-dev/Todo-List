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
    private final static String DATABASE_NAME = "database";
    private final static String ACTIVE_TASK_TABLE = "activeTaskTable";
    private final static String ARCHIVE_TASK_TABLE = "archiveTaskTable";
    private final static int DATABASE_VERSION = 1;

    private final static String taskName = "taskName";
    private final static String taskText = "taskText";
    private final static String deadlineTime = "deadlineTime";
    private final static String isImportant = "isImportant";

    private SQLiteDatabase database;
    private TaskDatabaseHelper taskHelper;

    public TaskDatabase(Context context) {
        taskHelper = new TaskDatabaseHelper(context);
    }

    public void add(String newTaskName, String newTaskText, long newDeadlineTime, int newIsImportant) {
        database = taskHelper.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(taskName, newTaskName);
        content.put(taskText, newTaskText);
        content.put(deadlineTime, newDeadlineTime);
        // true: 1, false: 0
        content.put(isImportant, newIsImportant);
        database.insert(ACTIVE_TASK_TABLE, null, content);
        database.close();
    }
    public List<Task> read() {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = database.query(ACTIVE_TASK_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String readTaskName = cursor.getString(cursor.getColumnIndex(taskName));
                String readTaskText = cursor.getString(cursor.getColumnIndex(taskText));
                long readDeadlineTime = cursor.getLong(cursor.getColumnIndex(deadlineTime));
                int readIsImportant = cursor.getInt(cursor.getColumnIndex(isImportant));
                Task task = new Task(readTaskName, readTaskText, readDeadlineTime, (readIsImportant == 1));
                taskList.add(task);
                Log.d("read", "size = " + taskList.size());
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return taskList;
    }

    private class TaskDatabaseHelper extends SQLiteOpenHelper {
        TaskDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            String createTable = " ( "
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "taskName TEXT NOT NULL, "
                    + "taskText TEXT, "
                    + "deadlineTime INTEGER NOT NULL, "
                    + "isImportant INTEGER NOT NULL );";
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
