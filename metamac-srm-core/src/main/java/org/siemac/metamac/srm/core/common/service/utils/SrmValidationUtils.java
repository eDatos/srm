package org.siemac.metamac.srm.core.common.service.utils;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;

public class SrmValidationUtils {

    public static boolean isInternationalStringEmpty(InternationalString internationalString) {
        if (internationalString == null) {
            return true;
        }
        if (internationalString.getTexts().size() == 0) {
            return true;
        }
        for (LocalisedString localisedString : internationalString.getTexts()) {
            if (StringUtils.isEmpty(localisedString.getLabel()) || StringUtils.isEmpty(localisedString.getLocale())) {
                return true;
            }
        }
        return false;
    }

    public static void checkExternallyPublished(String urn, SrmLifeCycleMetadata lifeCycle) throws MetamacException {
        if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(lifeCycle.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(ProcStatusEnum.EXTERNALLY_PUBLISHED);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
    }

    public static void checkArtefactCanBeModified(SrmLifeCycleMetadata lifeCycle, String urn) throws MetamacException {
        checkProcStatus(lifeCycle, urn, ProcStatusEnum.DRAFT, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION, ProcStatusEnum.VALIDATION_REJECTED);
    }

    public static void checkArtefactCanBeVersioned(SrmLifeCycleMetadata lifeCycle, String urn) throws MetamacException {
        checkProcStatus(lifeCycle, urn, ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED);
    }

    public static void checkProcStatus(SrmLifeCycleMetadata lifeCycle, String urn, ProcStatusEnum... procStatus) throws MetamacException {
        if (!ArrayUtils.contains(procStatus, lifeCycle.getProcStatus())) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatus);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();
        }
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

    /**
     * Returns TRUE if the variable with the identifier variableIdentifier is in the variable list
     * 
     * @param variableIdentifier
     * @param variables
     * @return
     */
    public static boolean isVariableInList(String variableIdentifier, List<Variable> variables) {
        for (Variable variable : variables) {
            if (StringUtils.equals(variableIdentifier, variable.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns TRUE if the family with the identifier familyIdentifier is in the famkily list
     * 
     * @param familyIdentifier
     * @param families
     * @return
     */
    public static boolean isFamilyInList(String familyIdentifier, List<VariableFamily> families) {
        for (VariableFamily family : families) {
            if (StringUtils.equals(familyIdentifier, family.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns TRUE if the codelist with the URN codelistUrn is in the codelist list
     * 
     * @param codelistUrn
     * @param codelists
     * @return
     */
    public static boolean isCodelistInList(String codelistUrn, List<CodelistVersionMetamac> codelists) {
        for (CodelistVersionMetamac codelist : codelists) {
            if (StringUtils.equals(codelistUrn, codelist.getMaintainableArtefact().getUrn())) {
                return true;
            }
        }
        return false;
    }
}