package com.sxj;

import java.util.concurrent.Callable;

public class MyDog implements Callable<MyDog>{
	//狗的名字
	private String name;
	//狗爱吃的食物
	private String food;
	
	MyDog(){
	}

	@Override
	public MyDog call() throws Exception {
		Thread.currentThread().sleep(1000);
		this.name="zhoup";
		this.food="pizza";
		return this;
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

	@Override
	public String toString() {
		return "MyDog [name=" + name + ", food=" + food + "]";
	}

	
}
