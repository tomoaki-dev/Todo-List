package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static jp.ac.u_tokyo.t.todo_list_utdroid.R.id.checkbox;

/**
 * Created by 智明 on 2016/12/23.
 */
//ネット上のソースとチャットアプリ（教材）ソース混ぜた
public class TaskAdapter extends ArrayAdapter {//ArrayAdapterはチャットアプリ（教材）から
    private LayoutInflater inflater;
    private int mCheckBox;//ここはネット上のソースから少し変えてチェックボックスにしている

    public TaskAdapter(Context context, List<Task> list) {
        super(context, 0, list);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCheckBox = checkbox;
    }


    @Override/*ここはチャットアプリをコピペ（変数名は変えている）*/
    public View getView(final int position, View convertView, final ViewGroup parent) {
         /* ビューを受け取る */
        View view = convertView;
        if (view == null) {
            /* 受け取ったビューがnullなら新しくビューを生成（cell_message.xmlを読み込み） */
            view = inflater.inflate(R.layout.cell_task, null);
        }
        /* 表示すべきデータの取得 */
        final Task item = (Task) this.getItem(position);
        if (item != null) {
            /* Viewの取得 */
            TextView taskName = (TextView) view.findViewById(R.id.taskName);
            TextView taskText = (TextView) view.findViewById(R.id.taskText);

            /* 名前とメッセージを表示 、ここはまだ作ってないTask.javaで扱う*/
            taskName.setText(item.task);
            taskText.setText(item.text);


            CheckBox cbx = (CheckBox) view.findViewById(mCheckBox);
            cbx.setTag(position);

            final ListView list = (ListView) parent;
            cbx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg) {
                    AdapterView.OnItemClickListener listener = list.getOnItemClickListener();
                    long id = getItemId(position);
                    listener.onItemClick((AdapterView<?>) parent, arg, position, id);
                }
            });


        }

        return view;
    }

}