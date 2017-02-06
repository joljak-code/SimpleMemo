package com.lwh8762.simplememo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lwh8762.simplememo.dialog.MemoAddDialogActivity;
import com.lwh8762.simplememo.dialog.MemoEditDialogActivity;

import java.io.File;

/**
 * Created by W on 2017-02-04.
 */

public class MemoService extends Service {

    private static MemoService me = null;

    private Notification notification = null;
    private NotificationManager notificationManager = null;
    private NotificationCompat.Builder notificationBuilder = null;

    private RemoteViews smallNotiView = null;
    private RemoteViews bigNotiView = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();

        me = this;

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/simple");
        if (!f.exists()) {
            f.mkdir();
        }
        MemoDataManager.setup(Environment.getExternalStorageDirectory().getAbsolutePath() + "/simple");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent memoAddBtnClickListener = PendingIntent.getBroadcast(this,0,new Intent("MemoAddButtonClickListener"),0);
        PendingIntent allMemoViewBtnClickListener = PendingIntent.getBroadcast(this,0,new Intent("AllMemoViewButtonClickListener"),0);

        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Memo Add", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                getApplicationContext().sendBroadcast(i);
                showMemoAddDialog();
            }
        },new IntentFilter("MemoAddButtonClickListener"));
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "All Memo View", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                getApplicationContext().sendBroadcast(i);
                showMemoEditDialog();
            }
        },new IntentFilter("AllMemoViewButtonClickListener"));

        smallNotiView = new RemoteViews(getPackageName(), R.layout.noti_view_small);
        smallNotiView.setOnClickPendingIntent(R.id.memoAddBtn, memoAddBtnClickListener);

        bigNotiView = new RemoteViews(getPackageName(), R.layout.noti_view_big);
        bigNotiView.setOnClickPendingIntent(R.id.memoAddBtn, memoAddBtnClickListener);
        bigNotiView.setOnClickPendingIntent(R.id.allViewBtn, allMemoViewBtnClickListener);

        notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Simple Memo")
                .setContent(smallNotiView)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH);
        notification = notificationBuilder.build();
        notification.bigContentView = bigNotiView;

        notificationManager.notify(1746, notification);

        updateNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destory", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(1746);
    }

    public void updateNotification() {
        int size = MemoDataManager.getSize();
        if (size > 0) {
            smallNotiView.setTextViewText(R.id.lastMemoView, MemoDataManager.getLastMemo());
            smallNotiView.setTextViewText(R.id.memoCountView, (MemoDataManager.getSize() > 1 ? "+" + String.valueOf(MemoDataManager.getSize() - 1) : ""));

            String fourMemo = MemoDataManager.getMemo(size - 1);
            for (int i = size - 1;i > (MemoDataManager.getSize() - 4 > 0 ? MemoDataManager.getSize() - 4 : 0);i --) {
                fourMemo += "\n" + MemoDataManager.getMemo(i - 1);
            }
            if (size == 1) {
                fourMemo += "\n";
            }
            bigNotiView.setTextViewText(R.id.memoView, fourMemo);
            bigNotiView.setTextViewText(R.id.allViewBtn, (MemoDataManager.getSize() > 4 ? "+" + String.valueOf(MemoDataManager.getSize() - 4) : ""));
        }else {
            bigNotiView.setTextViewText(R.id.memoView, "\n");
        }
        notificationManager.notify(1746, notification);
    }

    private void showMemoAddDialog() {
        try {
            Intent intent = new Intent(this, MemoAddDialogActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.send();
        }catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void showMemoEditDialog() {
        try {
            Intent intent = new Intent(this, MemoEditDialogActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.send();
        }catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static MemoService getMemoService() {
        return me;
    }
}
