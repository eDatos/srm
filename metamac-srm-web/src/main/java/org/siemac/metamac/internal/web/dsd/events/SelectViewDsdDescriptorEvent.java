package org.siemac.metamac.internal.web.dsd.events;

import org.siemac.metamac.domain_enum.domain.TypeComponentList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectViewDsdDescriptorEvent extends GwtEvent<SelectViewDsdDescriptorEvent.SelectViewDsdDescriptorHandler> {

    public interface SelectViewDsdDescriptorHandler extends EventHandler {

        void onSelectViewDsdDescriptor(SelectViewDsdDescriptorEvent event);
    }

    private static Type<SelectViewDsdDescriptorHandler> TYPE = new Type<SelectViewDsdDescriptorHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectViewDsdDescriptorHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, TypeComponentList descriptorType) {
        if (TYPE != null) {
            source.fireEvent(new SelectViewDsdDescriptorEvent(descriptorType));
        }
    }

    private final TypeComponentList descriptorType;

    public SelectViewDsdDescriptorEvent(TypeComponentList descriptorType) {
        this.descriptorType = descriptorType;
    }

    public TypeComponentList getDescriptorType() {
        return descriptorType;
    }

    @Override
    protected void dispatch(SelectViewDsdDescriptorHandler handler) {
        handler.onSelectViewDsdDescriptor(this);
    }

    public static Type<SelectViewDsdDescriptorHandler> getType() {
        return TYPE;
    }

}
