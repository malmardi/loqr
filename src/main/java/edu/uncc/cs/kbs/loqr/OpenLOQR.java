package edu.uncc.cs.kbs.loqr;

import fj.data.List;
import weka.core.Instances;

/**
 * Run relaxed queries against an ARFF dataset.
 * The ARFF file is specified on the command line.
 *
 * For example:
 * gradle run -Ptarget=OpenLOQR diabetes.arff
 */
public class OpenLOQR {
	public static void main(String[] args) {
		// Validate user input
		
		// Open, load and discretize the database
		Instances insts = null;
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
