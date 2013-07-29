package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.core.constants.SrmConstants;

public class CodeMetamacResultExtensionPoint {

    private Integer                   order;
    private String                    orderConcatenatedByLevel;
    private Boolean                   openness;
    private final Map<String, String> shortName = new HashMap<String, String>();
    private String                    variableElementCode;

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

    public void setOrderConcatenatedByLevel(String orderConcatenatedByLevel) {
        this.orderConcatenatedByLevel = orderConcatenatedByLevel;
    }

    /**
     * Get order. This order is a concatenation of orders of parents and this item, with X characteres by level
     */
    public String getOrderConcatenatedByLevel() {
        return orderConcatenatedByLevel;
    }

    public Map<String, String> getShortName() {
        return shortName;
    }

    public void setVariableElementCode(String variableElement) {
        this.variableElementCode = variableElement;
    }

    public String getVariableElementCode() {
        return variableElementCode;
    }

    public int getLevel() {
        return orderConcatenatedByLevel.length() / (SrmConstants.CODE_QUERY_COLUMN_ORDER_LENGTH + 1); // 6 of order + 1 of dot
    }
}
