package net.iaf.framework.app;

import android.app.Application;
import android.content.Intent;

public class BaseApplication extends Application {

	private static BaseApplication sContext = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
	}
	
	public static BaseApplication getContext() {
		return sContext;
	}
	
	public void exitApp() {
		Intent intent = new Intent(ExitHandler.ACTION_EXIT);
		sendBroadcast(intent);
	}
}
