package edu.uncc.cs.loqr;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.supervised.attribute.Discretize;
import fj.data.List;


public class Query {
	private static final Logger log = LogManager.getLogger(Query.class);
	public final List<Conjunct> conjuncts;
	
	public Query(List<Conjunct> conjuncts) {
		this.conjuncts = conjuncts;
	}
	public Query(Conjunct... conjuncts) {
		this.conjuncts = List.list(conjuncts);
	}
	
	/**
	 * Create a new query from a user-input string, such as the following
	 * [Price<=2000]^[CPU>=2.5]^[Display>=17]^[Weight<=3]
	 * 
	 * This is not a real parser - just a regex.
	 * @param text
	 * @return
	 */
	public static Query parse(String text, Instances insts, Discretize disc) {
		Pattern pat = Pattern.compile("\\[([A-Za-z]+)(<|<=|==|>|>=|!=)(\\d+(\\.\\d+)?)\\]\\^?");
		Matcher m = pat.matcher(text);
		List<Conjunct> conjuncts = List.nil();
		
		
		while (m.find()) {
			Attribute attr = insts.attribute(m.group(1));
			int block = discretize(attr,
					disc,
					Double.parseDouble(m.group(3)));
			conjuncts = conjuncts.cons(new Conjunct(attr,
					Op.read(m.group(2)),
					block));
		}
		return new Query(conjuncts);
	}
	
	/**
	 * Find the class 'value' belongs in, given a filter and an attribute.
	 * 
	 * @param attr		The attribute
	 * @param disc		The discretization filter
	 * @param value		The value
	 * @return
	 */
	public static int discretize(Attribute attr, Discretize disc, double value) {
		double[] cuts = disc.getCutPoints(attr.index());
		return insertionPoint(cuts, value, 0, cuts.length-1);
	}
	
	/**
	 * Search for the smallest cut point greater than a value
	 * @param cuts
	 * @param value
	 * @param start
	 * @param end
	 * @return
	 */
	public static int insertionPoint(double[] cuts, double value, int start, int end) {
		if (end > start) {
			int middle = (start+end) /2;
			if (value <= middle) {
				return insertionPoint(cuts, value, start, middle);
			} else {
				return insertionPoint(cuts, value, middle+1, end);
			}
		} else {
			return start;
		}
	}
}
