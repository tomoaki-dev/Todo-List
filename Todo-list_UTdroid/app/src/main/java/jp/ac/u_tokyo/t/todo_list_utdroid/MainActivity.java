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

    private List<Map<String, String>> mList;


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

        //アダプタ作成
        mList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this, mList,
                R.layout.cell_task,
                new String[]{LIST_ITEM_TEXT1, LIST_ITEM_TEXT2},
                new int[]{R.id.taskName, R.id.taskText}
        );

        // /クリックイベント処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ItemClick", "Position=" + String.valueOf(position));
            }
        });
        listView.setAdapter(adapter);

        // 表示するデータを設定
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(LIST_ITEM_TEXT1, String.valueOf(i));
            map.put(LIST_ITEM_TEXT2, "名前・・・");
            mList.add(map);

        }
    }

}