package com.sxj.lab2;

import java.io.FileNotFoundException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class Tasks {
	
	
	@Scheduled(cron="0 30 19 * * ?  ")
	void startInterface(){
		ImportController im = new ImportController();
		try {
			im.doImport();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QueryController qu = new QueryController();
		try {
			qu.doQuery();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UpdateController up = new UpdateController();
		try {
			up.doUpdate();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeleteController de = new DeleteController();
		try {
			de.doDelete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
