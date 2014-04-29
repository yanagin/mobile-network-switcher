package orz.yanagin.android.mns.controllers.widget;

import orz.yanagin.commons.android.ActivityHelper;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * モバイルネットワークの切り替えを行うウィジェット
 * @author k-yanagihara
 */
public class MobileNetworkSwitchWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		ActivityHelper.startService(
				context,
				MobileNetworkSwitchWidgetService.class);
	}

	@Override
	public void onDisabled(Context context) {
		ActivityHelper.stopService(
				context,
				MobileNetworkSwitchWidgetService.class);
	}

}
