package org.siemac.metamac.srm.web.dsd.events;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

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

    public static void fire(HasHandlers source, DataStructureDefinitionMetamacDto dataStructureDefinitionDto) {
        if (TYPE != null) {
            source.fireEvent(new SelectDsdEvent(dataStructureDefinitionDto));
        }
    }

    private final DataStructureDefinitionMetamacDto dataStructureDefinitionDto;

    public SelectDsdEvent(DataStructureDefinitionMetamacDto dataStructureDefinitionDto) {
        this.dataStructureDefinitionDto = dataStructureDefinitionDto;
    }

    public DataStructureDefinitionMetamacDto getDataStructureDefinitionDto() {
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
