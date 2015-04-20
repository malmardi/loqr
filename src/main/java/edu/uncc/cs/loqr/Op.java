package edu.uncc.cs.loqr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Basically an enum, but using symbols like <.
 * @author Sean
 */
public class Op {
	public final String name;
	public final BiFunction<Double, Double, Boolean> func;

	private static Map<String, Op> ops = new HashMap<>();	
	public static final Op LT = new Op("<", (a, b) -> a < b);
	public static final Op LE = new Op("<=", (a, b) -> a <= b);
	public static final Op EQ = new Op("==", (a, b) -> a == b);
	public static final Op GE = new Op(">=", (a, b) -> a >= b);
	public static final Op GT = new Op(">", (a, b) -> a > b);
	public static final Op NE = new Op("!=",(a, b) -> a != b);
	
	private Op(String name, BiFunction<Double, Double, Boolean> func) {
		this.name = name;
		this.func = func;
		ops.put(name, this);
	}
	
	public static Op read(String name) {
		return ops.get(name);
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * 
	 * @param op1
	 * @param op2
	 * @return true of op1 and op2 are same direction operators e.g., "<" & "<="
	 */
	public static boolean sameDir(String op1, String op2) {
		if(((op1.equals(Op.LT.name) || op1.equals(Op.LE.name)) && 
				(op2.equals(Op.LT.name) || op2.equals(Op.LE.name))) || 
				((op1.equals(Op.GT.name) || op1.equals(Op.GE.name)) && 
						(op2.equals(Op.GT.name) || op2.equals(Op.GE.name))))
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param op
	 * @return true if op is an equality operator i.e., != or ==
	 */
	public static boolean isEquality(String op) {
		if(op.equals(Op.EQ.name) || 
				op.equals(Op.NE.name))
			return true;
		else
			return false;
	}
}
