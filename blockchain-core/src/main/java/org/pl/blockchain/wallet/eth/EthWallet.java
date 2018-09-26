package org.pl.blockchain.wallet.eth;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.pl.blockchain.core.IWallet;
import org.pl.blockchain.wallet.KeystoreWalletEntity;
import org.pl.blockchain.wallet.MnemonicWalletEntity;
import org.pl.blockchain.wallet.WalletEntity;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;


public class EthWallet extends EthRPC implements IWallet {

	private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
	
	public EthWallet(HttpService httpservice){
		super(httpservice);
	}

	    

	public  WalletEntity generateWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		return get(keyPair);
	}
	

	public  WalletEntity restoreWallet(String privateKey){
		BigInteger pk = new BigInteger(privateKey,16);
		ECKeyPair keyPair = ECKeyPair.create(pk);
		return get(keyPair);
	}
	


	public  KeystoreWalletEntity generateWalletFile(String pass,File keystoreRoot) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException {
		ECKeyPair keyPair = Keys.createEcKeyPair();
		String keystore = WalletUtils.generateWalletFile(pass,keyPair,keystoreRoot,true);
		return get(keyPair,pass,keystore);
	}


	public  KeystoreWalletEntity generateWalletFile(String pass,File keystoreRoot,String privateKey) throws CipherException, IOException{
		BigInteger pk = new BigInteger(privateKey,16);
		ECKeyPair keyPair = ECKeyPair.create(pk);
		String keystore = WalletUtils.generateWalletFile(pass,keyPair,keystoreRoot,true);
		return get(keyPair,pass,keystore);
	}
	

	public  KeystoreWalletEntity restoreWalletFile(String pass,File keystore) throws IOException, CipherException{
		Credentials credentials= WalletUtils.loadCredentials(pass, keystore);
		ECKeyPair eCKeyPair=credentials.getEcKeyPair();
		return get(eCKeyPair,pass,keystore.getName());
	}

	public  MnemonicWalletEntity generateMnemonicWallet(String pass) throws CipherException, IOException{
		 byte[] initialEntropy = new byte[16];
	     secureRandom.nextBytes(initialEntropy);
		 String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
	     byte[] seed = MnemonicUtils.generateSeed(mnemonic, pass);
	     ECKeyPair keyPair = ECKeyPair.create(Hash.sha256(seed));
	     return get(keyPair,pass,mnemonic,0);
	}
	

	public  MnemonicWalletEntity restoreMnemonicWallet(String pass,String mnemonic) throws CipherException, IOException{
		Credentials credentials = WalletUtils.loadBip39Credentials(pass,mnemonic);
		ECKeyPair keyPair=credentials.getEcKeyPair();
		return get(keyPair,pass,mnemonic,0);
	}


	public  Credentials createCredentials(String privateKey){
		Credentials credentials=Credentials.create(privateKey);
		return credentials;
	}
	
	private  WalletEntity get(ECKeyPair keyPair){
		String privateKey = keyPair.getPrivateKey().toString(16);
		String publicKey = keyPair.getPublicKey().toString(16);
		String account = Keys.getAddress(keyPair);
		return new WalletEntity(privateKey,publicKey,"0x"+account);
	}
	
	private  KeystoreWalletEntity get(ECKeyPair keyPair,String pass,String keystore){
		String privateKey = keyPair.getPrivateKey().toString(16);
		String publicKey = keyPair.getPublicKey().toString(16);
		String account = Keys.getAddress(keyPair);
		return new KeystoreWalletEntity(privateKey,publicKey,"0x"+account,pass,keystore);
	}

	private MnemonicWalletEntity get(ECKeyPair keyPair,String pass,String mnemonic,long createTime){
		String privateKey = keyPair.getPrivateKey().toString(16);
		String publicKey = keyPair.getPublicKey().toString(16);
		String account = Keys.getAddress(keyPair);
		return new MnemonicWalletEntity(privateKey,publicKey,"0x"+account,pass,mnemonic,createTime); 
	}
	@Override
	public BigInteger getBalance(String address) throws IOException {
		// TODO Auto-generated method stub
		return getBalance(address,DefaultBlockParameterName.LATEST);
	}
	
	public BigInteger getBalance(String address, DefaultBlockParameterName block) throws IOException {
		// TODO Auto-generated method stub
		BigInteger balance = web3j.ethGetBalance(address, block).send().getBalance();
		return balance;
	}


	public BigDecimal getBalance(String address, Unit unit) throws IOException {
		// TODO Auto-generated method stub
		BigInteger balance=this.getBalance(address);
		if(balance!=null){
			BigDecimal oneEther = Convert.toWei(balance.toString(),unit);
			return oneEther;
		}
		return new BigDecimal(0);
	}


	public BigDecimal getBalance(String address,
			DefaultBlockParameterName block, Unit unit) throws IOException {
		// TODO Auto-generated method stub
		BigInteger balance=this.getBalance(address,block);
		if(balance!=null){
			BigDecimal oneEther = Convert.toWei(balance.toString(),unit);
			return oneEther;
		}
		return new BigDecimal(0);
	}

	public String transaction(String privateKey,String to, BigDecimal value) throws InterruptedException, IOException, TransactionException, Exception{
		// TODO Auto-generated method stub
		Credentials credentials= this.createCredentials(privateKey);
		TransactionReceipt receipt=Transfer.sendFunds(web3j, credentials, to, value, Unit.WEI).send();
		return  receipt.getTransactionHash();
	}

	

	@Override
	public String getDefaultAccount() throws IOException {
		// TODO Auto-generated method stub
		List<String> accounts=web3j.ethAccounts().send().getAccounts();
		if(accounts!=null&&accounts.size()>0){
			return accounts.get(0);
		}else{
			return null;
		}
	}
}
