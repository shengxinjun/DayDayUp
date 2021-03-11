package com.sxj.lab2;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class Tasks {
	
	
	@Scheduled(cron="0 40 9 * * ?  ")
	void startInterface(){
		ImportController im = new ImportController();
		try {
			im.doImport();
		} catch (Exception e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		QueryController qu = new QueryController();
		try {
			qu.doQuery();
		} catch (Exception e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		UpdateController up = new UpdateController();
		try {
			up.doUpdate();
		} catch (Exception e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		DeleteController de = new DeleteController();
		try {
			de.doDelete();
		} catch (Exception e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
	}
}
