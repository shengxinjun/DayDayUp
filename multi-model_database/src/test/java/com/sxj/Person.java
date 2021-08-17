package com.sxj;
public interface Person {
    //交作业
    void giveTask();
public    class Test implements Person{
    	
    	@Override
    	public void giveTask() {
    		System.out.println("");
    	}
    	public static void main(String[] args){
    		new Test().giveTask();
    	}
    }
}