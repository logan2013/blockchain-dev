package org.pl.blockchain.wallet;


public class KeystoreWalletEntity extends WalletEntity{
	
	private String pass;
	
	private String keystore;
	
	
	public KeystoreWalletEntity(String privateKey,String publicKey,String account,String pass,String keystore){
		super(privateKey,publicKey,account);
		this.pass=pass;
		this.keystore=keystore;
	}

	public String getPass() {
		return pass;
	}

	public String getKeystore() {
		return keystore;
	}
}
