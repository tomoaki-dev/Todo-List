package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final String LIST_ITEM_TEXT1 = "Task";
    private static final String LIST_ITEM_TEXT2 = "Text";

    private final int ADD_TASK = 0;
    private final int EDIT_TASK = 1;

    /* Intentにオブジェクトを添付する際もKey-Valueストアの考え方に従う */
    public final static String INTENT_KEY_RESULT = "intentKeyResult";
    public final static String INTENT_KEY_FILEPATH = "intentKeyFilePath";

    ListView listView;
    List<Task> taskList;

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    ArrayList<String> folderList = new ArrayList<>();
    Map<Integer,String> folderMap = new LinkedHashMap<>();

    TextView addFolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*画面横のドロワー（メニューの設定）*/

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

        roadFolderList();


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
        // 表示するデータを設定
        TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
        taskList = taskDatabase.read();
        if (taskList.size() == 0) {
            for (int i = 0; i < 5; i++) {
                int d = i % 12 + 1;
                long time = new GregorianCalendar(2017, d, d, d, d).getTimeInMillis();
                taskDatabase.add("Task " + i, "Task", time, 0, 0);
            }
            taskList = taskDatabase.read();
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


        //folder追加ボタン
        addFolder = (TextView)findViewById(R.id.add_folder);
        addFolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FolderAddActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            listView.setAdapter(new TaskAdapter(this, new TaskDatabase(this).read()));
            roadFolderList();
        }
    }

    //--------------------------------------------------------------------
    //Drawerの中身
    private void setFolderView(){
        ListView mListView = (ListView)findViewById(R.id.drawer_view);

        mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cell_folder,folderList));
    }
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

    private void roadFolderList(){
        TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
        folderMap = taskDatabase.readFolder();

        if(folderMap==null){
            taskDatabase.createNewFolder("Default");
        }

        folderList.clear();
        for(Integer folderID : folderMap.keySet()){
            folderList.add(folderMap.get(folderID));
        }

        setFolderView();
    }

}
