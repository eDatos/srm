package org.siemac.metamac.srm.core.normalisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;

/**
 * Based on Dice's coefficient used as a similarity measure
 */

public class DiceAproxStringMatch extends AproxStringMatch {

    public DiceAproxStringMatch(Map<String, String> dictionary) {
        super(dictionary);
    }

    @Override
    public List<MatchResult> getSuggestedWords(String word, int maxResults) throws MetamacException {
        List<MatchResult> results = new ArrayList<MatchResult>();
        for (String dictionaryKey : dictionary.keySet()) {
            double score = compareToDictionary(dictionaryKey, word);
            results.add(new MatchResult(dictionaryKey, score));
        }
        Collections.sort(results);
        return results.subList(0, maxResults);
    }

    private double compareToDictionary(String dictionaryKey, String word) {
        String normalizeWord = normaliseWord(word);
        List<String> wordPairs = wordMultiNgrams(normalizeWord);
        List<String> dictionaryPairs = getDictionaryNgrams(dictionaryKey);
        int intersection = 0;
        int union = dictionaryPairs.size() + wordPairs.size();
        for (String pair1 : dictionaryPairs) {
            if (wordPairs.contains(pair1)) {
                intersection++;
                wordPairs.remove(pair1);
            }
        }
        double score = ((2.0 * intersection) / union);
        return score;
    }
}
