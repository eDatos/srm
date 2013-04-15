package org.siemac.metamac.srm.core.task.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImportationCodeOrdersCsvHeader {

    private int                        columnsSize;
    private int                        codePosition;
    private int                        labelPosition;                                       // optional
    private int                        levelPosition;                                       // optional
    private int                        parentPosition;                                      // optional
    private final Map<String, Integer> orderVisualisations = new HashMap<String, Integer>();

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

    public int getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(int labelPosition) {
        this.labelPosition = labelPosition;
    }

    public int getLevelPosition() {
        return levelPosition;
    }

    public void setLevelPosition(int levelPosition) {
        this.levelPosition = levelPosition;
    }

    public int getOrderVisualisationColumn(String orderVisualisation) {
        return orderVisualisations.get(orderVisualisation);
    }

    public void addOrderVisualisations(String orderVisualisation, int column) {
        orderVisualisations.put(orderVisualisation, Integer.valueOf(column));
    }

    public Set<String> getOrderVisualisations() {
        return orderVisualisations.keySet();
    }

    public boolean hasOrderVisualisations() {
        return orderVisualisations.size() != 0;
    }
}
