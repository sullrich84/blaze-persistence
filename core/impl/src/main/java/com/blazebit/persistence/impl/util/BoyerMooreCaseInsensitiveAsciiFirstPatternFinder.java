package com.blazebit.persistence.impl.util;

public class BoyerMooreCaseInsensitiveAsciiFirstPatternFinder extends AbstractPatternFinder {

    // Only support ASCII
    private static final int radix = 256;
    private final int[] right;
    private final char[] pattern;

    public BoyerMooreCaseInsensitiveAsciiFirstPatternFinder(String pattern) {
        final int length = pattern.length();
        this.pattern = new char[length];

        this.right = new int[radix];
        for (int i = 0; i < radix; i++) {
            this.right[i] = -1;
        }
        for (int i = 0; i < length; i++) {
            final char c = Character.toLowerCase(pattern.charAt(i));
            this.pattern[i] = c;
            this.right[c] = i;
        }
    }

    public int indexIn(char[] text, int start, int end) {
        int m = pattern.length;
        int n = Math.min(text.length, end);
        int skip;
        for (int i = start; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                final char c = Character.toLowerCase(text[i + j]);
                if (pattern[j] != c) {
                    skip = Math.max(1, j - right[c]);
                    break;
                }
            }
            if (skip == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexIn(CharSequence text, int start, int end) {
        int m = pattern.length;
        int n = Math.min(text.length(), end);
        int skip;
        for (int i = start; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                final char c = Character.toLowerCase(text.charAt(i + j));
                if (pattern[j] != c) {
                    skip = Math.max(1, j - right[c]);
                    break;
                }
            }
            if (skip == 0) {
                return i;
            }
        }
        return -1;
    }
}