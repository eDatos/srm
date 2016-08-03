package org.siemac.metamac.srm.core.dsd.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.DataStructureDefinitionsCopyCallbackImpl;

// @Component() defined in spring xml configuration to set class attributes
public class DataStructureDefinitionsCopyCallbackMetamacImpl extends DataStructureDefinitionsCopyCallbackImpl {

    @Autowired
    private DataStructureDefinitionMetamacService dataStructureDefinitionMetamacService;

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
        // can not copy showDecimalsPrecisions here

        target.setStatisticalOperation(copy(source.getStatisticalOperation()));
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT)); // New structure in draft version
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
    }

    @Override
    public void createStructureVersionRelations(ServiceContext ctx, DataStructureDefinitionVersion sourceStructure, DataStructureDefinitionVersion targetStructure) throws MetamacException {
        super.createStructureVersionRelations(ctx, sourceStructure, targetStructure);

        DataStructureDefinitionVersionMetamac source = (DataStructureDefinitionVersionMetamac) sourceStructure;
        DataStructureDefinitionVersionMetamac target = (DataStructureDefinitionVersionMetamac) targetStructure;

        target = dataStructureDefinitionMetamacService.versioningHeadingAndStub(ctx, source, target);
        target = dataStructureDefinitionMetamacService.versioningShowDecimalsPrecision(ctx, source, target);
        target = dataStructureDefinitionMetamacService.versioningDimensionVisualisationInfo(ctx, source, target);
    }
}