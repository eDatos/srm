package org.siemac.metamac.srm.core.task.domain;

public class ImportationCodesTsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private int                    parentPosition;
    private int                    variableElementPosition;
    private InternationalStringTsv name;
    private InternationalStringTsv description;

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

    public InternationalStringTsv getName() {
        return name;
    }

    public void setName(InternationalStringTsv name) {
        this.name = name;
    }

    public InternationalStringTsv getDescription() {
        return description;
    }

    public void setDescription(InternationalStringTsv description) {
        this.description = description;
    }
}
