package org.siemac.metamac.internal.web.dsd.events;

import org.siemac.metamac.domain_dto.DataStructureDefinitionDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateDsdEvent extends GwtEvent<UpdateDsdEvent.UpdateDsdHandler> {

    public interface UpdateDsdHandler extends EventHandler {

        void onUpdateDsd(UpdateDsdEvent event);
    }

    private static Type<UpdateDsdHandler> TYPE = new Type<UpdateDsdHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateDsdHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, DataStructureDefinitionDto dataStructureDefinitionDto) {
        if (TYPE != null) {
            source.fireEvent(new UpdateDsdEvent(dataStructureDefinitionDto));
        }
    }

    private final DataStructureDefinitionDto dataStructureDefinitionDto;

    public UpdateDsdEvent(DataStructureDefinitionDto dataStructureDefinitionDto) {
        this.dataStructureDefinitionDto = dataStructureDefinitionDto;
    }

    public DataStructureDefinitionDto getDataStructureDefinitionDto() {
        return dataStructureDefinitionDto;
    }

    @Override
    protected void dispatch(UpdateDsdHandler handler) {
        handler.onUpdateDsd(this);
    }

    public static Type<UpdateDsdHandler> getType() {
        return TYPE;
    }

}
