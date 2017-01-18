package jp.ac.u_tokyo.t.todo_list_utdroid;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.drawable.btn_star_big_on;

/**
 * Created by 智明 on 2016/12/31.
 */

public class FolderAdapter extends ArrayAdapter{
    private LayoutInflater inflater;

    public FolderAdapter(Context context, List<String> folderList) {
        super(context, 0, folderList);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override /*ここは教材のチャットアプリを参考にしている*/
    public View getView(final int position, View convertView, final ViewGroup parent) {
         /* ビューを受け取る */
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            /* 受け取ったビューがnullなら新しくビューを生成 */
            // else 使い回し
            view = inflater.inflate(R.layout.cell_folder, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        /* 表示すべきデータの取得 */
        String name = (String) this.getItem(position);

        /* 名前とメッセージを表示 */
        viewHolder.folderName.setText(name);
        viewHolder.imageView.setImageResource(btn_star_big_on);

        return view;
    }

    private static class ViewHolder {
        TextView folderName;
        ImageView imageView;

        ViewHolder(View view) {
            this.folderName = (TextView) view.findViewById(R.id.folder_name);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
