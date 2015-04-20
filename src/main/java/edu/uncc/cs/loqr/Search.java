package edu.uncc.cs.loqr;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fj.data.List;
import weka.core.Instance;
import weka.core.Instances;

public class Search {
	private static final Logger log = LogManager.getLogger(Search.class);
	/**
	 * Perform a rigid Query against Instances.
	 * 
	 * TODO
	 * @param query
	 * @param insts
	 * @return
	 */
	public static Instances rigid(Query query, Instances insts) {
		Instances out = new Instances(insts, 16);
		for (int i=0; i < insts.numInstances(); i++) {
			Instance inst = insts.instance(i);
			boolean retain = true;
			for (Conjunct conj : query.conjuncts) {
				retain &= conj.op.func.apply(inst.value(conj.attr), (double) conj.value);
			}
			if (retain) out.add(inst);
		}
		return out;
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
	public static Instances relaxed(Query query, Instances insts) {

		// Generate rules from the database
		List<Rule> rules = Associations.associate(query, insts);
		
		/*
		 * Here you can expect to use the rule similarity metric:
		 * some_rule.similarity(other_rule);
		 */
		return rigid(query, insts);
	}
}
