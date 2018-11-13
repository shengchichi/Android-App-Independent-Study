package com.example.app;


import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.text.ParseException;

/**
 * Created by 笙笙 on 2017/7/31.
 */

public class PhoneActivity extends AppCompatActivity{

    private String username = "shengyu";
    private String domain = "sip.linphone.org";
    private String password ="ncufresh";
    Button button;

    public SipManager mSipManager = null;
    public SipProfile mSipProfile = null;
    public SipAudioCall call = null;
    public String sipAddress = "sip:shengyu2@sip.linphone.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Callout();
            }
        });

        Register();




    }
    private void Initiate()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                button = (Button) findViewById(R.id.button);
                button.setText("Call");
                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Callout();
                    }
                });
            }
        });
    }
    private void Register()
    {
        if (mSipManager == null) {
            mSipManager = SipManager.newInstance(this);
        }

        SipProfile.Builder builder = null;
        try {
            builder = new SipProfile.Builder(username, domain);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        builder.setPassword(password);

        mSipProfile = builder.build();
        SipRegistrationListener listener = new SipRegistrationListener() {
            @Override
            public void onRegistering(String s) {
                updateStatus("registering...");
            }

            @Override
            public void onRegistrationDone(String s, long l) {
                updateStatus("done...");
            }

            @Override
            public void onRegistrationFailed(String s, int i, String s1) {
                updateStatus("fail..."+s1);
            }
        };
        try {
            mSipManager.open(mSipProfile);
            mSipManager.register(mSipProfile, 30, listener);
            mSipManager.setRegistrationListener(mSipProfile.getUriString(),listener);
        } catch (SipException e) {
            e.printStackTrace();
        }
    }
    private void updateStatus(final String str)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                TextView labelView = (TextView) findViewById(R.id.textView12);
                labelView.setText(str);
            }
        });
    }
    public void updateStatus(final SipAudioCall call) {
        String useName = call.getPeerProfile().getDisplayName();
        if(useName == null) {
            useName = call.getPeerProfile().getUserName();
        }
        updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());
        this.runOnUiThread(new Runnable() {
            public void run() {
                button = (Button) findViewById(R.id.button);
                button.setText("Hang up");
                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            call.endCall();
                        } catch (SipException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    private void Callout()
    {
        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {

                @Override
                public void onCallEstablished(SipAudioCall call) {
                    Log.d("dsd", "onCallEstablished: established");
                    call.startAudio();
                    call.setSpeakerMode(false);
                    //call.toggleMute();
                    updateStatus(call);
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    // Do something.
                    updateStatus("Call Ended");
                    Initiate();
                }

                @Override
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                }

                @Override
                public void onError(SipAudioCall call, int errorCode, String errorMessage) {
                    super.onError(call, errorCode, errorMessage);
                    Log.d("dsd", "onError: "+errorMessage);
                }
            };
            Log.d("sdd", "Callout: "+mSipProfile.getUriString());
            call = mSipManager.makeAudioCall(mSipProfile.getUriString(), sipAddress, listener, 30);
            Log.d("dsds", "Callout: "+call.isInCall());
        } catch (SipException e) {
            e.printStackTrace();
        }

    }





}
