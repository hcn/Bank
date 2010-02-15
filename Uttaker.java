package com.mailtux.bank;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Uttaker implements Callable<Boolean> {

	static AtomicInteger antallBevegelser = new AtomicInteger(0);
	static AtomicInteger antallLock = new AtomicInteger(0);

	Konto fra;
	Konto til;
	private int belop;

	public Uttaker(Konto fra, Konto til,int belop) {
		this.fra = fra;
		this.til = til;
		this.belop=belop;
	}

	public Boolean call() {
		boolean suksess = fra.overfoer(this.belop, til);
		if (suksess)
			antallBevegelser.incrementAndGet();
		else
			antallLock.incrementAndGet();
		
		return suksess;
	}

	public static int getAntallBevegelser() {
		return antallBevegelser.intValue();
	}

	public static int getAntallLock() {
		return antallLock.intValue();
	}

}
