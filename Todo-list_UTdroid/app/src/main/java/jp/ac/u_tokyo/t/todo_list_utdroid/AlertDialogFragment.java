package jp.ac.u_tokyo.t.todo_list_utdroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by 智明 on 2017/01/18.
 */


//参考サイト　http://qiita.com/suzukihr/items/8973527ebb8bb35f6bb8
//http://qiita.com/kojionilk/items/71869b4b51f1591cdd7a
//https://developer.android.com/guide/topics/ui/dialogs.html

public class AlertDialogFragment extends DialogFragment {
    private static final String REQUEST = "request";
    private static final int REQUEST_TASK = 1;
    private static final int REQUEST_FOLDER = 2;

    String titleText;
    String messageText;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public static AlertDialogFragment newInstance(int requestCode) {
        AlertDialogFragment alert = new AlertDialogFragment();
        Bundle arg = new Bundle();
        arg.putInt(REQUEST, requestCode);
        alert.setArguments(arg);
        return alert;
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int requestCode = getArguments().getInt(REQUEST);
        switch (requestCode) {
            case REQUEST_TASK:
                titleText = "本当に削除しますか？";
                messageText = "この操作でこの項目は完全に削除されます。";
                break;
            case REQUEST_FOLDER:
                titleText = "本当にこのフォルダを削除しますか？";
                messageText = "フォルダ内の項目も全て削除されます。";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(titleText)
                .setMessage(messageText)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(AlertDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(AlertDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }
}
