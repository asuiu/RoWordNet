package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Class providing Tokenization functionality.
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class Tokenizer {

	HashSet<String> tblPunctuation = new HashSet<String>();
	static String punctuation = " .,;[]{}-_=+-/\\!?@#$%^&*()~<>\"`'";

	public static String[] tokenizeSentence(String sentence) {
		StringTokenizer tok = new StringTokenizer(sentence, punctuation, true);
		ArrayList<String> ret = new ArrayList<>();
		while (tok.hasMoreElements()) {
			String t = ((String) (tok.nextElement())).trim();
			if (!t.equals("")) {
				ret.add(t);
			}
		}
		return (String[]) ret.toArray(new String[0]);
	}

	public static String tokenizeAndJoinSentence(String sentence) {
		String[] toks = tokenizeSentence(sentence);
		StringBuilder rez = new StringBuilder();
		for (String tok : toks) {
			rez.append(tok + " ");
		}
		return rez.toString().trim();
	}
}
