package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.date_view;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.ratingBar;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.starReset;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.switch1;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.time;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.time_view;

/**
 * Created by 智明 on 2016/12/25.
 */

public class SubActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskText;

    Calendar calendar1 = Calendar.getInstance();//日付取得用
    Calendar calendar2 = Calendar.getInstance();//時刻取得用
    int year, month, day, hour, minute;
    float ImportanceRatio;
    public String Importance;

    //ListView listView = (ListView)findViewById(R.id.list_view);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        editTaskName = (EditText) findViewById(R.id.editTaskName);
        editTaskText = (EditText) findViewById(R.id.editTaskText);

        final TextView dateView = (TextView)findViewById(date_view);
        final TextView timeView = (TextView)findViewById(time_view);

        Switch s1 = (Switch) findViewById(R.id.switch1);

        dateView.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);

        /*タイムゾーンの設定（これを忘れるとTimePickerがおかしなことになる）*/
        // get the supported ids for GMT-09:00 (Japanese Standard Time)
        String[] ids = TimeZone.getAvailableIDs(-9 * 60 * 60 * 1000);
        // if no ids were returned, something is wrong. get out.
        if (ids.length == 0)
            System.exit(0);

        // create a Japanese Standard Time time zone
        SimpleTimeZone pdt = new SimpleTimeZone(-9 * 60 * 60 * 1000, ids[0]);



        //日付取得用リスナ作成
        final DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year,
                                  int month, int day) {
                //日付の取得
                calendar1.set(year, month, day);
                dateView.setText(DateFormat.format("yyyy/MM/dd",calendar1));

            }
        };

        //時刻取得用リスナ作成
        final TimePickerDialog.OnTimeSetListener TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker,
                                  int hour, int minute) {
                //時刻の取得(一度初期化してそれぞれセットしてあげないとダメらしい)
                calendar2.clear();
                calendar2.set(Calendar.HOUR_OF_DAY,hour);
                calendar2.set(Calendar.MINUTE,minute);
                timeView.setText(DateFormat.format("hh:mm",calendar2));

            }
        };

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    //ボタンを押すと日付・時刻の表示欄が表示される
                    dateView.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.VISIBLE);

                    dateView.setText(DateFormat.format("yyyy/MM/dd",calendar1));
                    timeView.setText(DateFormat.format("hh:mm",calendar2));

                    dateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            year = calendar1.get(Calendar.YEAR); // 年
                            month = calendar1.get(Calendar.MONTH); // 月
                            day = calendar1.get(Calendar.DAY_OF_MONTH); // 日

                            // 日付設定ダイアログの作成・リスナの登録
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, DateSetListener, year, month, day);

                            // 日付設定ダイアログの表示
                            datePickerDialog.show();

                        }
                    });

                    timeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hour = calendar2.get(Calendar.HOUR_OF_DAY); // 時
                            minute = calendar2.get(Calendar.MINUTE); // 分

                            // 時刻設定ダイアログの作成・リスナの登録
                            final TimePickerDialog timePickerDialog = new TimePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, TimeSetListener,hour,minute,true);

                            // 時刻設定ダイアログの表示
                            timePickerDialog.show();

                        }
                    });

                }else{
                    dateView.setVisibility(View.GONE);
                    timeView.setVisibility(View.GONE);
                }

            }

        });


        final RatingBar rb =(RatingBar)findViewById(ratingBar);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingbar, float rating, boolean fromUser) {
                ImportanceRatio = ratingbar.getRating();

            }
        });

        Button btn = (Button)findViewById(starReset);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                ImportanceRatio = 0;
                rb.setRating(0);
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
                year = calendar1.get(Calendar.YEAR); // 年
                month = calendar1.get(Calendar.MONTH)+1; // 月
                day = calendar1.get(Calendar.DAY_OF_MONTH); // 日
                hour = calendar2.get(Calendar.HOUR_OF_DAY); // 時
                minute = calendar2.get(Calendar.MINUTE); // 分

                if(ImportanceRatio==1.0){
                    Importance = "Low";
                }else if(ImportanceRatio==2.0){
                    Importance = "Middle";
                }else if(ImportanceRatio==3.0){
                    Importance = "High";
                };

                Toast.makeText(
                        SubActivity.this,
                         "{ "+name +" } "+"{ "+text+" }" + "  "+  year + "/" + month
                                + "/" + day +"  " + hour +":"+ minute + "Importance: " + Importance, Toast.LENGTH_LONG)
                        .show();



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
