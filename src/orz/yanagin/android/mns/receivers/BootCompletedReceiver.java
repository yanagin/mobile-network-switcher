package orz.yanagin.android.mns.receivers;

import orz.yanagin.android.mns.controllers.notification.MobileNetworkSwitchNotificationService;
import orz.yanagin.commons.android.ActivityHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * システム起動時に呼ばれるBroadcastReceiver
 * @author k-yanagihara
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        // 通知領域へ登録
        ActivityHelper.startService(context, MobileNetworkSwitchNotificationService.class);
	}

}
