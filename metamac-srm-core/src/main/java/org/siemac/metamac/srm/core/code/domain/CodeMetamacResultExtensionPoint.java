package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.core.constants.SrmConstants;

public class CodeMetamacResultExtensionPoint {

    private String                    order;
    private final Map<String, String> shortName = new HashMap<String, String>();
    private String                    variableElementCode;

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Get order. This order is a concatenation of orders of parents and this item, with X characteres by level
     */
    public String getOrder() {
        return order;
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
        return order.length() / (SrmConstants.CODE_QUERY_COLUMN_ORDER_LENGTH + 1); // 6 of order + 1 of dot
    }
}
