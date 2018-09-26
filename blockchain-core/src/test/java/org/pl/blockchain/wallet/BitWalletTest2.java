package org.pl.blockchain.wallet;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bitcoinj.wallet.UnreadableWalletException;
import org.junit.After;
import org.junit.Before;
import org.pl.blockchain.core.NetworkType;
import org.pl.blockchain.wallet.bitcoin.BitNetwork;
import org.pl.blockchain.wallet.bitcoin.BitWallet;

/**
 * Unit test for simple App.
 */
public class BitWalletTest2 
    extends TestCase
{
	
	private BitNetwork network;
	
	private BitWallet bitWallet;
	
	@Before
    public void setUp() throws Exception {
		network=new BitNetwork(NetworkType.TEST_NET);
		bitWallet=new BitWallet(network);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BitWalletTest2( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BitWalletTest2.class );
    }


    
    public void testGenerateWallet(){
    	WalletEntity walletEntity=bitWallet.generateWallet();
    	assertNotNull(walletEntity);
    }
    
    public void testRestoreWallet(){
    	WalletEntity walletEntity1=bitWallet.generateWallet();
    	WalletEntity walletEntity2=bitWallet.restoreWallet(walletEntity1.getPrivateKey());
    	assertEquals(walletEntity1.getAddress(), walletEntity2.getAddress());
    }
    public void testGenerateWalletFile() throws IOException{
    	File walletfile=new File("bitcoin.wallet");
    	WalletEntity walletEntity=bitWallet.generateWalletFile(walletfile);
		walletfile.delete();
    	assertNotNull(walletEntity);
    }
    public void testRestoreWalletFile() throws IOException, UnreadableWalletException{
    	File walletfile=new File("bitcoin.wallet");
    	WalletEntity walletEntity1=bitWallet.generateWalletFile(walletfile);
    	WalletEntity walletEntity2=bitWallet.restoreWalletFile(walletfile);
    	walletfile.delete();
    	assertEquals(walletEntity1.getAddress(), walletEntity2.getAddress());
    }
    
	public void testGetBalance() throws UnreadableWalletException, IOException{
		File walletfile=new File("bitcoin2.wallet");
		WalletEntity walletEntity=null;
		if(walletfile.exists()){
			walletEntity=bitWallet.restoreWalletFile(walletfile);
		}else{
			walletEntity=bitWallet.generateWalletFile(walletfile);
		}
		Long balance=bitWallet.getBalance();
		System.out.println("account:"+walletEntity.getAddress()+",balance:"+balance);
		assertNotNull(balance);
	}
    
	public void testGenerateMnemonicWallet() throws UnreadableWalletException{
		MnemonicWalletEntity walletEntity1=bitWallet.generateMnemonicWallet("a b c", "123456", System.currentTimeMillis());
		MnemonicWalletEntity walletEntity2=bitWallet.generateMnemonicWallet("a b c", "123456", System.currentTimeMillis());
		assertEquals(walletEntity1.getAddress(),walletEntity2.getAddress());
	}
	
//	System.out.println("privateKey1:"+walletEntity1.getPrivateKey());
//	System.out.println("publicKey1:"+walletEntity1.getPublicKey());
//	System.out.println("account1:"+walletEntity1.getAddress());
//	System.out.println("================================================");
//	System.out.println("privateKey2:"+walletEntity2.getPrivateKey());
//	System.out.println("publicKey2:"+walletEntity2.getPublicKey());
//	System.out.println("account2:"+walletEntity2.getAddress());


}

