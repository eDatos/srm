package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;
import java.util.Map;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdDimensionsAndCandidateVisualisations {

    @In(1)
    String                                dsdUrn;

    @Out(1)
    List<DimensionComponentDto>           dimensionComponentDtos;

    @Out(2)
    Map<String, List<RelatedResourceDto>> candidateOrdersByDimension;

    @Out(3)
    Map<String, List<RelatedResourceDto>> candidateOpennessLevelsByDimension;
}
