package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_ITEM_TEXT1 = "Task";
    private static final String LIST_ITEM_TEXT2 = "Text";

    private List<Task> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            /*このへんはいじってない*/
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view);

        //アダプタ作成(ネット上のソースコードコピペした部分だから教材のコードと齟齬が生じてる）
        taskList = new ArrayList<>();
        // 表示するデータを設定
        for (int i=0; i<100; i++) {
            int d = i%12 + 1;
            Task tmp = new Task("Task " + i, 2017, d, d, d ,d, false);
            taskList.add(tmp);
        }
        TaskAdapter adapter = new TaskAdapter(MainActivity.this, taskList);

        // /クリックイベント処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ItemClick", "Position=" + String.valueOf(position));
            }
        });
        listView.setAdapter(adapter);
    }
}