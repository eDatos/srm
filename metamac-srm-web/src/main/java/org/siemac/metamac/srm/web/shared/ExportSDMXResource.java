package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportSDMXResource {

    @In(1)
    List<String>            urns;

    @In(2)
    RelatedResourceTypeEnum resourcesType;

    @In(3)
    ExportDetailEnum        detail;

    @In(4)
    ExportReferencesEnum    references;

    @Out(1)
    String                  fileName;

    @Out(2)
    MetamacWebException     exception;
}
