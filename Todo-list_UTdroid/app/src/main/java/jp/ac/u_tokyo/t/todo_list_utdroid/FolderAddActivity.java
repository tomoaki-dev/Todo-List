package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 智明 on 2017/01/04.
 */

public class FolderAddActivity extends AppCompatActivity {

    private EditText folderName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_add);

        folderName = (EditText)findViewById(R.id.folderName);

        final Button buttonOK = (Button)findViewById(R.id.fbuttonOK);
        Button buttonCancel = (Button)findViewById(R.id.fbuttonCancel);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 入力を反映 */
                String name = folderName.getText().toString();


                if(name.isEmpty()){
                    Toast.makeText(FolderAddActivity.this,"名前を入力してください",Toast.LENGTH_SHORT).show();

                } else {
                    intent = new Intent();

                    /**TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());
                    if (taskID == -1) {
                        taskDatabase.add(name, text, deadlineTime, (int) importanceRatio, folderID);
                    } else {
                        taskDatabase.add(taskID, name, text, deadlineTime ,(int) importanceRatio, folderID);
                    }*/

                    //arrayList.add(name);//こんな感じで追加すればいい？

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