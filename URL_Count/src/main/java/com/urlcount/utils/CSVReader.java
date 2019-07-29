package com.urlcount.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.urlcount.object.CSSSelector;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CSVReader {

	public static ArrayList<CSSSelector> readCSVFiles(String fileName) throws Exception {
		ArrayList<CSSSelector> urlList = new ArrayList<CSSSelector>();
		try (Reader reader = Files.newBufferedReader(Paths.get(fileName));

				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader());) {

			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				String url = csvRecord.get("URL");
				String home=csvRecord.get(0);
				String tagFound = csvRecord.get(2);
				String tagNotFound = csvRecord.get(3);

				System.out.println("Record No - " + csvRecord.getRecordNumber());
				System.out.println("---------------");
				System.out.println("url : " + url);
				System.out.println("tags : " + tagFound);
				System.out.println("tags2 : " + tagNotFound);

				CSSSelector cSSSelector = new CSSSelector(url,home, tagFound, tagNotFound, "");
				urlList.add(cSSSelector);

				System.out.println("---------------\n\n");
			}
		}

		return urlList;

	}
}
