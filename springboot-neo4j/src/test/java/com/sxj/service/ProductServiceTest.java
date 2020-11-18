package com.sxj.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sxj.entity.Product;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	
	@Autowired
	private ProductService productService;
	@Test
	public void testSaveProduct() {
		Product product = new Product();
		product.setTitle("this is a product");
		product.setName("T-shirts");
		product.setPrice("1.1");
		product.setImgUrl("www.baidu.com");
		productService.saveProduct(product);
	}

	@Test
	public void testFindProduct() {
		Product pro = productService.findProduct("T-shirts");
		System.out.println(pro.toString());
	}
	@Test
	public void testUpdateProduct() {
		Product pro = productService.findProduct("T-shirts");
		pro.setTitle("jack");
		productService.saveProduct(pro);
	}

	@Test
	public void testDeleteProduct() {
		productService.deleteProduct("T-shirts");
	}

}
