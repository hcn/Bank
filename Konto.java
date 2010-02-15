package com.mailtux.bank;

import java.util.concurrent.locks.ReentrantLock;

public class Konto {

	private final ReentrantLock lock = new ReentrantLock();

	private volatile int saldo;

	public Konto(String navn, int belop) {
		this.saldo = belop;
	}

	public boolean overfoer(int belop, Konto mottaker) {
		boolean fraLocked = this.lock.tryLock();
		boolean mottakerLocked = mottaker.getLock().tryLock();
		
		try {
			if (fraLocked && mottakerLocked) {
					setSaldo(getSaldo() - belop);
					mottaker.setSaldo(mottaker.getSaldo() + belop);
					
				return true;
			} else {
				return false;
			}
			
		} finally {
			if (fraLocked)
				this.lock.unlock();
			if (mottakerLocked)
				mottaker.getLock().unlock();
		}
	}

	public int getSaldo() {
		return saldo;
	}

	private void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	public ReentrantLock getLock() {
		return lock;
	}
}

