package edu.uncc.cs.loqr;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fj.data.List;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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
		// Validate user input
		if (args.length == 0) {
			log.fatal("Need a database (an .arff file) to query.");
			System.exit(1);
		}
		
		// Open, load and discretize the database
		Instances insts = DataSource.read(args[0]);
		// Generate rules from the database
		List<Rule> rules = Associations.associate(insts);
		
		// Prompt the user and get a query definition
		Query q = new Query(List.nil());
		// Execute a relaxed query against the database
		Instances results = Search.relaxed(q, insts, rules);
		
		// Tell the user the results
		System.out.println(results);
	}
}
