package org.pl.blockchain.wallet.bitcoin;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Utils;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.pl.blockchain.core.INetwork;
import org.pl.blockchain.wallet.MnemonicWalletEntity;
import org.pl.blockchain.wallet.WalletEntity;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.MoreExecutors;

public class BitWallet extends BitRPC {

	private Wallet wallet;
	
	public BitWallet(INetwork getNetwork){
		super(getNetwork);
	}
	
	public void transaction(String address,String value) throws BlockStoreException, InsufficientMoneyException{
		Address to=Address.fromBase58(net, address);
		Coin coin = Coin.parseCoin(value);
		SendRequest req = SendRequest.to(to, coin);
//		req.feePerKb = Coin.parseCoin("0.0005");
		BlockChain blockChain = new BlockChain(net, wallet, createBlockStore());
		PeerGroup peerGroup = new PeerGroup(net, blockChain);
		Wallet.SendResult sendResult = wallet.sendCoins(peerGroup, req);
		sendResult.broadcastComplete.addListener(new Runnable() {
			  @Override
			  public void run() {
			      System.out.println("Coins Sent! Transaction hash is " + sendResult.tx.getHashAsString());
			  }
			}, MoreExecutors.sameThreadExecutor());
//		Transaction createdTx = sendResult.tx;
	}
	
	public  WalletEntity generateWallet() {
		ECKey key=new ECKey();
		Address address=key.toAddress(net);
//		String privateKey = key.getPrivKey().toString();
		String privateKey=key.getPrivateKeyEncoded(net).toBase58();
		String publicKey =Utils.HEX.encode(key.getPubKeyHash());
		String account = address.toString();
		return new WalletEntity(privateKey,publicKey,account);
	}
	

	public  WalletEntity restoreWallet(String privateKey){
		DumpedPrivateKey dp=DumpedPrivateKey.fromBase58(net, privateKey);
		//ECKey key=ECKey.fromPrivate(new BigInteger(privateKey));
		ECKey key=dp.getKey();
//		privateKey =key.getPrivKey().toString();
		privateKey=key.getPrivateKeyEncoded(net).toBase58();
		String publicKey=Utils.HEX.encode(key.getPubKeyHash());
		Address address=key.toAddress(net);
		String account=address.toString();
		return new WalletEntity(privateKey,publicKey,account);
	}
	
	public  WalletEntity generateWalletFile(File walletFile) throws IOException {
		wallet = new Wallet(net);
		wallet.saveToFile(walletFile);
		return getWalletFile();
	}
	
	public  WalletEntity restoreWalletFile(File walletFile) throws UnreadableWalletException {
		this.wallet=Wallet.loadFromFile(walletFile);
		return getWalletFile();
	}
	
	public MnemonicWalletEntity generateMnemonicWallet(String mnemonic,String passphrase, long creationTimeSeconds) throws UnreadableWalletException{
		DeterministicSeed seed = new DeterministicSeed(mnemonic, null, passphrase, creationTimeSeconds);
		wallet=Wallet.fromSeed(net, seed);
		return getWalletFile(seed,passphrase);
	}
	
	public String getAddress(){
		ECKey key=new ECKey();
		Address address=key.toAddress(net);
		return address.toString();
	}
	
	public long getBalance(){
		return wallet.getBalance().value;
	}
	
	private WalletEntity getWalletFile(){
		Address currentReceiveAddress = wallet.currentReceiveAddress();
		ECKey currentReceiveKey = wallet.currentReceiveKey();
		String privateKey = currentReceiveKey.getPrivateKeyEncoded(net).toBase58();
		String publicKey = Utils.HEX.encode(currentReceiveKey.getPubKeyHash());
		String account = currentReceiveAddress.toString();
		return new WalletEntity(privateKey,publicKey,account);
	}
	
	private MnemonicWalletEntity getWalletFile(DeterministicSeed seed,String passphrase){
		Address currentReceiveAddress = wallet.currentReceiveAddress();
		ECKey currentReceiveKey = wallet.currentReceiveKey();
		String privateKey = currentReceiveKey.getPrivateKeyEncoded(net).toBase58();
		String publicKey = Utils.HEX.encode(currentReceiveKey.getPubKeyHash());
		String account = currentReceiveAddress.toString();
		String mnemonic=Joiner.on(" ").join(seed.getMnemonicCode());
		long createTime=seed.getCreationTimeSeconds();
		return new MnemonicWalletEntity(privateKey,publicKey,account,passphrase,mnemonic,createTime);
	}
	/**
	 * 创建钱包同步
	 * @throws BlockStoreException
	 * @throws TimeoutException 
	 * @throws UnknownHostException 
	 */
	public void startWalletSync(BlockStore blockstore) throws BlockStoreException, TimeoutException{
		BlockChain block=new BlockChain(net, blockstore);
		PeerGroup peerGroup = new PeerGroup(net, block);
//		Peer peer = new Peer(net, block, new PeerAddress(InetAddress.getLocalHost(),18333), "Fangsea.io", "1.0");
//		PeerGroup peerGroup=PeerGroup.newWithTor(net, new BlockChain(net, blockstore), new TorClient());
		peerGroup.setUserAgent("Fangsea.io", "1.0");
		peerGroup.addWallet(wallet);
		peerGroup.addPeerDiscovery(new DnsDiscovery(net));
		peerGroup.startAsync();
		peerGroup.downloadBlockChain();
	}
	
	public BlockStore createBlockStore() throws BlockStoreException{
		return new SPVBlockStore(net, new File("./data/bitcoin/blockstore.spv"));
	}
}
