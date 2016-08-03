package org.siemac.metamac.srm.core.error;

import java.util.Locale;

import org.junit.Test;
import org.siemac.metamac.common.test.translations.CheckTranslationsTestBase;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;

public class SrmCheckTranslationsTest extends CheckTranslationsTestBase {

    // ----------------------------------------------------------------------------------------
    // MORE TESTS
    // ----------------------------------------------------------------------------------------

    @Test
    public void testCheckExistsAllTranslationsForServiceNoticeActions() throws Exception {
        checkExistsAllTranslationsForStrings(getServiceNoticeActionClasses(), getLocalesToTranslate());
    }

    @SuppressWarnings("rawtypes")
    public Class[] getServiceNoticeActionClasses() {
        return new Class[]{ServiceNoticeAction.class};
    }

    @Test
    public void testCheckExistsAllTranslationsForServiceNoticeMessages() throws Exception {
        checkExistsAllTranslationsForStrings(getServiceNoticeMessagesClasses(), getLocalesToTranslate());
    }

    @SuppressWarnings("rawtypes")
    public Class[] getServiceNoticeMessagesClasses() {
        return new Class[]{ServiceNoticeMessage.class};
    }

    // ----------------------------------------------------------------------------------------
    // OVERRIDE METHODS
    // ----------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionTypeClasses() {
        return new Class[]{ServiceExceptionType.class};
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionParameterClasses() {
        return new Class[]{ServiceExceptionParameters.class};
    }

    @Override
    public Locale[] getLocalesToTranslate() {
        return LOCALES_TO_TRANSLATE;
    }

}