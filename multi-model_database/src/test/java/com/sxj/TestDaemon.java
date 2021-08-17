package com.sxj;

import org.junit.Test;

public class TestDaemon {

	class God implements Runnable {
		@Override
		public void run() {
			while(true){
				System.out.println("God bless you");
			}
		}
	}

	class You implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println("you are alive");
			}
			System.out.println("you are dead");
		}
	
	}

	@Test
	public void t() throws InterruptedException {

		You you = new You();
		God god = new God();

		Thread t1 = new Thread(you);
		Thread t2 = new Thread(god);
		t2.setDaemon(true);
		t2.start();
		t1.start();
		System.out.println("the main thread is ending ");
		Thread.sleep(10);
	}
}
