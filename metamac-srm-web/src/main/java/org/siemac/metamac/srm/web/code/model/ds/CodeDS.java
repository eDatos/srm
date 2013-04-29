package org.siemac.metamac.srm.web.code.model.ds;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;

public class CodeDS extends ItemDS {

    public static final String SHORT_NAME               = "code-short-name";
    public static final String VARIABLE_ELEMENT         = "code-var-elem";
    public static final String VARIABLE_ELEMENT_VIEW    = "code-var-elem-view";
    // public static final String VARIABLE_ELEMENT_EDITION = "code-var-elem-edition";

    public static final String ORDER                    = "code-order";

    public static final String OPENNESS_LEVEL           = "code-openness-level";
    public static final String OPENNESS_LEVEL_ICON      = "code-openness-level-icon"; // Stores the URL of the node icon
    public static final String OPENNESS_LEVEL_INITIAL   = "code-openness-level-ini"; // Stores the value of the openness level. It is not modified when the user modifies the openness Level of a node.
}
