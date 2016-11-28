package org.ciat.cmit.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.ciat.cmit.model.CoefficientDomain;

public class App {

	public static void main(String[] args) {
		ArrayList<String> combinations = new ArrayList<String>();

		/* palvarez test cases */
		CoefficientDomain[] c = new CoefficientDomain[4];
		c[0] = new CoefficientDomain(17.0, 20.0, 0.15, new DecimalFormat("#0.00"));
		c[1] = new CoefficientDomain(2.5, 7.0, 0.3, new DecimalFormat("##0.0"));
		c[2] = new CoefficientDomain(9.0, 19.0, 0.3, new DecimalFormat("##0.0"));
		c[3] = new CoefficientDomain(11.0, 25.0, 0.3, new DecimalFormat("#0.00"));

		for (int i = 0; i < c.length; i++) {
			combinations = getCombinations(c[i], combinations);
		}
		combinations = addPrefixAndSufix(combinations);

		ArrayList<String> cultivars = writeCultivars(combinations);

		ArrayList<String> filex = writeFileX(cultivars);
	}

	private static ArrayList<String> writeFileX(ArrayList<String> cultivars) {
		ArrayList<String> filex = new ArrayList<>();

		for (String cultivar : cultivars) {
			File mergedFile = new File("CCLA1303_" + cultivar + ".BNX");
			File head = new File("sample\\CCLA1303_head.BNX");
			File tail = new File("sample\\CCLA1303_tail.BNX");

			FileWriter fstream = null;
			PrintWriter out = null;
			try {
				fstream = new FileWriter(mergedFile, true);
				out = new PrintWriter(fstream);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			FileInputStream fisHead;
			try {
				fisHead = new FileInputStream(head);
				BufferedReader inHead = new BufferedReader(new InputStreamReader(fisHead));

				String aLineHead;
				while ((aLineHead = inHead.readLine()) != null) {
					out.println(aLineHead);

				}

				inHead.close();
		

			out.println(" 1 BN "+cultivar+" CALIMA");

			FileInputStream fisTail;
		
				fisTail = new FileInputStream(tail);
				BufferedReader inTail = new BufferedReader(new InputStreamReader(fisTail));

				String aLineTail;
				while ((aLineTail = inTail.readLine()) != null) {
					out.println(aLineTail);
				}

				inTail.close();
				out.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}


		}

		return filex;
	}

	public static ArrayList<String> writeCultivars(ArrayList<String> combinations) {
		ArrayList<String> cultivars = new ArrayList<>();
		int i = 0;
		PrintWriter writer;
		DecimalFormat nf = new DecimalFormat("000000");
		try {
			writer = new PrintWriter("BNGRO046.CUL");
			writer.println("*DRYBEAN GENOTYPE COEFFICIENTS: CRGRO046 MODEL");
			writer.println("@VAR#  VRNAME.......... EXPNO   ECO#  CSDL PPSEN EM-FL FL-SH FL-SD SD-PM FL-LF LFMAX SLAVR SIZLF  XFRT WTPSD SFDUR SDPDV PODUR THRSH SDPRO SDLIP");
			writer.println("!                                        1     2     3     4     5     6     7     8     9    10    11    12    13    14    15    16    17    18");
			for (String combination : combinations) {
				cultivars.add(nf.format(i++) + "");
				writer.println(combination);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return cultivars;
	}

	@Deprecated
	private static ArrayList<String> addPrefixAndSufix(ArrayList<String> combinations) {
		String temp;
		ArrayList<String> newCombinations = new ArrayList<String>();
		DecimalFormat nf = new DecimalFormat("000000");
		int i = 0;
		for (String combination : combinations) {
			temp = nf.format(i++) + " CALIMA               . ANDIND 12.17 0.050 " + combination + " 18.00  0.98  320. 133.0  1.00 0.600  15.0  3.50  10.0  78.0  .235  .030";
			newCombinations.add(temp);
		}
		return newCombinations;

	}

	public static ArrayList<String> getCombinations(CoefficientDomain c, ArrayList<String> combinations) {
		String temp;
		ArrayList<String> newCombinations = new ArrayList<String>();

		if (combinations.size() == 0) {
			for (double j = c.getMinValue(); j <= c.getMaxValue(); j += c.getPeriod()) { // for the first coefficient
				temp = String.format(c.getStringFormat(), c.getNumberFormat().format(j) + "");
				newCombinations.add(temp);
			}
		} else {
			for (String combination : combinations) {
				for (double j = c.getMinValue(); j <= c.getMaxValue(); j += c.getPeriod()) {
					temp = combination + " " + String.format(c.getStringFormat(), c.getNumberFormat().format(j) + "");
					newCombinations.add(temp);

				}
			}

		}
		return newCombinations;
	}

	public static void printCombinations(ArrayList<String> combinations) {

		for (String combination : combinations) {
			System.out.println(combination);
		}
	}

}
