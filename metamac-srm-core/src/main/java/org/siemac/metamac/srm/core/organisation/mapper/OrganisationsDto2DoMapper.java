package org.siemac.metamac.srm.core.organisation.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

public interface OrganisationsDto2DoMapper {

    public OrganisationSchemeVersionMetamac organisationSchemeMetamacDtoToDo(OrganisationSchemeMetamacDto source) throws MetamacException;
    public OrganisationMetamac organisationMetamacDtoToDo(OrganisationMetamacDto source) throws MetamacException;
}
