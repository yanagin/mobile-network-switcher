package orz.yanagin.android.mns.receivers;

import orz.yanagin.android.mns.controllers.MobileNetworkSwitchAsyncTask;
import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.android.mns.utils.MobileNetworkController;
import orz.yanagin.android.mns.utils.NetworkStatus;
import orz.yanagin.android.mns.utils.NetworkType;
import orz.yanagin.commons.Classes;
import orz.yanagin.commons.android.ActivityHelper;
import orz.yanagin.commons.android.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Vibrator;

/**
 * モバイルネットワークの変更要求時に呼ばれるBroadcastReceiver
 * @author k-yanagihara
 */
abstract public class MobileNetworkSwitchRequestReceiverBase extends BroadcastReceiver {

	/** モバイルネットワーク接続要求アクション */
	public static final String MOBILE_NETWORK_CONNECT_REQUEST_ACTION = Classes.fullName(MobileNetworkSwitchRequestReceiverBase.class) + ".MOBILE_NETWORK_CONNECT_REQUEST_ACTION";

	/** モバイルネットワーク切断要求アクション */
	public static final String MOBILE_NETWORK_DISCONNECT_REQUEST_ACTION = Classes.fullName(MobileNetworkSwitchRequestReceiverBase.class) + ".MOBILE_NETWORK_DISCONNECT_REQUEST_ACTION";

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectionStatus connectionStatus = getConnectionStatus();
		String action = connectionStatus == ConnectionStatus.CONNECTED ? "接続" : "切断";

		Logger.debug(context, "モバイルネットワークの" + action + "が要求されました。intent:" + intent.getAction());

		// 接続状態の確認
		ConnectivityManager connectivityManager = ActivityHelper.getSystemService(context, Context.CONNECTIVITY_SERVICE);
		NetworkType activeNetworkType = new NetworkStatus(connectivityManager).getActiveNetwork();
		Logger.debug(context, "モバイルネットワークの接続状態 NetworkType:" + activeNetworkType);
		if ((connectionStatus == ConnectionStatus.CONNECTED && activeNetworkType != NetworkType.DISABLE)
				|| (connectionStatus == ConnectionStatus.DISCONNECTED && activeNetworkType == NetworkType.DISABLE)) {
			return;
		}

		// 変更要求を受信したことをバイブレーションで通知
		Vibrator vibrator = ActivityHelper.getSystemService(context, Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] {0, 2000, 200, 2000, 200, 2000}, -1);

		// 変更要求を通知
		context.sendBroadcast(new Intent(MobileNetworkSwitchAsyncTask.MOBILE_NETWORK_CONNECTIVITY_CHANGING_ACTION));

		// モバイルネットワークの変更
		MobileNetworkController mobileNetworkController = new MobileNetworkController(context);
		mobileNetworkController.connect();
		new MobileNetworkSwitchAsyncTask(
				context,
				mobileNetworkController,
				connectionStatus).execute();
		Logger.debug(context, "モバイルネットワークの" + action + "が完了しました。");

		success(context, intent);
	}

	protected void success(Context context, Intent intent) {};

	abstract protected ConnectionStatus getConnectionStatus();

}
