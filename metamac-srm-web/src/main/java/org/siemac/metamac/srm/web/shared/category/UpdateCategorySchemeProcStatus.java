package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCategorySchemeProcStatus {

    @In(1)
    String                   urn;

    @In(2)
    ProcStatusEnum           nextProcStatus;

    @In(3)
    ProcStatusEnum           currentProcStatus;

    @Out(1)
    CategorySchemeMetamacDto categorySchemeDto;

}
