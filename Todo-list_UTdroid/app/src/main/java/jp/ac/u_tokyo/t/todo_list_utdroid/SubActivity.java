package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.date_view;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.switch1;

/**
 * Created by 智明 on 2016/12/25.
 */

public class SubActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskText;

    Calendar calendar = Calendar.getInstance();
    int year, month, day;


    //ListView listView = (ListView)findViewById(R.id.list_view);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        editTaskName = (EditText) findViewById(R.id.editTaskName);
        editTaskText = (EditText) findViewById(R.id.editTaskText);

        final TextView dateView = (TextView)findViewById(date_view);

        Switch s1 = (Switch) findViewById(R.id.switch1);

        dateView.setVisibility(View.GONE);

        //日付取得用リスナ作成
        final DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year,
                                  int month, int day) {
                //日付の取得
                calendar.set(year, month, day);
                dateView.setText(DateFormat.format("yyyy/MM/dd",calendar));

            }
        };

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    //ボタンを押すと日付の表示欄が表示される
                    dateView.setVisibility(View.VISIBLE);

                    dateView.setText(DateFormat.format("yyyy/MM/dd",calendar));

                    dateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            year = calendar.get(Calendar.YEAR); // 年
                            month = calendar.get(Calendar.MONTH); // 月
                            day = calendar.get(Calendar.DAY_OF_MONTH); // 日

                            // 日付設定ダイアログの作成・リスナの登録
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, DateSetListener, year, month, day);

                            // 日付設定ダイアログの表示
                            datePickerDialog.show();

                        }
                    });

                }else{
                    dateView.setVisibility(View.GONE);
                }

            }

        });


        /* Intentの読み込み */
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                /* Intentに添付されたデータを取り出す */
                //MyData myData = extras.getParcelable(MainActivity.INTENT_KEY_RESULT);

                /* 内容を画面に表示 */
                //editTaskText.setText(myData.getMessage());
            }
        }


        findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 入力を反映 */
                String name = editTaskName.getText().toString();
                String text = editTaskText.getText().toString();
                year = calendar.get(Calendar.YEAR); // 年
                month = calendar.get(Calendar.MONTH)+1; // 月
                day = calendar.get(Calendar.DAY_OF_MONTH); // 日
                int hour = 0;
                int minute = 0;
                boolean isImportant = false;

                Toast.makeText(
                        SubActivity.this,
                         "{ "+name +" } "+"{ "+text+" }" + "  "+  year + "/" + month
                                + "/" + day, Toast.LENGTH_LONG)
                        .show();

                /*nameとtextに何か入れた途端に日付が初期化される？？？*/

                //本当はこっちでデータを出力したい
                //registerTask(name,text,year,month,day,hour,minute,isImportant);


                /* 処理結果を設定 */
                setResult(RESULT_OK, intent);

                /* この画面を終了 */
                finish();

                /* アニメーションを付与 */
                overridePendingTransition(R.anim.open_fade_in, R.anim.close_fade_out);
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 何も反映しない */


                /* 処理結果を設定 */
                setResult(RESULT_CANCELED, intent);

                /* この画面を終了 */
                finish();

                /* アニメーションを付与 */
                overridePendingTransition(R.anim.open_fade_in, R.anim.close_fade_out);
            }
        });
    }

    private void registerTask(String taskName, String taskText, int year, int month, int day, int hour, int minute, boolean isImportant){
        ArrayList<Task> taskList = new ArrayList<Task>();
        TaskAdapter adapter = new TaskAdapter(SubActivity.this, taskList);
        //listView.setAdapter(adapter);
    }

}
