package com.skysoft.slobodyanuk.employeestatistics.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.util.PreferencesManager;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.DATE_KEY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.EVENT_KEY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.ID_KEY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.IN_PASSAGE_KEY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.MESSAGE_NOTIFICATION_ID;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.OUT_PASSAGE_KEY;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class MessageHandler extends FirebaseMessagingService {

    private static final String TAG = MessageHandler.class.getCanonicalName();

    private static final String GROUP_KEY = "group";

    private String mMessage;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Log.e(TAG, "onMessageReceived: " + data);
        createNotification(data);
        testLogShow(data);
    }

    private void createNotification(Map<String, String> data) {
        Context context = getBaseContext();

        String id = data.get(ID_KEY);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        String title = "";
        if (data.get(EVENT_KEY).equals(IN_PASSAGE_KEY)) {
            title = getString(R.string.passage_in);
        } else if (data.get(EVENT_KEY).equals(OUT_PASSAGE_KEY)) {
            title = getString(R.string.passage_out);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_door)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY)
                .setAutoCancel(true)
                .setContentText(data.get(DATE_KEY) + ", key : " + id);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e(TAG, "create notification: build");

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

    private void testLogShow(Map<String, String> data) {
        String tmp = PreferencesManager.getInstance().getText();
        PreferencesManager.getInstance().setText(tmp.concat("\n" +
                "Event :: " + data.get(Globals.EVENT_KEY) +
                ", time :: " + data.get(Globals.DATE_KEY) +
                ", KEY :: " + data.get(Globals.ID_KEY) + "\n"));
        EventBus.getDefault().post(new FragmentEvent(data));
    }

}
