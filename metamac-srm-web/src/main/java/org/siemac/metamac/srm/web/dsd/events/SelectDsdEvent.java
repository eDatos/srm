package org.siemac.metamac.internal.web.dsd.events;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectDsdEvent extends GwtEvent<SelectDsdEvent.SelectDsdHandler> {

    public interface SelectDsdHandler extends EventHandler {

        void onSelectDsd(SelectDsdEvent event);
    }

    private static Type<SelectDsdHandler> TYPE = new Type<SelectDsdHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectDsdHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, DataStructureDefinitionDto dataStructureDefinitionDto) {
        if (TYPE != null) {
            source.fireEvent(new SelectDsdEvent(dataStructureDefinitionDto));
        }
    }

    private final DataStructureDefinitionDto dataStructureDefinitionDto;

    public SelectDsdEvent(DataStructureDefinitionDto dataStructureDefinitionDto) {
        this.dataStructureDefinitionDto = dataStructureDefinitionDto;
    }

    public DataStructureDefinitionDto getDataStructureDefinitionDto() {
        return dataStructureDefinitionDto;
    }

    @Override
    protected void dispatch(SelectDsdHandler handler) {
        handler.onSelectDsd(this);
    }

    public static Type<SelectDsdHandler> getType() {
        return TYPE;
    }

}
