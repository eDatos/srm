package org.siemac.metamac.srm.core.dsd.serviceimpl;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.DataStructureDefinitionsCopyCallbackImpl;

// @Component() defined in spring xml configuration to set class attributes
public class DataStructureDefinitionsCopyCallbackMetamacImpl extends DataStructureDefinitionsCopyCallbackImpl {

    /****************
     * DSD
     ****************/

    @Override
    public DataStructureDefinitionVersion createStructureVersion() {
        return new DataStructureDefinitionVersionMetamac();
    }

    @Override
    public void copyStructureVersion(DataStructureDefinitionVersion sourceSdmx, DataStructureDefinitionVersion targetSdmx) {
        super.copyStructureVersion(sourceSdmx, targetSdmx);

        DataStructureDefinitionVersionMetamac source = (DataStructureDefinitionVersionMetamac) sourceSdmx;
        DataStructureDefinitionVersionMetamac target = (DataStructureDefinitionVersionMetamac) targetSdmx;

        // Metamac Metadata
        target.setAutoOpen(source.getAutoOpen());
        target.setShowDecimals(source.getShowDecimals());
        // can not copy heading here, because they belong to same dsd, and new dimension in new version must relate to versioned related dimension
        // can not copy stub here, because they belong to same dsd, and new dimension in new version must relate to versioned related dimension

        // showDecimalsPrecisions
        copyShowDecimalsPrecision(source, target);

        target.setStatisticalOperation(copy(source.getStatisticalOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT)); // New structure in draft version
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public void copyShowDecimalsPrecision(DataStructureDefinitionVersion sourceSdmx, DataStructureDefinitionVersion targetSdmx) {
        super.copyShowDecimalsPrecision(sourceSdmx, targetSdmx);

        DataStructureDefinitionVersionMetamac source = (DataStructureDefinitionVersionMetamac) sourceSdmx;
        DataStructureDefinitionVersionMetamac target = (DataStructureDefinitionVersionMetamac) targetSdmx;

        for (MeasureDimensionPrecision measureDimensionPrecision : source.getShowDecimalsPrecisions()) {
            MeasureDimensionPrecision targetMeasureDimensionPrecion = new MeasureDimensionPrecision();
            targetMeasureDimensionPrecion.setConcept(measureDimensionPrecision.getConcept());
            targetMeasureDimensionPrecion.setShowDecimalPrecision(measureDimensionPrecision.getShowDecimalPrecision());
            target.addShowDecimalsPrecision(targetMeasureDimensionPrecion);
        }
    }
}