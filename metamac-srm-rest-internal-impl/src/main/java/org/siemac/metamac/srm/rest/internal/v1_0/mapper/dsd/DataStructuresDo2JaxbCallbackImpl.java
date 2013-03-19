package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import java.util.List;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructuresType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.GroupType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.MeasureDimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.MeasureListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.TimeDimensionType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureDo2JaxbCallback;

@org.springframework.stereotype.Component("dataStructuresDo2JaxbRestInternalCallbackMetamac")
public class DataStructuresDo2JaxbCallbackImpl implements StructureDo2JaxbCallback {

    @Autowired
    private DataStructuresDo2RestMapperV10 dataStructuresDo2RestMapperV10;

    @Override
    public DataStructuresType createDataStructuresJaxb(List<DataStructureDefinitionVersion> sourceList) {
        throw new IllegalArgumentException("createDataStructuresJaxb not supported");
    }

    @Override
    public DataStructureType createDataStructureJaxb(DataStructureDefinitionVersion source) {
        return new org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure();
    }

    @Override
    public void fillDataStructureJaxb(DataStructureDefinitionVersion source, DataStructureType target) {
        dataStructuresDo2RestMapperV10.toDataStructure((DataStructureDefinitionVersionMetamac) source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure) target);
    }

    @Override
    public DimensionListType createDimensionListJaxb(ComponentList source) {
        return new DimensionListType();
    }

    @Override
    public GroupType createGroupJaxb(ComponentList source) {
        return new GroupType();
    }

    @Override
    public MeasureListType createMeasureListJaxb(ComponentList source) {
        return new MeasureListType();
    }

    @Override
    public AttributeListType createAttributeListJaxb(ComponentList source) {
        return new AttributeListType();
    }

    @Override
    public DimensionType createDimensionJaxb(Dimension source) {
        return new DimensionType();
    }

    @Override
    public MeasureDimensionType createMeasureDimensionJaxb(MeasureDimension source) {
        return new MeasureDimensionType();
    }

    @Override
    public TimeDimensionType createTimeDimensionJaxb(TimeDimension source) {
        return new TimeDimensionType();
    }

    @Override
    public AttributeType createAttributeJaxb(DataAttribute source) {
        return new Attribute();
    }

    @Override
    public void fillAttributeJaxb(DataAttribute source, AttributeType target) {
        dataStructuresDo2RestMapperV10.toAttribute(source, (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute) target);
    }
}