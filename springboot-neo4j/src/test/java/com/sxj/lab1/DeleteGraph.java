/*package com.sxj.lab1;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.sxj.entity.Product;
import com.sxj.entity.Vendor;
import com.sxj.service.ProductService;
import com.sxj.service.RelationService;
import com.sxj.service.VendorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteGraph {

	@Autowired
	private VendorService vendorService;
	
	@Autowired
	private RelationService relationService;
	
	@Autowired
	private ProductService productService;
	@Test
	public void testDeleteGraph() {
		int count = 0;
		long startTime = System.currentTimeMillis();
		deleteRelation(count);
		deleteVendor(count);
		deleteProduct(count);
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

	}
	void deleteProduct(int count){
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				productService.deleteProduct(item[0]);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	void deleteVendor(int count){
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Vendor\\Vendor.csv"));// 换成你的文件名
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				vendorService.deleteVendor(item[0]);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	void deleteRelation(int count){
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\BrandByProduct.csv"));// 换成你的文件名

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				relationService.deleteRelation(item[0], item[1]);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	public static String filterSqlString(String sqlStr) {
		if (StringUtils.isEmpty(sqlStr)) {
			return sqlStr;
		}
		sqlStr = sqlStr.replace("'", "");
		sqlStr = sqlStr.replace("\\", "");
		return sqlStr;
	}
}
*/