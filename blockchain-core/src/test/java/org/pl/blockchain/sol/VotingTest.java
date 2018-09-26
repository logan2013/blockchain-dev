package org.pl.blockchain.sol;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.pl.blockchain.EthAbstractTest;
import org.pl.blockchain.util.StringByte;
import org.pl.blockchain.wallet.WalletEntity;
import org.web3j.crypto.CipherException;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

/**
 * Unit test for simple App.
 */
public class VotingTest 
    extends EthAbstractTest
{
	
	private WalletEntity fromwalletEntity;
	
	private boolean unlocked=false;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public VotingTest( String testName )
    {
        super( testName );
    }
    
	@Before
    public void setUp() {
		super.setUp();
		try {
			File fromkeystore =new File(baseUrl+"\\UTC--2018-07-09T08-04-31.301136200Z--2d6aa421bf681f0d61d62d1a2a62efe729ea1a74");
			fromwalletEntity=wallet.restoreWalletFile(passphrase, fromkeystore);
			unlocked=wallet.unlockAccount(fromwalletEntity.getAddress(), passphrase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CipherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( VotingTest.class );
    }


    public void testDeploy() throws Exception  
    {
		
		ClientTransactionManager ctm = new ClientTransactionManager(web3j,fromwalletEntity.getAddress());
		String contractAddress="0x70ad210de533900d23bc174034b07533257fb1d5";
		Voting voting=Voting.load(contractAddress, web3j, ctm, Contract.GAS_PRICE,Contract.GAS_LIMIT);
		if(voting==null&&unlocked){
	    	//准备参数：候选人名单
	    	List<byte[]> candidates = new ArrayList<byte[]>();
	    	candidates.add(StringByte.stringToBytes32("Tommy"));
	    	candidates.add(StringByte.stringToBytes32("Jerry"));
	    	candidates.add(StringByte.stringToBytes32("Micky"));
	    	//部署合约
	    	voting = Voting.deploy(web3j,ctm,Contract.GAS_PRICE,Contract.GAS_LIMIT,candidates).send();
	    	//获取合约的部署地址
	    	contractAddress = voting.getContractAddress();
	    	System.out.println("contractAddress:"+contractAddress);
		}
		assertNotNull(voting);
    }
    
    public void testVoting() throws Exception{
    	ClientTransactionManager ctm = new ClientTransactionManager(wallet.getWeb3j(),fromwalletEntity.getAddress());
		String contractAddress="0x70ad210de533900d23bc174034b07533257fb1d5";
		Voting voting=Voting.load(contractAddress, web3j, ctm, Contract.GAS_PRICE,Contract.GAS_LIMIT);
		if(voting!=null){
			byte[] bytes=StringByte.stringToBytes32("Tommy");
			voting.voteFor(bytes).send();
			BigInteger vote=voting.getVotesFor(bytes).send();
			System.out.println("vote:"+vote);
			assertNotNull(vote);
		}
		assertNotNull(voting);
    }
}

