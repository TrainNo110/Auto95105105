package com.loopautodial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.android.internal.telephony.ITelephony;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateRun implements Runnable {
    //电话服务  
    private TelephonyManager telManager;
    private ITelephony iTelephony;
    private Handler handler;
    
    public PhoneStateRun(ADTelephony adTelephony, Handler handler) {  
        this.telManager = adTelephony.getTelephonyManager();
        this.iTelephony = adTelephony.getITelephony();
        this.handler = handler;
    }
    
    @Override  
    public void run() {  
        //获取当前话机状态  
        int callState = telManager.getCallState();  
        Log.i("TestService", "开始.........." + Thread.currentThread().getName());  
        //记录拨号开始时间  
        long threadStart = System.currentTimeMillis();  
        Process process = null;  
        InputStream inputstream;  
        BufferedReader bufferedreader;  

        try {  
			process = Runtime.getRuntime().exec("logcat -b radio -c");
			process.waitFor();
            process = Runtime.getRuntime().exec("logcat -v time -b radio");  
            inputstream = process.getInputStream();  
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
            bufferedreader = new BufferedReader(inputstreamreader);  
            String str = "";  
            long dialingStart = 0;  
            boolean enableVibrator = false;  
            boolean isAlert = false;
            int currentCallState;
            while ((str = bufferedreader.readLine()) != null) {  
                //如果话机状态从摘机变为空闲,销毁线程  
            	currentCallState = telManager.getCallState();
                if (callState == TelephonyManager.CALL_STATE_OFFHOOK && currentCallState == TelephonyManager.CALL_STATE_IDLE) {  
                    break;  
                }else{
                	callState = currentCallState;
                }
                // 线程运行20秒后自动销毁  
                if (System.currentTimeMillis() - threadStart > 30000) {  
                    break;  
                }  
                if (str.contains("GET_CURRENT_CALLS") || str.contains("CALL_STATE_CHANGED")){
                	Log.i("TestService", Thread.currentThread().getName() + ":" + str);  
                }
                // 记录GSM状态DIALING  
                if (str.contains("GET_CURRENT_CALLS") && str.contains("DIALING")) {  
                    // 当DIALING开始并且已经经过ALERTING或者首次DIALING  
                    if (!isAlert || dialingStart == 0) {  
                        //记录DIALING状态产生时间  
                        dialingStart = System.currentTimeMillis();  
                        isAlert = false;  
                    }  
                    continue;  
                }  
                if (str.contains("GET_CURRENT_CALLS") && str.contains("ALERTING") && !enableVibrator) {  
                      
                    long temp = System.currentTimeMillis() - dialingStart;  
                    isAlert = true;
                    iTelephony.endCall();
                    handler.sendEmptyMessage(1);
                    //这个是关键,当第一次DIALING状态的时间,与当前的ALERTING间隔时间在1.5秒以上并且在20秒以内的话  
                    //那么认为下次的ACTIVE状态为通话接通.  
                    if (temp > 1500 && temp < 20000) {  
                        enableVibrator = true; 
                        Log.i("TestService", "间隔时间....." + temp + "....." + Thread.currentThread().getName());  
                    }  
                    continue;  
                }  
                if (str.contains("GET_CURRENT_CALLS") && str.contains("ACTIVE")) {  
                    Log.i("TestService", "电话已接通"  + "....." + Thread.currentThread().getName());
                    enableVibrator = false;  
                    break;  
                }  
            }  
            Log.i("TestService", "结束.........." + Thread.currentThread().getName());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }
    }  
}
