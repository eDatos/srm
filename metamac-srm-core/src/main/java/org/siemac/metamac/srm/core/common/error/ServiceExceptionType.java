package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_PROC_STATUS       = create("exception.srm.concepts.concept_scheme.wrong_proc_status");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_EMPTY_RELATED_OPERATION = create("exception.srm.concepts.concept_scheme.empty_related_operation");

}
