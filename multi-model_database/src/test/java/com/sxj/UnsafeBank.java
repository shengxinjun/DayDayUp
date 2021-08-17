package com.sxj;

import org.junit.Test;

public class UnsafeBank {

	@Test
	public void t() throws InterruptedException {
		Account a1 = new Account(200);
		Drawing drawing = new Drawing(a1,140,0);
		Thread t1 = new Thread(drawing);
		t1.start();
		Drawing drawing2 = new Drawing(a1,140,0);
		Thread t2 = new Thread(drawing2);
		t2.start();
		 Thread.sleep(1000);
	}

	class Drawing implements Runnable {

		private Account account;
		private Integer drawingMoney;
		private Integer nowMoney;

		Drawing(Account account,Integer drawingMoney,Integer nowMoney) {
			
			// TODO Auto-generated method stub
			this.account=account;
			this.drawingMoney=drawingMoney;
			this.nowMoney=nowMoney;
		}
		
		@Override
		public void run() {
			synchronized(account){
				if(account.money<drawingMoney){

					System.out.println("meiqianle");
					return ;
				}
				account.money = account.money - drawingMoney;
				nowMoney = drawingMoney + nowMoney;

				System.out.println("余额：" + account.money + ",手上有：" + nowMoney);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}
	}

	class Account {
		private Integer money;
		Account(Integer mongey){
			this.money=mongey;
		}

	}
}
