package orz.yanagin.android.mns.utils;

import orz.yanagin.commons.Strings;
import android.net.NetworkInfo;

/**
 * ネットワーク種別
 * @author k-yanagihara
 */
public enum NetworkType {

	/** 無効 */
	DISABLE,

	/** モバイルネットワーク */
	MOBILE,

	/** Wi-Fi */
	WIFI,

	/** 不明 */
	UNKNOWN;

	/**
	 * ネットワーク種別を返す
	 * @param networkInfo NetworkInfo
	 * @return ネットワーク種別
	 */
	public static NetworkType create(NetworkInfo networkInfo) {
		if (networkInfo == null || Strings.isEmptyOrNull(networkInfo.getTypeName())) {
			return DISABLE;
		}
		if (networkInfo.getTypeName().toUpperCase().equals("MOBILE")) {
			return MOBILE;
		}
		if (networkInfo.getTypeName().toUpperCase().equals("WIFI")) {
			return WIFI;
		}
		return UNKNOWN;
	}

}
