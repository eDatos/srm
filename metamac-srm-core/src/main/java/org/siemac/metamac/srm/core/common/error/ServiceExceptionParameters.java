package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

public class ServiceExceptionParameters extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters {

    // CONCEPT_SCHEME
    public static final String CONCEPT_SCHEME_CODE               = CONCEPT_SCHEME + ".code";
    public static final String CONCEPT_SCHEME_TYPE               = CONCEPT_SCHEME + ".type";
    public static final String CONCEPT_SCHEME_RELATED_OPERATION  = CONCEPT_SCHEME + ".related_operation";

    // LIFECYCLE
    public static final String PROC_STATUS_DRAFT                 = ItemSchemeMetamacProcStatusEnum.DRAFT.name();
    public static final String PROC_STATUS_PRODUCTION_VALIDATION = ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.name();
    public static final String PROC_STATUS_DIFFUSION_VALIDATION  = ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.name();
    public static final String PROC_STATUS_VALIDATION_REJECTED   = ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED.name();
    public static final String PROC_STATUS_INTERNALLY_PUBLISHED  = ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.name();
    public static final String PROC_STATUS_EXTERNALLY_PUBLISHED  = ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.name();
}
