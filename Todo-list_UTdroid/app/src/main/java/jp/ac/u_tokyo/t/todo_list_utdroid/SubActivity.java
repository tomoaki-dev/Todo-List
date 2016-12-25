package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by 智明 on 2016/12/25.
 */

public class SubActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        editTaskName = (EditText)findViewById(R.id.editTaskName);
        editTaskText = (EditText)findViewById(R.id.editTaskText);

        /* Intentの読み込み */
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null){
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
}