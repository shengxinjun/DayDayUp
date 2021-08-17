package com.sxj;

import org.junit.Test;

public class TestPC {

	@Test
	public void t(){

		Container c =  new Container();
		new Producer(c).start();;
		new Consumer(c).start();;
	}

}

class Chicken {

}

class Container {
	Chicken[] chickens = new Chicken[10];

	int count = 0;

	public synchronized void push(Chicken c) {
		if (count == chickens.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		chickens[count++] = c;
		this.notify();
	}

	public synchronized int pop() {
		if (count == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 count--;
		this.notify();
		return count+1;
	}
}
	class Producer extends Thread {
		Container container;

		public Producer(Container container) {
			this.container = container;
		}

		@Override
		public void run() {
			Chicken c = new Chicken();
			for (int i = 0; i < 100; i++) {
				container.push(c);
				System.out.println("生产了第" + i + "只鸡");
			}

		}
	}

class Consumer extends Thread{

	Container container;
	
	public Consumer(Container container){
		this.container=container;
	}
	
	@Override
	public void run() {
		for(int i=0;i<100;i++){
			System.out.println("消费了第"+container.pop()+"只鸡");
		}
	}
}