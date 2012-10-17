package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Common
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS   = create("exception.srm.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType ITEM_SCHEME_WITHOUT_ITEMS      = create("exception.srm.item_scheme.without_items");

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE      = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND         = create("exception.srm.concepts.concept_type.not_found");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE = create("exception.srm.organisations.organisation_scheme.wrong_type");

    // Security
    public static final CommonServiceExceptionType SECURITY_ACTION_NOT_ALLOWED    = create("exception.srm.security.action_not_allowed");
}
