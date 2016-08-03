package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategorisationsByArtefact {

    @In(1)
    String                  urn;

    @Out(1)
    List<CategorisationDto> categorisationDtos;
}
