package com.loopautodial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AutoDial extends Activity implements OnClickListener{
	
	Button mDialBtn;
	Button mBeijingBureauBtn;
	Button mShanghaiBureauBtn;
	Button mGuangzhouCompanyBtn;
	Button mHarbinBureauBtn;
	Button mShengyangBureauBtn;
	Button mJinanBureauBtn;
	Button mTaiyuanBureauBtn;
	Button mZhengzhouBureauBtn;
	Button mXianBureauBtn;
	Button mNanchangBureauBtn;
	Button mLanzhouBureauBtn;
	Button mNanningBureauBtn;
	Button mChengduBureauBtn;
	Button mKunmingBureauBtn;
	Button mQingzangCompanyBtn;
	Button mHohhotBureauBtn;
	Button mUrumqiBureauBtn;
	
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
	
	private static final int MAX_RETRY = 12;
	
	private static final String[] AREA_BEIJING = {"010", "022", "0335", "0353", "0534",
		"0310", "0311", "0312", "0313", "0314", "0315", "0316", "0317", "0318", "0319"
		};
	private static final String[] AREA_SHANGHAI = {"021", "025", "0523", "0527", "0580", 
		"0510", "0511", "0512", "0513", "0514", "0515", "0516", "0517", "0518", "0519", 
		"0550", "0551", "0552", "0553", "0554", "0555", "0556", "0557", "0558", "0559", 
		"0570", "0571", "0572", "0573", "0574", "0575", "0576", "0577", "0578", "0579", 
		"0561", "0562", "0563", "0564", "0566"
		};
	private static final String[] AREA_GUANGZHOU_GUANGDONGSHENG = {"020", "0660", "0662", "0663", "0668",
		"0750", "0751", "0752", "0753", "0754", "0755", "0756", "0757", "0758", "0759",
		"0760", "0762", "0763", "0766", "0768", "0769"
		};
	private static final String[] AREA_GUANGZHOU_HUNAN = {"0730", "0731", "0734", "0735", "0736", "0737", "0738", "0739",
		"0743", "0744", "0745", "0746"
		};
	private static final String[] AREA_GUANGZHOU_HAINAN = {"0898"};
	private static final String[] AREA_HERBIN = {"0451", "0452", "0453", "0454", "0455", "0456", "0457", "0458", "0459",
		"0464", "0467", "0468", "0469", "0470"
		};
	private static final String[] AREA_SHENGYANG = {"024", "0411", "0412", "0414", "0415", "0416", "0417", "0418", "0419", 
		"0423", "0427", "0429", "0431", "0432", "0433", "0434", "0435", "0436", "0437", "0438", "0439", "0475", "0476", "0482"
		};
	private static final String[] AREA_JINAN = {"0530", "0531", "0532", "0533", "0535", "0536", "0537", "0538", "0539",
		"0543", "0546", "0631", "0632", "0633", "0634", "0635"
		};
	private static final String[] AREA_TAIYUAN = {"0350", "0351", "0352", "0354", "0357", "0358", "0359", "0349"};
	private static final String[] AREA_ZHENGZHOU = {"0371", "0355", "0356", "0370", "0372",
		"0373", "0374", "0375", "0377", "0378", "0379", "0391", "0392", "0393", "0398"
		};
	private static final String[] AREA_XIAN = {"029", "0911", "0912", "0913", "0914", "0915", "0916", "0917", "0919"};
	private static final String[] AREA_NANCHANG = {"0591", "0592", "0593", "0594", "0595", "0596", "0597", "0598", "0599", "0701",  
		"0790", "0791", "0792", "0793", "0794", "0795", "0796", "0797", "0798", "0799"
		};
	private static final String[] AREA_NANNING = {"0770", "0771", "0772", "0773", "0774", "0775", "0776", "0777", "0778", "0779"};
	private static final String[] AREA_LANZHOU = {"0930", "0931", "0932", "0933", "0934", "0935", "0936", "0937", "0938", "0939",
		"0941", "0943", "0951", "0952", "0953", "0954", "0955"
		};
	private static final String[] AREA_CHENGDU = {"023", "028", "0812", "0813", "0816", "0817", "0818",	"0825", "0826", "0827",
		"0830", "0831", "0832", "0833", "0834", "0835", "0836", "0837", "0838", "0839",
		"0851", "0852", "0853", "0854", "0855", "0856", "0857", "0858", "0859"
		};
	private static final String[] AREA_KUNMING = {"0691", "0692", "0883", "0886", "0887", "0888",
		"0870", "0871", "0872", "0873", "0874", "0875", "0876", "0877", "0878", "0879"
		};
	private static final String[] AREA_HOHHOT = {"0471", "0472", "0473", "0474", "0477", "0478", "0479", "0483"};
	private static final String[] AREA_URUMQI = {"0901", "0902", "0903", "0906", "0908", "0909",
		"0990", "0991", "0992", "0993", "0994", "0995", "0996", "0997", "0998", "0999"
		};
	private static final String[] AREA_QINGZANG = {"0891", "0892", "0893", "0894", "0895", "0896", "0897",
		"0970", "0971", "0972", "0973", "0974", "0975", "0976", "0977", "0978", "0979"
		};
	
	private static final int GUANGTIE_GUANGZHOU = 0;
	private static final int GUANGTIE_HUNAN = 1;
	private static final int GUANGTIE_HAINAN = 2;
	
	private static final String PHONE_GLOBAL = "95105105";
	private static final String PHONE_XIAN = "96688";
	private static final String PHONE_CHENGDU = "96006";
	private static final String PHONE_GUANGZHOU = "96020088";

	@SuppressWarnings("unused")
	private static final String[] AREA_WUHAN = {"027", "0376", "0394", "0395", "0396",
		"0710", "0711", "0712", "0713", "0714", "0715", "0716", "0717", "0718", "0719",
		"0722", "0724", "0728"
	};
	
	@SuppressWarnings("unused")
	private static final String PHONE_PINGDINGSHAN = "037595105106";
	
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
		mShanghaiBureauBtn = (Button)findViewById(R.id.shanghai_bureau_btn);
		mGuangzhouCompanyBtn = (Button)findViewById(R.id.guangzhou_company_btn);
		mHarbinBureauBtn = (Button)findViewById(R.id.harbin_bureau_btn);
		mShengyangBureauBtn = (Button)findViewById(R.id.shenyang_bureau_btn);
		mJinanBureauBtn = (Button)findViewById(R.id.jinan_bureau_btn);
		mTaiyuanBureauBtn = (Button)findViewById(R.id.taiyuan_bureau_btn);
		mZhengzhouBureauBtn = (Button)findViewById(R.id.zhengzhou_bureau_btn);
		mXianBureauBtn = (Button)findViewById(R.id.xian_bureau_btn);
		mNanchangBureauBtn = (Button)findViewById(R.id.nanchang_bureau_btn);
		mLanzhouBureauBtn = (Button)findViewById(R.id.lanzhou_bureau_btn);
		mNanningBureauBtn = (Button)findViewById(R.id.nanning_bureau_btn);
		mChengduBureauBtn = (Button)findViewById(R.id.chengdu_bureau_btn);
		mKunmingBureauBtn = (Button)findViewById(R.id.kunming_bureau_btn);
		mQingzangCompanyBtn = (Button)findViewById(R.id.qingzang_company_btn);
		mHohhotBureauBtn = (Button)findViewById(R.id.hohhot_bureau_btn);
		mUrumqiBureauBtn = (Button)findViewById(R.id.urumqi_bureau_btn);
		mDialBtn = (Button)findViewById(R.id.dial_btn);
	}
	
	protected void setListener(){
		mBeijingBureauBtn.setOnClickListener(this);
		mShanghaiBureauBtn.setOnClickListener(this);
		mGuangzhouCompanyBtn.setOnClickListener(this);
		mHarbinBureauBtn.setOnClickListener(this);
		mShengyangBureauBtn.setOnClickListener(this);
		mJinanBureauBtn.setOnClickListener(this);
		mTaiyuanBureauBtn.setOnClickListener(this);
		mZhengzhouBureauBtn.setOnClickListener(this);
		mXianBureauBtn.setOnClickListener(this);
		mNanchangBureauBtn.setOnClickListener(this);
		mLanzhouBureauBtn.setOnClickListener(this);
		mNanningBureauBtn.setOnClickListener(this);
		mChengduBureauBtn.setOnClickListener(this);
		mKunmingBureauBtn.setOnClickListener(this);
		mQingzangCompanyBtn.setOnClickListener(this);
		mHohhotBureauBtn.setOnClickListener(this);
		mUrumqiBureauBtn.setOnClickListener(this);
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
		switch(v.getId()){
		case R.id.beijing_bureau_btn:
		case R.id.shanghai_bureau_btn:
		case R.id.harbin_bureau_btn:
		case R.id.shenyang_bureau_btn:
		case R.id.jinan_bureau_btn:
		case R.id.taiyuan_bureau_btn:
		case R.id.zhengzhou_bureau_btn:
		case R.id.xian_bureau_btn:
		case R.id.nanchang_bureau_btn:
		case R.id.lanzhou_bureau_btn:
		case R.id.nanning_bureau_btn:
		case R.id.chengdu_bureau_btn:
		case R.id.kunming_bureau_btn:
		case R.id.qingzang_company_btn:
		case R.id.hohhot_bureau_btn:
		case R.id.urumqi_bureau_btn:
			mCurrentAreaCodeList.clear();
			mPhoneNumList.clear();
			mPhoneNumList.add(PHONE_GLOBAL);
			setTelNumList(v.getId());
			mCount = MAX_RETRY;
			startAutoDial();
			break;
		case R.id.guangzhou_company_btn:

			showGuangTieDialog();
		case R.id.dial_btn:
			break;
		}
	}
	
	private void setTelNumList(int id){
		switch(id){
		case R.id.beijing_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_BEIJING));
			break;
		case R.id.shanghai_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_SHANGHAI));
			break;
		case R.id.harbin_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_HERBIN));
			break;
		case R.id.shenyang_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_SHENGYANG));
			break;
		case R.id.jinan_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_JINAN));
			break;
		case R.id.taiyuan_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_TAIYUAN));
			break;
		case R.id.zhengzhou_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_ZHENGZHOU));
			break;
		case R.id.xian_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_XIAN));
			mPhoneNumList.add(PHONE_XIAN);
			break;
		case R.id.nanchang_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_NANCHANG));
			break;
		case R.id.lanzhou_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_LANZHOU));
			break;
		case R.id.nanning_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_NANNING));
			break;
		case R.id.chengdu_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_CHENGDU));
			mPhoneNumList.add(PHONE_CHENGDU);
			break;
		case R.id.kunming_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_KUNMING));
			break;
		case R.id.qingzang_company_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_QINGZANG));
			break;
		case R.id.hohhot_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_HOHHOT));
			break;
		case R.id.urumqi_bureau_btn:
			mCurrentAreaCodeList.addAll(Arrays.asList(AREA_URUMQI));
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
	
	public void showGuangTieDialog(){
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.please_choose_province));
		builder.setItems(new String[] {getString(R.string.guangdong), getString(R.string.hunan),	getString(R.string.hainan)},
				new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				mCurrentAreaCodeList.clear();
				mPhoneNumList.clear();
				mPhoneNumList.add(PHONE_GLOBAL);
				switch(which){
				case GUANGTIE_GUANGZHOU:
					mCurrentAreaCodeList.addAll(Arrays.asList(AREA_GUANGZHOU_GUANGDONGSHENG));
					mPhoneNumList.add(PHONE_GUANGZHOU);
					break;
				case GUANGTIE_HUNAN:
					mCurrentAreaCodeList.addAll(Arrays.asList(AREA_GUANGZHOU_HUNAN));
					mPhoneNumList.add(PHONE_GUANGZHOU);
					break;
				case GUANGTIE_HAINAN:
					mCurrentAreaCodeList.addAll(Arrays.asList(AREA_GUANGZHOU_HAINAN));
					break;
				}
				dialog.dismiss();
				mCount = MAX_RETRY;
				startAutoDial();

			}
		});
		final Dialog dialog = builder.create();
		dialog.show();
	}
}
