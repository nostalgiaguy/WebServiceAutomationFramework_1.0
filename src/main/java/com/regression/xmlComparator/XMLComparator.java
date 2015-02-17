package com.regression.xmlComparator;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLComparator {



	/**
	 * compare two expected and generated xmls.
	 * @return 
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean compareXml(String expectedResponseXml,String generatedResponseXml) throws SAXException, IOException {
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		/** list of regular expressions that custom difference listener used during xml comparison. */
		final List<String> ignorableXPathsRegex = new ArrayList<String>();

		
		ignorableXPathsRegex.add("\\/Envelope\\[1\\]\\/Header\\[1\\]\\/MessageID\\[1\\]\\/text()");
		ignorableXPathsRegex.add("\\/Envelope\\[1\\]\\/Header\\[1\\]\\/RelatesTo\\[1\\]\\/text()");
		ignorableXPathsRegex.add("\\/Envelope\\[1\\]\\/Body\\[1\\]\\/OTA_HotelAvailRS\\[1\\]\\/@TimeStamp");
	

		final String expectedXml = expectedResponseXml;                                  
		final String generatedXml = generatedResponseXml;                                     
		Diff diff;
		FileInputStream fileStream1 = null;
		FileInputStream fileStream2 = null;

		try {
			fileStream1 = new FileInputStream(expectedXml);
			fileStream2 = new FileInputStream(generatedXml);
			InputSource inputSource1 = new InputSource(fileStream1);
			InputSource inputSource2 = new InputSource(fileStream2);
			diff = new Diff(inputSource1, inputSource2);
			RegexBasedDifferenceListener ignorableElementsListener = new RegexBasedDifferenceListener(ignorableXPathsRegex);
			/** setting our custom difference listener */
			diff.overrideDifferenceListener(ignorableElementsListener);


			DetailedDiff detDiff = new DetailedDiff(diff);
			@SuppressWarnings("rawtypes")
			List differences = detDiff.getAllDifferences();

			for (Object object : differences) {
				Difference difference = (Difference)object;
				System.out.println(difference);

			}
			if(diff.similar()){
				if (diff.identical()){
					System.out.println("Similar?..........................." + diff.similar());
					System.out.println("Identical?........................." + diff.identical());
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Two xml are not similar");
		return false;
		

	}

}
