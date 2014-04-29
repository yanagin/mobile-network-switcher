package orz.yanagin.android.mns.receivers;

import orz.yanagin.android.mns.utils.ConnectionStatus;

/**
 * モバイルネットワークの切断要求時に呼ばれるBroadcastReceiver
 * @author k-yanagihara
 */
public class MobileNetworkDisonnectRequestReceiver extends MobileNetworkSwitchRequestReceiverBase {

	@Override
	protected ConnectionStatus getConnectionStatus() {
		return ConnectionStatus.DISCONNECTED;
	}

}
