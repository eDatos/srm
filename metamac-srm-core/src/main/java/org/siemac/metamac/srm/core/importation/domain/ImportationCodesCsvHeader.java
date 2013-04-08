package org.siemac.metamac.srm.core.importation.domain;

public class ImportationCodesCsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private int                    parentPosition;
    private int                    variableElementPosition;
    private InternationalStringCsv name;
    private InternationalStringCsv description;

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

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public int getVariableElementPosition() {
        return variableElementPosition;
    }

    public void setVariableElementPosition(int variableElementPosition) {
        this.variableElementPosition = variableElementPosition;
    }

    public InternationalStringCsv getName() {
        return name;
    }

    public void setName(InternationalStringCsv name) {
        this.name = name;
    }

    public InternationalStringCsv getDescription() {
        return description;
    }

    public void setDescription(InternationalStringCsv description) {
        this.description = description;
    }
}
