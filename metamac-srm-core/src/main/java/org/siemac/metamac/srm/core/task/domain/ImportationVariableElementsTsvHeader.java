package org.siemac.metamac.srm.core.task.domain;

public class ImportationVariableElementsTsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private InternationalStringTsv shortName;

    public int getColumnsSize() {
        return columnsSize;
    }

    public void setColumnsSize(int columnsSize) {
        this.columnsSize = columnsSize;
    }

    public int getCodePosition() {
        return codePosition;
    }

    public void setCodePosition(int codePosition) {
        this.codePosition = codePosition;
    }

    public InternationalStringTsv getShortName() {
        return shortName;
    }

    public void setShortName(InternationalStringTsv shortName) {
        this.shortName = shortName;
    }
}
