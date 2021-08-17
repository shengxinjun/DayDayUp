package com.sxj;

import org.junit.Test;

public class TestState {
	
	@Test
	public void main() throws InterruptedException{
		Thread thread =new Thread(()->{
			for(int i=0;i<5;i++){
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("/////");
		});
		
		Thread.State state = thread.getState();
		System.out.println(state);
		
		thread.start();
		state = thread.getState();
		System.out.println(state);
		
		while(state!=Thread.State.TERMINATED){
			Thread.sleep(100);
			state = thread.getState();
			System.out.println(state);
		}
		
	}
}
