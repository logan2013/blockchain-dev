package org.pl.blockchain.wallet.eth;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.pl.blockchain.core.IJsonRpc;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class EthRPC implements IJsonRpc {
	
	private HttpService httpservice;
	
	protected Web3j web3j;
	
	protected Admin admin;
	
	public EthRPC(HttpService httpservice){
		this.httpservice=httpservice;
		this.web3j=Web3j.build(httpservice);
		admin=Admin.build(httpservice);
	}

	@Override
	public String clientVersion() {
		// TODO Auto-generated method stub
		try {
			Request<?, Web3ClientVersion> request = web3j.web3ClientVersion();
			Web3ClientVersion response = request.send();
			return response.getWeb3ClientVersion();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean unlockAccount(String address,String passphrase) throws InterruptedException, ExecutionException{
		PersonalUnlockAccount personalUnlockAccount =admin.personalUnlockAccount(address, passphrase).sendAsync().get();
		return personalUnlockAccount.accountUnlocked();
	}

	public Web3j getWeb3j() {
		return web3j;
	}
	
	
}
