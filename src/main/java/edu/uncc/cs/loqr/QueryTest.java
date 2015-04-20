package edu.uncc.cs.loqr;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.util.Pair;

import org.junit.Test;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class QueryTest {

	@Test
	public void testDistance() throws Exception {
		Instances insts = DataSource.read("/home/wshalaby/work/github/loqr/diabetes.arff");
		HashMap<Integer,String[]> tests = new HashMap<Integer,String[]>();
		tests.put(1, new String[]{"[class==0]", "[class==1]"});
		tests.put(2, new String[]{"[class==1]", "[class==1]"});
		
		tests.put(3, new String[]{"[preg==3]", "[preg==3]"});
		tests.put(4, new String[]{"[preg==3]", "[preg!=3]"});
		tests.put(5, new String[]{"[preg!=3]", "[preg!=3]"});
		
		tests.put(6, new String[]{"[preg!=3]^[plas=50]", "[preg!=3]^[plas=50]"});
		
		tests.put(6, new String[]{"[preg<15]", "[preg<13]"});
		tests.put(7, new String[]{"[preg<15]", "[preg<11]"});
		tests.put(8, new String[]{"[preg>3]", "[preg>5]"});
		tests.put(9, new String[]{"[preg>3]", "[preg>8]"});
		
		tests.put(10, new String[]{"[preg<=15]", "[preg<=13]"});
		tests.put(11, new String[]{"[preg<=15]", "[preg<=11]"});
		tests.put(12, new String[]{"[preg>=3]", "[preg>=5]"});
		tests.put(13, new String[]{"[preg>=3]", "[preg>=8]"});
		
		tests.put(14, new String[]{"[preg<15]", "[preg>13]"});
		tests.put(15, new String[]{"[preg<15]", "[preg>11]"});
		tests.put(16, new String[]{"[preg>3]", "[preg<5]"});
		tests.put(17, new String[]{"[preg>3]", "[preg<8]"});
		
		tests.put(18, new String[]{"[preg<=15]", "[preg>13]"});
		tests.put(19, new String[]{"[preg<=15]", "[preg>11]"});
		tests.put(20, new String[]{"[preg>=3]", "[preg<5]"});
		tests.put(21, new String[]{"[preg>=3]", "[preg<8]"});
		
		tests.put(22, new String[]{"[preg<15]", "[preg>16]"});
		tests.put(23, new String[]{"[preg>15]", "[preg<11]"});
		
		tests.put(24, new String[]{"[preg<15]^[plas==45]", "[preg>13]^[plas==45]"});
		tests.put(25, new String[]{"[preg<15]^[plas==45]", "[preg>13]^[plas==34]"});
		
		tests.put(26, new String[]{"[preg<15]^[plas<45]", "[preg<12]^[plas<20]"});
		tests.put(27, new String[]{"[preg<15]^[plas<45]", "[preg<12]^[plas<23]"});
		
		tests.put(28, new String[]{"[preg<15]^[plas<45]", "[plas<23]"});
		
		
		
		for(String[] Qs: tests.values()) {
			Query Q = Query.parse(Qs[0], insts);
			Query Qi = Query.parse(Qs[1], insts);
			System.out.println(Qs[0]+","+Qs[1]+"="+Q.distance(Qi, insts));
		}	
	}
}
