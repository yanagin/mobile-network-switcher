package orz.yanagin.android.mns.controllers;

import orz.yanagin.android.mns.pro.R;
import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.android.mns.utils.MobileNetworkController;
import orz.yanagin.commons.Classes;
import orz.yanagin.commons.Timer;
import orz.yanagin.commons.android.ActivityHelper;
import orz.yanagin.commons.android.Logger;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;

/**
 * 非同期でモバイルネットワークの切り替えを行うタスク
 * @author k-yanagihara
 */
public class MobileNetworkSwitchAsyncTask extends
		AsyncTask<Void, Void, Boolean> {

	/** 接続状態を変更する前にブロードキャストされるアクション */
	public static final String MOBILE_NETWORK_CONNECTIVITY_CHANGING_ACTION = Classes.fullName(MobileNetworkSwitchAsyncTask.class) + ".MOBILE_NETWORK_CONNECTIVITY_CHANGING_ACTION";

	/** 接続状態が変化した場合にブロードキャストされるアクション */
	public static final String MOBILE_NETWORK_CONNECTIVITY_CHANGED_ACTION = Classes.fullName(MobileNetworkSwitchAsyncTask.class) + ".MOBILE_NETWORK_CONNECTIVITY_CHANGED_ACTION";

	/** Context */
	private final Context context;

	/** モバイルネットワークを操作するクラス */
	private final MobileNetworkController mobileNetworkController;

	/** 切り替え後のネットワーク接続状態 */
	private final ConnectionStatus connectionStatus;

	/** ProgressDialog */
	private final ProgressDialog progressDialog;

	/**
	 * コンストラクタ
	 * @param context Context
	 * @param mobileNetworkController モバイルネットワークを操作するクラス
	 * @param connectionStatus 切り替え後のネットワーク接続状態
	 */
	public MobileNetworkSwitchAsyncTask(Context context, MobileNetworkController mobileNetworkController, ConnectionStatus connectionStatus) {
		this.context = context;
		this.mobileNetworkController = mobileNetworkController;
		this.connectionStatus = connectionStatus;
		if (context instanceof Activity) {
			progressDialog = new ProgressDialog(context);
		} else {
			progressDialog = null;
		}
	}

	@Override
	protected void onPreExecute() {
		if (progressDialog == null) {
			return;
		}

		// プログレスダイアログの設定
		String message = "Now " + (ConnectionStatus.CONNECTED == connectionStatus ? context.getString(R.string.connecting_dialog_message) : context.getString(R.string.disconnecting_dialog_message));
		progressDialog.setMessage(message);
		progressDialog.show();

		Logger.info(context, "モバイルネットワークの設定を開始します。");
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// 60秒間リトライを行う
		for (int i = 0; i < 60; i++) {
			if (connectionStatus == mobileNetworkController.getConnectionStatus()) {
				Logger.info(context, "モバイルネットワークの設定に成功しました。ConnectionStatus:" + connectionStatus.name());
				Timer.sleep(2000);	// ある程度の時間ダイアログを表示するため
				return true;
			}

			Timer.sleep(1000);
		}

		Logger.error(context, "モバイルネットワークの設定に失敗しました。ConnectionStatus:" + connectionStatus.name());

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// 切り替えが完了したことを通知
		context.sendBroadcast(new Intent(MOBILE_NETWORK_CONNECTIVITY_CHANGED_ACTION));

		// 状態が変更されたことをバイブレーションで通知
		Vibrator vibrator = ActivityHelper.getSystemService(context, Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] {0, 250, 100, 250}, -1);

		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		// 起動したアクティビティを終了
		if (context instanceof Activity) {
			Activity.class.cast(context).finish();
		}
	}

}
