package com.loopautodial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Random;

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
	Button mBeijingBureauBtn;
	
	AutoDial mAutoDial = AutoDial.this;
	Context mContext;
	ADTelephony mADTelephony;
	Handler mHandler;
	Runnable mPhoneStateRun;
	Process mProcess;
	
	Random mRandom;
	
	int mCount;
	
	List<String> mCurrentAreaCodeList = new ArrayList<String>();
	List<String> mPhoneNumList = new ArrayList<String>();
	
	private static final int MAX_RETRY = 6;
	
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
		mRandom = new Random(System.currentTimeMillis());
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
		mBeijingBureauBtn = (Button)findViewById(R.id.beijing_bureau_btn);
		mDialBtn = (Button)findViewById(R.id.dial_btn);
	}
	
	protected void setListener(){
		mBeijingBureauBtn.setOnClickListener(this);
		mDialBtn.setOnClickListener(this);
		mADTelephony.getTelephonyManager().listen(new PhoneStateListener(){
			
			@Override
			public void onCallStateChanged(int state, String incomingNumber){
				super.onCallStateChanged(state, incomingNumber);
				if(shouldRedial && state == TelephonyManager.CALL_STATE_IDLE){
					startAutoDial();
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
		mCurrentAreaCodeList.clear();
		mPhoneNumList.clear();
		mPhoneNumList.add(PHONE_GLOBAL);
		switch(v.getId()){
		case R.id.beijing_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_BEIJING));
			mCount = MAX_RETRY;
			startAutoDial();
			break;
		case R.id.dial_btn:
			break;
		}
	}
	
	private String makeNumber(){
		String areaCode;
		if(mCurrentAreaCodeList.size() == 0 || mPhoneNumList.size() == 0){
			return PHONE_GLOBAL;
		}else{
			areaCode = mCurrentAreaCodeList.get(Math.abs(mRandom.nextInt()) % mCurrentAreaCodeList.size());
			if(mPhoneNumList.size() == 1){
				return areaCode + mPhoneNumList.get(0);
			}else{
				return areaCode + mPhoneNumList.get(Math.abs(mRandom.nextInt()) % mPhoneNumList.size());
			}
		}
	}
	
	private void startAutoDial(){
		String number = makeNumber();
		if(mCount <= 0){
			return;
		}
		Log.d("Trap", number);
		mADTelephony.call(number);
		mCount = mCount - 1;
		shouldRedial = false;
		mHandler.removeCallbacks(mPhoneStateRun);
		mHandler.postDelayed(mPhoneStateRun, 200);
	}
}
