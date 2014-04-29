package orz.yanagin.android.mns.receivers;

import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * モバイルネットワークの接続要求時に呼ばれるBroadcastReceiver
 * @author k-yanagihara
 */
public class MobileNetworkConnectRequestReceiver extends MobileNetworkSwitchRequestReceiverBase {

	@Override
	protected ConnectionStatus getConnectionStatus() {
		return ConnectionStatus.CONNECTED;
	}

	@Override
	protected void success(Context context, Intent intent) {
		Intent disconnectIntent = new Intent(MobileNetworkSwitchRequestReceiverBase.MOBILE_NETWORK_DISCONNECT_REQUEST_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, disconnectIntent, 0);

		long wakeupTime = System.currentTimeMillis() + (30 * 1000);

		AlarmManager alarmManager = ActivityHelper.getSystemService(context, Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, wakeupTime, sender);
	}

}
