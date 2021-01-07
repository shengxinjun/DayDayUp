package com.lab2;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class Tasks {

	
	
	@Scheduled(cron="0 33 12 * * ?  ")
	void startInterface(){
		ImportDataController im = new ImportDataController();
		try {
			im.importDocument();
		} catch (Exception e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		VisitDataController qu = new VisitDataController();
		try {
			qu.visitDocument();
		} catch (Exception e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		UpdateDataController up = new UpdateDataController();
		try {
			up.updateDocument();
		} catch (Exception e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		DeleteDataController de = new DeleteDataController();
		try {
			de.deleteDocument();
		} catch (Exception e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
	}

}
