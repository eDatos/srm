package org.siemac.metamac.srm.core.code.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;

/**
 * Implementation of CodesMetamacService.
 */
@Service("codesMetamacService")
public class CodesMetamacServiceImpl extends CodesMetamacServiceImplBase {

    @Autowired
    private CodesService codesService;

    // @Autowired
    // private ItemSchemeVersionRepository itemSchemeVersionRepository;
    //
    // @Autowired
    // private CodeRepository codeRepository;
    //
    // @Autowired
    // @Qualifier("codelistLifeCycle")
    // private LifeCycle codelistLifeCycle;
    //
    // @Autowired
    // @Qualifier("codesCopyCallbackMetamac")
    // private CodesCopyCallback codesCopyCallback;

    public CodesMetamacServiceImpl() {
    }

    public CodelistVersionMetamac retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codesService.retrieveCodelistByUrn(ctx, urn);
    }

}
