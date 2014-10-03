package org.siemac.metamac.srm.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.MaintainableArtefactDto;

public interface NoticesRestInternalFacade {

    public static final String BEAN_ID = "noticesRestInternalFacade";

    void createInternalPublicationNotification(ServiceContext serviceContext, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) throws MetamacWebException;
    void createExternalPublicationNotification(ServiceContext serviceContext, MaintainableArtefactDto maintainableArtefactDto, TypeExternalArtefactsEnum resourceType) throws MetamacWebException;
}
