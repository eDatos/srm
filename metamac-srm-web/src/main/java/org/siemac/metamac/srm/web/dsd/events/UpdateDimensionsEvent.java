package org.siemac.metamac.srm.web.dsd.events;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateDimensionsEvent extends GwtEvent<UpdateDimensionsEvent.UpdateDimensionsHandler> {

    public interface UpdateDimensionsHandler extends EventHandler {

        void onUpdateDimensions(UpdateDimensionsEvent event);
    }

    private static Type<UpdateDimensionsHandler> TYPE = new Type<UpdateDimensionsHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateDimensionsHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, List<DimensionComponentDto> dimensionComponentDtos) {
        if (TYPE != null) {
            source.fireEvent(new UpdateDimensionsEvent(dimensionComponentDtos));
        }
    }

    private final List<DimensionComponentDto> dimensionComponentDtos;

    public UpdateDimensionsEvent(List<DimensionComponentDto> dimensionComponentDtos) {
        this.dimensionComponentDtos = dimensionComponentDtos;
    }

    public List<DimensionComponentDto> getDimensionComponentDtos() {
        return dimensionComponentDtos;
    }

    @Override
    protected void dispatch(UpdateDimensionsHandler handler) {
        handler.onUpdateDimensions(this);
    }

    public static Type<UpdateDimensionsHandler> getType() {
        return TYPE;
    }

}
