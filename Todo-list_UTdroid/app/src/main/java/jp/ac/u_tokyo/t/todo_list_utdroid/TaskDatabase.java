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

/*
 * 参考
 * https://developer.android.com/training/basics/data-storage/databases.html?hl=ja
 * http://asky.hatenablog.com/entry/2016/05/08/013038
 * http://tomcky.hatenadiary.jp/entry/2013/08/25/102029
 */

public class TaskDatabase {
    /* 定数 */
    private final static String DATABASE_NAME = "TaskList.db";
    private final static String TASK_TABLE = "taskTable";
    private final static String FOLDER_TABLE = "folderTable";
    private final static int DATABASE_VERSION = 1;

    /* SQL 列の名前 */
    private final static String TASK_ID = "taskID";
    private final static String TASK_NAME = "taskName";
    private final static String TASK_TEXT = "taskText";
    private final static String DEADLINE_TIME = "deadlineTime";
    private final static String TASK_IMPORTANCE = "taskImportance";
    private final static String FOLDER_NAME = "folderName";
    private final static String COMPLETE_TIME = "completeTime";

    /* メンバ変数 */
    private SQLiteDatabase database;
    private TaskDatabaseHelper taskHelper;

    TaskDatabase(Context context) {
        taskHelper = new TaskDatabaseHelper(context);
    }

    /* デーダベースに追加 */
    void add(String taskName, String taskText, long deadlineTime, int taskImportance, String folderName,long completeTime) {
        database = taskHelper.getWritableDatabase();
        database.insert(TASK_TABLE, null, makeContentValues(taskName, taskText, deadlineTime, taskImportance, folderName, completeTime));
        database.close();
    }

    /* IDを指定して追加 */
    void update(int taskID, String taskName, String taskText, long deadlineTime, int taskImportance, String folderName, long completeTime) {
        database = taskHelper.getWritableDatabase();
        database.update(TASK_TABLE, makeContentValues(taskName, taskText, deadlineTime, taskImportance, folderName, completeTime),
                TASK_ID + "=?", new String[]{String.valueOf(taskID)});
        database.close();
    }

