package org.siemac.metamac.srm.web.client;

import org.siemac.metamac.srm.web.client.gin.MetamacSrmWebGinjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MetamacSrmWeb implements EntryPoint {

    private static MetamacSrmWebConstants    constants;
    private static MetamacSrmWebCoreMessages coreMessages;
    private static MetamacSrmWebMessages     messages;

    public final MetamacSrmWebGinjector      ginjector = GWT.create(MetamacSrmWebGinjector.class);

    interface GlobalResources extends ClientBundle {

        @NotStrict
        @Source("resources/MetamacSrmWebStyles.css")
        CssResource css();
    }

    public void onModuleLoad() {
        // This is required for GWT-Platform proxy's generator.
        DelayedBindRegistry.bind(ginjector);
        ginjector.getPlaceManager().revealCurrentPlace();

        // Inject global styles
        GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();
    }

    public static MetamacSrmWebConstants getConstants() {
        if (constants == null) {
            constants = (MetamacSrmWebConstants) GWT.create(MetamacSrmWebConstants.class);
        }
        return constants;
    }

    public static MetamacSrmWebCoreMessages getCoreMessages() {
        if (coreMessages == null) {
            coreMessages = (MetamacSrmWebCoreMessages) GWT.create(MetamacSrmWebCoreMessages.class);
        }
        return coreMessages;
    }

    public static MetamacSrmWebMessages getMessages() {
        if (messages == null) {
            messages = (MetamacSrmWebMessages) GWT.create(MetamacSrmWebMessages.class);
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
