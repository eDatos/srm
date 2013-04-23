package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

public class ImportSDMXStructureWindow extends ImportResourceWindow {

    public ImportSDMXStructureWindow() {
        super(getConstants().actionImportSDMXResource(), getConstants().sDMXResource());
    }
}
