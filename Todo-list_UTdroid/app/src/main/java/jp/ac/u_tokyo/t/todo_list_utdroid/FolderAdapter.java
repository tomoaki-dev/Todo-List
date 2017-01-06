package jp.ac.u_tokyo.t.todo_list_utdroid;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 智明 on 2016/12/31.
 */

public class FolderAdapter extends ArrayAdapter{
    private LayoutInflater inflater;

    ArrayList<String> folderList = new ArrayList<>();
    Map<Integer,String> folderMap = new LinkedHashMap<>();

    public FolderAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override /*ここはチャットアプリをコピペ（変数名は変えている）*/
    // TODO: ViewHolder
    public View getView(final int position, View convertView, final ViewGroup parent) {
         /* ビューを受け取る */
        View view = convertView;
        if (view == null) {
            /* 受け取ったビューがnullなら新しくビューを生成 */
            // else 使い回し
            view = inflater.inflate(R.layout.cell_folder, null);
        }
        /* 表示すべきデータの取得 */
        TaskDatabase taskDatabase = new TaskDatabase(getContext());
        folderMap = taskDatabase.readFolder();

        if(folderMap==null){
            taskDatabase.createNewFolder("Default");
        }

        folderList.clear();
        for(Integer folderID : folderMap.keySet()){
            folderList.add(folderMap.get(folderID));
        }

        String name = folderList.get(position);

        /* Viewの取得 */
        TextView folderName = (TextView) view.findViewById(R.id.folder_name);

        /* 名前とメッセージを表示 */
        folderName.setText(name);


        return view;
    }
}