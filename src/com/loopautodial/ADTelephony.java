package com.loopautodial;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

public class ADTelephony{
	
	private ITelephony mITelephony;
	
	private TelephonyManager mTelephonyManager;
	
	public ITelephony getITelephony(){
		return mITelephony;
	}
	
	public TelephonyManager getTelephonyManager(){
		return mTelephonyManager;
	}
	
	public ADTelephony(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE); 
		this.mTelephonyManager = telephonyManager; 
	    Class<TelephonyManager> c = TelephonyManager.class;  
	    Method getITelephonyMethod = null; 
	    this.mITelephony = null;
	    try {  
	        getITelephonyMethod = c.getDeclaredMethod("getITelephony",(Class[]) null); // 获取声明的方法   
	        getITelephonyMethod.setAccessible(true);  
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (NoSuchMethodException e) {  
	        e.printStackTrace();  
	    }  
	  
	    try {  
	    	this.mITelephony = (ITelephony) getITelephonyMethod.invoke(telephonyManager, (Object[]) null); // 获取实例   
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}
	public void dial(Context context, String number) {
        try {
            Method dial = mITelephony.getClass().getDeclaredMethod("dial", String.class);
            dial.invoke(mITelephony, number);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
	public void call(Context context, String number) {
        try {
            Method dial = mITelephony.getClass().getDeclaredMethod("call", String.class);
            dial.invoke(mITelephony, number);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
