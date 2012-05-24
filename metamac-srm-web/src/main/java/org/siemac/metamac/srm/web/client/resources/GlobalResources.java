package org.siemac.metamac.srm.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface GlobalResources extends ClientBundleWithLookup {

    public static final GlobalResources RESOURCE = GWT.create(GlobalResources.class);

    // CSS
    // @Source("MetamacSrmWebStyles.css")
    // CssStyles css();
    //
    // interface CssStyles extends MetamacSrmWebStyles { }

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/info.png")
    ImageResource info();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/remove.png")
    ImageResource remove();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/annotations.png")
    ImageResource annotations();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/add_annotation.png")
    ImageResource addAnnotation();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/import.png")
    ImageResource importDsd();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/export.png")
    ImageResource exportDsd();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext.png")
    ImageResource resultSetNext();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst.png")
    ImageResource resultSetFirst();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast.png")
    ImageResource resultSetLast();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious.png")
    ImageResource resultSetPrevious();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetnext_Disabled.png")
    ImageResource resultSetNextDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetfirst_Disabled.png")
    ImageResource resultSetFirstDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetlast_Disabled.png")
    ImageResource resultSetLastDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/resultsetprevious_Disabled.png")
    ImageResource resultSetPreviousDisabled();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/pending_publication.png")
    ImageResource pendingPublication();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/internal_publish.png")
    ImageResource internalPublish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/external_publish.png")
    ImageResource externalPublish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reject.png")
    ImageResource reject();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/version.png")
    ImageResource version();
}
