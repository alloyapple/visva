package com.visva.android.flashlight.utilities;

public class MorseCodeConverter {
	private static long SPEED_BASE = 100;
    private static long DOT = SPEED_BASE;
    private static long DASH = SPEED_BASE * 3;
    private static long GAP = SPEED_BASE;
    private static long LETTER_GAP = SPEED_BASE * 3;
    private static long WORD_GAP = SPEED_BASE * 7;
 
    /** The characters from 'A' to 'Z' */
    private static long[][] LETTERS = new long[][] {
        /* A */ new long[] { DOT, GAP, DASH },
        /* B */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DOT },
        /* C */ new long[] { DASH, GAP, DOT, GAP, DASH, GAP, DOT },
        /* D */ new long[] { DASH, GAP, DOT, GAP, DOT },
        /* E */ new long[] { DOT },
        /* F */ new long[] { DOT, GAP, DOT, GAP, DASH, GAP, DOT },
        /* G */ new long[] { DASH, GAP, DASH, GAP, DOT },
        /* H */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT },
        /* I */ new long[] { DOT, GAP, DOT },
        /* J */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DASH },
        /* K */ new long[] { DASH, GAP, DOT, GAP, DASH },
        /* L */ new long[] { DOT, GAP, DASH, GAP, DOT, GAP, DOT },
        /* M */ new long[] { DASH, GAP, DASH },
        /* N */ new long[] { DASH, GAP, DOT },
        /* O */ new long[] { DASH, GAP, DASH, GAP, DASH },
        /* P */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DOT },
        /* Q */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DASH },
        /* R */ new long[] { DOT, GAP, DASH, GAP, DOT },
        /* S */ new long[] { DOT, GAP, DOT, GAP, DOT },
        /* T */ new long[] { DASH },
        /* U */ new long[] { DOT, GAP, DOT, GAP, DASH },
        /* V */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DASH },
        /* W */ new long[] { DOT, GAP, DASH, GAP, DASH },
        /* X */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DASH },
        /* Y */ new long[] { DASH, GAP, DOT, GAP, DASH, GAP, DASH },
        /* Z */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DOT },
    };

    /** The characters from '0' to '9' */
    private static long[][] NUMBERS = new long[][] {
        /* 0 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH },
        /* 1 */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH },
        /* 2 */ new long[] { DOT, GAP, DOT, GAP, DASH, GAP, DASH, GAP, DASH },
        /* 3 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DASH, GAP, DASH },
        /* 4 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DASH },
        /* 5 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT },
        /* 6 */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT },
        /* 7 */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DOT, GAP, DOT },
        /* 8 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DOT, GAP, DOT },
        /* 9 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DOT },
    };

    private static final long[] ERROR_GAP = new long[] { GAP };

    /** Return the pattern data for a given character */
    public static long[] pattern(char c) {
        if (c >= 'A' && c <= 'Z') {
            return LETTERS[c - 'A'];
        }
        if (c >= 'a' && c <= 'z') {
            return LETTERS[c - 'a'];
        }
        else if (c >= '0' && c <= '9') {
            return NUMBERS[c - '0'];
        }
        else {
            return ERROR_GAP;
        }
    }

    public static long[] pattern(String str) {
        boolean lastWasWhitespace;
        int strlen = str.length();

        // Calculate how long our array needs to be.
        int len = 1;
        lastWasWhitespace = true;
        for (int i=0; i<strlen; i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!lastWasWhitespace) {
                    len++;
                    lastWasWhitespace = true;
                }
            } else {
                if (!lastWasWhitespace) {
                    len++;
                }
                lastWasWhitespace = false;
                len += pattern(c).length;
            }
        }

        // Generate the pattern array.  Note that we put an extra element of 0
        // in at the beginning, because the pattern always starts with the pause,
        // not with the vibration.
        long[] result = new long[len+1];
        result[0] = 0;
        int pos = 1;
        lastWasWhitespace = true;
        for (int i=0; i<strlen; i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!lastWasWhitespace) {
                    result[pos] = WORD_GAP;
                    pos++;
                    lastWasWhitespace = true;
                }
            } else {
                if (!lastWasWhitespace) {
                    result[pos] = LETTER_GAP;
                    pos++;
                }
                lastWasWhitespace = false;
                long[] letter = pattern(c);
                System.arraycopy(letter, 0, result, pos, letter.length);
                pos += letter.length;
            }
        }
        return result;
    }
    
    public static void setSpeedBase(long __speed) {
    	SPEED_BASE = __speed;
    	DOT = SPEED_BASE;
        DASH = SPEED_BASE * 3;
        GAP = SPEED_BASE;
        LETTER_GAP = SPEED_BASE * 3;
        WORD_GAP = SPEED_BASE * 7;
        
        resetLettersArray();
        resetNumbersArray();
    }
    
    private static void resetLettersArray() {
    	LETTERS = new long[][] {
	        /* A */ new long[] { DOT, GAP, DASH },
	        /* B */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DOT },
	        /* C */ new long[] { DASH, GAP, DOT, GAP, DASH, GAP, DOT },
	        /* D */ new long[] { DASH, GAP, DOT, GAP, DOT },
	        /* E */ new long[] { DOT },
	        /* F */ new long[] { DOT, GAP, DOT, GAP, DASH, GAP, DOT },
	        /* G */ new long[] { DASH, GAP, DASH, GAP, DOT },
	        /* H */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT },
	        /* I */ new long[] { DOT, GAP, DOT },
	        /* J */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DASH },
	        /* K */ new long[] { DASH, GAP, DOT, GAP, DASH },
	        /* L */ new long[] { DOT, GAP, DASH, GAP, DOT, GAP, DOT },
	        /* M */ new long[] { DASH, GAP, DASH },
	        /* N */ new long[] { DASH, GAP, DOT },
	        /* O */ new long[] { DASH, GAP, DASH, GAP, DASH },
	        /* P */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DOT },
	        /* Q */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DASH },
	        /* R */ new long[] { DOT, GAP, DASH, GAP, DOT },
	        /* S */ new long[] { DOT, GAP, DOT, GAP, DOT },
	        /* T */ new long[] { DASH },
	        /* U */ new long[] { DOT, GAP, DOT, GAP, DASH },
	        /* V */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DASH },
	        /* W */ new long[] { DOT, GAP, DASH, GAP, DASH },
	        /* X */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DASH },
	        /* Y */ new long[] { DASH, GAP, DOT, GAP, DASH, GAP, DASH },
	        /* Z */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DOT },
    	};
    }
    
    private static void resetNumbersArray() {
    	NUMBERS = new long[][] {
	        /* 0 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH },
	        /* 1 */ new long[] { DOT, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DASH },
	        /* 2 */ new long[] { DOT, GAP, DOT, GAP, DASH, GAP, DASH, GAP, DASH },
	        /* 3 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DASH, GAP, DASH },
	        /* 4 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DASH },
	        /* 5 */ new long[] { DOT, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT },
	        /* 6 */ new long[] { DASH, GAP, DOT, GAP, DOT, GAP, DOT, GAP, DOT },
	        /* 7 */ new long[] { DASH, GAP, DASH, GAP, DOT, GAP, DOT, GAP, DOT },
	        /* 8 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DOT, GAP, DOT },
	        /* 9 */ new long[] { DASH, GAP, DASH, GAP, DASH, GAP, DASH, GAP, DOT },
	    };
    }
}
