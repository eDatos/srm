package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class CreateCodesHierarchy {

    @In(1)
    String                 codelistUrn;

    @In(2)
    String                 parentUrn;

    @In(3)
    List<ItemHierarchyDto> codesMetamacDto;
}
