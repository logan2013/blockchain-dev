package org.pl.blockchain.wallet;


public class WalletEntity {

	private String privateKey;
	private String publicKey;
	private String address;
	
	public WalletEntity(String privateKey,String publicKey,String address){
		this.privateKey=privateKey;
		this.publicKey=publicKey;
		this.address=address;
	}
	
	
	public String getPrivateKey() {
		return privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public String getAddress() {
		return address=address;
	}
}
