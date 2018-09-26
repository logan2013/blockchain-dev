package org.pl.blockchain.wallet.eth;

import java.util.function.Consumer;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;

import rx.Observable;

public class EthEventSubscribe extends EthRPC {

	public EthEventSubscribe(HttpService httpservice) {
		super(httpservice);
		// TODO Auto-generated constructor stub
		web3j = Web3j.build(httpservice,1000,Async.defaultExecutorService());
	}

	/**
	 * 新区快产生事件
	 * @param consumer
	 */
	public void blockObservable(Consumer<EthBlock.Block> consumer) {
		// TODO Auto-generated method stub
		web3j.blockObservable(true).subscribe(ethBlock -> {
		  EthBlock.Block block = ethBlock.getBlock();
		  consumer.accept(block);
		});
	}
	
	/**
	 * 新交易产生事件
	 * @param consumer
	 */
	public void transactionObservable(Consumer<Transaction> consumer){
		// TODO Auto-generated method stub
	   web3j.transactionObservable().subscribe(tx -> {
		   consumer.accept(tx);
		});
	}	
	
	/**
	 * 监听待定交易事件
	 * @param consumer
	 */
	public void pendingTransactionObservable(Consumer<Transaction> consumer){
		web3j.pendingTransactionObservable().subscribe(tx -> {
		   consumer.accept(tx);
		});
	}

	public <T> void  contractEvent(Observable<T> observable,Consumer<T> consumer){
		observable.subscribe(event->{
			consumer.accept(event);
		});
	}
}
