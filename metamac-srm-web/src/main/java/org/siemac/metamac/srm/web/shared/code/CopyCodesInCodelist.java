package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class CopyCodesInCodelist {

    @In(1)
    String           codelistSourceUrn;

    @In(2)
    String           codelistTargetUrn;

    @In(3)
    List<CodeToCopy> codesToCopy;
}
