package com.sxj;
import java.util.*;

import org.junit.Test;
public class Main{
	class Student{
		private String name;
		Student(String name){
			this.name=name;
		}
		@Override
		public String toString() {
			return "Student [name=" + name + "]";
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
		
	}
	@Test
    public void a(){
		Student s = new Student("xiaoliu");
		Object o = s;
		s = (Student)o;
		try {
			System.out.println(s.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}