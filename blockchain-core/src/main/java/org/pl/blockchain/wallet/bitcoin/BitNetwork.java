package org.pl.blockchain.wallet.bitcoin;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.pl.blockchain.core.INetwork;
import org.pl.blockchain.core.NetworkType;

public class BitNetwork implements INetwork {

	protected NetworkType nt;
	
	public BitNetwork(){
		
	}

	public BitNetwork(NetworkType nt){
		this.nt=nt;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(NetworkType nt) {
		// TODO Auto-generated method stub
		NetworkParameters  params=null;
		switch(nt){
			case TEST_NET:
				params= TestNet3Params.get();
				break;
			case REG_TEST_NET:
				params= RegTestParams.get();
				break;
			default:
				params= MainNetParams.get();
				break;
		}
		return (T) params;
	}

	@Override
	public <T> T get() {
		// TODO Auto-generated method stub
		return this.get(nt);
	}
}
