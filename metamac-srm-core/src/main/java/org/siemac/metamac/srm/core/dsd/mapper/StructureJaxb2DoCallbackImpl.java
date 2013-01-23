package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.DsdsMetamacService;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DsdsMetamacService dsdsMetamacService;

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
    public void dataStructureDefinitionJaxbToDoExtension(ServiceContext ctx, DataStructureType source, DataStructureDefinitionVersion target) throws MetamacException {
        DataStructureDefinitionVersionMetamac targetMetamac = (DataStructureDefinitionVersionMetamac) target;

        // Fill Meta-data
        dsdsMetamacService.preCreateDataStructureDefinition(ctx, targetMetamac);
        targetMetamac.getMaintainableArtefact().setFinalLogic(Boolean.FALSE); // In Metamac, all artifacts imported are marked as final false

        // TODO Completar con herencia en metadatos, primero AVISAR A SAN para ver si el completado de metadatos de la versión
        // anterior se puede hacer antes o después del precreate para hacer cosas como sólo t dejo q sean vacíos si es la primera versión
        // en el resto los deberíashaber copiado, y por tanto se comprueban las restricciones. Ver si se fastidiase algo si lo hago ANTES y
        // comentarlo con SAN.
    }

    @Override
    public void dimensionDescriptorJaxbToDoExtension(ServiceContext ctx, DimensionListType source, DimensionDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void measureDescriptorJaxbToDoExtension(ServiceContext ctx, MeasureListType source, MeasureDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void groupDimensionDescriptorJaxbToDoExtension(ServiceContext ctx, GroupType source, GroupDimensionDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void attributeDescriptorJaxb2DoExtension(ServiceContext ctx, AttributeListType source, AttributeDescriptor target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void dimensionJaxbToDoExtension(ServiceContext ctx, DimensionType source, Dimension target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void measureDimensionJaxbToDoExtension(ServiceContext ctx, MeasureDimensionType source, MeasureDimension target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void timeDimensionJaxbToDoExtension(ServiceContext ctx, TimeDimensionType source, TimeDimension target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void primaryMeasureJaxbToDoExtension(ServiceContext ctx, PrimaryMeasureType source, PrimaryMeasure target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void dataAttributeJaxbToDoExtension(ServiceContext ctx, AttributeType source, DataAttribute target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    @Override
    public void reportingYearStartDayJaxbToDoExtension(ServiceContext ctx, ReportingYearStartDayType source, ReportingYearStartDay target) throws MetamacException {
        // Meta-data in previous version -> Nothing to extends
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(ServiceContext ctx, DataStructureDefinitionVersion source) throws MetamacException {
        validateRestrictionsGeneral(ctx, source);
    }

}
