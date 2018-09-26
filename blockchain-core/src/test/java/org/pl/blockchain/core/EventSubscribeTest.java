package org.pl.blockchain.core;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.pl.blockchain.EthAbstractTest;
import org.pl.blockchain.sol.Voting;
import org.pl.blockchain.sol.Voting.VoteEventResponse;
import org.pl.blockchain.wallet.WalletEntity;
import org.pl.blockchain.wallet.eth.EthEventSubscribe;
import org.pl.blockchain.wallet.eth.EthNetwork;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

import rx.Observable;

/**
 * Unit test for simple App.
 */
public class EventSubscribeTest 
    extends EthAbstractTest
{
	
	private EthEventSubscribe eventSubscribe;
	
	private WalletEntity fromwalletEntity;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EventSubscribeTest( String testName )
    {
        super( testName );
    }
    
	@Before
    public void setUp() {
		super.setUp();
		try {
			eventSubscribe=new EthEventSubscribe(new EthNetwork(NetworkType.REG_TEST_NET).get());
			File fromkeystore =new File(baseUrl+"\\UTC--2018-07-09T08-04-31.301136200Z--2d6aa421bf681f0d61d62d1a2a62efe729ea1a74");
			fromwalletEntity=wallet.restoreWalletFile(passphrase, fromkeystore);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CipherException e) {
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
        return new TestSuite( EventSubscribeTest.class );
    }


    public void testBlockObservable() throws Exception  
    {
    	eventSubscribe.blockObservable(block->{
    		  System.out.println("hash: " + block.getHash());
    		  System.out.println("number: " + block.getNumber());
    	});
    }
    
    public void testTransactionObservable(){
    	eventSubscribe.transactionObservable(tx->{
    		  System.out.println("hash: " + tx.getHash());
			  System.out.println("from: " + tx.getFrom());
			  System.out.println("to: " + tx.getTo());
    	});
    }
    
    public void testPendingTransactionObservable(){
    	eventSubscribe.pendingTransactionObservable(tx->{
    		  System.out.println("hash: " + tx.getHash());
    	});
    }
    
    public void testContractEvent(){
    	ClientTransactionManager ctm = new ClientTransactionManager(wallet.getWeb3j(),fromwalletEntity.getAddress());
		String contractAddress="0x70ad210de533900d23bc174034b07533257fb1d5";
		Voting voting=Voting.load(contractAddress, web3j, ctm, Contract.GAS_PRICE,Contract.GAS_LIMIT);
		if(voting!=null){
			Observable<VoteEventResponse> observable=voting.voteEventObservable(new EthFilter());
			eventSubscribe.contractEvent(observable, event->{
				 System.out.println(event.voter + " : " + new String(event.candidate));
			});
		}
    }
}

