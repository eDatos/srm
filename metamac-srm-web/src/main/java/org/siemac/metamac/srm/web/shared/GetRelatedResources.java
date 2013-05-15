package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetRelatedResources {

    @In(1)
    StructuralResourcesRelationEnum structuralResourcesRelationEnum;

    @In(2)
    int                             firstResult;

    @In(3)
    int                             maxResults;

    @In(4)
    MetamacWebCriteria              criteria;

    @Out(1)
    List<RelatedResourceDto>        relatedResourceDtos;

    @Out(2)
    Integer                         firstResultOut;

    @Out(3)
    Integer                         totalResults;
}
