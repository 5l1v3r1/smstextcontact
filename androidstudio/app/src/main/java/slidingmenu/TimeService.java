package slidingmenu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.devillord.smstextcontact.R;

public class TimeService extends Service {
	// constant

	public static final long NOTIFY_INTERVAL = 3600 * 1000; // 1 dakika da

	// run on another Thread to avoid crash
	private final Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// cancel if already existed
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
				NOTIFY_INTERVAL);
	}

	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// display toast

					ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

					NetworkInfo nf = cn.getActiveNetworkInfo();
					if (nf != null && nf.isConnected() == true) {


						//new Don().execute(url); //notification için kullanılabilir
					}
				}

			});
		}

	}



	private String getJsonFromUrl(String strUrl) {
		String strJson = null;
		InputStream is = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(strUrl);
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			strJson = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			strJson = null;
		}
		return strJson;
	}



	private class Don extends AsyncTask<String, Void, String> {

		Context ctx;
		String strJson = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(final String... url) {
			strJson = getJsonFromUrl(url[0]);
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.i(strJson, "Donen Deger");
			try {
				displayNotificationOne("x", "x");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private int numMessagesOne = 0;
	private NotificationManager myNotificationManager;
	private final int notificationIdOne = 111;

	protected void displayNotificationOne(String Numara, String Aciklama) {

		// Invoking the default notification service
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext());

		mBuilder.setContentTitle(getApplicationContext().getString(
				R.string.app_name));
		mBuilder.setContentText(Aciklama);
		mBuilder.setTicker(Aciklama);
		mBuilder.setSmallIcon(R.drawable.tools);

		// Increase notification number every time a new notification
		// arrives
		mBuilder.setNumber(++numMessagesOne);
		mBuilder.setAutoCancel(true);
		mBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		resultIntent.putExtra("notificationId", Numara);

		// This ensures that navigating backward from the Activity leads out
		// of
		// the app to Home page
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(getApplicationContext());
		// Adds the back stack for the Intent
		stackBuilder.addParentStack(MainActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_ONE_SHOT // can only be used once
				);
		// start the activity when the user clicks the notification text
		mBuilder.setContentIntent(resultPendingIntent);

		myNotificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// pass the Notification object to the system
		myNotificationManager.notify(notificationIdOne, mBuilder.build());
	}

}