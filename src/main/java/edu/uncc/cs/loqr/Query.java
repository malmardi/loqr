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
	public static Query parse(String text, Instances insts) {
		Pattern pat = Pattern.compile("\\[([A-Za-z]+)(<|<=|==|>|>=|!=)(\\d+(\\.\\d+)?)\\]\\^?");
		Matcher m = pat.matcher(text);
		List<Conjunct> conjuncts = List.nil();
		
		
		while (m.find()) {
			Attribute attr = insts.attribute(m.group(1));
			/*int block = discretize(attr,
					disc,
					Double.parseDouble(m.group(3)));*/
			conjuncts = conjuncts.cons(new Conjunct(attr,
					Op.read(m.group(2)),
					Double.parseDouble(m.group(3))));
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
	
	/**
	 * Measure distance of query Qi with original query Q to determine 
	 * nearest one Qr
	 * Notes
	 * ======
	 * 1. If Q is: Price ≤ $2, 000 ^ Display ≥ 17 ^ Weight ≤ 3 lbs and 
	 * Qi is: Price ≤ $2, 300 ^ Cpu < 2.1 GHz ^ Display ≤ 13 then Cpu 
	 * is ignored as it doesn't appear in Q 
	 * so final Qr will be: Price ≤ $2, 300 ^ Display ≤ 13
	 * 	 
	 * Examples
	 * =========
	 * Q --> Price ≤ $2,000 ^ Display ≥ 17 ^ CPU ≥ 2.5 GHz ^ Weight ≤ 3 lbs ^ HDD ≥ 60GB
	 * Q1 --> Price ≤ $2,900 ^ Display ≥ 18 ^ Weight ≤ 4 lbs ^ CPU ≥ 2.5 GHz
	 * Q2 --> Price ≥ $3,500 ^ CPU ≥ 2.5 GHz
	 * Q3 -->  Price ≤ $2,000 ^ HDD ≥ 60 ^ Weight ≤ 4 lbs^ CPU ≥ 2.5 GHz
	 * Q4 --> Brand == "Sony" ^ CPU ≥ 2.5 GHz
	 * @param insts 
	 * @param Q: original user query
	 * @param Qs: list of queries learned from domain knowledge
	 * @param insts: instances from the dataset
	 * @return Qr: nearest rule to original query
	 */
	public static Query nearest(Query Q, List<Query> Qs, Instances insts) {
		// TODO Auto-generated method stub
		double min_dist = Double.MAX_VALUE;
		Query Qr = null;
		for(Query Qi : Qs) {
			// TODO: drop conjuncts in Qs not in Q
			double d = Q.distance(Qi, insts);
			if(d<=min_dist) {
				min_dist = d;
				Qr = Qi;
			}
		}
		
		return Qr;
	}
	
	/**
	 * What is it?
	 * ============
	 * loqr uses nearest-neighbor techniques to find the learned 
	 * query that is the most similar to the failing query.
	 * Assumptions
	 * ============
	 * Attributes are either nominal or numeric
	 * Nominal attributes use only == or != operators
	 * Given values are within permitted ranges as it appears in the dataset
	 * 
	 * Notes
	 * ======
	 *  nominal attributes distance=1 if they are not same operator and same values 
	 *  numeric attributes distance=1 if operator == or != and not same values
	 *    
	 * Examples
	 * =========
	 * Q: preg==3 && Qi: pre>3 --> distance = 0 
	 * Q: preg!=3 && Qi: pre<3 --> distance = 0
	 * Q: preg!=3 && Qi: pre<3 --> distance = 0 
	 *  
	 * @param other
	 * @param insts 
	 * @return some distance measure between two queries
	 */
	public double distance(Query Q, Instances insts) {
		double dist = 0.0;
		for(Conjunct co : conjuncts) {
			boolean found = false;
			double min = Double.NEGATIVE_INFINITY;
			double max = Double.POSITIVE_INFINITY;
			
			double Q_range_min = Double.NEGATIVE_INFINITY;
			double Q_range_max = Double.POSITIVE_INFINITY;
			
			if(co.attr.isNumeric()) {
				min = insts.attributeStats(co.attr.index()).numericStats.min;
				max = insts.attributeStats(co.attr.index()).numericStats.max;
				
				if(co.op.name.equals(Op.GE.name)) {
					Q_range_min = co.value;
					Q_range_max = max;
				}
				else if(co.op.name.equals(Op.GT.name)) {
					Q_range_min = co.value+1;
					Q_range_max = max;
				}
				else if(co.op.name.equals(Op.LE.name)) {
					Q_range_min = min;
					Q_range_max = co.value;
				}
				else if(co.op.name.equals(Op.LT.name)) {
					Q_range_min = min;
					Q_range_max = co.value-1;
				}
				else if(co.op.name.equals(Op.LT.name)) {
					Q_range_min = min-1;
					Q_range_max = co.value;
				}
			}
			
			for(Conjunct c : Q.conjuncts) {
				if(c.attr.name()==co.attr.name()) {
					found = true;
					if(c.attr.isNominal()) { // discrete attribute 
						if(c.op.equals(co.op) && 
								c.value==co.value)
							dist += 0.0;
						else
							dist += 1.0;
					}
					else if(c.attr.isNumeric()) {
						// numeric attributes distance=1 if operator == or != and not same values
						if(Op.isEquality(c.op.name) || 
								Op.isEquality(co.op.name)) {
							if(co.op.name.equals(c.op.name) && 
									co.value==c.value)
								dist += 0.0;
							else
								dist += 1.0;
						}
						else { // >,>=.<, <=							
							double Qi_range_min = Double.NEGATIVE_INFINITY;
							double Qi_range_max = Double.POSITIVE_INFINITY;
							
							if((c.op.name.equals(Op.GE.name) || 
									c.op.name.equals(Op.GT.name)) && 
									c.value<max) {
								if(c.op.name.equals(Op.GT.name))
									Qi_range_min = c.value+1;
								else
									Qi_range_min = c.value;			
								
								if(Op.sameDir(c.op.name, co.op.name)) {
									Qi_range_max = max;
								}
								else if(c.value<Q_range_max) {
									Qi_range_max = Q_range_max;
								}
							}
							else if((c.op.name.equals(Op.LE.name) || 
									c.op.name.equals(Op.LT.name)) && 
									c.value>min) {
								if(c.op.name.equals(Op.LT.name))
									Qi_range_max = c.value-1;
								else
									Qi_range_max = c.value;			
								
								if(Op.sameDir(c.op.name, co.op.name)) {
									Qi_range_min = min;
								}
								else if(c.value>Q_range_min) {
									Qi_range_min = Q_range_min;
								}
							}
							if(Qi_range_max!=Double.POSITIVE_INFINITY && 
									Qi_range_min!=Double.NEGATIVE_INFINITY) {
								dist += Math.abs((Q_range_max-Q_range_min)-(Qi_range_max-Qi_range_min))/(max-min);
							}
							else
								dist += 1.0;
						}
					}
					else // unsupported attribute type 
						dist += 1.0;
					
					break;
				}
			}
			if(found==false) // penalize Qi for missing attributes
				dist += 1.0;
		}
		return dist;
	}
	
	public static Query relax(Query Q, Query Qr)
	{
		
		List<Conjunct> C3 = List.nil();
		
		for(Conjunct c1 : Q.conjuncts) 
		{
			for(Conjunct c2 : Qr.conjuncts) 
			{
				if(c1.attr.name()==c2.attr.name()) {
					if((c1.op.name.equals(Op.EQ.name) && 
							c2.op.name.equals(Op.EQ.name) || 
							(c1.op.name.equals(Op.NE.name) && 
									c2.op.name.equals(Op.NE.name)) && 
							c1.value==c2.value))//Discrete
						C3 = C3.cons(c1);
					else if((c1.op.name.equals(Op.LE.name)||c1.op.name.equals(Op.LT.name))&&(c2.op.name.equals(Op.LE.name)||c2.op.name.equals(Op.LT.name)))//Both LT or LE
						if(c2.value <= c1.value)//Choose the upper limit 
							C3 = C3.cons(c1);
						else 
							C3 = C3.cons(c2);
					else if((c1.op.name.equals(Op.GE.name)||c1.op.name.equals(Op.GT.name))&&(c2.op.name.equals(Op.GE.name)||c2.op.name.equals(Op.GT.name)))//Both GE or GT
						if(c2.value <= c1.value)//Choose the lower
							C3 = C3.cons(c2);
						else 
							C3 = C3.cons(c1);		
					else if((c1.op.name.equals(Op.GE.name)||c1.op.name.equals(Op.GT.name))&&
							(c2.op.name.equals(Op.LE.name)||c2.op.name.equals(Op.LT.name))) {//Different types of inequalities
						if(c1.value<=c2.value)
						{
							C3 = C3.cons(c1);
							C3 = C3.cons(c2);
						}
					}
					else if((c1.op.name.equals(Op.LE.name)||c1.op.name.equals(Op.LT.name))&&
							(c2.op.name.equals(Op.GE.name)||c2.op.name.equals(Op.GT.name)) && 
							(c1.value>=c2.value))//Different types of inequalities
					{
						C3 = C3.cons(c1);
						C3 = C3.cons(c2);
					}
				}
			}
		}
	
		
		return new Query(C3);
	}
}
