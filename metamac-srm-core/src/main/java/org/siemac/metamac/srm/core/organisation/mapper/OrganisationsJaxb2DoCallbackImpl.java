package org.siemac.metamac.srm.core.organisation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.importation.ImportationMetamacCommonValidations;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsJaxb2DoCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencySchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencyType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataConsumerSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataConsumerType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataProviderSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataProviderType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationSchemesType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitType;

@org.springframework.stereotype.Component("organisationsMetamacJaxb2DoCallback")
public class OrganisationsJaxb2DoCallbackImpl extends ImportationMetamacCommonValidations implements OrganisationsJaxb2DoCallback {

    /**************************************************************************
     * CREATES
     **************************************************************************/
    @Override
    public List<OrganisationSchemeVersion> createOrganisationSchemesDo(OrganisationSchemesType source) {
        return new ArrayList<OrganisationSchemeVersion>();
    }

    @Override
    public OrganisationSchemeVersion createOrganisationSchemeDo() {
        return new OrganisationSchemeVersionMetamac();
    }

    @Override
    public Organisation createOrganisationDo() {
        return new OrganisationMetamac();
    }

    /**************************************************************************
     * EXTENSIONS
     **************************************************************************/
    @Override
    public void agencySchemeJaxbToDoExtension(AgencySchemeType source, OrganisationSchemeVersion target) {
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata
        targetMetamac.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
    }

    @Override
    public void agencyJaxbToDoExtension(AgencyType source, Organisation target) {
        // TODO metamac agencyJaxbToDoExtension
    }

    @Override
    public void dataConsumerSchemeJaxbToDoExtension(DataConsumerSchemeType source, OrganisationSchemeVersion target) {
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata
        targetMetamac.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
    }

    @Override
    public void dataConsumerJaxbToDoExtension(DataConsumerType source, Organisation target) {
        // TODO metamac dataConsumerJaxbToDoExtension
    }

    @Override
    public void dataProviderSchemeJaxbToDoExtension(DataProviderSchemeType source, OrganisationSchemeVersion target) {
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata
        targetMetamac.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
    }

    @Override
    public void dataProviderJaxbToDoExtension(DataProviderType source, Organisation target) {
        // TODO metamac dataProviderJaxbToDoExtension
    }

    @Override
    public void organisationUnitSchemeJaxbToDoExtension(OrganisationUnitSchemeType source, OrganisationSchemeVersion target) {
        OrganisationSchemeVersionMetamac targetMetamac = (OrganisationSchemeVersionMetamac) target;

        // Fill metadata
        targetMetamac.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
    }

    @Override
    public void organisationUnitJaxbToDoExtension(OrganisationUnitType source, Organisation target) {
        // TODO metamac organisationUnitJaxbToDoExtension
    }

    /**************************************************************************
     * VALIDATE
     **************************************************************************/
    @Override
    public void validateRestrictions(OrganisationSchemeVersion source) throws MetamacException {
        validateRestrictionsGeneral(source);
        if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(source.getOrganisationSchemeType())) {
            validateRestrictionsMaintainableArtefact(source.getMaintainableArtefact(), false);
        } else {
            validateRestrictionsMaintainableArtefact(source.getMaintainableArtefact(), true);
        }

    }

}
