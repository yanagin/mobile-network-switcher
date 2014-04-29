package orz.yanagin.android.mns.controllers;

import java.util.List;

import orz.yanagin.android.mns.configurations.Application;
import orz.yanagin.android.mns.configurations.Configurations;
import orz.yanagin.android.mns.configurations.ConjunctionApplicationStartActivity;
import orz.yanagin.android.mns.pro.R;
import orz.yanagin.android.mns.utils.ConnectionStatus;
import orz.yanagin.android.mns.utils.MobileNetworkController;
import orz.yanagin.commons.Timer;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * モバイルネットワークの切り替えを行うアクティビティ
 * ProgressDialogを利用するため、サービスではなくアクティビティを用いる
 * @author k-yanagihara
 */
public class MobileNetworkSwitchActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();

		MobileNetworkController mobileNetworkController = new MobileNetworkController(this);
		ConnectionStatus connectionStatus = mobileNetworkController.getConnectionStatus();

		if (ConnectionStatus.CONNECTED == connectionStatus) {
			disconnect();
		} else {
			connect();
		}
	}

	/**
	 * モバイルネットワークへの接続を行う
	 */
	void connect() {
		// 連携するアプリケーションを確認
		final List<Application> applications = Configurations.getConjunctionApplications(this);
		if (applications == null || applications.isEmpty()) {
			changeConnectionStatis(ConnectionStatus.CONNECTED);
			return;
		}

		// 「連携しない」選択肢
		ComponentName componentName = getComponentName();
		applications.add(0, new Application(getString(R.string.application_none), componentName.getPackageName(), componentName.getClassName()));

		new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("Start Application")
			.setAdapter(
				new ConnectApplicationAdapter(this, applications),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						changeConnectionStatis(ConnectionStatus.CONNECTED);

						if (which == 0) {
							return;
						}

						final int selected = which;
						new Thread(new Runnable() {
							@Override
							public void run() {
								Timer.sleep(1500);	// スグにダイアログが消えないように少し待つ

								Intent startApplicationIntent = new Intent(MobileNetworkSwitchActivity.this, ConjunctionApplicationStartActivity.class);
								startApplicationIntent.putExtra(ConjunctionApplicationStartActivity.APPLICATION, applications.get(selected));
								startActivity(startApplicationIntent);
							}
						}).start();
					}
				}
			)
			.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			})
			.show();
	}

	/**
	 * モバイルネットワークの切断を行う
	 */
	void disconnect() {
		changeConnectionStatis(ConnectionStatus.DISCONNECTED);
	}

	void changeConnectionStatis(ConnectionStatus connectionStatus) {
		// 接続状態の確認
		MobileNetworkController mobileNetworkController = new MobileNetworkController(this);
		if (mobileNetworkController.getConnectionStatus() == connectionStatus) {
			return;
		}

		// 接続状態の変更開始を通知
		sendBroadcast(new Intent(MobileNetworkSwitchAsyncTask.MOBILE_NETWORK_CONNECTIVITY_CHANGING_ACTION));

		// 状態を切り替える
		if (ConnectionStatus.CONNECTED == connectionStatus) {
			mobileNetworkController.connect();
		} else {
			mobileNetworkController.disconnect();
		}

		// モバイルネットワークを切り替えるタスクを呼び出す
		new MobileNetworkSwitchAsyncTask(
				this,
				mobileNetworkController,
				connectionStatus).execute();
	}

	/**
	 * 連携するアプリケーションを選択するアダプタ
	 * @author k-yanagihara
	 */
	static class ConnectApplicationAdapter extends BaseAdapter {

		private final List<Application> applications;

		private final LayoutInflater layoutInflater;

		private final PackageManager packageManager;

		public ConnectApplicationAdapter(Context context, List<Application> applications) {
			this.applications = applications;
			layoutInflater = ActivityHelper.getSystemService(context, Context.LAYOUT_INFLATER_SERVICE);
			packageManager = context.getPackageManager();
		}

		@Override
		public int getCount() {
			return applications.size();
		}

		@Override
		public Object getItem(int i) {
			return applications.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null) {
				view = layoutInflater.inflate(R.layout.connect_application, null);
			}

			Application application = applications.get(i);
			ImageView.class.cast(view.findViewById(R.id.connect_application_icon)).setImageDrawable(application.loadIcon(packageManager));
			TextView.class.cast(view.findViewById(R.id.connect_application_name)).setText(application.getTitle());

			return view;
		}

	}

}
