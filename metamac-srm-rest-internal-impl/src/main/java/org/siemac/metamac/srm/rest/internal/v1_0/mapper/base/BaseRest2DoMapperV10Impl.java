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
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;

public abstract class BaseRest2DoMapperV10Impl {

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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SculptorPropertyCriteria getSculptorPropertyCriteriaDate(MetamacRestQueryPropertyRestriction propertyRestriction, Property propertyEntity, Class entity, boolean embedded) {
        String propertyName = null;
        if (embedded) {
            propertyName = ((LeafProperty) propertyEntity).getEmbeddedName();
        } else {
            propertyName = propertyEntity.getName();
        }
        return new SculptorPropertyCriteria(new LeafProperty(propertyName, CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, entity),
                propertyRestrictionValueToDate(propertyRestriction.getValue()), propertyRestriction.getOperationType());
    }

}