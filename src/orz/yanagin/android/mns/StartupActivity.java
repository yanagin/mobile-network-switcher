package orz.yanagin.android.mns;

import orz.yanagin.android.mns.configurations.ConfigurationActivity;
import orz.yanagin.android.mns.controllers.notification.MobileNetworkSwitchNotificationService;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.Activity;
import android.os.Bundle;

/**
 * 起動時に実行するアクティビティ
 * @author k-yanagihara
 */
public class StartupActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 通知領域へ登録
        ActivityHelper.startService(this, MobileNetworkSwitchNotificationService.class);

        ActivityHelper.startActivity(this, ConfigurationActivity.class);

        // 表示させないように自身を終了する
        finish();
	}

}
