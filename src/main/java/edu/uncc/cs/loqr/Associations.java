package edu.uncc.cs.loqr;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fj.data.List;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Associations {
	private static final Logger log = LogManager.getLogger(Associations.class);
	/**
	 * Generate associations between values of different attributes using LERS
	 * @param insts
	 * @return
	 */
	public static List<Rule> associate(Query query, Instances insts) {
		List<Rule> rules = List.nil();
		for (Conjunct conj : query.conjuncts) {
			try {
				J48 tree = new J48();
				Instances thresholded = applyConjunct(conj, insts);
				tree.buildClassifier(thresholded);
				System.out.println(tree.prefix());
				rules = rules.append(extractRules(thresholded, tree));
				
				//System.out.println(tree.toSource("Associations"));
			} catch (Exception e) {
				// We have no idea what it is and we can't do anything ab
				e.printStackTrace();
			}
		}
		System.out.println(rules);
		return List.nil();
	}
	
	/**
	 * Return only the results matching a single Conjunct
	 * @param conj
	 * @param insts
	 * @return
	 * @throws Exception 
	 */
	private static Instances applyConjunct(Conjunct conj, Instances insts) throws Exception {
		// Prepare the output matrix
		Instances outs = new Instances(insts);
		outs.setClass(conj.attr);
		
		// For every row
		for (int i=0; i<insts.numInstances(); i++) {
			double[] row = insts.instance(i).toDoubleArray();
			row[conj.attr.index()] =
					conj.op.func.apply(row[conj.attr.index()], conj.value)?
							1 : 0;
			outs.add(new Instance(1.0, row));
		}
		
		
		// Update the attribute type to match the new filter results
		NumericToNominal renamer = new NumericToNominal();
		renamer.setInputFormat(outs); // Required before filtering
		// which attribute to convert
		renamer.setAttributeIndicesArray(new int[]{conj.attr.index()});
		return Filter.useFilter(outs, renamer);
	}
	
	/**
	 * Extract the tree from its string representation
	 * @param tree
	 * @return
	 */
	private static List<Rule> extractRules(Instances insts, J48 tree) {
		List<String> lines = List.iterableList(
				Arrays.asList(tree.toString().split("\n")))
				.drop(3); // Skip the header
		log.debug(tree);
		
		return extractRules(insts, lines, List.nil());
	}
	
	/**
	 * Process one line at a time, in context, to create new rules
	 * @param insts
	 * @param lines
	 * @param context
	 * @return
	 */
	private static List<Rule> extractRules(Instances insts, List<String> lines, List<Conjunct> context) {
		if (lines.isEmpty()) {
			return List.nil();
		}
		
		// The subject of analysis
		String line = lines.head();
		
		// How much are we indented?
		int rank = StringUtils.countMatches(line, "|");
		
		// Trim back the context until it explains the indent
		while (rank < context.length()) {
			context = context.tail();
		}
		
		// Match the rule in the summary
		Matcher mat = Pattern.compile("([a-z]+) (=|<=|>) ([^:]+)(: (?<leaf>\\d))?").matcher(line);

		
		List<Rule> rules = List.nil();
		if (mat.find()) {
			// Create a conjunct from the regex
			Attribute attr = insts.attribute(mat.group(1));
			
			double value;
			try {
				// It's a double attribute
				value = Double.parseDouble(mat.group(3));
			} catch (NumberFormatException e) {
				// It's a string attribute
				value = attr.indexOfValue(mat.group(3));
			}
			
			// Add an antecedent
			context = List.cons(
					new Conjunct(
						attr,
						Op.read(mat.group(2)),
						value),
					context);

			// Add our rule only if we are at a leaf
			String leaf = mat.group("leaf");
			if (leaf != null) {
				rules = List.single(new Rule(context, List.single(new Conjunct(
						insts.classAttribute(),
						Op.EQ,
						Double.parseDouble(leaf)
						))));
			}
		}
		
		// Then add the rest of the lines in this new context
		return rules.append(extractRules(insts, lines.tail(), context));
	}
}
