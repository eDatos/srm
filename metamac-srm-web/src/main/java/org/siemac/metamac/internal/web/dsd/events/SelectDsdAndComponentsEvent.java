package org.siemac.metamac.internal.web.dsd.events;

import java.util.List;

import org.siemac.metamac.domain_dto.ComponentDto;
import org.siemac.metamac.domain_dto.DataAttributeDto;
import org.siemac.metamac.domain_dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain_dto.DescriptorDto;
import org.siemac.metamac.domain_dto.DimensionComponentDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectDsdAndComponentsEvent extends GwtEvent<SelectDsdAndComponentsEvent.SelectDsdAndComponentsHandler> {

    public interface SelectDsdAndComponentsHandler extends EventHandler {

        void onSelectDsdAndComponents(SelectDsdAndComponentsEvent event);
    }

    private static Type<SelectDsdAndComponentsHandler> TYPE = new Type<SelectDsdAndComponentsHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectDsdAndComponentsHandler> getAssociatedType() {
        return TYPE;
    }

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, DataStructureDefinitionDto dataStructureDefinitionDto, ComponentDto primaryMeasure, List<DimensionComponentDto> dimensionComponentDtos,
            List<DataAttributeDto> dataAttributeDtos, List<DescriptorDto> groupKeys) {
        if (TYPE != null) {
            source.fireEvent(new SelectDsdAndComponentsEvent(dataStructureDefinitionDto, primaryMeasure, dimensionComponentDtos, dataAttributeDtos, groupKeys));
        }
    }

    private final DataStructureDefinitionDto  dataStructureDefinitionDto;
    private final ComponentDto                primaryMeasure;
    private final List<DimensionComponentDto> dimensionComponentDtos;
    private final List<DataAttributeDto>      dataAttributeDtos;
    private final List<DescriptorDto>         groupKeys;

    public SelectDsdAndComponentsEvent(DataStructureDefinitionDto dataStructureDefinitionDto, ComponentDto primaryMeasure, List<DimensionComponentDto> dimensionComponentDtos,
            List<DataAttributeDto> dataAttributeDtos, List<DescriptorDto> groupKeys) {
        this.dataStructureDefinitionDto = dataStructureDefinitionDto;
        this.primaryMeasure = primaryMeasure;
        this.dimensionComponentDtos = dimensionComponentDtos;
        this.dataAttributeDtos = dataAttributeDtos;
        this.groupKeys = groupKeys;
    }

    public DataStructureDefinitionDto getDataStructureDefinitionDto() {
        return dataStructureDefinitionDto;
    }

    public ComponentDto getPrimaryMeasure() {
        return primaryMeasure;
    }

    public List<DimensionComponentDto> getDimensionComponentDtos() {
        return dimensionComponentDtos;
    }

    public List<DataAttributeDto> getDataAttributeDtos() {
        return dataAttributeDtos;
    }

    public List<DescriptorDto> getGroupKeys() {
        return groupKeys;
    }

    @Override
    protected void dispatch(SelectDsdAndComponentsHandler handler) {
        handler.onSelectDsdAndComponents(this);
    }

    public static Type<SelectDsdAndComponentsHandler> getType() {
        return TYPE;
    }

}
