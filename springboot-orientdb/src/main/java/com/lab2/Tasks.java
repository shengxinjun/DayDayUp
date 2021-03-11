package com.lab2;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Tasks {
	@Scheduled(cron="0 36 9 * * ?  ")
	void startInterface(){
		ImportController im = new ImportController();
		try {
			//im.importRelationData();
		} catch (Exception e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		QueryController qu = new QueryController();
		try {
			//qu.queryRelationData();
		} catch (Exception e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		UpdateController up = new UpdateController();
		try {
			//up.updateRelationData();
		} catch (Exception e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		DeleteController de = new DeleteController();
		try {
			//de.deleteRelationData();
		} catch (Exception e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
		try {
			//im.importDocumentData();
		} catch (Exception e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		try {
			//qu.queryDocumentData();
		} catch (Exception e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		try {
			//up.updateDocumentData();
		} catch (Exception e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		try {
			//de.deleteDocumentData();
		} catch (Exception e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
		try {
			im.importGraphData();
		} catch (Exception e) {
			System.out.println("插入时出错");
			e.printStackTrace();
		}
		try {
			qu.queryGraphData();
		} catch (Exception e) {
			System.out.println("查询时出错");
			e.printStackTrace();
		}
		try {
			up.updateGraphData();
		} catch (Exception e) {
			System.out.println("更新时出错");
			e.printStackTrace();
		}
		try {
			de.deleteGraphData();
		} catch (Exception e) {
			System.out.println("删除时出错");
			e.printStackTrace();
		}
	}


}
