package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_ITEM_TEXT1 = "Task";
    private static final String LIST_ITEM_TEXT2 = "Text";

    private final int ADD_TASK = 0;
    private final int EDIT_TASK = 1;

    /* Intentにオブジェクトを添付する際もKey-Valueストアの考え方に従う */
    public final static String INTENT_KEY_RESULT = "intentKeyResult";
    public final static String INTENT_KEY_FILEPATH = "intentKeyFilePath";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view);

        //アダプタ作成(ネット上のソースコードコピペした部分だから教材のコードと齟齬が生じてる）
        // 表示するデータを設定
        TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
        List<Task> taskList = taskDatabase.read();
        if (taskList.size() == 0) {
            for (int i = 0; i < 15; i++) {
                int d = i % 12 + 1;
                long time = new GregorianCalendar(2017, d, d, d, d).getTimeInMillis();
                taskDatabase.add("Task " + i, "Task", time, 0);
            }
            taskList = taskDatabase.read();
        }
        listView.setAdapter(new TaskAdapter(MainActivity.this, taskList));

        // /クリックイベント処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ItemClick", "Position=" + String.valueOf(position));
            }
        });
    }
}