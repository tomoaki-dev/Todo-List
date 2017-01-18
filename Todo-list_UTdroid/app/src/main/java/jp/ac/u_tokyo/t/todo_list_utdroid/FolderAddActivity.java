package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 智明 on 2017/01/04.
 */

public class FolderAddActivity extends FragmentActivity implements AlertDialogFragment.NoticeDialogListener{

    private EditText folderName;
    String name = null;
    String newName;

    Intent intent;

    @Override/*教材のIntentを参考にしている*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_add);

        folderName = (EditText)findViewById(R.id.folderName);

        final Button buttonOK = (Button)findViewById(R.id.fbuttonOK);
        Button buttonCancel = (Button)findViewById(R.id.fbuttonCancel);
        Button buttonDelete = (Button)findViewById(R.id.fbuttonDetete);
        View deleteView = (View)findViewById(R.id.fdeleteView);

        buttonDelete.setVisibility(View.GONE);
        deleteView.setVisibility(View.GONE);


        intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                name = extras.getString("folderName");
                folderName.setText(name);
                buttonDelete.setVisibility(View.VISIBLE);
                deleteView.setVisibility(View.VISIBLE);
            }
        }



        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 入力を反映 */
                newName = folderName.getText().toString();

                TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());

                if(newName.isEmpty()) {
                    Toast.makeText(FolderAddActivity.this, "名前を入力してください", Toast.LENGTH_SHORT).show();

                } else {
                    if (name != null) {
                        taskDatabase.updateFolder(name,newName);
                    }else if (name != null && name.equals(newName)) {
                        //なにもしない
                    }else{
                        taskDatabase.createNewFolder(newName);
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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();

                //ダイアログを表示させる
                showAlertDialog();
            }
        });

    }


    //参考サイト　https://developer.android.com/guide/topics/ui/dialogs.html
    public void showAlertDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = AlertDialogFragment.newInstance(2);
        dialog.show(getSupportFragmentManager(), "AlertDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        /* タスクを削除 */
        TaskDatabase taskDatabase = new TaskDatabase(getApplicationContext());

        taskDatabase.deleteFolder(name);

        /* 処理結果を設定 */
        setResult(RESULT_OK, intent);

        /* アニメーションを付与 */
        overridePendingTransition(R.anim.open_fade_in, R.anim.close_fade_out);

        /* この画面を終了 */
        finish();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        //何もしない
    }
}