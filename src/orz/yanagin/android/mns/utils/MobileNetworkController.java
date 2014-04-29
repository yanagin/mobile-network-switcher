package orz.yanagin.android.mns.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import orz.yanagin.commons.android.ActivityHelper;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * モバイルネットワークを操作するクラス
 * @author k-yanagihara
 */
public class MobileNetworkController {

	/** ConnectivityManager */
	private final ConnectivityManager connectivityManager;

	/**
	 * コンストラクタ
	 * @param context Context
	 */
	public MobileNetworkController(Context context) {
		connectivityManager = ActivityHelper.getSystemService(context, Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 接続する
	 */
	public void connect() {
		if (isMobileDataEnabled()) {
			return;
		}

		setMobileDataEnabled(true);
	}

	/**
	 * 切断する
	 */
	public void disconnect() {
		if (!isMobileDataEnabled()) {
			return;
		}

		setMobileDataEnabled(false);
	}

	/**
	 * ネットワーク接続状態を返す
	 * @return ネットワーク接続状態
	 */
	public ConnectionStatus getConnectionStatus() {
		return isMobileDataEnabled() ? ConnectionStatus.CONNECTED : ConnectionStatus.DISCONNECTED;
	}

	void setMobileDataEnabled(boolean enabled) {
	    try {
			Object connectivityManagerStub = getConnectivityManagerStub();
			Class<?> connectivityManagerStubClass = getConnectivityManagerStubClass(connectivityManagerStub);

			Method setMobileDataEnabledMethod = connectivityManagerStubClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(connectivityManagerStub, enabled);
		} catch (Exception e) {
			throw new MobileNetworkException("モバイルネットワークの切り替えでエラーが発生しました。enabled:" + enabled, e);
		}
	}

	boolean isMobileDataEnabled() {
	    try {
			Object connectivityManagerStub = getConnectivityManagerStub();
			Class<?> connectivityManagerStubClass = getConnectivityManagerStubClass(connectivityManagerStub);

			Method getMobileDataEnabledMethod = connectivityManagerStubClass.getDeclaredMethod("getMobileDataEnabled");
			getMobileDataEnabledMethod.setAccessible(true);
			Object result = getMobileDataEnabledMethod.invoke(connectivityManagerStub);
			return Boolean.TRUE.equals(result);
		} catch (Exception e) {
			throw new MobileNetworkException("モバイルネットワークの状態確認でエラーが発生しました。", e);
		}
	}

	Object getConnectivityManagerStub() throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
		Field serviceField = connectivityManagerClass.getDeclaredField("mService");
		serviceField.setAccessible(true);
		return serviceField.get(connectivityManager);
	}

	Class<?> getConnectivityManagerStubClass(Object connectivityManagerStub) throws ClassNotFoundException {
		return Class.forName(connectivityManagerStub.getClass().getName());
	}

	/**
	 * モバイルネットワークの操作時の例外
	 * @author k-yanagihara
	 */
	public static class MobileNetworkException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		/**
		 * @param detailMessage 詳細メッセージ
		 * @param throwable Throwable
		 */
		public MobileNetworkException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

	}

}
