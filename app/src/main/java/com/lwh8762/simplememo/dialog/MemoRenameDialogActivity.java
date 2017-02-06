package com.lwh8762.simplememo.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.lwh8762.simplememo.MemoDataManager;
import com.lwh8762.simplememo.MemoService;
import com.lwh8762.simplememo.R;

/**
 * Created by W on 2017-02-05.
 */

public class MemoRenameDialogActivity extends Activity {
    private EditText memoInput = null;
    private Button cancelBtn = null;
    private Button applyBtn = null;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rename);

        position = getIntent().getIntExtra("position", 0);

        memoInput = (EditText) findViewById(R.id.memoInput);
        memoInput.setText(MemoDataManager.getMemo(position));
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        applyBtn = (Button) findViewById(R.id.applyBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = memoInput.getText().toString();
                if (memo.isEmpty()) return;
                MemoDataManager.setMemo(position, memo);
                MemoEditDialogActivity.getMemoEditDialogActivity().notifyDataSetChanged();
                MemoService.getMemoService().updateNotification();
                finish();
            }
        });
    }
}
