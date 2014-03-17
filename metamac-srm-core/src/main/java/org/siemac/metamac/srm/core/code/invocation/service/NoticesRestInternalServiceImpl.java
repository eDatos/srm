package org.siemac.metamac.srm.core.code.invocation.service;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.srm.core.code.invocation.MetamacApisLocator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static Logger       logger = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);

    @Autowired
    private MetamacApisLocator  restApiLocator;

    @Autowired
    private SrmConfiguration    configurationService;

    @Autowired
    private TranslateExceptions translateExceptions;

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            String localisedMessage = localisedException.getMessage();

            createBackgroundNotification(actionCode, localisedMessage, user);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }

    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedMessage = LocaleUtil.getMessageForCode(successMessageCode, locale);
            localisedMessage = MessageFormat.format(localisedMessage, successMessageParameters);
            createBackgroundNotification(actionCode, localisedMessage, user);
        } catch (MetamacException e) {
            logger.error("Error creating createSuccessBackgroundNotification:", e);
        }
    }

    private void createBackgroundNotification(String actionCode, String message, String user) throws MetamacException {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedAction = LocaleUtil.getMessageForCode(actionCode, locale);

            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES.getName();
            String subject = "[" + sendingApp + "] " + localisedAction;

            // @formatter:off
            Notice notification = NoticeBuilder.notification()
                                                .withMessagesWithoutResources(message)
                                                .withSendingApplication(sendingApp)
                                                .withReceivers(user)
                                                .withSendingUser(user)
                                                .withSubject(subject)
                                                .build();
            // @formatter:on

            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification);
        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }

    }

    // -------------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------------

    private MetamacException manageNoticesInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_NOTICES_INTERNAL, restApiLocator.getNoticesRestInternalFacadeV10());
    }
}
