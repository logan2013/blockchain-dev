package org.pl.blockchain.wallet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.pl.blockchain.core.NetworkType;
import org.pl.blockchain.wallet.eth.EthNetwork;
import org.pl.blockchain.wallet.eth.EthWallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * Unit test for simple App.
 */
public class EthWalletTest 
    extends TestCase
{
	private static String PASSWORD="123456";
	
	private static File tempDir;
	
	private EthWallet wallet;
	
	
	@Before
    public void setUp() throws Exception {
		wallet=new EthWallet(new EthNetwork(NetworkType.REG_TEST_NET).get());
		tempDir = createTempDir();
    }

    @After
    public void tearDown() throws Exception {
		 for (File file:tempDir.listFiles()) {
		     file.delete();
		 }
		 tempDir.delete();
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EthWalletTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EthWalletTest.class );
    }

    private static File createTempDir() throws Exception {
        return Files.createTempDirectory(
        		EthWalletTest.class.getSimpleName() + "-testkeys").toFile();
    }

    public void testGenerateWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException
    {
    	WalletEntity walletEntity =wallet.generateWallet();
		assertNotNull(walletEntity);
    }
    

    public void testRestoreWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException{
    	WalletEntity walletEntity1 =wallet.generateWallet();
    	WalletEntity walletEntity2 =wallet.restoreWallet(walletEntity1.getPrivateKey());
    	assertEquals(walletEntity2.getPrivateKey(), walletEntity1.getPrivateKey());
    }
    
    public void testGenerateWalletFile() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException{
    	WalletEntity walletEntity=wallet.generateWalletFile(PASSWORD, tempDir);
    	assertNotNull(walletEntity);
    }
    
    public void testGenerateWalletFileByPK() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException{
    	WalletEntity walletEntity1 =wallet.generateWallet();
    	WalletEntity walletEntity2=wallet.generateWalletFile(PASSWORD, tempDir,walletEntity1.getPrivateKey());
    	assertEquals(walletEntity2.getPrivateKey(), walletEntity1.getPrivateKey());
    }
    
    public void testRestoreWalletFile() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException{
    	KeystoreWalletEntity walletEntity1= wallet.generateWalletFile(PASSWORD, tempDir);
    	File keystore=new File(tempDir.getPath()+"\\"+walletEntity1.getKeystore());
    	WalletEntity walletEntity2=wallet.restoreWalletFile(PASSWORD, keystore);
    	assertEquals(walletEntity2.getPrivateKey(), walletEntity1.getPrivateKey());
    }
    
    
    public void testGenerateMnemonicWallet() throws CipherException, IOException{
    	MnemonicWalletEntity walletMnemonicEntity = wallet.generateMnemonicWallet(PASSWORD);
    	assertNotNull(walletMnemonicEntity);
    }
    
    public void testRestoreMnemonicWallet() throws CipherException, IOException{
    	MnemonicWalletEntity walletMnemonicEntity1 = wallet.generateMnemonicWallet(PASSWORD);
    	MnemonicWalletEntity walletMnemonicEntity2 =wallet.restoreMnemonicWallet(PASSWORD, walletMnemonicEntity1.getMnemonic());
    	assertEquals(walletMnemonicEntity2.getMnemonic(), walletMnemonicEntity1.getMnemonic());
    }
    
    public void testCreateCredentials() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException{
    	WalletEntity walletEntity =wallet.generateWallet();
    	Credentials credentials= wallet.createCredentials(walletEntity.getPrivateKey());
    	assertNotNull(credentials);
    }
    public void testGetBalance() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException{
    	WalletEntity walletEntity=wallet.generateWalletFile(PASSWORD, tempDir);
    	String address=walletEntity.getAddress();
    	BigInteger balance1=wallet.getBalance(address);
    	BigInteger balance2=wallet.getBalance(address,DefaultBlockParameterName.LATEST);
    	BigDecimal balance3=wallet.getBalance(address,Unit.ETHER);
    	BigDecimal balance4=wallet.getBalance(address,DefaultBlockParameterName.LATEST,Unit.ETHER);
    	assertNotNull(balance1);
    	assertEquals(balance1, balance2);
    	assertNotNull(balance3);
    	assertEquals(balance3, balance4);
    }
    
    public void testGetDefaultAccount() throws IOException {
    	String address=wallet.getDefaultAccount();
    	assertNotNull(address);
    }
    
    public void testTransaction() throws InterruptedException, TransactionException, Exception {
		String baseUrl="D:\\development\\workspace\\blockchain\\node\\data\\keystore";
		File tokeystore =new File(baseUrl+"\\UTC--2018-07-09T10-42-19.803000000Z--b7477e898b7db4e0482d1a40cb0e5b66a127a963.json");
		File fromkeystore =new File(baseUrl+"\\UTC--2018-07-09T08-04-31.301136200Z--2d6aa421bf681f0d61d62d1a2a62efe729ea1a74");
		WalletEntity fromwalletEntity=wallet.restoreWalletFile("pupudaye", fromkeystore);
		KeystoreWalletEntity towalletEntity=null;
		if(tokeystore.exists()){
			towalletEntity=wallet.restoreWalletFile("123456",tokeystore);
		}else{
			tokeystore =new File(baseUrl);
			towalletEntity=wallet.generateWalletFile("123456",tempDir);
		}
		String to=towalletEntity.getAddress();
		BigInteger frombalance=wallet.getBalance(fromwalletEntity.getAddress());
		BigInteger oldbalance=wallet.getBalance(to);
		System.out.println(to);
		BigDecimal value = Convert.toWei("10", Convert.Unit.ETHER); //wei
		System.out.println("from:"+fromwalletEntity.getAddress()+" send ether("+value+") to:"+to);
		String txhash=wallet.transaction(fromwalletEntity.getPrivateKey(),to,value);
		System.out.println(txhash);
		BigInteger newbalance=wallet.getBalance(to);
		System.out.println("frombalance:"+frombalance+",tooldbalance:"+oldbalance+",tonewbalance:"+newbalance);
		assertEquals(newbalance, oldbalance.add(value.toBigInteger()));
    }
    
}
