package orz.yanagin.android.mns.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ネットワーク状態を管理するクラス
 * @author k-yanagihara
 */
public class NetworkStatus {

	/** ConnectivityManager */
	private final ConnectivityManager connectivityManager;

	/** 有効なネットワーク種別 */
	private NetworkType activeNetwork;

	/** 対応するネットワーク種別 */
	private List<NetworkType> networkTypes = new ArrayList<NetworkType>();

	/** ネットワークの状態が変更した場合のリスナ */
	private OnNetworkStateChangeListener onNetworkStateChangeListener;

	/**
	 * コンストラクタ
	 * @param connectivityManager ConnectivityManager
	 */
	public NetworkStatus(ConnectivityManager connectivityManager) {
		this.connectivityManager = connectivityManager;
		update();
	}

	/**
	 * ネットワーク状態を更新する
	 */
	public void update() {
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		activeNetwork = NetworkType.create(activeNetworkInfo);
		if (activeNetworkInfo != null && onNetworkStateChangeListener != null) {
			onNetworkStateChangeListener.onNetworkStateChange(
					activeNetwork,
					activeNetworkInfo.isConnected());
		}

		NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
		networkTypes.clear();
		if (allNetworkInfo != null) {
			for (NetworkInfo networkInfo : allNetworkInfo) {
				NetworkType networkType = NetworkType.create(networkInfo);
				networkTypes.add(networkType);
			}
		}
		Collections.sort(networkTypes);
	}

	/**
	 * 有効なネットワーク種別を返す
	 * @return 有効なネットワーク種別
	 */
	public NetworkType getActiveNetwork() {
		return activeNetwork;
	}

	/**
	 * ネットワーク種別が対応するか返す
	 * @param networkType ネットワーク種別
	 * @return 対応する場合true
	 */
	public boolean isSupported(NetworkType networkType) {
		return Collections.binarySearch(networkTypes, networkType) >= 0;
	}

	/**
	 * @param onNetworkStateChangeListener
	 */
	public void setOnNetworkStateChangeListener(OnNetworkStateChangeListener onNetworkStateChangeListener) {
		this.onNetworkStateChangeListener = onNetworkStateChangeListener;
	}

	/**
	 * ネットワークの状態が変更した場合のリスナ
	 * @author k-yanagihara
	 */
	public static interface OnNetworkStateChangeListener {

		/**
		 * @param networkType ネットワーク種別
		 * @param isConnected 接続している場合true
		 */
		public void onNetworkStateChange(NetworkType networkType, boolean isConnected);

	}

}
