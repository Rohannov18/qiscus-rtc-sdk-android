package com.qiscus.rtc.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qiscus.rtc.QiscusRTC;
import com.qiscus.rtc.sample.service.WebsocketService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private WebsocketService websocketService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            websocketService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            websocketService = ((WebsocketService.Binder)service).getService();
            websocketService.onStartCommand(null, 0, 0);
        }
    };

    private EditText etTargetUsername;
    private EditText etRoomId;
    private Button btnVoiceCall;
    private Button btnVideoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!QiscusRTC.hasSession()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        bindService(WebsocketService.startIntent(getApplicationContext()), serviceConnection, Context.BIND_IMPORTANT);
        getApplicationContext().startService(WebsocketService.startIntent(getApplicationContext()));
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    public String generateRoomCall() {
        String room = "callId" + String.valueOf(System.currentTimeMillis());
        return room;
    }

    private void initView() {
        etTargetUsername = (EditText) findViewById(R.id.target_username);
        etRoomId = (EditText) findViewById(R.id.room_id);
        btnVoiceCall = (Button) findViewById(R.id.btn_voice_call);
        etRoomId.setText(generateRoomCall());
        btnVoiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTargetUsername.getText().toString().isEmpty() && !etRoomId.getText().toString().isEmpty()) {
                    WebsocketService.initCall(etRoomId.getText().toString(), QiscusRTC.CallType.VOICE, etTargetUsername.getText().toString(), QiscusRTC.getUser(), "http://dk6kcyuwrpkrj.cloudfront.net/wp-content/uploads/sites/45/2014/05/avatar-blank.jpg");

                    QiscusRTC.CallActivityBuilder.buildCallWith(etRoomId.getText().toString())
                            .setCallAs(QiscusRTC.CallAs.CALLER)
                            .setCallType(QiscusRTC.CallType.VOICE)
                            .setCallerUsername(QiscusRTC.getUser())
                            .setCalleeUsername(etTargetUsername.getText().toString())
                            .setCalleeDisplayName(etTargetUsername.getText().toString())
                            .setCalleeDisplayAvatar("http://dk6kcyuwrpkrj.cloudfront.net/wp-content/uploads/sites/45/2014/05/avatar-blank.jpg")
                            .show(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "Target user and room id required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnVideoCall = (Button) findViewById(R.id.btn_video_call);
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTargetUsername.getText().toString().isEmpty() && !etRoomId.getText().toString().isEmpty()) {
                    WebsocketService.initCall(etRoomId.getText().toString(), QiscusRTC.CallType.VIDEO, etTargetUsername.getText().toString(), QiscusRTC.getUser(), "http://dk6kcyuwrpkrj.cloudfront.net/wp-content/uploads/sites/45/2014/05/avatar-blank.jpg");

                    QiscusRTC.CallActivityBuilder.buildCallWith(etRoomId.getText().toString())
                            .setCallAs(QiscusRTC.CallAs.CALLER)
                            .setCallType(QiscusRTC.CallType.VIDEO)
                            .setCallerUsername(QiscusRTC.getUser())
                            .setCalleeUsername(etTargetUsername.getText().toString())
                            .setCalleeDisplayName(etTargetUsername.getText().toString())
                            .setCalleeDisplayAvatar("http://dk6kcyuwrpkrj.cloudfront.net/wp-content/uploads/sites/45/2014/05/avatar-blank.jpg")
                            .show(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "Target user and room id required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
