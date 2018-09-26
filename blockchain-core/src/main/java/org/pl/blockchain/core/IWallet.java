package org.pl.blockchain.core;

import java.io.IOException;
import java.math.BigInteger;


public interface IWallet {
	
	public String getDefaultAccount() throws IOException;
	
	public BigInteger getBalance(String address) throws IOException;
	

}
