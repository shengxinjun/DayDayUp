package com.sxj.lab2;

import java.io.FileNotFoundException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class Tasks {
	
	
	@Scheduled(cron="0 0 20 * * ?  ")
	void startInterface(){
		ImportController im = new ImportController();
		try {
			im.doImport();
		} catch (FileNotFoundException e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		QueryController qu = new QueryController();
		try {
			qu.doQuery();
		} catch (FileNotFoundException e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		UpdateController up = new UpdateController();
		try {
			up.doUpdate();
		} catch (FileNotFoundException e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		DeleteController de = new DeleteController();
		try {
			de.doDelete();
		} catch (FileNotFoundException e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
	}
}
