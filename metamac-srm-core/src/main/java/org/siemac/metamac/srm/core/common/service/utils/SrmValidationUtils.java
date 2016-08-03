package org.siemac.metamac.srm.core.common.service.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;

public class SrmValidationUtils extends SdmxSrmValidationUtils {

    public static void checkArtefactDiffusionValidation(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, urn, ProcStatusEnum.DIFFUSION_VALIDATION);
    }

    public static void checkArtefactInternallyOrExternallyPublished(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, urn, ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED);
    }

    public static void checkArtefactInternallyPublished(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, urn, ProcStatusEnum.INTERNALLY_PUBLISHED);
    }

    public static void checkArtefactExternallyPublished(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, urn, ProcStatusEnum.EXTERNALLY_PUBLISHED);
    }

    public static void checkArtefactCanBeModified(SrmLifeCycleMetadata lifeCycle, String urn) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, urn, ProcStatusEnum.DRAFT, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION, ProcStatusEnum.VALIDATION_REJECTED);
    }

    public static void checkArtefactCanBeVersioned(MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle, boolean isTemporal) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, maintainableArtefact.getUrn(), ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED);
        SdmxSrmValidationUtils.checkMaintainableArtefactNotImported(maintainableArtefact, isTemporal);
    }

    public static void checkArtefactCanBeVersionedAsTemporal(MaintainableArtefact maintainableArtefact, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        checkArtefactProcStatus(lifeCycle, maintainableArtefact.getUrn(), ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED);
    }

    public static void checkArtefactProcStatus(SrmLifeCycleMetadata lifeCycle, String urn, ProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, lifeCycle.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
    }

    public static MetamacExceptionItem checkArtefactProcStatusReturningExceptionItem(SrmLifeCycleMetadata lifeCycle, String urn, ProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, lifeCycle.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString)
                    .build();
        }
        return null;
    }

    /**
     * An artefact can not change if 1) it is imported 2) it is created, but it is final or it is not first version
     */
    public static void checkMaintainableArtefactCanChangeCodeIfChanged(MaintainableArtefact maintainableArtefact) throws MetamacException {
        if (Boolean.TRUE.equals(maintainableArtefact.getIsCodeUpdated())) {
            if (Boolean.TRUE.equals(maintainableArtefact.getIsImported()) || !SdmxSrmValidationUtils.isMaintainableArtefactNotFinalAndFirstVersion(maintainableArtefact)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE)
                        .build();
            }
        }
    }

    public static void checkNotAlphabeticalOrderVisualisation(CodelistOrderVisualisation orderVisualisation) throws MetamacException {
        if (SrmServiceUtils.isAlphabeticalOrderVisualisation(orderVisualisation)) {
            throw new MetamacException(ServiceExceptionType.CODELIST_ALPHABETICAL_ORDER_OPERATION_NOT_SUPPORTED, orderVisualisation.getNameableArtefact().getUrn());
        }
    }

    public static void checkNotOpenedOpennessVisualisation(CodelistOpennessVisualisation opennessVisualisation) throws MetamacException {
        if (SrmServiceUtils.isAllExpandedOpennessVisualisation(opennessVisualisation)) {
            throw new MetamacException(ServiceExceptionType.CODELIST_ALL_EXPANDED_OPENNESS_VISUALISATION_OPERATION_NOT_SUPPORTED, opennessVisualisation.getNameableArtefact().getUrn());
        }
    }

    public static void checkVariableIsGeographical(Variable variable) throws MetamacException {
        if (!VariableTypeEnum.GEOGRAPHICAL.equals(variable.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_MUST_BE_GEOGRAPHICAL).withMessageParameters(variable.getNameableArtefact().getUrn()).build();
        }
    }

    /**
     * Do not check required metadata when it is a new artefact and it is imported. It will checked when update ItemScheme or Item, or when send to production validation
     */
    public static boolean mustValidateMetadataRequired(ItemSchemeVersion itemSchemeVersion, boolean creating) {
        if (creating && BooleanUtils.isTrue(itemSchemeVersion.getMaintainableArtefact().getIsImported())) {
            // false it it is imported, because required metadata can not be filled automatically
            return false;
        }
        // creating not imported, updating...
        return true;
    }
}