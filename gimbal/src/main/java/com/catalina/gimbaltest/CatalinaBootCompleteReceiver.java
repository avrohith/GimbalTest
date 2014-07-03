package com.catalina.gimbaltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class CatalinaBootCompleteReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent service = new Intent(context, GeoFenceService.class);
			context.startService(service);
			FileLogger.getInstance().write("Boot complete event received ---- starting service again");
		}				
	}
	
}
