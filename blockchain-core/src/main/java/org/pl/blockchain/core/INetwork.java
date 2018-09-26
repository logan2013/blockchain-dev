package org.pl.blockchain.core;


public interface INetwork {

	public <T> T get();

	public <T> T get(NetworkType nt);
}
