package phoneme;

/* 
English text to phoneme Package in Java, 
derived from the C source code written by John A. Wasser <speech@John-Wasser.com>, 
available at http://ww.John-Wasser.com/TextToSpeech/

Translated to Java by Olivier Sarrat, olivier_sarrat@hotmail.com

Adapted for music by Nathan Vance, nathav63@gmail.com
*/

/*
**      English to Phoneme translation.
**
**      English.rules are made up of four parts:
**      
**              The left context.
**              The text to match.
**              The right context.
**              The phonemes to substitute for the matched text.
**
**      Procedure:
**
**              Separate each block of letters (apostrophes included) 
**              and add a space on each side.  For each unmatched 
**              letter in the word, look through the English.rules where the 
**              text to match starts with the letter in the word.  If 
**              the text to match is found and the right and left 
**              context patterns also match, output the phonemes for 
**              that rule and skip to the next unmatched letter.
**
**
**      Special Context Symbols:
**
**              #       One or more vowels
**              :       Zero or more consonants
**              ^       One consonant.
**              .       One of B, D, V, G, J, L, M, N, R, W or Z (voiced 
**                      consonants)
**              %       One of ER, E, ES, ED, ING, ELY (a suffix)
**                      (Right context only)
**              +       One of E, I or Y (a "front" vowel)
*/

public class Phoneme {

	private static boolean isalpha(char chr) {
		return (!(chr < 'A' || chr > 'Z'));
	}

	private static boolean isvowel(char chr) {
		return (chr == 'A' || chr == 'E' || chr == 'I' || chr == 'O' || chr == 'U');
	}

	private static boolean isconsonant(char chr) {
		return (isalpha(chr) && !isvowel(chr));
	}

	private static String xlate_word(String word) {
		int index; /* Current position in word */
		int type; /* First letter of match part */
		int wordLength = word.length();

		StringBuilder phoneme = new StringBuilder();
		int indexRule;

		index = 1; /* Skip the initial blank */
		do {
			if (isalpha(word.charAt(index)))
				type = word.charAt(index) - 'A' + 1;
			else
				type = 0;

			indexRule = find_rule(word, index, English.rules[type]);

			if (indexRule == -1)
				index++;
			else {
				phoneme.append(English.rules[type][indexRule][English.OUT_PART]);
				index += English.rules[type][indexRule][English.MATCH_PART].length();
			}
		} while (index < wordLength);
		return phoneme.toString();
	}

	private static int find_rule(String word, int index, String chosenRules[][]) {
		String rule[];
		int indexRule = 0;
		String left, match, right;

		while(true) /* Search for the rule */
		{
			rule = chosenRules[indexRule];
			indexRule++;
			match = rule[English.MATCH_PART];

			if (match == "!%@$#") /* bad symbol! */
			{
				return -1; /* Skip it! */
			}
			
			//Does the match match the word at index?
			if(word.length() < index+match.length() || ! match.equals(word.substring(index, index+match.length())))
				continue;

			left = rule[English.LEFT_PART];
			right = rule[English.RIGHT_PART];

			if (!leftmatch(left, word, index - 1))
				continue;
			if (!rightmatch(right, word, index+match.length()))
				continue;

			return --indexRule;
		}
	}

