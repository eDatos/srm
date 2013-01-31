package org.siemac.metamac.srm.web.shared;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetRelatedResources {

    @In(1)
    RelatedArtefactsEnum     relatedArtefactsEnum;

    @In(2)
    String                   dsdUrn;

    @In(3)
    String                   criteria;

    @Out(1)
    List<RelatedResourceDto> relatedResourceDtos;

    @Out(2)
    Integer                  firstResultOut;

    @Out(3)
    Integer                  totalResults;
}
