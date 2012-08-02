package org.siemac.metamac.srm.web.dsd.events;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SelectDsdAndDescriptorsEvent extends GwtEvent<SelectDsdAndDescriptorsEvent.SelectDsdAndDescriptorsHandler> {

    public interface SelectDsdAndDescriptorsHandler extends EventHandler {

        void onSelectDsdAndDescriptors(SelectDsdAndDescriptorsEvent event);
    }

    private static Type<SelectDsdAndDescriptorsHandler> TYPE = new Type<SelectDsdAndDescriptorsHandler>();

    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, DataStructureDefinitionDto dataStructureDefinitionDto, DescriptorDto primaryMeasure, DescriptorDto dimensions, DescriptorDto attributes,
            List<DescriptorDto> groupKeys) {
        if (TYPE != null) {
            source.fireEvent(new SelectDsdAndDescriptorsEvent(dataStructureDefinitionDto, primaryMeasure, dimensions, attributes, groupKeys));
        }
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SelectDsdAndDescriptorsHandler> getAssociatedType() {
        return TYPE;
    }

    private final DataStructureDefinitionDto dataStructureDefinitionDto;
    private final DescriptorDto              primaryMeasure;
    private final DescriptorDto              dimensions;
    private final DescriptorDto              attributes;
    private final List<DescriptorDto>        groupKeys;

    public SelectDsdAndDescriptorsEvent(DataStructureDefinitionDto dataStructureDefinitionDto, DescriptorDto primaryMeasure, DescriptorDto dimensions, DescriptorDto attributes,
            List<DescriptorDto> groupKeys) {
        this.dataStructureDefinitionDto = dataStructureDefinitionDto;
        this.primaryMeasure = primaryMeasure;
        this.dimensions = dimensions;
        this.attributes = attributes;
        this.groupKeys = groupKeys;
    }

    public DataStructureDefinitionDto getDataStructureDefinitionDto() {
        return dataStructureDefinitionDto;
    }

    public DescriptorDto getPrimaryMeasure() {
        return primaryMeasure;
    }

    public DescriptorDto getDimensions() {
        return dimensions;
    }

    public DescriptorDto getAttributes() {
        return attributes;
    }

    public List<DescriptorDto> getGroupKeys() {
        return groupKeys;
    }

    @Override
    protected void dispatch(SelectDsdAndDescriptorsHandler handler) {
        handler.onSelectDsdAndDescriptors(this);
    }

    public static Type<SelectDsdAndDescriptorsHandler> getType() {
        return TYPE;
    }

}
