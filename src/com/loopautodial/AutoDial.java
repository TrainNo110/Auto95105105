package com.loopautodial;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_dial);
		mContext = mAutoDial.getBaseContext();
		mADTelephony = new ADTelephony(mContext);
		mPhoneStateRun = new PhoneStateRun(mAutoDial, mADTelephony);
		mHandler = new Handler();
		findViews();
		setListener();
	}
	
	protected void findViews(){
		mDialBtn = (Button)findViewById(R.id.dial_btn);
	}
	
	protected void setListener(){
		mDialBtn.setOnClickListener(this);
		
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
			mADTelephony.call(mContext, "13917671578");
			mHandler.post(mPhoneStateRun);
			break;
		}
	}
	

}
