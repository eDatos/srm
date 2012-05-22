package org.siemac.metamac.internal.web.shared;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure=false)
public class DeleteConceptSchemeList {

    @In(1)
    List<String> uuids;
}
