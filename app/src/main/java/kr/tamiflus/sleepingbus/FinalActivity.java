package kr.tamiflus.sleepingbus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;
import kr.tamiflus.sleepingbus.utils.InfomationDBHelper;

public class FinalActivity extends Activity {
    public static final int NOTIFICATION_ID = 1234;


    ArrivingBus arrivingBus;
    BusStation destStation, departStation;
    InfomationDBHelper helper;
    boolean isMarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();
        destStation = BusStationToStrArray.arrToList(intent.getStringArrayExtra("destStation"));
        arrivingBus = ArrivingBus.ArrayToArrivingBus(intent.getStringArrayExtra("arrivingBus"));
        departStation = BusStationToStrArray.arrToList(intent.getStringArrayExtra("departStation"));

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinator);
        TextView summary = (TextView) findViewById(R.id.summaryText);
        TextView boardBtn = (TextView) findViewById(R.id.boardBtn);
        TextView bookmarkBtn = (TextView) findViewById(R.id.bookMarkBtn);

        layout.setBackgroundColor(getResources().getColor(R.color.jikhang));
        boardBtn.setTextColor(getResources().getColor(R.color.jikhang));
//        summary.setText(String.format("%s 행\n%s번 버스", "천호역.천호사거리", "1113-1"));
        summary.setText(String.format("%s 행\n%s번 버스", destStation.getName(), arrivingBus.getRouteName()));

        helper = new InfomationDBHelper(getApplicationContext());
        isMarked = helper.hasBookMark(arrivingBus.getRouteId(), departStation.getCode(),destStation.getCode());
        if(isMarked) bookmarkBtn.setText("즐겨찾기 등록취소");

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMarked) {
                    isMarked = false;
                    bookmarkBtn.setText("즐겨찾기 등록하기");
                    helper.removeBookMark(arrivingBus.getRouteId(), departStation.getCode(),destStation.getCode());
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FinalActivity.this);

                    alert.setTitle("즐겨찾기 이름 입력");
                    alert.setMessage("즐겨찾기 이름을 입력해 주세요");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(getApplicationContext());
                    input.setTextColor(Color.BLACK);
                    input.setHint("집에 가는 길");
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            isMarked = true;
                            bookmarkBtn.setText("즐겨찾기 등록취소");

                            helper.addBookMark(value, arrivingBus.getRouteId(), departStation.getCode(),destStation.getCode());
                        }
                    });


                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                }
                            });

                    alert.show();

                }
            }
        });

        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 다 설정되었을 때 알람 실행하기
                if (AlarmService.shouldContinue) {
                    Toast.makeText(FinalActivity.this, "이미 설정된 알람이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlarmService.shouldContinue = true;
                    Log.d("FinalActivity", arrivingBus.toString());
                    Log.d("FinalActivity", destStation.toString());
                    Intent intent = new Intent(FinalActivity.this, AlarmService.class);
                    intent.putExtra("plateNo", arrivingBus.getPlateNo());
                    intent.putExtra("routeId", arrivingBus.getRouteId());
                    intent.putExtra("stationId", destStation.getCode());
                    Log.d("SetAlarm", "plateNo : " + arrivingBus.getPlateNo());
                    Log.d("SetAlarm", "routeId : " + arrivingBus.getRouteId());
                    Log.d("SetAlarm", "stationId : " + destStation.getCode());

                    AlarmService.shouldContinue = true;
                    startService(intent);
                    Toast.makeText(FinalActivity.this, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    createNotification();
                }
            }
        });
    }

    public void createNotification() {
        Log.d("createNotification", "createNotification()");
//        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setContentTitle("SleepingBus")
//                .setContentText("알람 설정")
////                .setSmallIcon(R.drawable.ic_launcher)
//                .setOngoing(true)
//                .setAutoCancel(false);
//
//        Intent intent = new Intent(this, AlarmDisableActivity.class);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(AlarmDisableActivity.class);
//        stackBuilder.addNextIntent(intent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,  PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(pendingIntent);
//
//        notificationManager.notify(NOTIFICATION_ID, builder.build());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.stop)
                        .setContentTitle("불면증의 버스")
                        .setContentText("버스 알람 설정됨")
                        .setOngoing(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, AlarmDisableActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AlarmDisableActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void cancelNotification() {
        Log.d("cancelNoti", "cancelNoti");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }
}
