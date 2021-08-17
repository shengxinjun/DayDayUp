package com.sxj;

import org.junit.Test;

public class TestPriority extends Thread{
	@Test
	public void t(){
		TestPriority t1 = new TestPriority();
		TestPriority t2 = new TestPriority();
		TestPriority t3 = new TestPriority();
		TestPriority t4 = new TestPriority();

		Thread th1=new Thread(t1,"t1");
		th1.setPriority(1);
		Thread th2=new Thread(t1,"t2");
		th2.setPriority(2);
		Thread th3=new Thread(t1,"t3");
		th3.setPriority(8);
		Thread th4=new Thread(t1,"t4");
		th4.setPriority(10);

		th1.start();
		th2.start();
		th3.start();
		th4.start();
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+"klkl;kl;kl");
	}
	
}
