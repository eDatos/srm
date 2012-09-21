package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class ServiceExceptionParameters extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters {

    public static final String IDENTIFIER                        = "identifier";

    // CONCEPT_SCHEME
    public static final String CONCEPT_SCHEME_CODE               = CONCEPT_SCHEME + ".code";
    public static final String CONCEPT_SCHEME_TYPE               = CONCEPT_SCHEME + ".type";
    public static final String CONCEPT_SCHEME_RELATED_OPERATION  = CONCEPT_SCHEME + ".related_operation";
    // TYPES
    public static final String CONCEPT_SCHEME_TYPE_GLOSSARY      = ConceptSchemeTypeEnum.GLOSSARY.name();
    public static final String CONCEPT_SCHEME_TYPE_ROLE          = ConceptSchemeTypeEnum.ROLE.name();
    public static final String CONCEPT_SCHEME_TYPE_OPERATION     = ConceptSchemeTypeEnum.OPERATION.name();
    public static final String CONCEPT_SCHEME_TYPE_TRANSVERSAL   = ConceptSchemeTypeEnum.TRANSVERSAL.name();

    // CONCEPT
    public static final String CONCEPT_PLURAL_NAME               = CONCEPT + ".plural_name";
    public static final String CONCEPT_ACRONYM                   = CONCEPT + ".acronym";
    public static final String CONCEPT_DESCRIPTION_SOURCE        = CONCEPT + ".description_source";
    public static final String CONCEPT_CONTEXT                   = CONCEPT + ".context";
    public static final String CONCEPT_DOC_METHOD                = CONCEPT + ".doc_method";
    public static final String CONCEPT_SDMX_RELATED_ARTEFACT     = CONCEPT + ".sdmx_related_artefact";
    public static final String CONCEPT_DERIVATION                = CONCEPT + ".derivation";
    public static final String CONCEPT_LEGAL_ACTS                = CONCEPT + ".legal_acts";
    public static final String CONCEPT_EXTENDS                   = CONCEPT + ".extends";

    // LIFECYCLE
    public static final String PROC_STATUS_DRAFT                 = ProcStatusEnum.DRAFT.name();
    public static final String PROC_STATUS_PRODUCTION_VALIDATION = ProcStatusEnum.PRODUCTION_VALIDATION.name();
    public static final String PROC_STATUS_DIFFUSION_VALIDATION  = ProcStatusEnum.DIFFUSION_VALIDATION.name();
    public static final String PROC_STATUS_VALIDATION_REJECTED   = ProcStatusEnum.VALIDATION_REJECTED.name();
    public static final String PROC_STATUS_INTERNALLY_PUBLISHED  = ProcStatusEnum.INTERNALLY_PUBLISHED.name();
    public static final String PROC_STATUS_EXTERNALLY_PUBLISHED  = ProcStatusEnum.EXTERNALLY_PUBLISHED.name();
}
