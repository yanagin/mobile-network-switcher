package orz.yanagin.android.mns.configurations;

import orz.yanagin.android.mns.controllers.MobileNetworkSwitchActivity;
import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.android.mns.utils.MobileNetworkController;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 連携するアプリケーションを起動するアクティビティ
 * startActivityForResultを利用して、起動したアプリケーションが終了したタイミングでモバイルネットワークの切断を行う
 * @author k-yanagihara
 */
public class ConjunctionApplicationStartActivity extends Activity {

	/** アプリケーションをインテントに保持するときの名称 */
	public static final String APPLICATION = "application";

	/** 連携アプリケーション起動のリザルトコード */
	private static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Application application = Application.class.cast(intent.getExtras().getSerializable(APPLICATION));

		Intent startApplicationIntent = new Intent();
		startApplicationIntent.setClassName(application.getPackageName(), application.getName());
		startActivityForResult(startApplicationIntent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CODE == requestCode) {
			// モバイルネットワークに接続したままの場合は切断を行う
			MobileNetworkController mobileNetworkController = new MobileNetworkController(this);
			ConnectionStatus connectionStatus = mobileNetworkController.getConnectionStatus();
			if (ConnectionStatus.CONNECTED == connectionStatus) {
				ActivityHelper.startActivity(this, MobileNetworkSwitchActivity.class);
			}
			finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
