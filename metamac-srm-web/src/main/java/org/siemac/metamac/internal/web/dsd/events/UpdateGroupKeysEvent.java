package org.siemac.metamac.internal.web.dsd.events;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateGroupKeysEvent extends GwtEvent<UpdateGroupKeysEvent.UpdateGroupKeysHandler> {

    public interface UpdateGroupKeysHandler extends EventHandler {

        void onUpdateGroupKeys(UpdateGroupKeysEvent event);
    }

    private static Type<UpdateGroupKeysHandler> TYPE = new Type<UpdateGroupKeysHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateGroupKeysHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, List<DescriptorDto> groupKeys) {
        if (TYPE != null) {
            source.fireEvent(new UpdateGroupKeysEvent(groupKeys));
        }
    }

    private final List<DescriptorDto> groupKeys;

    public UpdateGroupKeysEvent(List<DescriptorDto> groupKeys) {
        this.groupKeys = groupKeys;
    }

    public List<DescriptorDto> getGroupKeys() {
        return groupKeys;
    }

    @Override
    protected void dispatch(UpdateGroupKeysHandler handler) {
        handler.onUpdateGroupKeys(this);
    }

    public static Type<UpdateGroupKeysHandler> getType() {
        return TYPE;
    }

}
