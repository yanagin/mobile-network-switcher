package orz.yanagin.android.mns.controllers.notification;

import orz.yanagin.android.mns.controllers.MobileNetworkSwitchServiceBase;
import orz.yanagin.android.mns.pro.R;
import android.content.Context;

/**
 * モバイルネットワークの切り替えを行う通知領域をコントロールするクラス
 * @author k-yanagihara
 */
public class MobileNetworkSwitchNotificationService extends MobileNetworkSwitchServiceBase {

	/** モバイルネットワークの切り替えを行う通知領域のボタン */
	private MobileNetworkSwitchNotification mobileNetworkSwitchNotification;

	@Override
	protected void initializeView() {
        // 通知領域へスイッチを登録する
		mobileNetworkSwitchNotification = new MobileNetworkSwitchNotification(this);
		mobileNetworkSwitchNotification.regist();
	}

	@Override
	protected void onConnected(Context context) {
		mobileNetworkSwitchNotification.updateIcon(R.drawable.connect);
	}

	@Override
	protected void onChanging(Context context) {
		mobileNetworkSwitchNotification.updateIcon(R.drawable.changing);
	}

	@Override
	protected void onDisonnected(Context context) {
		mobileNetworkSwitchNotification.updateIcon(R.drawable.disconnect);
	}

}
