package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.content.*;

//для запуска сервиса макроритмов в автозагрузке
public class macroRithmReceiver extends BroadcastReceiver {
	
	final String LOG_TAG = "myLogs";

	public void onReceive(Context context, Intent intent) {
		//Log.d(LOG_TAG, "onReceive " + intent.getAction());
		context.startService(new Intent(context, macrorithmService.class));
	}
}
