package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.springframework.stereotype.Component;

@Component
public class BaseRest2DoMapperV10Impl implements BaseRest2DoMapperV10 {


    public static RestException toRestExceptionParameterIncorrect(String parameter) {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameter);
        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }

    public static Date propertyRestrictionValueToDate(String value) {
        return value != null ? CoreCommonUtil.transformISODateTimeLexicalRepresentationToDateTime(value).toDate() : null;
    }

    public static ProcStatusEnum propertyRestrictionValueToProcStatusEnum(String value) {
        return value != null ? ProcStatusEnum.valueOf(value) : null;
    }

    @SuppressWarnings("rawtypes")
    public static SculptorPropertyCriteria getSculptorPropertyCriteriaDate(MetamacRestQueryPropertyRestriction propertyRestriction, Property propertyEntity) {
        return new SculptorPropertyCriteria(new LeafProperty<CategorySchemeVersionMetamac>(propertyEntity.getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true,
                CategorySchemeVersionMetamac.class), propertyRestrictionValueToDate(propertyRestriction.getValue()));
    }
}