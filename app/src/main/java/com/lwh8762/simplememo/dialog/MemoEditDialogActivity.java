package com.lwh8762.simplememo.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwh8762.simplememo.MemoDataManager;
import com.lwh8762.simplememo.MemoService;
import com.lwh8762.simplememo.R;

import java.util.ArrayList;

/**
 * Created by W on 2017-02-05.
 */

public class MemoEditDialogActivity extends Activity {
    private static MemoEditDialogActivity me = null;

    private ListView memoListView = null;
    private MemoArrayAdapter memoArrayAdapter = null;

    private Button closeBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_memoedit);

        me = this;

        memoListView = (ListView) findViewById(R.id.memoListVIew);
        memoArrayAdapter = new MemoArrayAdapter(this);
        memoListView.setAdapter(memoArrayAdapter);

        closeBtn = (Button) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MemoArrayAdapter extends ArrayAdapter<String> {
        private LayoutInflater layoutInflater = null;
        private Context context = null;

        public MemoArrayAdapter(Context context) {
            super(context, 0, MemoDataManager.getMemoList());
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.view_memo, null);
            TextView contentView = (TextView) view.findViewById(R.id.contentView);
            contentView.setText(MemoDataManager.getMemo(position));

            ImageButton editBtn = (ImageButton) view.findViewById(R.id.editBtn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MemoEditDialogActivity.this, MemoRenameDialogActivity.class);
                        intent.putExtra("position", position);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MemoEditDialogActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        pendingIntent.send();
                    }catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            });
            ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?");
                    builder.setNegativeButton("CANCEL", null);
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                            MemoDataManager.removeMemo(position);
                            MemoArrayAdapter.this.notifyDataSetChanged();
                            MemoService.getMemoService().updateNotification();
                        }
                    });
                    builder.create().show();
                }
            });

            return view;
        }
    }

    public static MemoEditDialogActivity getMemoEditDialogActivity() {
        return me;
    }

    public void notifyDataSetChanged() {
        memoArrayAdapter.notifyDataSetChanged();
    }
}
