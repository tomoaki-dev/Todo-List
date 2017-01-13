package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.checkbox;
import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.never;

/**
 * Created by 智明 on 2016/12/23.
 */
//ネット上のソースとチャットアプリ（教材）ソース混ぜた
public class TaskAdapter extends ArrayAdapter {//ArrayAdapterはチャットアプリ（教材）から
    private LayoutInflater inflater;

    public TaskAdapter(Context context, List<Task> list) {
        super(context, 0, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override /*ここはチャットアプリをコピペ（変数名は変えている）*/
    // TODO: ViewHolder
    public View getView(final int position, View convertView, final ViewGroup parent) {
         /* ビューを受け取る */
        View view = convertView;
        if (view == null) {
            /* 受け取ったビューがnullなら新しくビューを生成（cell_message.xmlを読み込み） */
            // else 使い回し
            view = inflater.inflate(R.layout.cell_task, null);
        }
        /* 表示すべきデータの取得 */
        final Task item = (Task) this.getItem(position);
        if (item != null) {
            /* Viewの取得 */
            TextView taskName = (TextView) view.findViewById(R.id.taskName);
            TextView deadlineTime = (TextView) view.findViewById(R.id.deadlineTime);
            TextView remainDay = (TextView) view.findViewById(R.id.remainDay);
            final CheckBox taskCheckBox = (CheckBox) view.findViewById(R.id.taskCheckBox);

            /* 名前とメッセージを表示 */
            taskName.setText(item.getName());
            if (item.getDeadlineTime().getTimeInMillis() != 0) {
                deadlineTime.setText(DateFormat.format("yyyy/MM/dd, E, kk:mm", item.getDeadlineTime()));
                remainDay.setText("あと" + item.remainDay() + "日");
            } else {
                deadlineTime.setText("いつか");
            }

            if(item.getCompleteTime().getTimeInMillis() == 0){
                taskCheckBox.setChecked(false);
            }else{
                taskCheckBox.setChecked(true);
            }
            // 画面外でチェックを外す (一時的)
            //taskCheckBox.setChecked(false);
            //taskCheckBox.setTag(position);

            //final ListView list = (ListView) parent;
            taskCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, item.getName() + " removed", Snackbar.LENGTH_LONG).show();
                    // getAdapter() は参照渡し？
                    //ArrayAdapter<Task> adapter = (ArrayAdapter<Task>) ((ListView) parent).getAdapter();
                    TaskDatabase taskDatabase = new TaskDatabase(getContext());

                    long completeTime = new GregorianCalendar().getTimeInMillis();

                    if(taskCheckBox.isChecked()) {
                        Snackbar.make(v, item.getName() + " removed", Snackbar.LENGTH_LONG).show();
                    }else {
                        completeTime = 0;
                        Snackbar.make(v, item.getName() + " restored", Snackbar.LENGTH_LONG).show();
                    }

                    taskDatabase.add(
                            item.getTaskID(),
                            item.getName(),
                            item.getText(),
                            item.getDeadlineTime().getTimeInMillis(),
                            item.getTaskImportance(),
                            item.getFolderName(),
                            completeTime);

                }
            });
        }
        return view;
    }
}