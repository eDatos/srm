package org.siemac.metamac.srm.web.code.model.ds;

import com.smartgwt.client.data.DataSource;

public class VariableElementOperationDS extends DataSource {

    public static final String ID                  = "op-id";
    public static final String CODE                = "op-code";
    public static final String OPERATION_TYPE      = "op-type";
    public static final String OPERATION_TYPE_ENUM = "op-type-enum";
    public static final String OPERATION_SOURCES   = "op-sources";
    public static final String OPERATION_TARGETS   = "op-targets";

    // In this field the list of elements is stored (source or target elements, depending on the operation type)
    // public static final String ELEMENTS = "op-elements";

    public static final String DTO                 = "op-dto";
}
