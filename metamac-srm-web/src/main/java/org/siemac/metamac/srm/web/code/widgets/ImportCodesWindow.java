package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.widgets.ImportResourceWindow;

public class ImportCodesWindow extends ImportResourceWindow {

    public ImportCodesWindow() {
        super(getConstants().actionImportCodes());
    }
}
