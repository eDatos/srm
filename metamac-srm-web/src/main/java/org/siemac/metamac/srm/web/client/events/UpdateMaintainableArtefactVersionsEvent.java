package org.siemac.metamac.srm.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateMaintainableArtefactVersionsEvent extends GwtEvent<UpdateMaintainableArtefactVersionsEvent.UpdateMaintainableArtefactVersionsHandler> {

    public interface UpdateMaintainableArtefactVersionsHandler extends EventHandler {

        void onUpdateMaintainableArtefactVersions(UpdateMaintainableArtefactVersionsEvent event);
    }

    private static Type<UpdateMaintainableArtefactVersionsHandler> TYPE = new Type<UpdateMaintainableArtefactVersionsHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateMaintainableArtefactVersionsHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, String urn) {
        if (TYPE != null) {
            source.fireEvent(new UpdateMaintainableArtefactVersionsEvent(urn));
        }
    }

    private final String urn;

    public UpdateMaintainableArtefactVersionsEvent(String urn) {
        this.urn = urn;
    }

    public String getUrn() {
        return urn;
    }

    @Override
    protected void dispatch(UpdateMaintainableArtefactVersionsHandler handler) {
        handler.onUpdateMaintainableArtefactVersions(this);
    }

    public static Type<UpdateMaintainableArtefactVersionsHandler> getType() {
        return TYPE;
    }
}
