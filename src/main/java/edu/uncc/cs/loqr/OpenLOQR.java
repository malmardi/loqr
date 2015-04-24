package edu.uncc.cs.loqr;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fj.data.List;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

/**
 * Run relaxed queries against an ARFF dataset.
 * The ARFF file is specified on the command line.
 *
 * For example:
 * gradle run -Ptarget=OpenLOQR diabetes.arff
 */
public class OpenLOQR {
	private static final Logger log = LogManager.getLogger(OpenLOQR.class);
	public static void main(String[] args) throws Exception {
		String filename;
		// Validate user input
		if (args.length == 0) {
			log.info("No .arff specified. Using diabetes.arff.");
			filename = "diabetes.arff";
		} else {
			filename = args[0];
		}
		
		// Open, load the database
		Instances insts = DataSource.read(filename);
		insts.setClassIndex(insts.attribute("class").index());
		
		// get query from user
		Scanner in = new Scanner(System.in);
		System.out.println("Enter your query (e.g. [preg<=4]^[plas<=20]): ");
		String query = in.next();
		in.close();
		Query q = Query.parse(query, insts);
		
		// execute query against the database, relax if needed
		Instances results = Search.execute(q, insts);
		
		// Tell the user the results
		if(results.numInstances()>0) {
			System.out.println(results);
		}
	}
}
