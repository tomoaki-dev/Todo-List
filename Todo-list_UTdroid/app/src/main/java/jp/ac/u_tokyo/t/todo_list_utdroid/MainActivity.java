package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String LIST_ITEM_TEXT1 = "Task";
    private static final String LIST_ITEM_TEXT2 = "Text";

    private final int ADD_TASK = 0;
    private final int EDIT_TASK = 1;
    private final int ADD_FOLDER = 2;
    private final int EDIT_FOLDER = 3;

    /* Intentにオブジェクトを添付する際もKey-Valueストアの考え方に従う */
    public final static String INTENT_KEY_RESULT = "intentKeyResult";
    public final static String INTENT_KEY_FILEPATH = "intentKeyFilePath";

    ListView listView;
    List<Task> taskList;

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    TextView addFolder;
    TextView showAll;
    TextView showDoneTask;
    TextView folderNameView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*画面横のドロワー（メニューの設定）*/
        //参考サイト　http://qiita.com/yuto_aka_ike/items/ee7511bd2fee70b4ab49
        //http://www.riaxdnp.jp/?p=6965　　　http://android.keicode.com/basics/ui-navigation-drawer.php

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mtoolbar);

        //ナビゲーションドロワーの設定
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mtoolbar,R.string.open,R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        loadFolderList();




        //-----------------------------------------------------------------------------------

        /*画面のコンテンツの設定*/
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivityForResult(intent, ADD_TASK);
            }
        });

        listView = (ListView) findViewById(R.id.list_view);

        //アダプタ作成
        //参考サイト　http://blogs.gine2.jp/taka/archives/2966
        // 表示するデータを設定
        final TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
        taskList = taskDatabase.readAllTasks();
        if (taskList.size() == 0) {
            for (int i = 0; i < 5; i++) {
                int d = i % 12 + 1;
                long time = new GregorianCalendar(2017, d, d, d, d).getTimeInMillis();
                taskDatabase.add("Task " + i, "Task", time, 0, taskDatabase.readFolder().get(0),0);
            }
            taskList = taskDatabase.readAllTasks();
        }
        listView.setAdapter(new TaskAdapter(MainActivity.this, taskList));


        // /クリックイベント処理
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listview = (ListView) parent;
                //Log.d("ItemClick", "Position=" + String.valueOf(position));
                Task task = (Task) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("id",task.getTaskID());
                startActivityForResult(intent,EDIT_TASK);


                Toast.makeText(
                        MainActivity.this,
                        "longclick Item id:"+ task.getTaskID(), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }


        });

        folderNameView = (TextView)findViewById(R.id.folder_name);
        folderNameView.setText("全て表示");

        //folder追加ボタン
        addFolder = (TextView)findViewById(R.id.add_folder);
        addFolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FolderActivity.class);
                startActivityForResult(intent, ADD_FOLDER);
            }
        });

        //全て表示
        showAll = (TextView)findViewById(R.id.show_all);
        showAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                taskList = taskDatabase.readAllTasks();
                listView.setAdapter(new TaskAdapter(MainActivity.this, taskList));
                folderNameView.setText("全て表示");
                mDrawerLayout.closeDrawers();
            }
        });

        //実行済み
        showDoneTask = (TextView)findViewById(R.id.show_done_task);
        showDoneTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                taskList = taskDatabase.readDoneTasks();
                listView.setAdapter(new TaskAdapter(MainActivity.this, taskList));
                folderNameView.setText("実行済み");
                mDrawerLayout.closeDrawers();
            }
        });

    }


    //--------------------------------------------------------------------
    // 参考サイト　http://www.riaxdnp.jp/?p=6965
    //Drawerの中身
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // アクションバー上のボタン選択時のハンドリング
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        // ActionBarDrawerToggleにイベント渡す
        // 渡さないと、ドロワーボタンを押しても開かない
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // ActionBarDrawerToggleとMainActivityの状態を同期する
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // ActionBarDrawerToggleにイベント渡す
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    //-------------------------------------------

    private void loadFolderList(){
        final  TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
        List<String> folderList = taskDatabase.readFolder();

        if(folderList.size() == 0){
            taskDatabase.createNewFolder("Default");
            folderList = taskDatabase.readFolder();
        }

        ListView mFolderListView = (ListView)findViewById(R.id.drawer_view);
        mFolderListView.setAdapter(new FolderAdapter(getApplicationContext(), folderList));

        mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>parent, View v, int position,long id) {
                String folderName = (String) parent.getItemAtPosition(position);
                taskList = taskDatabase.readTasksByFolder(folderName);
                listView.setAdapter(new TaskAdapter(MainActivity.this, taskList));

                folderNameView.setText("フォルダ: "+folderName);
                mDrawerLayout.closeDrawers();
            }
        });

        mFolderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?>parent, View v, int position,long id) {
                Intent intent = new Intent(MainActivity.this, FolderActivity.class);
                intent.putExtra("folderName",(String) parent.getItemAtPosition(position));

                startActivityForResult(intent, EDIT_FOLDER);

                return false;
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ADD_TASK && resultCode == RESULT_OK) {
            listView.setAdapter(new TaskAdapter(this, taskList));

        }else if (requestCode == EDIT_TASK && resultCode == RESULT_OK) {
            listView.setAdapter(new TaskAdapter(this,taskList));
            loadFolderList();

        }else if (requestCode == ADD_FOLDER && resultCode == RESULT_OK) {
            loadFolderList();

        }else if (requestCode == EDIT_FOLDER && resultCode == RESULT_OK){
            listView.setAdapter(new TaskAdapter(this, new TaskDatabase(this).readAllTasks()));
            folderNameView.setText("全て表示");
            loadFolderList();
        }
    }

}
