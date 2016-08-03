package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionConceptScheme {

    @In(1)
    String                  urn;

    @In(2)
    VersionTypeEnum         versionType;

    @Out(1)
    ConceptSchemeMetamacDto conceptSchemeDto;

}
