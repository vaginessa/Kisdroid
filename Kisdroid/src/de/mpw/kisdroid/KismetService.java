package de.mpw.kisdroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class KismetService extends Service {

	private KismetBinder mBinder;
	private String TAG = "KISMET Service";
	private KismetClient client;
	private String SERVER = "192.168.2.11";
	private int PORT = 2501;

	private NotificationManager nManager;
	private String NTitel = "Kisdroid Service";
	private String NDetail = "Kisdroid Service l�uft";
	int icon = R.drawable.ic_launcher;
	private Notification nBenachrichtigung;

	private final boolean debug = true;
	private final static int ID_RUNNING = 2304;

	@Override
	// Binder Methode f�r den Service
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	// Erstellung des Services
	@Override
	public void onCreate() {
		super.onCreate();
		if (debug) {
			Log.d(TAG, "OnCreate() aufgerufgen");
		}
		// Neues Kismet Client Objekt erstellen
		client = new KismetClient(SERVER, PORT, this.getApplicationContext());

		// Notification Manager holen
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		if (debug) {
			Log.d(TAG, "OnDestroy() aufgerufgen");
		}
		// Client stopen
		client.stopClient();
		// Notification l�schen
		nManager.cancel(ID_RUNNING);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Client starten
		client.start();
		// Wenn es einen Fehler beim starten gab, wird er ausgegeben
		if (client.Fehler != "") {
			Toast fehler = Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.toast_fehler) + client.Fehler,
					Toast.LENGTH_SHORT);
			fehler.show();
		} else {
			super.onStart(intent, startId);
			// Benachrichtigung erstellen und anzeigen das der Service gestartet
			// wurde
			// Basis Daten der Benachrichtigung festlegen
			// "icon" ist das App icon, Ndetail ist der Detailtext
			nBenachrichtigung = new Notification(icon, NDetail, System.currentTimeMillis());
			// Den Context der Anwendung holen
			Context context = getApplicationContext();
			// Den intent und Pending intent auf die Hauptactivity setzen
			Intent mainintent = new Intent(context, KisdroidActivity.class);
			PendingIntent Pendingintent = PendingIntent.getActivity(context, 0, mainintent, 0);
			nBenachrichtigung.setLatestEventInfo(context, NTitel, NDetail, Pendingintent);
			// Benachrichtigung anzeigen
			nManager.notify(ID_RUNNING, nBenachrichtigung);

		}
		if (debug) {
			Log.d(TAG, "OnStart() aufgerufgen" + intent.getPackage() + "Mit der StartID:" + startId);
		}
	}

}
