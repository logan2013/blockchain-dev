package org.pl.blockchain.wallet;


public class MnemonicWalletEntity extends WalletEntity{
	
	private String pass;
	
	private long createTime;
	
	private String mnemonic;
	
	public MnemonicWalletEntity(String privateKey,String publicKey,String account,String pass,String mnemonic,long createTime){
		super(privateKey,publicKey,account);
		this.pass=pass;
		this.mnemonic=mnemonic;
		this.createTime=createTime;
	}

	public String getPass() {
		return pass;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public long getCreateTime() {
		return createTime;
	}

	
}
