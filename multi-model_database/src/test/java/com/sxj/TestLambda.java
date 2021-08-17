package com.sxj;

public class TestLambda {
	
	public static void main(String args[]) {
		
		// 6.lambda表达式
		ILike like = ()-> {
				System.out.println("i like lambda");
		};
		like.lambda();
	}
}

// 1.定义一个函数式接口
interface ILike {
	void lambda();
}
