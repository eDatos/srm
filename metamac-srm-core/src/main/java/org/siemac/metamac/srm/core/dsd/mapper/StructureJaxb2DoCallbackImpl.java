package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AttributeType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataStructuresType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.GroupType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.MeasureDimensionType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.MeasureListType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.PrimaryMeasureType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.ReportingYearStartDayType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.TimeDimensionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.core.task.utils.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseJaxb2DoInheritUtils;
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

@Component("structureMetamacJaxb2DoCallback")
public class StructureJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements StructureJaxb2DoCallback {

    @Autowired
    private DataStructureDefinitionMetamacService dataStructureDefinitionMetamacService;

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
    public void dataStructureDefinitionJaxbToDoExtensionPreCreate(ServiceContext ctx, DataStructureType source, DataStructureDefinitionVersion previous, DataStructureDefinitionVersion target)
            throws MetamacException {
        DataStructureDefinitionVersionMetamac previousMetamac = (DataStructureDefinitionVersionMetamac) previous;
        DataStructureDefinitionVersionMetamac targetMetamac = (DataStructureDefinitionVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            targetMetamac.getMaintainableArtefact().setName(
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getMaintainableArtefact().getName(), targetMetamac.getMaintainableArtefact().getName())); // Name
            targetMetamac.getMaintainableArtefact().setDescription(
                    BaseJaxb2DoInheritUtils.inheritInternationalStringAsNew(previousMetamac.getMaintainableArtefact().getDescription(), targetMetamac.getMaintainableArtefact().getDescription())); // Description
            BaseJaxb2DoInheritUtils.inheritAnnotations(previousMetamac.getMaintainableArtefact().getAnnotations(), targetMetamac.getMaintainableArtefact().getAnnotations()); // Annotations

            targetMetamac.setStatisticalOperation(BaseCopyAllMetadataUtils.copy(previousMetamac.getStatisticalOperation()));

            // Visualization Metadata
            targetMetamac.setAutoOpen(previousMetamac.getAutoOpen());
            targetMetamac.setShowDecimals(previousMetamac.getShowDecimals());
            // can not copy heading here, because they belong to same dsd, and new dimension in new version must relate to versioned related dimension
            // can not copy stub here, because they belong to same dsd, and new dimension in new version must relate to versioned related dimension
            // can not copy showDecimalsPrecisions here
        }

        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // Validate and complete fill
        dataStructureDefinitionMetamacService.preCreateDataStructureDefinition(ctx, targetMetamac);
    }

    @Override
    public void dataStructureDefinitionJaxbToDoExtensionPostCreate(ServiceContext ctx, DataStructureType source, DataStructureDefinitionVersion previous, DataStructureDefinitionVersion target)
            throws MetamacException {
        DataStructureDefinitionVersionMetamac previousMetamac = (DataStructureDefinitionVersionMetamac) previous;
        DataStructureDefinitionVersionMetamac targetMetamac = (DataStructureDefinitionVersionMetamac) target;

        // Fill metadata heritable
        if (previousMetamac != null) {
            // Heading and Stub
            targetMetamac = dataStructureDefinitionMetamacService.versioningHeadingAndStub(ctx, previousMetamac, targetMetamac);

            // ShowDecimalsPRecicions
            targetMetamac = dataStructureDefinitionMetamacService.versioningShowDecimalsPrecision(ctx, previousMetamac, targetMetamac);

            // DimensionVisualisationInfo
            targetMetamac = dataStructureDefinitionMetamacService.versioningDimensionVisualisationInfo(ctx, previousMetamac, targetMetamac);
        }

    }

    @Override
    public void dimensionDescriptorJaxbToDoExtension(ServiceContext ctx, DimensionListType source, DimensionDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends

        // In Metamac the annotations of descriptors aren't imported (METAMAC-1577)
        target.removeAllAnnotations();
    }

    @Override
    public void measureDescriptorJaxbToDoExtension(ServiceContext ctx, MeasureListType source, MeasureDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends

        // In Metamac the annotations of descriptors aren't imported (METAMAC-1577)
        target.removeAllAnnotations();
    }
    @Override
    public void groupDimensionDescriptorJaxbToDoExtension(ServiceContext ctx, GroupType source, GroupDimensionDescriptor previous, GroupDimensionDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends

        // In Metamac the annotations of descriptors aren't imported (METAMAC-1577)
        target.removeAllAnnotations();
    }

    @Override
    public void attributeDescriptorJaxb2DoExtension(ServiceContext ctx, AttributeListType source, AttributeDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends

        // In Metamac the annotations of descriptors aren't imported (METAMAC-1577)
        target.removeAllAnnotations();
    }

    @Override
    public void dimensionJaxbToDoExtension(ServiceContext ctx, DimensionType source, Dimension previous, Dimension target) throws MetamacException {
        if (previous != null) {
            target.setSpecialDimensionType(previous.getSpecialDimensionType());

            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    @Override
    public void measureDimensionJaxbToDoExtension(ServiceContext ctx, MeasureDimensionType source, MeasureDimension previous, MeasureDimension target) throws MetamacException {
        if (previous != null) {
            target.setSpecialDimensionType(previous.getSpecialDimensionType());

            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    @Override
    public void timeDimensionJaxbToDoExtension(ServiceContext ctx, TimeDimensionType source, TimeDimension previous, TimeDimension target) throws MetamacException {
        if (previous != null) {
            target.setSpecialDimensionType(previous.getSpecialDimensionType());

            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    @Override
    public void primaryMeasureJaxbToDoExtension(ServiceContext ctx, PrimaryMeasureType source, PrimaryMeasure previous, PrimaryMeasure target) throws MetamacException {
        if (previous != null) {
            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    @Override
    public void dataAttributeJaxbToDoExtension(ServiceContext ctx, AttributeType source, DataAttribute previous, DataAttribute target) throws MetamacException {
        if (previous != null) {
            target.setSpecialAttributeType(previous.getSpecialAttributeType());

            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    @Override
    public void reportingYearStartDayJaxbToDoExtension(ServiceContext ctx, ReportingYearStartDayType source, ReportingYearStartDay previous, ReportingYearStartDay target) throws MetamacException {
        if (previous != null) {
            target.setSpecialAttributeType(previous.getSpecialAttributeType());

            // Inherit translations (for all international strings)
            BaseJaxb2DoInheritUtils.inheritAnnotations(previous.getAnnotations(), target.getAnnotations()); // Annotations
        }
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, DataStructureDefinitionVersion source) throws MetamacException {
        validateRestrictionsStructureVersionVersion(ctx, source);
    }
}
