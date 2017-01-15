package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.date_view;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.ratingBar;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.starReset;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.time_view;

/**
 * Created by 智明 on 2016/12/25.
 */

public class SubActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskText;

    Calendar calendar; //取得用
    int year, month, day, hour, minute;
    float importanceRatio;
    long deadlineTime = 0;
    Intent intent;
    int taskID = -1;
    String folderName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        final TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());

        editTaskName = (EditText) findViewById(R.id.editTaskName);
        editTaskText = (EditText) findViewById(R.id.editTaskText);

        final TextView dateView = (TextView)findViewById(date_view);
        final TextView timeView = (TextView)findViewById(time_view);

        final Switch s1 = (Switch) findViewById(R.id.switch1);

        final Button buttonOK = (Button)findViewById(R.id.buttonOK);
        Button buttonCancel = (Button)findViewById(R.id.buttonCancel);

        calendar = new GregorianCalendar();

        dateView.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);


        //日時設定
        //参考サイト　http://techbooster.jpn.org/andriod/ui/9757/
        //http://tech.pjin.jp/blog/2016/02/23/android-%E6%97%A5%E4%BB%98%E3%83%BB%E6%99%82%E5%88%BB%E3%81%AE%E5%85%A5%E5%8A%9B%E3%80%80datepickertimepicker/
        //http://techbooster.jpn.org/andriod/application/8234/

        //日付取得用リスナ作成
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year,
                                  int month, int day) {
                //日付の取得
                calendar.set(year, month, day);
                dateView.setText(DateFormat.format("yyyy/MM/dd",calendar));

            }
        };

        //時刻取得用リスナ作成
        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker,
                                  int hour, int minute) {
                //時刻の取得(一度初期化してそれぞれセットしてあげないとダメらしい)
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
                timeView.setText(DateFormat.format("kk:mm", calendar));
                Log.d("time_set", "calendar = " + calendar.getTimeInMillis());
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


        //spinnerに表示するfolderの一覧
        //参考サイト　http://www.adakoda.com/android/000074.html
        List<String> folderList = taskDatabase.readFolder();
        ArrayAdapter folderAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, folderList);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(folderAdapter);



        /* Intentの読み込み */
        intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                taskID = extras.getInt("id");

                //TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
                Task task = taskDatabase.getTaskById(taskID);
                /* ここでTaskの中身を受け取ってそれぞれの変数に値を代入する */
                editTaskName.setText(task.getName());
                editTaskText.setText(task.getText());
                importanceRatio = task.getTaskImportance();
                rb.setRating(importanceRatio);

                folderName = task.getFolderName();

                //今はトースト焼いてる
                Toast.makeText(SubActivity.this,"TaskID: " + taskID + "  FolderName:"+ folderName,Toast.LENGTH_SHORT).show();

                if(task.getDeadlineTime().getTimeInMillis() != 0){
                    s1.setChecked(true);
                    calendar = task.getDeadlineTime();

                    dateView.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.VISIBLE);

                    dateView.setText(DateFormat.format("yyyy/MM/dd",calendar));
                    timeView.setText(DateFormat.format("kk:mm", calendar));

                    dateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, dateSetListener,
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.show();
                        }
                    });

                    timeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final TimePickerDialog timePickerDialog = new TimePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, timeSetListener,
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE), true);
                            timePickerDialog.show();
                        }
                    });
                }
            }
        }

        //日付設定ボタンを押した時の設定
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateView.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.VISIBLE);

                    dateView.setText(DateFormat.format("yyyy/MM/dd",calendar));
                    timeView.setText(DateFormat.format("kk:mm",calendar));

                    dateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            year = calendar.get(Calendar.YEAR); // 年
                            month = calendar.get(Calendar.MONTH); // 月
                            day = calendar.get(Calendar.DAY_OF_MONTH); // 日

                            // 日付設定ダイアログの作成・リスナの登録
                            final DatePickerDialog datePickerDialog = new DatePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, dateSetListener, year, month, day);

                            // 日付設定ダイアログの表示
                            datePickerDialog.show();

                        }
                    });

                    timeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hour = calendar.get(Calendar.HOUR_OF_DAY); // 時
                            minute = calendar.get(Calendar.MINUTE); // 分

                            // 時刻設定ダイアログの作成・リスナの登録
                            final TimePickerDialog timePickerDialog = new TimePickerDialog(SubActivity.this,
                                    android.R.style.Theme_Holo_Dialog, timeSetListener,hour,minute,true);

                            // 時刻設定ダイアログの表示
                            timePickerDialog.show();

                        }
                    });
                } else {
                    dateView.setVisibility(View.GONE);
                    timeView.setVisibility(View.GONE);
                }
            }
        });

        //spinnerの初期値の変更（再編集時に元々選択されていたフォルダを表示させる）
        int defaultPosition = 0;
        if (folderName != null) {
            for (int i = 0; i < folderAdapter.getCount(); i++) {
                if (folderName.equals(folderAdapter.getItem(i))) {
                    defaultPosition = i;
                    break;
                }
            }
            // デバッグ用
            Toast.makeText(SubActivity.this, "folder error", Toast.LENGTH_SHORT).show();
        }
        spinner.setSelection(defaultPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                folderName = (String) spinner.getSelectedItem();

                Toast.makeText(SubActivity.this, folderName, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });





        //この辺りは教材のIntentを参考にしている
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 入力を反映 */
                String name = editTaskName.getText().toString();
                String text = editTaskText.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(SubActivity.this,"名前を入力してください",Toast.LENGTH_SHORT).show();

                } else {
                    intent = new Intent();

                    if (s1.isChecked()) {
                        deadlineTime = calendar.getTimeInMillis();
                    }
                    Log.d("update", "deadline = " + deadlineTime);

                    TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
                    if (taskID == -1) {
                        taskDatabase.add(name, text, deadlineTime, (int) importanceRatio, folderName,0);
                    } else {
                        taskDatabase.update(taskID, name, text, deadlineTime ,(int) importanceRatio, folderName,0);
                    }

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
