package com.ryde;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;

import com.ryde.constant.Constant;
import com.ryde.model.CompanyModel;

import java.util.ArrayList;

/**
 * The Class SplashScreen will launched at the start of the application. It will
 * be displayed for 3 seconds and than finished automatically and it will also
 * start the next activity of app.
 */
public class SplashScreen extends Activity {

	private SharedPreferences.Editor editor;
	private SharedPreferences prefs;

	/** Check if the app is running. */
	private boolean isRunning;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
		prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);
		isRunning = true;


		Constant.companyModelList = new ArrayList<>();
		CompanyModel companyModel = new CompanyModel();

		for (int i = 0; i < Constant.cntCompany; i++) {
			Constant.checkedCompanyValues[i] = prefs.getBoolean(Constant.CheckedCompanyListShared[i], false);
			companyModel = new CompanyModel(Constant.companyName[i], Constant.companyFare[i], Constant.companyFarePlus[i], Constant.checkedCompanyValues[i], i, Constant.companyPickupTime[i]);
			Constant.companyModelList.add(companyModel);
		}

		if (ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: permission check
			ActivityCompat.requestPermissions( SplashScreen.this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					Constant.Permission_ACCESS_FINE_LOCATION);
			return;
		}

		startSplash();

	}

	/**
	 * Starts the count down timer for 3-seconds. It simply sleeps the thread
	 * for 3-seconds.
	 */
	private void startSplash()
	{

		new Thread(new Runnable() {
			@Override
			public void run()
			{

				try
				{
					Thread.sleep(1000);

				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							doFinish();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * If the app is still running than this method will start the
	 * activity and finish the Splash.
	 */
	private synchronized void doFinish()
	{

		if (isRunning)
		{
			isRunning = false;
			Intent i = new Intent(SplashScreen.this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			isRunning = false;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(requestCode == Constant.Permission_ACCESS_FINE_LOCATION) {
			startSplash();
		}
	}
}