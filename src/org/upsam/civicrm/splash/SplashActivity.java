package org.upsam.civicrm.splash;

import org.upsam.civicrm.MainActivity;
import org.upsam.civicrm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Actividad inicial de la aplicación
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class SplashActivity extends Activity{
	
	
	private boolean mIsBackButtonPressed;
	private static int SPLASH_DURATION = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splash);
				
		new Handler().postDelayed(new Runnable(){
			
			    @Override
				public void run() {			    	
			    	SplashActivity.this.finish();
			    	if(!mIsBackButtonPressed)
			    	{			    		
			    		Intent intent = new Intent(SplashActivity.this,MainActivity.class);
			    		SplashActivity.this.startActivity(intent);						
			    	}
				}
		
		}, SPLASH_DURATION);
		
	}
		
	@Override
	public void onBackPressed() {
		mIsBackButtonPressed = Boolean.TRUE.booleanValue();
		super.onBackPressed();
	}
}
