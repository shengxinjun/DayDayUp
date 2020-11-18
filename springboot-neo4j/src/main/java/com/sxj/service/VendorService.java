package com.sxj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxj.dao.VendorDao;
import com.sxj.entity.Vendor;


@Service
public class VendorService {
	
	@Autowired
	private VendorDao vendorDao;
	public void  saveVendor(Vendor vendor){
    	vendorDao.save(vendor);
    	
    }
	public Vendor findVendor(String name){
		return vendorDao.findVendorByName(name);
	}
	
	public void deleteVendor(String name){
		vendorDao.deleteVendorByName(name);
	}
	
	public Vendor findVendorByProduct(String name){
		return vendorDao.findVendorByProduct(name);
	}
	
	public void updateVendorOnlyForTest(String name){
		vendorDao.updateVendorOnlyForTest(name);
	}
}
