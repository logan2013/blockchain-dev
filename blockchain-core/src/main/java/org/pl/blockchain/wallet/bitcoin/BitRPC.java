package org.pl.blockchain.wallet.bitcoin;

import org.bitcoinj.core.NetworkParameters;
import org.pl.blockchain.core.IJsonRpc;
import org.pl.blockchain.core.INetwork;

public class BitRPC implements IJsonRpc {
	
	protected NetworkParameters net;
	
	public BitRPC(INetwork getNetwork){
		net=getNetwork.get();
	}

	@Override
	public String clientVersion() {
		// TODO Auto-generated method stub
		return null;
	}
}
