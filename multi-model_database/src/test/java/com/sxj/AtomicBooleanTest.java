package com.sxj;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanTest implements Runnable{
	
	private static AtomicBoolean flag = new AtomicBoolean(true);
	int count=0;
	public static void main(String args[]){
		AtomicBooleanTest abt = new AtomicBooleanTest();
		
		Thread t1 = new Thread(abt);
		t1.start();
		Thread t2 = new Thread(abt);
		t2.start();
	}
	@Override
	public void run() {
		if(flag.compareAndSet(true, false)){
			System.out.println(Thread.currentThread().getName()+"进来了");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+"要结束了");
			flag.set(true);
		}else{
			try {
				System.out.println(Thread.currentThread().getName()+"在等待");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			run();
		}
	}
}