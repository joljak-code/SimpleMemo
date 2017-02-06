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

public class MemoAddDialogActivity extends Activity {
    private EditText memoInput = null;
    private Button cancelBtn = null;
    private Button addBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_memoadd);

        memoInput = (EditText) findViewById(R.id.memoInput);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        addBtn = (Button) findViewById(R.id.addBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = memoInput.getText().toString();
                if (memo.isEmpty()) return;
                MemoDataManager.addMemo(memo);
                MemoService.getMemoService().updateNotification();
                finish();
            }
        });
    }
}
