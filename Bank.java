package com.mailtux.bank;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

public class Bank extends TestCase {

	private static final int MARIUS_PENGER = 3000;
	private static final int AFFEN_PENGER = 2000;
	private static final int HCN_PENGER = 1000;
	private static final int ANTALL_PARALLELLE = 100;
	private static final int ANTALL_BEVEGELSER = 10000;
	private  Konto hcn;
	private  Konto affen;
	private  Konto marius;

	public static void main(String[] args) {

		Bank tester=new Bank();
		tester.doit();
	}

	public void testit() {
		for (int i = 0; i < 1000; i++) {

			Bank tester=new Bank();
			tester.doit();
			
			assertEquals(6000, tester.getHcn().getSaldo()+tester.getMarius().getSaldo()+tester.getAffen().getSaldo());
		}


	}

	private void doit() {

		hcn = new Konto("hcn", HCN_PENGER);
		affen = new Konto("affen", AFFEN_PENGER);
		marius = new Konto("marius", MARIUS_PENGER);
		
		System.out.println("Sum f¿r vi starter: "
				+ (hcn.getSaldo() + marius.getSaldo() + affen.getSaldo()));
		
		System.out.println("Antall parallelle trŒder: "+ANTALL_PARALLELLE);
		System.out.println("Antall overf¿ringer: "+6*ANTALL_BEVEGELSER);
		

		ExecutorService threadExecutor = Executors.newFixedThreadPool(ANTALL_PARALLELLE);

		fyllJobber(threadExecutor);

		threadExecutor.shutdown();

		try {
			threadExecutor.awaitTermination(1000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		System.out.println("\nFerdig!\n");

		System.out.println("\naffen har " + affen.getSaldo());
		System.out.println("marius har " + marius.getSaldo());
		System.out.println("hcn har " + hcn.getSaldo());
		System.out.println("Tilsammen "
				+ (hcn.getSaldo() + marius.getSaldo() + affen.getSaldo()));

		int antallBevegelser = Uttaker.getAntallBevegelser();
		int antallLock = Uttaker.getAntallLock();

		System.out.println("\nBevegelser: "
				+ antallBevegelser);
		System.out.println("LŒser: " + antallLock);
		System.out.println("Total: " + (antallLock+antallBevegelser));

	}


	private void fyllJobber(ExecutorService threadExecutor) {


		Random rbelop = new Random();

		int ix;
		for (ix=0; ix <ANTALL_BEVEGELSER; ix++) {
			Future<Boolean> f=threadExecutor.submit(new Uttaker(hcn,affen,rbelop.nextInt(1000)));
			f=threadExecutor.submit(new Uttaker(affen,hcn,rbelop.nextInt(1000)));
			f=threadExecutor.submit(new Uttaker(affen,marius,rbelop.nextInt(1000)));
			f=threadExecutor.submit(new Uttaker(marius,affen,rbelop.nextInt(1000)));
			f=threadExecutor.submit(new Uttaker(marius,hcn,rbelop.nextInt(1000)));
			f=threadExecutor.submit(new Uttaker(hcn,marius,rbelop.nextInt(1000)));
		}
	}

	public Konto getHcn() {
		return hcn;
	}

	public Konto getAffen() {
		return affen;
	}

	public Konto getMarius() {
		return marius;
	}
}

	