	private static boolean leftmatch(
			String pattern, /* pattern to match in text */
			String context, /* text to be matched */
			int indexText)/* index of last char of text to be matched */
	{
		int pat;
		int count;

		if (pattern == "") /* null string matches any context */
		{
			return true;
		}

		/* point to last character in pattern string */
		count = pattern.length();
		pat = count - 1;

		for (; count > 0; pat--, count--) {
			/* First check for simple text or space */
			if (isalpha(pattern.charAt(pat)) || pattern.charAt(pat) == '\'' || pattern.charAt(pat) == ' ')
				if (pattern.charAt(pat) != context.charAt(indexText))
					return false;
				else {
					indexText--;
					continue;
				}

			if(pattern.charAt(pat) == '#'){
				/* One or more vowels */
				if (!isvowel(context.charAt(indexText)))
					return false;
				indexText--;
				while (isvowel(context.charAt(indexText)))
					indexText--;
			} else if (pattern.charAt(pat) == ':') {
				/* Zero or more consonants */
				while (isconsonant(context.charAt(indexText)))
					indexText--;
			} else if (pattern.charAt(pat) == '^') {
				/* One consonant */
				if (!isconsonant(context.charAt(indexText)))
					return false;
				indexText--;
			} else if (pattern.charAt(pat) == '.') {
				/* B, D, V, G, J, L, M, N, R, W, Z */
				if (context.charAt(indexText) != 'B' && context.charAt(indexText) != 'D'
						&& context.charAt(indexText) != 'V' && context.charAt(indexText) != 'G'
						&& context.charAt(indexText) != 'J' && context.charAt(indexText) != 'L'
						&& context.charAt(indexText) != 'M' && context.charAt(indexText) != 'N'
						&& context.charAt(indexText) != 'R' && context.charAt(indexText) != 'W'
						&& context.charAt(indexText) != 'Z')
					return false;
				indexText--;
			} else if (pattern.charAt(pat) == '+') {
				/* E, I or Y (front vowel) */
				if (context.charAt(indexText) != 'E' && context.charAt(indexText) != 'I'
						&& context.charAt(indexText) != 'Y')
					return false;
				indexText--;
			} else {
				System.err.println("Bad char in left rule: '" + pattern.charAt(pat) + "'");
				return false;
			}
		}

		return true;
	}

	private static boolean rightmatch(
			String pattern, /* pattern to match in text */
			String context, /* text to be matched */
			int indexText)/* index of last char of text to be matched */
	{
		if (pattern == "") /* null string matches any context */
			return true;

		int pat = 0;
		for (pat = 0; pat != pattern.length(); pat++) {
			/* First check for simple text or space */
			if (isalpha(pattern.charAt(pat)) || pattern.charAt(pat) == '\'' || pattern.charAt(pat) == ' ')
				if (pattern.charAt(pat) != context.charAt(indexText))
					return false;
				else {
					indexText++;
					continue;
				}
			
			if (pattern.charAt(pat) == '#') {
				/* One or more vowels */
				if (!isvowel(context.charAt(indexText)))
					return false;

				indexText++;

				while (isvowel(context.charAt(indexText)))
					indexText++;
			} else if (pattern.charAt(pat) == ':') {
				/* Zero or more consonants */
				while (isconsonant(context.charAt(indexText)))
					indexText++;
			} else if (pattern.charAt(pat) == '^') {
				/* One consonant */
				if (!isconsonant(context.charAt(indexText)))
					return false;
				indexText++;
			} else if (pattern.charAt(pat) == '.') {
				/* B, D, V, G, J, L, M, N, R, W, Z */
				if (context.charAt(indexText) != 'B' && context.charAt(indexText) != 'D'
						&& context.charAt(indexText) != 'V' && context.charAt(indexText) != 'G'
						&& context.charAt(indexText) != 'J' && context.charAt(indexText) != 'L'
						&& context.charAt(indexText) != 'M' && context.charAt(indexText) != 'N'
						&& context.charAt(indexText) != 'R' && context.charAt(indexText) != 'W'
						&& context.charAt(indexText) != 'Z')
					return false;
				indexText++;
			} else if (pattern.charAt(pat) == '+') {
				/* E, I or Y (front vowel) */
				if (context.charAt(indexText) != 'E' && context.charAt(indexText) != 'I'
						&& context.charAt(indexText) != 'Y')
					return false;
				indexText++;
			} else if (pattern.charAt(pat) == '%') {
				/* ER, E, ES, ED, ING, ELY (a suffix) */
				String sub = context.substring(indexText);
				if(sub.startsWith("ING") || sub.startsWith("ELY"))
					indexText += 3;
				else if(sub.startsWith("ER") || sub.startsWith("ES") || sub.startsWith("ED"))
					indexText += 2;
				else if(sub.startsWith("E"))
					indexText += 1;
				else
					return false;
			} else {
				System.err.println("Bad char in right rule:'" + pattern.charAt(pat) + "'");
				return false;
			}
		}

		return true;
	}

	public static String toPhoneme(String text) {

		return (xlate_word(" " + text.toUpperCase() + " "));
	}

}