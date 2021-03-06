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
	public static Instances execute(Query Q, Instances insts) {

		// check if the query fails
		Instances results = rigid(Q, insts);
		if(results.numInstances()==0) {
			// generate rules from the database
			List<Rule> rules = Associations.associate(Q, insts);
			if(rules.length()>0) {
				List<Query> Qs = List.nil();
				for(Rule r : rules) {
					Qs = Qs.cons(new Query(r.antecedents));
				}
				
				// get nearest query to original query
				Query Qr = Query.nearest(Q, Qs, insts);
				System.out.println("Nearest rule: "+Qr.conjuncts);
				
				// Relax
				Query relaxed = Query.relax(Q, Qr);
				System.out.println("Relaxed query: "+relaxed.conjuncts);
				/*
				 * TODO: make required relaxation to original query constraints using nearest rule
				 */
				
				results = rigid(relaxed, insts);
			}
			else {
				System.out.println("No rules found to relax this query.");
			}
		}
		
		return results;
	}
}
