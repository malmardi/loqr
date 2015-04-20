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
	
	static {
		ops.put("=", EQ); // A one-equals synonym
	}
	
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
}
