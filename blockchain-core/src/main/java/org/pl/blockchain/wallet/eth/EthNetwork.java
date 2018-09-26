package org.pl.blockchain.wallet.eth;

import org.pl.blockchain.core.INetwork;
import org.pl.blockchain.core.NetworkType;
import org.web3j.protocol.http.HttpService;

public class EthNetwork implements INetwork {

	private NetworkType nt;
	
	
	public EthNetwork(){
		
	}

	public EthNetwork(NetworkType nt){
		this.nt=nt;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(NetworkType nt) {
		// TODO Auto-generated method stub
		String net_url=getNetUrl(nt);
		return (T) new HttpService(net_url);
	}

	@Override
	public <T> T get() {
		// TODO Auto-generated method stub
		return this.get(nt);
	}

	/**
	 * 读取配置文件获取不同的网络配置
	 * @param nt
	 * @return
	 */
	private String getNetUrl(NetworkType nt){
		String net_url=null;
		switch(nt){
			case TEST_NET:
				net_url="http://192.168.91.76:8545";
				break;
			case REG_TEST_NET:
				net_url="http://127.0.0.1:8545";
				break;
			default:
				net_url="http://192.168.91.76:8545";
				break;
		}
		return net_url;
	}
	
}
