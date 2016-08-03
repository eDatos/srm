package org.siemac.metamac.srm.web.dsd.events;

import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;

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

    public static void fire(HasHandlers source, DsdTabTypeEnum descriptorType) {
        if (TYPE != null) {
            source.fireEvent(new SelectViewDsdDescriptorEvent(descriptorType));
        }
    }

    private final DsdTabTypeEnum descriptorType;

    public SelectViewDsdDescriptorEvent(DsdTabTypeEnum tabType) {
        this.descriptorType = tabType;
    }

    public DsdTabTypeEnum getDsdTabType() {
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
