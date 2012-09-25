package org.siemac.metamac.srm.core.dsd.serviceimpl.utils;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.NoSpecifiedRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasureRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.ReportingYearStartDay;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.StructureDoCopyUtils.StructureCopyCallback;

@Component("structureCopyCallback")
public class StructureCopyCallbackMetamacImpl implements StructureCopyCallback {

    /****************
     * DSD
     ****************/

    @Override
    public DataStructureDefinitionVersion copyDataStructureDefinitionVersion(DataStructureDefinitionVersion source) {
        DataStructureDefinitionVersionMetamac sourceMetamac = (DataStructureDefinitionVersionMetamac) source;

        DataStructureDefinitionVersionMetamac target = new DataStructureDefinitionVersionMetamac();

        // Metamac Metadata
        target.setLifecycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT)); // New structure in draft version

        return target;
    }

    /***************
     * Descriptors
     ***************/

    @Override
    public AttributeDescriptor copyAttributeDescriptor(AttributeDescriptor source) {
        AttributeDescriptor target = new AttributeDescriptor();
        return target;
    }

    @Override
    public DimensionDescriptor copyDimensioDescriptor(DimensionDescriptor source) {
        DimensionDescriptor target = new DimensionDescriptor();
        return target;
    }

    @Override
    public GroupDimensionDescriptor copyGroupDimensionDescriptor(GroupDimensionDescriptor source) {
        GroupDimensionDescriptor target = new GroupDimensionDescriptor();
        return target;
    }

    @Override
    public MeasureDescriptor copyMeasureDescriptor(MeasureDescriptor source) {
        MeasureDescriptor target = new MeasureDescriptor();
        return target;
    }

    /*************
     * Components
     *************/

    @Override
    public DataAttribute copyDataAttribute(DataAttribute source) {
        DataAttribute target = new DataAttribute();
        return target;
    }

    @Override
    public ReportingYearStartDay copyReportingYearStartDay(ReportingYearStartDay source) {
        ReportingYearStartDay target = new ReportingYearStartDay();
        return target;
    }

    @Override
    public Dimension copyDimension(Dimension source) {
        Dimension target = new Dimension();
        return target;
    }

    @Override
    public MeasureDimension copyMeasureDimension(MeasureDimension source) {
        MeasureDimension target = new MeasureDimension();
        return target;
    }

    @Override
    public TimeDimension copyTimeDimension(TimeDimension source) {
        TimeDimension target = new TimeDimension();
        return target;
    }

    @Override
    public PrimaryMeasure copyPrimaryMeasure(PrimaryMeasure source) {
        PrimaryMeasure target = new PrimaryMeasure();
        return target;
    }

    /*************
     * AttributeRelationships
     *************/
    @Override
    public DimensionRelationship copyDimensionRelationship(DimensionRelationship source) {
        DimensionRelationship target = new DimensionRelationship();
        return target;
    }

    @Override
    public GroupRelationship copyGroupRelationship(GroupRelationship source) {
        GroupRelationship target = new GroupRelationship();
        return target;
    }

    @Override
    public NoSpecifiedRelationship copyNoSpecifiedRelationship(NoSpecifiedRelationship source) {
        NoSpecifiedRelationship target = new NoSpecifiedRelationship();
        return target;
    }

    @Override
    public PrimaryMeasureRelationship copyPrimaryMeasureRelationship(PrimaryMeasureRelationship source) {
        PrimaryMeasureRelationship target = new PrimaryMeasureRelationship();
        return target;
    }

}