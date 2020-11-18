package com.sxj.lab1;

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
import com.sxj.service.VendorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateGraph {

	@Autowired
	private VendorService vendorService;
	@Test
	public void testUpdateGraph() {
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader("D:\\初次读研请多指教\\数据集\\Unibench_Multi-model_data\\Product\\Product.csv"));// 换成你的文件名

			int count = 0;

			long startTime = System.currentTimeMillis();
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = filterSqlString(line);
				String item[] = line.split(",");// CSV格式文件为逗号分隔符文件，这里根据逗号切分
				vendorService.updateVendorOnlyForTest(item[0]);
				if (++count % 100 == 0)
					System.out.println("正在执行第" + count + "条数据！");
			}

			long endTime = System.currentTimeMillis();
			System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
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
