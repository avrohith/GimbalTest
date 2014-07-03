package com.catalina.gimbaltest;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;

public class GeoFenceService extends Service implements PlaceEventListener {

	private static int notificationId = 10;
	private ContextPlaceConnector contextPlaceConnector;
	private ContextCoreConnector contextCoreConnector;
	
	private static final String TAG = "GIMBAL";
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		FileLogger.getInstance().write("Geo fence service started");
		
		contextPlaceConnector = ContextPlaceConnectorFactory.get(getApplicationContext());
		contextPlaceConnector.addPlaceEventListener(this);
		
		contextCoreConnector = ContextCoreConnectorFactory.get(getApplicationContext()); 
		FileLogger.getInstance().write("context connector enabled: "+contextCoreConnector.isPermissionEnabled());
		
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub		
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void placeEvent(PlaceEvent arg0) {
		// TODO Auto-generated method stub
		FileLogger.getInstance().write("Event: "+arg0.getEventType()+ "Place: "+arg0.getPlace().getPlaceName());
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);        
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Geo "+arg0.getEventType());
        builder.setContentText("Location - "+arg0.getPlace().getPlaceName());
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{0, 5000});

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationId++;
        notificationManager.notify(notificationId, builder.build());		
	}

	@Override
	public void onDestroy() {
		FileLogger.getInstance().write("OnDestroy called, GeoFenceService is being killed");
		contextPlaceConnector.removePlaceEventListener(this);
		super.onDestroy();		
	}
	
}
