package org.siemac.metamac.soap.criteria.mapper;

import java.math.BigInteger;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.soap.common.v1_0.domain.ListBase;

// // TODO put in common library if more soap services are created
public class SculptorCriteria2SoapCriteria {

    @SuppressWarnings("rawtypes")
    public static void toPagedResult(PagedResult source, ListBase target, Integer limit) {
        target.setOffset(BigInteger.valueOf(source.getStartRow()));
        target.setLimit(BigInteger.valueOf(limit)); // when PagingParameter is build as rowAccess pageSize is unknown
        if (source.isTotalCounted()) {
            target.setTotal(BigInteger.valueOf(source.getTotalRows()));
        }
    }
}
