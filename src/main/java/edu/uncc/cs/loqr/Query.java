package edu.uncc.cs.loqr;

import fj.data.List;


public class Query {
	public final List<Conjunct> conjuncts;
	
	public Query(List<Conjunct> conjuncts) {
		this.conjuncts = conjuncts;
	}
	
	/**
	 * Create a new query from a user-input string, such as the following
	 * [Price<=2000]^[CPU>=2.5]^[Display>=17]^[Weight<=3]
	 * TODO
	 * @param text
	 * @return
	 */
	public static Query parse(String text) {
		return new Query(List.nil());
	}
}
