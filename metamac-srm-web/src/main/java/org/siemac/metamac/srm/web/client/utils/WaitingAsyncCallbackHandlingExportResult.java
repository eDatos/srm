package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.HasHandlers;

public class WaitingAsyncCallbackHandlingExportResult extends WaitingAsyncCallbackHandlingError<ExportSDMXResourceResult> {

    private final HasHandlers source;

    public WaitingAsyncCallbackHandlingExportResult(HasHandlers source) {
        super(source);
        this.source = source;
    }

    @Override
    public void onWaitSuccess(ExportSDMXResourceResult result) {
        if (result.getFileName() != null && result.getException() != null) {
            ShowMessageEvent.fireWarningMessageWithError(source, getMessages().exportationWithErrorsWarning(), result.getException());
            org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
        } else if (result.getFileName() != null && result.getException() == null) {
            org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
        } else {
            ShowMessageEvent.fireErrorMessage(source, result.getException());
        }
    }
}
