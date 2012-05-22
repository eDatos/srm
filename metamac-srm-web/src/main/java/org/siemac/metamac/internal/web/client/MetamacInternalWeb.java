package org.siemac.metamac.internal.web.client;

import org.siemac.metamac.internal.web.client.gin.MetamacInternalWebGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MetamacInternalWeb implements EntryPoint {

    private static MetamacInternalWebConstants    constants;
    private static MetamacInternalWebCoreMessages coreMessages;
    private static MetamacInternalWebMessages     messages;

    public final MetamacInternalWebGinjector      ginjector = GWT.create(MetamacInternalWebGinjector.class);

    interface GlobalResources extends ClientBundle {

        @NotStrict
        @Source("resources/MetamacInternalWebStyles.css")
        CssResource css();
    }

    public void onModuleLoad() {
        // This is required for GWT-Platform proxy's generator.
        DelayedBindRegistry.bind(ginjector);
        ginjector.getPlaceManager().revealCurrentPlace();

        // Inject global styles
        GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();
    }

    public static MetamacInternalWebConstants getConstants() {
        if (constants == null) {
            constants = (MetamacInternalWebConstants) GWT.create(MetamacInternalWebConstants.class);
        }
        return constants;
    }

    public static MetamacInternalWebCoreMessages getCoreMessages() {
        if (coreMessages == null) {
            coreMessages = (MetamacInternalWebCoreMessages) GWT.create(MetamacInternalWebCoreMessages.class);
        }
        return coreMessages;
    }

    public static MetamacInternalWebMessages getMessages() {
        if (messages == null) {
            messages = (MetamacInternalWebMessages) GWT.create(MetamacInternalWebMessages.class);
        }
        return messages;
    }

    public static final String LOCAL_HOST  = GWT.getHostPageBaseURL();
    public static final String REMOTE_HOST = GWT.getHostPageBaseURL();

    public static String getRelativeURL(String url) {
        String realModuleBase;

        if (GWT.isScript()) {
            String moduleBase = GWT.getModuleBaseURL();

            // Use for deployment to PRODUCTION server
            realModuleBase = REMOTE_HOST;

            // Use to test compiled browser locally
            if (moduleBase.indexOf("localhost") != -1) {
                realModuleBase = LOCAL_HOST;
            }
        } else {
            // This is the URL for GWT Hosted mode
            realModuleBase = LOCAL_HOST;

        }

        return realModuleBase + url;
    }

}
