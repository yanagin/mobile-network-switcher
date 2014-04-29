package orz.yanagin.android.mns.controllers;

import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.android.mns.utils.MobileNetworkController;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * モバイルネットワークの切り替えを行うVIEWをコントロールするクラス
 * @author k-yanagihara
 */
public abstract class MobileNetworkSwitchServiceBase extends Service {

	@Override
	public void onCreate() {
		super.onCreate();

		initializeView();

        // モバイルネットワーク状態の変更を補足する
		registerReceiver(
				mobileNetworkConnectivityChangingReceiver,
				new IntentFilter(MobileNetworkSwitchAsyncTask.MOBILE_NETWORK_CONNECTIVITY_CHANGING_ACTION));
		registerReceiver(
				mobileNetworkConnectivityChangeReceiver,
				new IntentFilter(MobileNetworkSwitchAsyncTask.MOBILE_NETWORK_CONNECTIVITY_CHANGED_ACTION));

		// モバイルネットワークの状態を反映
		ConnectionStatus connectionStatus = new MobileNetworkController(this).getConnectionStatus();
		if (ConnectionStatus.CONNECTED == connectionStatus) {
			onConnected(this);
		} else {
			onDisonnected(this);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mobileNetworkConnectivityChangingReceiver);
		unregisterReceiver(mobileNetworkConnectivityChangeReceiver);
	}

	/**
	 * VIEWの初期化処理
	 */
	abstract protected void initializeView();

	/**
	 * 接続状態への移行
	 * @param context Context
	 */
	abstract protected void onConnected(Context context);

	/**
	 * 接続切り替え状態への移行
	 * @param context Context
	 */
	abstract protected void onChanging(Context context);

	/**
	 * 未接続状態への移行
	 * @param context Context
	 */
	abstract protected void onDisonnected(Context context);

	/** モバイルネットワーク状態の変更開始を補足するブロードキャストレシーバ */
	private BroadcastReceiver mobileNetworkConnectivityChangingReceiver = new  BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 状態を表すアイコンへ変更
			onChanging(context);
		}

	};

	/** モバイルネットワーク状態の変更を補足するブロードキャストレシーバ */
	private BroadcastReceiver mobileNetworkConnectivityChangeReceiver = new  BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectionStatus connectionStatus = new MobileNetworkController(context).getConnectionStatus();
			if (connectionStatus == ConnectionStatus.CONNECTED) {
				onConnected(context);
			} else {
				onDisonnected(context);
			}
		}

	};

}
