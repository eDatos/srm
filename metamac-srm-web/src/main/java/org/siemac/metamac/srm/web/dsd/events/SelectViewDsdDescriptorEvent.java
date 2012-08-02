package org.siemac.metamac.srm.web.dsd.events;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
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
