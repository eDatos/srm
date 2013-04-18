package org.siemac.metamac.srm.core.normalisation;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;

public abstract class AproxStringMatch {

    protected Map<String, String>     dictionary;
    private Map<String, List<String>> dictionaryNgrams;
    private final int                 initialNgrams = 3;
    private final int                 finalNgrams   = 3;

    public AproxStringMatch(Map<String, String> dictionary) {
        this.dictionary = dictionary;
        buildDictionaryNgrams(dictionary);
    }

    protected List<String> getDictionaryNgrams(String dictionaryKey) {
        return dictionaryNgrams.get(dictionaryKey);
    }

    protected String normaliseWord(String word) {
        String wordNormalised = word.toLowerCase();
        wordNormalised = wordNormalised.replaceAll(",", StringUtils.EMPTY);
        wordNormalised = wordNormalised.replaceAll("'", StringUtils.EMPTY);
        wordNormalised = wordNormalised.replaceAll("\\(", StringUtils.EMPTY);
        wordNormalised = wordNormalised.replaceAll("\\)", StringUtils.EMPTY);
        wordNormalised = Normalizer.normalize(wordNormalised, Normalizer.Form.NFD);
        wordNormalised = wordNormalised.replaceAll("\\p{InCombiningDiacriticalMarks}+", StringUtils.EMPTY);
        return wordNormalised;
    }

    /**
     * @return a collection of N-character Strings
     */
    protected List<String> wordMultiNgrams(String word) {
        List<String> allPairs = new ArrayList<String>();
        if (initialNgrams != 0 && finalNgrams != 0) {
            for (int i = initialNgrams; i <= finalNgrams; i++) {
                allPairs.addAll(wordNgrams(word, i));
            }
        }
        // Add word limits
        allPairs.addAll(wordNgramsLimits(word, finalNgrams + 1));
        return allPairs;
    }

    private List<String> wordNgramsLimits(String str, int n) {
        List<String> allPairs = new ArrayList<String>();
        String[] words = str.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            List<String> boundaryWordNgrams = buildNgramsWithTwoWordsBoundary(n, word1, word2);
            allPairs.addAll(boundaryWordNgrams);
        }
        return allPairs;
    }

    private List<String> buildNgramsWithTwoWordsBoundary(int ngramSize, String wordLeft, String wordRight) {
        // We take n-1, last letters in word1
        int init1 = wordLeft.length() - (ngramSize - 1);
        if (init1 < 0) {
            init1 = 0;
        }
        // and n-1 first letters in word2,
        int end2 = ngramSize - 1;
        if (end2 > wordRight.length()) {
            end2 = wordRight.length();
        }
        String limitWord = wordLeft.substring(init1) + wordRight.substring(0, end2);
        return ngrams(limitWord, ngramSize);
    }

    /**
     * @return a collection of adjacent letter pairs contained in the input string
     */
    private List<String> ngrams(String str, int n) {
        List<String> ngrams = new ArrayList<String>();
        if (str.length() <= n || n == 0) {
            ngrams.add(str);
            return ngrams;
        }
        for (int i = 0; i < str.length(); i++) {
            if (i + n <= str.length()) {
                ngrams.add(str.substring(i, i + n));
            }
        }
        return ngrams;
    }

    /**
     * @return a collection of n-character strings
     */
    private List<String> wordNgrams(String str, int n) {
        List<String> allPairs = new ArrayList<String>();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("\\s+");
        // For each word
        for (int i = 0; i < words.length; i++) {
            // Find the pairs of characters
            List<String> ngramsInWord = ngrams(words[i], n);
            allPairs.addAll(ngramsInWord);
        }
        return allPairs;
    }

    private void buildDictionaryNgrams(Map<String, String> dictionary) {
        dictionaryNgrams = new HashMap<String, List<String>>(dictionary.size());
        for (String dictionaryKey : dictionary.keySet()) {
            String dictionaryWord = dictionary.get(dictionaryKey);
            String dictionaryWordNormalized = normaliseWord(dictionaryWord);
            dictionaryNgrams.put(dictionaryKey, wordMultiNgrams(dictionaryWordNormalized));
        }
    }

    public abstract List<MatchResult> getSuggestedWords(String word, int maxResults) throws MetamacException;
}
