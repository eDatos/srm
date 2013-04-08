package org.siemac.metamac.srm.core.importation.domain;

public class ImportationVariableElementsCsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private InternationalStringCsv shortName;

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

    public InternationalStringCsv getShortName() {
        return shortName;
    }

    public void setShortName(InternationalStringCsv shortName) {
        this.shortName = shortName;
    }
}
