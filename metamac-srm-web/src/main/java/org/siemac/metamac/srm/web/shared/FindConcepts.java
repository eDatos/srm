package org.siemac.metamac.internal.web.shared;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class FindConcepts {

    @In(1)
    String                  uriConceptScheme;

    @Out(1)
    List<ExternalItemBtDto> concepts;

}
