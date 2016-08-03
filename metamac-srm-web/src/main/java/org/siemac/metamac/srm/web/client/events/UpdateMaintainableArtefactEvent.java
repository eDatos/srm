package org.siemac.metamac.srm.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateMaintainableArtefactEvent extends GwtEvent<UpdateMaintainableArtefactEvent.UpdateMaintainableArtefactHandler> {

    public interface UpdateMaintainableArtefactHandler extends EventHandler {

        void onUpdateMaintainableArtefact(UpdateMaintainableArtefactEvent event);
    }

    private static Type<UpdateMaintainableArtefactHandler> TYPE = new Type<UpdateMaintainableArtefactHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateMaintainableArtefactHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, String urn) {
        if (TYPE != null) {
            source.fireEvent(new UpdateMaintainableArtefactEvent(urn));
        }
    }

    private final String urn;

    public UpdateMaintainableArtefactEvent(String urn) {
        this.urn = urn;
    }

    public String getUrn() {
        return urn;
    }

    @Override
    protected void dispatch(UpdateMaintainableArtefactHandler handler) {
        handler.onUpdateMaintainableArtefact(this);
    }

    public static Type<UpdateMaintainableArtefactHandler> getType() {
        return TYPE;
    }
}
