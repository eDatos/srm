package org.siemac.metamac.srm.web.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface GlobalResources extends ClientBundleWithLookup {

    public static final GlobalResources RESOURCE = GWT.create(GlobalResources.class);

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/annotations.png")
    ImageResource annotations();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/add_annotation.png")
    ImageResource addAnnotation();

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
    @Source("images/validate_production.png")
    ImageResource validateProduction();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/validate_diffusion.png")
    ImageResource validateDiffusion();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/publish_internally.png")
    ImageResource internalPublish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/publish_externally.png")
    ImageResource externalPublish();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/reject.png")
    ImageResource reject();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/version.png")
    ImageResource version();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/segregate.png")
    ImageResource segregate();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/fusion.png")
    ImageResource fusion();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/folder_opened.png")
    ImageResource folderOpened();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/folder_closed.png")
    ImageResource folderClosed();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/fit.png")
    ImageResource fit();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("images/copy.png")
    ImageResource copy();
}
