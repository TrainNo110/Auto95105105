package com.loopautodial;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.LogWriter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AutoDial extends Activity implements OnClickListener{
	
	Button mDialBtn;
	AutoDial mAutoDial = AutoDial.this;
	Context mContext;
	ADTelephony mADTelephony;
	Handler mHandler;
	Runnable mPhoneStateRun;
	Process mProcess;
	
	private static final String[] AREA_BEIJING = {"010", "022", "0335", "0353", "0534",
		"0310", "0311", "0312", "0313", "0314", "0315", "0316", "0317", "0318", "0319", 
		};
	//TODO: Add other area codes here
	
	private static final String PHONE_GLOBAL = "95105105";
	private static final String PHONE_XIAN = "96688";
	private static final String PHONE_CHENGDU = "96006";
	
	boolean shouldRedial = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_dial);
		mContext = mAutoDial.getBaseContext();
		mADTelephony = new ADTelephony(mContext);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Log.d("Trap", "Handling Message");
				if(msg.what == 1){
					shouldRedial = true;
				}
			}
		};
		mPhoneStateRun = new PhoneStateRun(mADTelephony, mHandler);
		findViews();
		setListener();
	}
	
	protected void findViews(){
		mDialBtn = (Button)findViewById(R.id.dial_btn);
	}
	
	protected void setListener(){
		mDialBtn.setOnClickListener(this);
		mADTelephony.getTelephonyManager().listen(new PhoneStateListener(){
			
			@Override
			public void onCallStateChanged(int state, String incomingNumber){
				super.onCallStateChanged(state, incomingNumber);
				Log.d("Trap", "Listened");
				if(shouldRedial && state == TelephonyManager.CALL_STATE_IDLE){
					Log.d("Trap", "IDLE:Redial");
					mADTelephony.call("10086");
					shouldRedial = false;
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.dial_btn:
			mADTelephony.call("13917671578");
			shouldRedial = false;
			mHandler.post(mPhoneStateRun);
			break;
		}
	}
	

}
