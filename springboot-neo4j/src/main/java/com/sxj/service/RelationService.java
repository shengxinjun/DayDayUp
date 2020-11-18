package com.sxj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxj.dao.RelationDao;


@Service
public class RelationService {

	
	@Autowired
	private RelationDao relationDao;
	
	public void saveRelation(String vname,String pname){
		
		relationDao.saveRelation(vname,pname);
	}
	
	public void deleteRelation(String vname,String pname){
		relationDao.deleteRelation(vname,pname);
	}
}
