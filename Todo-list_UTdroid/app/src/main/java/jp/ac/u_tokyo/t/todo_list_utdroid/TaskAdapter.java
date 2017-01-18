package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

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


    /*
     * 参考
     * http://hyoromo.hatenablog.com/entry/20090912/1252777077
     * http://outofmem.hatenablog.com/entry/2014/10/29/040510
     * http://qiita.com/enkaism/items/33d2475eb84451361e2d
     */

    @Override /*ここは教材のチャットアプリを参考にしている*/
    public View getView(final int position, View convertView, final ViewGroup parent) {
         /* ビューを受け取る */
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            /* 受け取ったビューがnullなら新しくビューを生成（cell_message.xmlを読み込み） */
            // else 使い回し
            view = inflater.inflate(R.layout.cell_task, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        /* 表示すべきデータの取得 */
        final Task item = (Task) this.getItem(position);
        if (item != null) {
            /* 名前とメッセージを表示 */
            viewHolder.taskName.setText(item.getName());
            if (item.getDeadlineTime().getTimeInMillis() != 0) {
                viewHolder.deadlineTime.setText(DateFormat.format("yyyy/MM/dd, E, kk:mm", item.getDeadlineTime()));
                viewHolder.remainDay.setText("あと" + item.remainDay() + "日");
            } else {
                viewHolder.deadlineTime.setText("いつか");
            }

            final CheckBox checkBox = viewHolder.taskCheckBox;
            if(item.getCompleteTime().getTimeInMillis() == 0){
                checkBox.setChecked(false);
            }else{
                checkBox.setChecked(true);
            }
            viewHolder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskDatabase taskDatabase = new TaskDatabase(getContext());

                    if(checkBox.isChecked()) {
                        Snackbar.make(v, item.getName() + " removed", Snackbar.LENGTH_SHORT).show();
                        taskDatabase.setTaskCompleted(item, true);
                    }else {
                        Snackbar.make(v, item.getName() + " restored", Snackbar.LENGTH_SHORT).show();
                        taskDatabase.setTaskCompleted(item, false);
                    }

                }
            });
        }
        return view;
    }

    private static class ViewHolder {
        TextView taskName;
        TextView deadlineTime;
        TextView remainDay;
        CheckBox taskCheckBox;

        ViewHolder(View view) {
            this.taskName = (TextView) view.findViewById(R.id.taskName);
            this.deadlineTime = (TextView) view.findViewById(R.id.deadlineTime);
            this.remainDay = (TextView) view.findViewById(R.id.remainDay);
            this.taskCheckBox = (CheckBox) view.findViewById(R.id.taskCheckBox);
        }
    }
}
