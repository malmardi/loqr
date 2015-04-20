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
		int L1=C1.length();
		int L2=C2.length();
		int length3;
		int length4;
		List<Conjunct> C3 = List.nil();//contains the longer between C1 and C2
		List<Conjunct> C4 = List.nil();//contains the shorter between C1 and C2
		List<Conjunct> C5 = List.nil();
		List<Conjunct> C6 = List.nil();
		
		if (L1>=L2)
			{
			length3=L1;
			length4=L2;
			C3=C1;
			C4=C2;
			
			}
		else 
		{
			length3=L2;
			length4=L1;
			C3=C2;
			C4=C1;
		}
		for(Conjunct c : C3) {
			for(Conjunct co : C4) {
				
			}
		}
		
		for(int i=0; i<length3; i++)
		{
			for(int j=0; j<=length4;i++)
			if (C3.index(i).attr==C4.index(j).attr)
			{
				C5.cons((C3.index(i)));
				C5.index(i).attr=C3.index(i).attr;
				C5.index(i).value=C3.index(i).value;
				C5.index(i).op=C3.index(i).op;
				
				C6.index(i).attr=C4.index(i).attr;
				C6.index(i).value=C4.index(i).value;
				C6.index(i).op=C4.index(i).op;
				
				
				
			}
			
		}
		//Now C5 and C6 have the same attributes in same order
		
		
				
		for(int i=0; i<C5.length(); i++)
		{
			switch(C5.index(i).op.name)
			{
			
			//Discrete Case
			case "EQ": 
				if(C5.index(i).value!=C6.index(i).value)
					C6.drop(1);//if the discrete values are not the same, then drop the attribute
			
			//Continuous Cases	
			case "LE": 
				if(C6.index(i).op.name=="<" || C6.index(i).op.name=="<=")
					if(C6.index(i).value < C5.index(i).value)
						C6.index(i).value=C5.index(i).value;
						
			case "LT": 
				if(C6.index(i).op.name=="<"||C6.index(i).op.name=="<=")
					if(C6.index(i).value<C5.index(i).value)
						C6.index(i).value=C5.index(i).value;
			case "GE": 
				if(C6.index(i).op.name==">"||C6.index(i).op.name==">=")
					if(C6.index(i).value>C5.index(i).value)
						C6.index(i).value=C5.index(i).value;
			case "GT": 
				if(C6.index(i).op.name==">"||C6.index(i).op.name==">=")
					if(C6.index(i).value>C5.index(i).value)
						C6.index(i).value=C5.index(i).value;
					
			}
		}
		
		return C6;
	}
	
	public double similarity(Rule other) {
		return 0.0;
	}
}
