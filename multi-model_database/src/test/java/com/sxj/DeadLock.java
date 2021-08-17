package com.sxj;

import org.junit.Test;

public class DeadLock {

	@Test
	public void t(){
		Markup m1 = new Markup(1);
		Markup m2 = new Markup(2);
		Thread t1 = new Thread(m1);
		Thread t2 = new Thread(m2);
		t1.start();
		t2.start();
	}
	
class Mirror{
	private int num;
	Mirror(int num){
		this.num= num;
	}
}
class Clpstick{
	private int num;
	Clpstick(int num) {
		// TODO Auto-generated constructor stub
		this.num=num;
	}
}

class Markup extends Thread{
	
	Mirror m = new Mirror(1);
	Clpstick c = new Clpstick(1);
	int flag;
	Markup(int flag){
		this.flag=flag;
	}
	@Override
	public void run() {
		if(flag==1){
			synchronized(m){
				int count=0;
				if(m!=null){
					System.out.println(Thread.currentThread().getName()+"获取到了镜子");
					m=null;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(c!=null){
					System.out.println(Thread.currentThread().getName()+"获取到了口红");
				}
			}
		}else{
			if(c!=null){
				System.out.println(Thread.currentThread().getName()+"获取到了口红");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(m!=null){
				System.out.println(Thread.currentThread().getName()+"获取到了镜子");
				m=null;
			}
		}
	}
}
}
