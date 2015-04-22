package edu.uncc.cs.loqr;

import fj.data.List;

public class Rule {
	public final List<Conjunct> antecedents;
	public final List<Conjunct> consequents;
	
	public Rule(List<Conjunct> ante, List<Conjunct> cons) {
		antecedents = ante;
		consequents = cons;
	}
	
	/**
	 * Return some measure between two Rules
	 * 
	 * TODO
	 * @param other
	 * @return
	 */
	public double similarity(Rule other) {
		return 0.0;
	}

	@Override
	public String toString() {
		return String.format("Rule [antecedents=%s, consequents=%s]",
				antecedents, consequents);
	}
}