    /* 配列に読み込み */
    List<Task> readAllTasks() {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = database.query(TASK_TABLE, null, DEADLINE_TIME + "!=0 AND " + COMPLETE_TIME + "=0", null, null, null, DEADLINE_TIME);
        /* 一つ目に移動しつつ存在を確認 */
        if (cursor.moveToFirst()) {
            int taskIDColumnNumber = cursor.getColumnIndex(TASK_ID);
            int taskNameColumnNumber = cursor.getColumnIndex(TASK_NAME);
            int taskTextColumnNumber = cursor.getColumnIndex(TASK_TEXT);
            int deadlineTimeColumnNumber = cursor.getColumnIndex(DEADLINE_TIME);
            int taskImportanceColumnNumber = cursor.getColumnIndex(TASK_IMPORTANCE);
            int folderNameColumnNumber = cursor.getColumnIndex(FOLDER_NAME);
            int completeTimeColumnNumber = cursor.getColumnIndex(COMPLETE_TIME);

            do {
                int taskID = cursor.getInt(taskIDColumnNumber);
                String taskName = cursor.getString(taskNameColumnNumber);
                String taskText = cursor.getString(taskTextColumnNumber);
                long deadlineTime = cursor.getLong(deadlineTimeColumnNumber);
                int taskImportance = cursor.getInt(taskImportanceColumnNumber);
                String folderName = cursor.getString(folderNameColumnNumber);
                long completeTime = cursor.getLong(completeTimeColumnNumber);
                taskList.add(new Task(taskID, taskName, taskText, deadlineTime, taskImportance, folderName, completeTime));
            } while (cursor.moveToNext());
        } // else 0件
        cursor = database.query(TASK_TABLE, null, DEADLINE_TIME + "=0 AND " + COMPLETE_TIME + "=0", null, null, null, TASK_ID);
        if (cursor.moveToFirst()) {
            int taskIDColumnNumber = cursor.getColumnIndex(TASK_ID);
            int taskNameColumnNumber = cursor.getColumnIndex(TASK_NAME);
            int taskTextColumnNumber = cursor.getColumnIndex(TASK_TEXT);
            int deadlineTimeColumnNumber = cursor.getColumnIndex(DEADLINE_TIME);
            int taskImportanceColumnNumber = cursor.getColumnIndex(TASK_IMPORTANCE);
            int folderNameColumnNumber = cursor.getColumnIndex(FOLDER_NAME);

            do {
                int taskID = cursor.getInt(taskIDColumnNumber);
                String taskName = cursor.getString(taskNameColumnNumber);
                String taskText = cursor.getString(taskTextColumnNumber);
                long deadlineTime = cursor.getLong(deadlineTimeColumnNumber);
                int taskImportance = cursor.getInt(taskImportanceColumnNumber);
                String folderName = cursor.getString(folderNameColumnNumber);
                taskList.add(new Task(taskID, taskName, taskText, deadlineTime, taskImportance, folderName, 0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return taskList;
    }

    /*フォルダごとに配列に読み込み*/
    List<Task> readTasksByFolder(String folderName) {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        Cursor cursor = database.query(TASK_TABLE, null, FOLDER_NAME + "=? AND " + COMPLETE_TIME + "=0", new String[]{folderName}, null, null, FOLDER_NAME);
        /* 一つ目に移動しつつ存在を確認 */
        if (cursor.moveToFirst()) {
            int taskIDColumnNumber = cursor.getColumnIndex(TASK_ID);
            int taskNameColumnNumber = cursor.getColumnIndex(TASK_NAME);
            int taskTextColumnNumber = cursor.getColumnIndex(TASK_TEXT);
            int deadlineTimeColumnNumber = cursor.getColumnIndex(DEADLINE_TIME);
            int taskImportanceColumnNumber = cursor.getColumnIndex(TASK_IMPORTANCE);
            int completeTimeColumnNumber = cursor.getColumnIndex(COMPLETE_TIME);

            do {
                int taskID = cursor.getInt(taskIDColumnNumber);
                String taskName = cursor.getString(taskNameColumnNumber);
                String taskText = cursor.getString(taskTextColumnNumber);
                long deadlineTime = cursor.getLong(deadlineTimeColumnNumber);
                int taskImportance = cursor.getInt(taskImportanceColumnNumber);
                long completeTime = cursor.getLong(completeTimeColumnNumber);
                taskList.add(new Task(taskID, taskName, taskText, deadlineTime, taskImportance, folderName, completeTime));
            } while (cursor.moveToNext());
        } // else 0件
        cursor.close();
        database.close();
        return taskList;
    }

    List<Task> readDoneTasks() {
        database = taskHelper.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();
        // sort?
        Cursor cursor = database.query(TASK_TABLE, null, COMPLETE_TIME + "!=0", null, null, null, null);
        /* 一つ目に移動しつつ存在を確認 */
        if (cursor.moveToFirst()) {
            int taskIDColumnNumber = cursor.getColumnIndex(TASK_ID);
            int taskNameColumnNumber = cursor.getColumnIndex(TASK_NAME);
            int taskTextColumnNumber = cursor.getColumnIndex(TASK_TEXT);
            int deadlineTimeColumnNumber = cursor.getColumnIndex(DEADLINE_TIME);
            int taskImportanceColumnNumber = cursor.getColumnIndex(TASK_IMPORTANCE);
            int folderNameColumnNumber = cursor.getColumnIndex(FOLDER_NAME);
            int completeTimeColumnNumber = cursor.getColumnIndex(COMPLETE_TIME);

            do {
                int taskID = cursor.getInt(taskIDColumnNumber);
                String taskName = cursor.getString(taskNameColumnNumber);
                String taskText = cursor.getString(taskTextColumnNumber);
                long deadlineTime = cursor.getLong(deadlineTimeColumnNumber);
                int taskImportance = cursor.getInt(taskImportanceColumnNumber);
                String folderName = cursor.getString(folderNameColumnNumber);
                long completeTime = cursor.getLong(completeTimeColumnNumber);
                taskList.add(new Task(taskID, taskName, taskText, deadlineTime, taskImportance, folderName, completeTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return taskList;
    }

    // 本当はTaskクラスでやるべきな気がする…
    void setTaskCompleted(Task task, boolean isCompleted) {
        // 一時的な処理
        database = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long completeTime = 0;
        if (isCompleted) {
            completeTime = System.currentTimeMillis();
        }
        contentValues.put(COMPLETE_TIME, completeTime);
        database.update(TASK_TABLE, contentValues, TASK_ID + "=?", new String[]{String.valueOf(task.getTaskID())});
        database.close();
    }

    //タスクの削除
    void delete(Task task) {
        // 一時的な処理
        database = taskHelper.getWritableDatabase();
        database.delete(TASK_TABLE, TASK_ID + "=?", new String[]{String.valueOf(task.getTaskID())});
        database.close();
    }

    //フォルダの削除
    void deleteFolder(String folderName) {
        // フォルダの中身を削除
        database = taskHelper.getWritableDatabase();
        database.delete(TASK_TABLE, FOLDER_NAME + " =?", new String[]{folderName});

        //フォルダそのものを削除
        database.delete(FOLDER_TABLE, FOLDER_NAME + "=?", new String[]{folderName});
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
        String folderName = cursor.getString(cursor.getColumnIndex(FOLDER_NAME));
        long completeTime = cursor.getLong(cursor.getColumnIndex(COMPLETE_TIME));
        cursor.close();
        database.close();
        return new Task(taskID, taskName, taskText, deadlineTime, taskImportance, folderName, completeTime);
    }

    /* フォルダ作成 */
    void createNewFolder(String folderName) {
        database = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOLDER_NAME, folderName);
        database.insert(FOLDER_TABLE, null, contentValues);
    }

    //フォルダ編集
    void updateFolder(String oldFolderName, String newFolderName){
        database = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOLDER_NAME, newFolderName);
        database.update(FOLDER_TABLE, contentValues, FOLDER_NAME + "=?", new String[]{oldFolderName});
        database.update(TASK_TABLE, contentValues, FOLDER_NAME + "=?", new String[]{oldFolderName});

    }

    // 要検討
    List<String> readFolder() {
        List<String> folderList = new ArrayList<>();
        database = taskHelper.getReadableDatabase();
        Cursor cursor = database.query(FOLDER_TABLE, null, null, null, null, null, FOLDER_NAME);
        if (cursor.moveToFirst()) {
            int folderNameColumn = cursor.getColumnIndex(FOLDER_NAME);
            do {
                folderList.add(cursor.getString(folderNameColumn));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return folderList;
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
                    + TASK_IMPORTANCE + " INTEGER NOT NULL, "
                    + FOLDER_NAME + " TEXT,"
                    + COMPLETE_TIME + " INTEGER);";
            Log.d("onCreate", createTaskTable);
            database.execSQL(createTaskTable);
            String createFolderTable = "CREATE TABLE " + FOLDER_TABLE + " ("
                    + FOLDER_NAME + " TEXT NOT NULL);";
            database.execSQL(createFolderTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase taskDatabase, int oldVersion, int newVersion) {
            // do nothing
        }
    }

    private ContentValues makeContentValues(String taskName, String taskText, long deadlineTime, int taskImportance, String folderName, long completeTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, taskName);
        contentValues.put(TASK_TEXT, taskText);
        contentValues.put(DEADLINE_TIME, deadlineTime);
        contentValues.put(TASK_IMPORTANCE, taskImportance);
        contentValues.put(FOLDER_NAME, folderName);
        contentValues.put(COMPLETE_TIME, completeTime);
        return contentValues;
    }
}
