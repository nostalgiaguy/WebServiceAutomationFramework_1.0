package com.regression.test;


import java.io.IOException;

import org.junit.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.regression.ServiceRequest.ARIShopRequest;
import com.regression.xmlComparator.XMLComparator;

public class DummyTest {
	
	ARIShopRequest ar = new ARIShopRequest();
	XMLComparator  xc = new XMLComparator();

	@Test
	public void dummy_test_1(){


		try {
			System.out.println("Send AriShop Request");
			ar.getAriShopResponse();
			boolean actual=xc.compareXml(ar.actualResponsePath, ar.expectedResponsePath);
			Assert.assertEquals(actual, true);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
