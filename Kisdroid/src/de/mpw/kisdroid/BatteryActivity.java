package de.mpw.kisdroid;

import de.mpw.kisdroid.protocols.Battery;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends Activity {
	
	
	private ProgressBar pb_battery_status;
	private TextView tx_bat_percentage;

	private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			pb_battery_status.setProgress(intent.getIntExtra(Battery.EXTRA_PERCENTAGE, 100));
			tx_bat_percentage.setText(String.valueOf(intent.getIntExtra(Battery.EXTRA_PERCENTAGE,100)));
		}
	};
	
			
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery);
		pb_battery_status = (ProgressBar) findViewById(R.id.pb_battery_status);
		tx_bat_percentage = (TextView) findViewById(R.id.tx_bat_percent);

	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		getApplicationContext().unregisterReceiver(mBroadcast);
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		final IntentFilter filter = new IntentFilter(KismetMsgHandler.ACTION_BATTERY);
		getApplicationContext().registerReceiver(mBroadcast, filter);
		super.onResume();
	}

}