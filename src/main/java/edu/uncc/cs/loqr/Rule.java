package edu.uncc.cs.loqr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.core.Attribute;
import weka.core.Instances;
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
