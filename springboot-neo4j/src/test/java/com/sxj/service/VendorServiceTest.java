/*package com.sxj.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sxj.entity.Vendor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VendorServiceTest {
	@Autowired
	private VendorService vendorService;

	@Test
	public void saveVendor() {

		Vendor vendor = new Vendor();
		vendor.setCountry("USA");
		vendor.setIndustry("sport");
		vendor.setName("Nike");
		vendorService.saveVendor(vendor);
	}
	@Test
	public void findVendor() {

		Vendor vendor = vendorService.findVendor("Nike");
		System.out.println(vendor.toString());
	}
	@Test
	public void updateVendor() {

		Vendor vendor = new Vendor();
		vendor = vendorService.findVendor("Nike");
		vendor.setCountry("C H I N A");
		vendorService.saveVendor(vendor);
		System.out.println(vendor.toString());
	}
	@Test
	public void deleteVendor() {

		vendorService.deleteVendor("Nike");
	}
}
*/