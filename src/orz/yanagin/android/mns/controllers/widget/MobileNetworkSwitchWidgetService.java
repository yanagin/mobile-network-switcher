package orz.yanagin.android.mns.controllers.widget;

import orz.yanagin.android.mns.controllers.MobileNetworkSwitchActivity;
import orz.yanagin.android.mns.controllers.MobileNetworkSwitchServiceBase;
import orz.yanagin.android.mns.pro.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * モバイルネットワークの切り替えを行うウィジェットをコントロールするクラス
 * @author k-yanagihara
 */
public class MobileNetworkSwitchWidgetService extends MobileNetworkSwitchServiceBase {

	private RemoteViews mobileNetworkSwitchWidget;

	private AppWidgetManager appWidgetManager;

	@Override
	protected void initializeView() {
		// モバイルネットワーク切り替えアクティビティを起動するインテント
		PendingIntent pendingIntent = PendingIntent.getActivity(
				this,
				0,
				new Intent(this, MobileNetworkSwitchActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		// ウィジェットに紐付ける
		mobileNetworkSwitchWidget = new RemoteViews(getPackageName(), R.layout.appwidget);
		mobileNetworkSwitchWidget.setOnClickPendingIntent(R.id.mobileNetworkStatusImage, pendingIntent);

        // ウィジェット更新
    	ComponentName thisWidget = new ComponentName(this, MobileNetworkSwitchWidget.class);
        appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(thisWidget,mobileNetworkSwitchWidget);
	}

	@Override
	protected void onConnected(Context context) {
		updateIcon(context, R.drawable.connect);

	}

	@Override
	protected void onChanging(Context context) {
		updateIcon(context, R.drawable.changing);
	}

	@Override
	protected void onDisonnected(Context context) {
		updateIcon(context, R.drawable.disconnect);
	}

	void updateIcon(Context context, int icon) {
		mobileNetworkSwitchWidget.setImageViewResource(
				R.id.mobileNetworkStatusImage,
				icon);

		appWidgetManager.updateAppWidget(
				new ComponentName(context, MobileNetworkSwitchWidget.class),
				mobileNetworkSwitchWidget);
	}

}
