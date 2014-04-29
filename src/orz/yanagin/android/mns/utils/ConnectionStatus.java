package orz.yanagin.android.mns.utils;

/**
 * ネットワーク接続状態
 * @author k-yanagihara
 */
public enum ConnectionStatus {

	/** 接続 */
	CONNECTED,

	/** 未接続 */
	DISCONNECTED;

	/**
	 * ネットワーク接続状態を返す
	 * @param isConnected 接続している場合true
	 * @return ネットワーク接続状態
	 */
	public static ConnectionStatus create(boolean isConnected) {
		return isConnected ? CONNECTED : DISCONNECTED;
	}

}
