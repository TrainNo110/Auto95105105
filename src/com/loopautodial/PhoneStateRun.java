package com.loopautodial;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.android.internal.telephony.ITelephony;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateRun implements Runnable {

	private TelephonyManager telManager;
    private ITelephony iTelephony;
    private Handler handler;
    
    private static final int DIALING_TIME_LIMIT = 12000;
    private static final int ALERTING_TIME_LIMIT = 4000;
    
    public PhoneStateRun(ADTelephony adTelephony, Handler handler) {  
        this.telManager = adTelephony.getTelephonyManager();
        this.iTelephony = adTelephony.getITelephony();
        this.handler = handler;
    }
    
    @Override  
    public void run() {  
        int callState = telManager.getCallState();  
        Log.i("TestService", "开始.........." + Thread.currentThread().getName());  
        long threadStart = System.currentTimeMillis();  
        Process process = null;  
        InputStream inputstream;  
        BufferedReader bufferedreader;  

        try {  
			process = Runtime.getRuntime().exec("logcat -b radio -c");	//清理raido域里的旧log
			process.waitFor();
            process = Runtime.getRuntime().exec("logcat -v time -b radio");  //开始运行logcat
            inputstream = process.getInputStream();  
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
            bufferedreader = new BufferedReader(inputstreamreader);  
            String str = "";  
            long dialingStart = threadStart;
            long alertingStart = -1;
            boolean isDialing = false;
            boolean isAlerting = false;
            int currentCallState;
            while ((str = bufferedreader.readLine()) != null) {
            	
                //如果话机状态从摘机变为空闲,销毁线程  
            	currentCallState = telManager.getCallState();
                if (callState == TelephonyManager.CALL_STATE_OFFHOOK && currentCallState == TelephonyManager.CALL_STATE_IDLE) {  
                    break;  
                }else{
                	callState = currentCallState;
                }
                // 拨打一定时间，或等待一定时间后自动重拨  
                if ((!isAlerting && System.currentTimeMillis() - dialingStart > DIALING_TIME_LIMIT) || 
                		(isAlerting && System.currentTimeMillis() - alertingStart > ALERTING_TIME_LIMIT)) {
                    iTelephony.endCall();
                    handler.sendEmptyMessage(1);
                    break;  
                }  
                if (str.contains("GET_CURRENT_CALLS")){
                	Log.i("Trap", str);
                }

                // 记录GSM状态DIALING  
                if (str.contains("GET_CURRENT_CALLS") && str.contains("DIALING")) {  
                    if (!isDialing) {  
                        //记录DIALING状态产生时间 
                        dialingStart = System.currentTimeMillis();  
                        isDialing = true;  
                    }  
                    continue;  
                }  
                
                // 记录状态ALERTING
                if (str.contains("GET_CURRENT_CALLS") && str.contains("ALERTING")){
                	if (!isAlerting){
                		//记录ALERTING状态产生时间
                		alertingStart = System.currentTimeMillis();
                		isAlerting = true;
                	}
                	continue;
                }


                if (str.contains("GET_CURRENT_CALLS") && str.contains("ACTIVE")) {  
                    Log.i("TestService", "电话已接通"  + "....." + Thread.currentThread().getName());
                    break;  
                }  
            }  
            Log.i("TestService", "结束.........." + Thread.currentThread().getName());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }
    }  
}
