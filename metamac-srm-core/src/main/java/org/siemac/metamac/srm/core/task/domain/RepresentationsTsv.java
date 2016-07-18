package org.siemac.metamac.srm.core.task.domain;

public class RepresentationsTsv {

    public enum RepresentationEum {
        ENUMERATED, NON_ENUMERATED
    };

    private int typePosition  = -1; // Number of column for the Representation Type field (zero-index based)
    private int valuePosition = -1; // Number of column for the Representation Value field (zero-index based)

    public int getTypePosition() {
        return typePosition;
    }

    public void setTypePosition(int typePosition) {
        this.typePosition = typePosition;
    }

    public int getValuePosition() {
        return valuePosition;
    }

    public void setValuePosition(int valuePosition) {
        this.valuePosition = valuePosition;
    }

    public boolean isRepresentationHeaderComplete() {
        if (typePosition != -1 && valuePosition != -1) {
            return true;
        }
        return false;
    }
}
