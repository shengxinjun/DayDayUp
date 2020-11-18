package com.sxj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxj.dao.ProductDao;
import com.sxj.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	public void  saveProduct(Product product){
		productDao.save(product);
	}

	public Product findProduct(String name){
		return productDao.findProductByName(name);
	}
	
	public void deleteProduct(String name){
		Product product = findProduct(name);
		productDao.delete(product);
	}
	
}
