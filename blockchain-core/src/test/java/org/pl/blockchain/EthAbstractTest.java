package org.pl.blockchain;

import junit.framework.TestCase;

import org.junit.Before;
import org.pl.blockchain.core.NetworkType;
import org.pl.blockchain.wallet.eth.EthNetwork;
import org.pl.blockchain.wallet.eth.EthWallet;
import org.web3j.protocol.Web3j;

/**
 * Unit test for simple App.
 */
public class EthAbstractTest 
    extends TestCase
{
	protected String baseUrl="D:\\development\\workspace\\blockchain\\node\\data\\keystore";;
	
	protected String passphrase="pupudaye";
	
	protected EthWallet wallet;
	
	protected Web3j web3j;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EthAbstractTest( String testName )
    {
        super( testName );
    }
    
	@Before
    public void setUp() {
		wallet=new EthWallet(new EthNetwork(NetworkType.REG_TEST_NET).get());
		web3j=wallet.getWeb3j();
    }
}
