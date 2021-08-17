package com.sxj;

public class MyCat implements Runnable{
	//名字
	private String name;
	//食物
	private String food;
	
	MyCat(String name,String food){
		this.name= name;
		this.food=food;
	}
	public MyCat() {
		
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		name="zhoup";
		food="pizza";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	
	
	
}
