package org.siemac.metamac.srm.core.normalisation;

public class MatchResult implements Comparable<MatchResult> {

    private final String dictionaryKey;
    private double       score;

    public MatchResult(String dictionaryKey, double score) {
        this.dictionaryKey = dictionaryKey;
        this.score = score;
    }

    public String getDictionaryKey() {
        return dictionaryKey;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(MatchResult other) {
        return -1 * Double.compare(score, other.score);
    }

}
