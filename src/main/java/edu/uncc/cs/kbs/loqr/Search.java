package edu.uncc.cs.kbs.loqr;


import fj.data.List;
import weka.core.FastVector;
import weka.core.Instances;

public class Search {
	/**
	 * Perform a rigid Query against Instances.
	 * 
	 * TODO
	 * @param query
	 * @param insts
	 * @return
	 */
	public static Instances rigid(Query query, Instances insts) {
		return new Instances("Example", new FastVector(), 0);
	}
	
	/**
	 * Perform a relaxed Query, possibly based on rigid Queries.
	 * 
	 * TODO
	 * @param query
	 * @param insts
	 * @param rules
	 * @return
	 */
	public static Instances relaxed(Query query, Instances insts, List<Rule> rules) {
		
		/*
		 * Here you can expect to use the rule similarity metric:
		 * some_rule.similarity(other_rule);
		 */
		return rigid(query, insts);
	}
}
