package orz.yanagin.android.mns.configurations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 設定情報
 * @author k-yanagihara
 */
public class Configurations {

	/** SharedPreferencesの名前 */
	private static final String NAME = ".MobileNetworkSwitcherPro";

	/**
	 * @param context Context
	 * @return SharedPreferences
	 */
	static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(context.getPackageName() + NAME, Context.MODE_WORLD_WRITEABLE);
	}

	/**
	 * @param context Context
	 * @param key キー
	 * @param value 保存する値
	 */
	public static void set(Context context, String key, String value) {
		getSharedPreferences(context).edit()
			.putString(key, value)
			.commit();
	}

	/**
	 * @param context Context
	 * @param key キー
	 * @return 取得した値
	 */
	public static String get(Context context, String key) {
		return getSharedPreferences(context).getString(key, null);
	}

	/**
	 * @param context Context
	 * @param application 連携を行うアプリケーション
	 */
	public static void addConjunctionApplication(Context context, Application application) {
		removeConjunctionApplication(context, application);
		String value =get(context, "application");
		if (value == null) {
			value = "";
		}
		value += application + ",";
		set(context, "application", value);
	}

	/**
	 * @param context Context
	 * @param application 連携を解除するアプリケーション
	 */
	public static void removeConjunctionApplication(Context context, Application application) {
		String value =get(context, "application");
		if (value == null) {
			value = "";
		}
		value = value.replace(application + ",", "");
		set(context, "application", value);
	}

	/**
	 * @param context Context
	 * @return 連携アプリケーションのリスト
	 */
	public static List<Application> getConjunctionApplications(Context context) {
		List<Application> result = new ArrayList<Application>();
		String value =get(context, "application");
		if (value == null) {
			return result;
		}
		String[] array = value.split(",");
		Application application = null;
		for (String str : array) {
			application = Application.fromString(str);
			if (application != null) {
				result.add(application);
			}
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * @param context Context
	 * @param application アプリケーション
	 * @return 連携アプリケーションの場合true
	 */
	public static boolean isConjunctionApplication(Context context, Application application) {
		return Collections.binarySearch(
				getConjunctionApplications(context),
				application) >= 0;
	}

}
