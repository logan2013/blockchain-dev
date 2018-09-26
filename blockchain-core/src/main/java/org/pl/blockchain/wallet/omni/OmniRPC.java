package org.pl.blockchain.wallet.omni;

import org.pl.blockchain.core.INetwork;
import org.pl.blockchain.wallet.bitcoin.BitWallet;

import foundation.omni.net.OmniNetworkParameters;

public class OmniRPC extends BitWallet {

	protected OmniNetworkParameters omninet;
	
	public OmniRPC(INetwork getNetwork){
		super(getNetwork);
		this.omninet=getNetwork.get();
	}
	
	@Override
	public String clientVersion() {
		// TODO Auto-generated method stub
		return null;
	}

}
