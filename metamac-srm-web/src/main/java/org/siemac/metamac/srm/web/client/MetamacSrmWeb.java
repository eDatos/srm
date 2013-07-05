package org.siemac.metamac.srm.web.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.gin.MetamacSrmWebGinjector;
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerAction;
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerResult;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.web.common.client.MetamacSecurityEntryPoint;
import org.siemac.metamac.web.common.client.gin.MetamacWebGinjector;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MetamacSrmWeb extends MetamacSecurityEntryPoint {

    private static final Boolean               SECURITY_ENABLED = true;

    private static Logger                      logger           = Logger.getLogger(MetamacSrmWeb.class.getName());

    private static MetamacPrincipal            principal;
    private static SrmWebConstants             constants;
    private static SrmWebCoreMessages          coreMessages;
    private static SrmWebMessages              messages;

    private static OrganisationMetamacDto      defaultMaintainer;

    public static final MetamacSrmWebGinjector ginjector        = GWT.create(MetamacSrmWebGinjector.class);

    @Override
    public void onModuleLoad() {
        setUncaughtExceptionHandler();

        prepareApplication(SECURITY_ENABLED);

    }

    @Override
    protected void onBeforeLoadApplication() {
        ginjector.getDispatcher().execute(new GetDefaultMaintainerAction(), new WaitingAsyncCallback<GetDefaultMaintainerResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading the default maintainer");
                showErrorPage();
            }
            @Override
            public void onWaitSuccess(GetDefaultMaintainerResult result) {
                // Store the default maintainer
                defaultMaintainer = result.getOrganisationMetamacDto();

                loadApplication();
            }
        });
    }

    public static OrganisationMetamacDto getDefaultMaintainer() {
        return defaultMaintainer;
    }

    public static MetamacPrincipal getCurrentUser() {
        return MetamacSrmWeb.principal;
    }

    public static SrmWebConstants getConstants() {
        if (constants == null) {
            constants = (SrmWebConstants) GWT.create(SrmWebConstants.class);
        }
        return constants;
    }

    public static SrmWebCoreMessages getCoreMessages() {
        if (coreMessages == null) {
            coreMessages = (SrmWebCoreMessages) GWT.create(SrmWebCoreMessages.class);
        }
        return coreMessages;
    }

    public static SrmWebMessages getMessages() {
        if (messages == null) {
            messages = (SrmWebMessages) GWT.create(SrmWebMessages.class);
        }
        return messages;
    }

    public static void showErrorPage() {
        ginjector.getPlaceManager().revealErrorPlace(null);
    }

    // Security Entry point

    @Override
    protected String getApplicationTitle() {
        return getConstants().appTitle();
    }

    @Override
    protected MetamacPrincipal getPrincipal() {
        return principal;
    }

    @Override
    protected void setPrincipal(MetamacPrincipal principal) {
        MetamacSrmWeb.principal = principal;
    }

    @Override
    protected String getSecurityApplicationId() {
        return SrmConstants.SECURITY_APPLICATION_ID;
    };

    @Override
    protected MetamacWebGinjector getWebGinjector() {
        return ginjector;
    }
}
