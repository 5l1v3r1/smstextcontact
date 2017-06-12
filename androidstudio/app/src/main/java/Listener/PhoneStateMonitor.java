package Listener;

import genel.stconst;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateMonitor extends PhoneStateListener {
	Context context;

	public PhoneStateMonitor(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	// This Method Automatically called when changes is detected in Phone State
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);

		stconst.AramaKaydiGuncellensin = 1;
		stconst.ArayanKisi = incomingNumber;
		// Checking The phone state
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE: // Telefon Bosta

			break;
		case TelephonyManager.CALL_STATE_RINGING: // Telefon Caliyor

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // Cagri Sonlandirildi

			break;

		}

	}

}
