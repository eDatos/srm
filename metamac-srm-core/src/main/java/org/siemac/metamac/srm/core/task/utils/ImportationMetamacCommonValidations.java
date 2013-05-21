package org.siemac.metamac.srm.core.task.utils;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;

public abstract class ImportationMetamacCommonValidations {

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    private StructureVersionRepository  structureVersionRepository;

    /**
     * Besides this, other validations are performed in the methods preCreate of services.
     * 
     * @param ctx
     * @param previous
     * @param source
     * @param canBeNotFinal
     * @throws MetamacException
     */
    public void validateRestrictionsItemSchemeVersion(ServiceContext ctx, ItemSchemeVersion source, boolean canBeNotFinal) throws MetamacException {

        validateRestrictionsMaintainableArtefact(ctx, source.getMaintainableArtefact(), canBeNotFinal);

        if (!canBeNotFinal && !BooleanUtils.isTrue(source.getMaintainableArtefact().getIsImported())) {
            // Check does not exist any version 'no final'
            ItemSchemeVersion itemSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer(), source
                    .getMaintainableArtefact().getCode());
            if (itemSchemeVersionNoFinal != null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_EXIST_NOT_FINAL_VERSION)
                        .withMessageParameters(itemSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
            }
        }

        // Check: Unable to import artifacts with value of TRUE in the field isPartial.
        if (BooleanUtils.isTrue(source.getIsPartial())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL).build();
        }
    }

    /**
     * Besides this, other validations are performed in the methods preCreate of services.
     * 
     * @param ctx
     * @param previous
     * @param source
     * @param canBeNotFinal
     * @throws MetamacException
     */
    public void validateRestrictionsStructureVersionVersion(ServiceContext ctx, StructureVersion source) throws MetamacException {

        validateRestrictionsMaintainableArtefact(ctx, source.getMaintainableArtefact(), false);

        // Check does not exist any version 'no final'
        StructureVersion structureVersionNoFinal = structureVersionRepository.findStructureVersionNoFinalClient(source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer(), source
                .getMaintainableArtefact().getCode());
        if (structureVersionNoFinal != null && !BooleanUtils.isTrue(source.getMaintainableArtefact().getIsImported())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_EXIST_NOT_FINAL_VERSION)
                    .withMessageParameters(structureVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    /**
     * Besides this, other validations are performed in the methods preCreate of services.
     * 
     * @param <T>
     * @param ctx
     * @param source
     * @param canBeNotFinal
     * @throws MetamacException
     */
    public <T extends MaintainableArtefact> void validateRestrictionsCategorisation(ServiceContext ctx, T source, boolean canBeNotFinal) throws MetamacException {
        validateRestrictionsMaintainableArtefact(ctx, source, canBeNotFinal);
    }

    /**
     * Besides this, other validations are performed in the methods preCreate of services.
     * 
     * @param <T>
     * @param ctx
     * @param source
     * @param canBeNotFinal
     * @throws MetamacException
     */
    private <T extends MaintainableArtefact> void validateRestrictionsMaintainableArtefact(ServiceContext ctx, T source, boolean canBeNotFinal) throws MetamacException {
        // Check: All artifacts imported in METAMAC must be FINAL unless AgencySchemes, DataProviderSchemes and DataConsumerSchemes.
        if (!source.getFinalLogic() && !canBeNotFinal && !BooleanUtils.isTrue(source.getIsImported())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_FINAL_LOGIC)
                    .build();
        }

        // Check: Unable to import artifacts with value of TRUE in the field isExternalReference.
        if (BooleanUtils.isTrue(source.getIsExternalReference())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                    .withMessageParameters(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE).build();
        }

    }
}
