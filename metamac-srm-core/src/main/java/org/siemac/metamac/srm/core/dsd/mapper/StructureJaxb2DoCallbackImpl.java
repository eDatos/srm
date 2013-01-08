package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure;
import com.arte.statistic.sdmx.srm.core.structure.domain.ReportingYearStartDay;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AttributeListType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AttributeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataStructureType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataStructuresType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DimensionListType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DimensionType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.GroupType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.MeasureDimensionType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.MeasureListType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.PrimaryMeasureType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ReportingYearStartDayType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.TimeDimensionType;

@Component("structureMetamacJaxb2DoCallback")
public class StructureJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements StructureJaxb2DoCallback {

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public List<DataStructureDefinitionVersion> createDataStructuresDo(DataStructuresType source) {
        List<DataStructureDefinitionVersion> dataStructureDefinitionVersionList = new ArrayList<DataStructureDefinitionVersion>();

        return dataStructureDefinitionVersionList;
    }

    @Override
    public DataStructureDefinitionVersion createDataStructureDo(DataStructureType source) {
        DataStructureDefinitionVersion dataStructureDefinitionVersion = new DataStructureDefinitionVersionMetamac();

        return dataStructureDefinitionVersion;
    }

    @Override
    public DimensionDescriptor createDimensionDescriptorDo(DimensionListType source) {
        DimensionDescriptor dimensionDescriptor = new DimensionDescriptor();

        return dimensionDescriptor;
    }

    @Override
    public GroupDimensionDescriptor createGroupDescriptorDo(GroupType source) {
        GroupDimensionDescriptor groupDimensionDescriptor = new GroupDimensionDescriptor();

        return groupDimensionDescriptor;
    }

    @Override
    public MeasureDescriptor createMeasureDescriptorDo(MeasureListType source) {
        MeasureDescriptor measureDescriptor = new MeasureDescriptor();

        return measureDescriptor;
    }

    @Override
    public AttributeDescriptor createAttributeDescriptorDo(AttributeListType source) {
        AttributeDescriptor attributeDescriptor = new AttributeDescriptor();

        return attributeDescriptor;
    }

    @Override
    public Dimension createDimensionDo(DimensionType source) {
        Dimension dimension = new Dimension();

        return dimension;
    }

    @Override
    public MeasureDimension createMeasureDimensionDo(MeasureDimensionType source) {
        MeasureDimension measureDimension = new MeasureDimension();

        return measureDimension;
    }

    @Override
    public TimeDimension createTimeDimensionDo(TimeDimensionType source) {
        TimeDimension timeDimension = new TimeDimension();

        return timeDimension;
    }

    @Override
    public PrimaryMeasure createPrimaryMeasureDo(PrimaryMeasureType source) {
        PrimaryMeasure primaryMeasure = new PrimaryMeasure();

        return primaryMeasure;
    }

    @Override
    public ReportingYearStartDay createReportingYearStartDayDo(ReportingYearStartDayType source) {
        ReportingYearStartDay reportingYearStartDay = new ReportingYearStartDay();

        return reportingYearStartDay;
    }

    @Override
    public DataAttribute createDataAttribute(AttributeType source) {
        DataAttribute dataAttribute = new DataAttribute();

        return dataAttribute;
    }

    /**************************************************************************
     * FILLS
     **************************************************************************/
    @Override
    public void dataStructureDefinitionJaxbToDoExtension(DataStructureType source, DataStructureDefinitionVersion target) {
        // TODO Completar METAMAC dataStructureDefinitionJaxbToDoExtension
        int a = 23;
    }

    @Override
    public void dimensionDescriptorJaxbToDoExtension(DimensionListType source, DimensionDescriptor target) {
        // TODO Completar METAMAC dimensionDescriptorJaxbToDoExtension
    }

    @Override
    public void measureDescriptorJaxbToDoExtension(MeasureListType source, MeasureDescriptor target) {
        // TODO Completar METAMAC measureDescriptorJaxbToDoExtension
    }

    @Override
    public void groupDimensionDescriptorJaxbToDoExtension(GroupType source, GroupDimensionDescriptor target) {
        // TODO Completar METAMAC groupDimensionDescriptorJaxbToDoExtension
    }

    @Override
    public void attributeDescriptorJaxb2DoExtension(AttributeListType source, AttributeDescriptor target) {
        // TODO Completar METAMAC attributeDescriptorJaxb2DoExtension
    }

    @Override
    public void dimensionJaxbToDoExtension(DimensionType source, Dimension target) {
        // TODO Completar METAMAC dimensionJaxbToDoExtension
    }

    @Override
    public void measureDimensionJaxbToDoExtension(MeasureDimensionType source, MeasureDimension target) {
        // TODO Completar METAMAC measureDimensionJaxbToDoExtension
    }

    @Override
    public void timeDimensionJaxbToDoExtension(TimeDimensionType source, TimeDimension target) {
        // TODO Completar METAMAC timeDimensionJaxbToDoExtension
    }

    @Override
    public void primaryMeasureJaxbToDoExtension(PrimaryMeasureType source, PrimaryMeasure target) {
        // TODO Completar METAMAC primaryMeasureJaxbToDoExtension
    }

    @Override
    public void dataAttributeJaxbToDoExtension(AttributeType source, DataAttribute target) {
        // TODO Completar METAMAC dataAttributeJaxbToDoExtension
    }

    @Override
    public void reportingYearStartDayJaxbToDoExtension(ReportingYearStartDayType source, ReportingYearStartDay target) {
        // TODO Completar METAMAC reportingYearStartDayJaxbToDoExtension
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, DataStructureDefinitionVersion source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
    }

}
