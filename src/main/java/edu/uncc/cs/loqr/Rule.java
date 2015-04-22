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
	
	
	
	List<Conjunct> Relaxation(List<Conjunct> C1, List<Conjunct> C2)
	{
		
		List<Conjunct> C3 = List.nil();
		
		for(Conjunct c1 : C1) 
		{
			for(Conjunct c2 : C2) 
			{
				if(c1.attr.name()==c2.attr.name())//Discrete
					if(c1.op.name.equals(Op.EQ.name) && c2.op.name.equals(Op.EQ.name))
						C3 = List.cons(new Conjunct(c1), C3);
					else if((c1.op.name.equals(Op.LE.name)||c1.op.name.equals(Op.LT.name))&&(c2.op.name.equals(Op.LE.name)||c2.op.name.equals(Op.LT.name)))//Both LT or LE
						if(c2.value <= c1.value)//Choose the upper limit 
							C3 = List.cons(new Conjunct(c1), C3);
						else 
							C3 = List.cons(new Conjunct(c2), C3);
					else if((c1.op.name.equals(Op.GE.name)||c1.op.name.equals(Op.GT.name))&&(c2.op.name.equals(Op.GE.name)||c2.op.name.equals(Op.GT.name)))//Both GE or GT
						if(c2.value <= c1.value)//Choose the lower
							C3 = List.cons(new Conjunct(c2), C3);
						else 
							C3 = List.cons(new Conjunct(c1), C3);		
					else if((c1.op.name.equals(Op.GE.name)||c1.op.name.equals(Op.GT.name))&&(c2.op.name.equals(Op.LE.name)||c2.op.name.equals(Op.LT.name)))//Different types of inequalities
						if(c1.value<=c2.value)
						{
							C3 = List.cons(new Conjunct(c1), C3);
							C3 = List.cons(new Conjunct(c2), C3);
						}
						else ;
					else if((c1.op.name.equals(Op.LE.name)||c1.op.name.equals(Op.LT.name))&&(c2.op.name.equals(Op.GE.name)||c2.op.name.equals(Op.GT.name)))//Different types of inequalities
						if(c1.value>=c2.value)
						{
							C3 = List.cons(new Conjunct(c1), C3);
							C3 = List.cons(new Conjunct(c2), C3);
						}
							
							
				
			}
		}
	
		
		return C3;
	}
	
	public double similarity(Rule other) {
		return 0.0;
	}
}
