package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_PROC_STATUS = create("exception.srm.concepts.concept_scheme.wrong_proc_status");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND           = create("exception.srm.concepts.concept_type.not_found");
    public static final CommonServiceExceptionType CONCEPT_WITH_RELATED_CONCEPTS    = create("exception.srm.concepts.related_concepts");
    public static final CommonServiceExceptionType SECURITY_ACTION_NOT_ALLOWED      = create("exception.srm.security.action_not_allowed");
}
