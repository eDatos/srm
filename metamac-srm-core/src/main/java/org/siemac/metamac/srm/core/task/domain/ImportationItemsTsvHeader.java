package org.siemac.metamac.srm.core.task.domain;

public class ImportationItemsTsvHeader {

    private int                    columnsSize;   // Number of columns of TSV File
    private int                    codePosition;  // Number of column for Code field (zero-index based)
    private int                    parentPosition; // Number of column for Parent field (zero-index based)
    private InternationalStringTsv name;
    private InternationalStringTsv description;
    private InternationalStringTsv comment;

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

    public InternationalStringTsv getComment() {
        return comment;
    }

    public void setComment(InternationalStringTsv comment) {
        this.comment = comment;
    }
}
