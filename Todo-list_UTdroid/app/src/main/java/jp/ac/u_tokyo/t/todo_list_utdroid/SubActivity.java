package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
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
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.date_view;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.deadlineTime;
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

    GregorianCalendar calendar1 = new GregorianCalendar();//日付取得用
    Calendar calendar2 = Calendar.getInstance();//時刻取得用
    int year, month, day, hour, minute;
    float importanceRatio;
    public String importance;
    long deadlineTime = 0;
    Intent intent;
    int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        editTaskName = (EditText) findViewById(R.id.editTaskName);
        editTaskText = (EditText) findViewById(R.id.editTaskText);

        final TextView dateView = (TextView)findViewById(date_view);
        final TextView timeView = (TextView)findViewById(time_view);

        Switch s1 = (Switch) findViewById(R.id.switch1);

        final Button buttonOK = (Button)findViewById(R.id.buttonOK);
        Button buttonCancel = (Button)findViewById(R.id.buttonCancel);


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
                timeView.setText(DateFormat.format("kk:mm",calendar2));

            }
        };


        //RatingBar(重要度)の設定
        final RatingBar rb =(RatingBar)findViewById(ratingBar);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingbar, float rating, boolean fromUser) {
                importanceRatio = ratingbar.getRating();

            }
        });

        //RatingBarのリセットボタンの設定
        Button btn = (Button)findViewById(starReset);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                importanceRatio = 0;
                rb.setRating(0);
            }
        });


        /* Intentの読み込み */
        intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                taskID = extras.getInt("id");
                //今はトースト焼いてる
                Toast.makeText(SubActivity.this,"called id is " + taskID,Toast.LENGTH_SHORT).show();

                /**  ここでTaskの中身を受け取ってそれぞれの変数に値を代入する
                editTaskName = ;
                editTaskText = ;
                importanceRatio = ;
                rb.setRating(importanceRatio);
                calendar1.set(year.);
                calendar1.set(month, );
                calender1.set(day, );
                calender2.set(hour,);
                calender2.set(minute, );


                 */
                 if(deadlineTime!=0){
                     s1.setChecked(true);
                 }







            }
        }







        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true) {
                    //ボタンを押すと日付・時刻の表示欄が表示される
                    dateView.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.VISIBLE);

                    dateView.setText(DateFormat.format("yyyy/MM/dd",calendar1));
                    timeView.setText(DateFormat.format("kk:mm",calendar2));

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
                    deadlineTime = 1;
                }else{
                    dateView.setVisibility(View.GONE);
                    timeView.setVisibility(View.GONE);
                    deadlineTime = 0;
                }

            }

        });






        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 入力を反映 */
                String name = editTaskName.getText().toString();
                String text = editTaskText.getText().toString();
                year = calendar1.get(Calendar.YEAR); // 年
                month = calendar1.get(Calendar.MONTH); // 月
                day = calendar1.get(Calendar.DAY_OF_MONTH); // 日
                hour = calendar2.get(Calendar.HOUR_OF_DAY); // 時
                minute = calendar2.get(Calendar.MINUTE); // 分

                if(importanceRatio==1.0){
                    importance = "Low";
                }else if(importanceRatio==2.0){
                    importance = "Middle";
                }else if(importanceRatio==3.0){
                    importance = "High";
                };

                if(name.isEmpty()){
                    Toast.makeText(SubActivity.this,"名前を入力してください",Toast.LENGTH_SHORT).show();

                }else{
                    intent = new Intent();

                    if (deadlineTime != 0) {
                        deadlineTime = new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis();
                    }
                    Log.d("add", "deadline = " + deadlineTime);


                    TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
                    taskDatabase.add(name, text, deadlineTime, (int) importanceRatio);



                /* 処理結果を設定 */
                    setResult(RESULT_OK, intent);

                /* アニメーションを付与 */
                    overridePendingTransition(R.anim.open_fade_in, R.anim.close_fade_out);

                /* この画面を終了 */
                    finish();
                }




            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                /* 何も反映しない */


                /* 処理結果を設定 */
                setResult(RESULT_CANCELED, intent);

                /* アニメーションを付与 */
                overridePendingTransition(R.anim.open_fade_in, R.anim.close_fade_out);

                /* この画面を終了 */
                finish();
            }
        });
    }

}
