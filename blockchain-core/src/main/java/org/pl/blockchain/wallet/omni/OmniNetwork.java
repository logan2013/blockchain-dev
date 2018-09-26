package org.pl.blockchain.wallet.omni;

import org.pl.blockchain.core.NetworkType;
import org.pl.blockchain.wallet.bitcoin.BitNetwork;

import foundation.omni.net.OmniMainNetParams;
import foundation.omni.net.OmniNetworkParameters;
import foundation.omni.net.OmniRegTestParams;
import foundation.omni.net.OmniTestNetParams;

public class OmniNetwork extends BitNetwork {

	public OmniNetwork() {

	}

	public OmniNetwork(NetworkType nt) {
		this.nt = nt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(NetworkType nt) {
		// TODO Auto-generated method stub
		OmniNetworkParameters params = null;
		switch (nt) {
		case TEST_NET:
			params = OmniTestNetParams.get();
			break;
		case REG_TEST_NET:
			params = OmniRegTestParams.get();
			break;
		default:
			params = OmniMainNetParams.get();
			break;
		}
		return (T) params;
	}
}
