package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionCategoryScheme {

    @In(1)
    String                   urn;

    @In(2)
    VersionTypeEnum          versionType;

    @Out(1)
    CategorySchemeMetamacDto categorySchemeMetamacDto;

}
