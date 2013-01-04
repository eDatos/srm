package org.siemac.metamac.srm.web.client.events;

import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectMenuButtonEvent extends GwtEvent<SelectMenuButtonEvent.SelectMenuButtonHandler> {

    public interface SelectMenuButtonHandler extends EventHandler {

        void onSelectMenuButton(SelectMenuButtonEvent event);
    }

    private static Type<SelectMenuButtonHandler> TYPE = new Type<SelectMenuButtonHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectMenuButtonHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, ToolStripButtonEnum resourceType) {
        if (TYPE != null) {
            source.fireEvent(new SelectMenuButtonEvent(resourceType));
        }
    }

    private final ToolStripButtonEnum resourceType;

    public SelectMenuButtonEvent(ToolStripButtonEnum resourceType) {
        this.resourceType = resourceType;
    }

    public ToolStripButtonEnum getResourceType() {
        return resourceType;
    }

    @Override
    protected void dispatch(SelectMenuButtonHandler handler) {
        handler.onSelectMenuButton(this);
    }

    public static Type<SelectMenuButtonHandler> getType() {
        return TYPE;
    }
}
