package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

public class ImportCodesWindow extends ImportResourceWindow {

    public ImportCodesWindow() {
        super(getConstants().actionImportCodes(), getConstants().codes());
    }
}
