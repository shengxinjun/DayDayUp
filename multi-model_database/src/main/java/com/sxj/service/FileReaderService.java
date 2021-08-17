package com.sxj.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;
@Service
public class FileReaderService {
	public BufferedReader readerLocalOrderJson(){
		File file = new File("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Order\\Order.json");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerOrderJson(){
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Order/Order.json");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerLocalProductCSV(){
		File file = new File("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerProductCSV(){
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Product/Product.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerLocalPersonCSV(){
		File file = new File("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Customer\\person_0_0.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerPersonCSV(){
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/Customer/person_0_0.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerLocalRelationCSV(){
		File file = new File("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\SocialNetwork\\person_knows_person_0_0.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
	public BufferedReader readerRelationCSV(){
		File file = new File("/home/shengxinjun/Unibench_Multi-model_data/SocialNetwork/person_knows_person_0_0.csv");
		FileInputStream fis;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "GB2312");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BufferedReader(isr);
	}
}
