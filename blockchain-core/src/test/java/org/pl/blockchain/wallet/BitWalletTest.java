package org.pl.blockchain.wallet;

import java.io.File;
import java.util.concurrent.TimeoutException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bitcoinj.store.BlockStoreException;
import org.junit.After;
import org.junit.Before;
import org.pl.blockchain.core.NetworkType;
import org.pl.blockchain.wallet.bitcoin.BitNetwork;
import org.pl.blockchain.wallet.bitcoin.BitWallet;

/**
 * Unit test for simple App.
 */
public class BitWalletTest 
    extends TestCase
{
	
	private BitNetwork network;
	
	private BitWallet bitWallet;
	@Before
    public void setUp() throws Exception {
		network=new BitNetwork(NetworkType.TEST_NET);
		bitWallet=new BitWallet(network);
		File walletfile=new File("bitcoin.wallet");
		if(walletfile.exists()){
			bitWallet.restoreWalletFile(walletfile);
		}else{
			bitWallet.generateWalletFile(walletfile);
		}
		
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BitWalletTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BitWalletTest.class );
    }


    public void testCreateWalletSync() throws  BlockStoreException, TimeoutException 
    {
    	bitWallet.startWalletSync(bitWallet.createBlockStore());
    }
    
}

