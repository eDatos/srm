package org.siemac.metamac.srm.core.code.domain.shared;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public class CodeMetamacVisualisationResult extends ItemVisualisationResult {

    private static final long     serialVersionUID = 1L;

    private VariableElementVisualisationResult variableElement;
    private Integer               order;
    private Boolean               openness;

    public CodeMetamacVisualisationResult() {
    }

    public VariableElementVisualisationResult getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(VariableElementVisualisationResult variableElement) {
        this.variableElement = variableElement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getOpenness() {
        return openness;
    }

    public void setOpenness(Boolean openness) {
        this.openness = openness;
    }
}