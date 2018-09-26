package org.pl.blockchain.wallet.bitcoin;

import org.bitcoinj.store.BlockStore;



public class BitBlockStoreFactory {

	public final static String SPV_BLOCK_STORE="";
	
	public BlockStore get(String type){
		BlockStore blockStore=null;
		switch (type) {
		case SPV_BLOCK_STORE:
			
			break;
		default:
			break;
		}
		return blockStore;
	}
}
