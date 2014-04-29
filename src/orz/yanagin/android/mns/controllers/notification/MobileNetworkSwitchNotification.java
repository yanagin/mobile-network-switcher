package orz.yanagin.android.mns.controllers.notification;

import orz.yanagin.android.mns.controllers.MobileNetworkSwitchActivity;
import orz.yanagin.android.mns.pro.R;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * モバイルネットワークの切り替えを行う通知領域のボタン
 * @author k-yanagihara
 */
public class MobileNetworkSwitchNotification {

	/** 登録時のID */
	private static final int NOTIFICATION_ID = 1;

	/** Context */
	private final Context context;

	/**  NotificationManager */
	private final NotificationManager notificationManager;

	/** Notification */
	private Notification notification;

	/**
	 * コンストラクタ
	 * @param context Context
	 */
	public MobileNetworkSwitchNotification(Context context) {
		this.context = context;
		notificationManager = ActivityHelper.getSystemService(context, Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 通知領域への登録を行う
	 */
	public void regist() {
		// モバイルネットワーク切り替えアクティビティを起動するインテント
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context,
				0,
				new Intent(context, MobileNetworkSwitchActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		// 「進捗」へスイッチを登録
		notification = new Notification(
				R.drawable.ic_launcher,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;	// 動作フラグを書き換える(常駐)
		notification.setLatestEventInfo(
				context,
				context.getString(R.string.app_name),
				"Mobile Network ON/OFF",
				pendingIntent);
		notification.icon = R.drawable.changing;

		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 通知領域から削除する
	 */
	public void remove() {
		notificationManager.cancelAll();
	}

	/**
	 * 通知領域のアイコンを更新する
	 * @param icon アイコン
	 */
	public void updateIcon(int icon) {
		if (notification.icon == icon) {
			return;
		}
		notification.icon = icon;
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

}

