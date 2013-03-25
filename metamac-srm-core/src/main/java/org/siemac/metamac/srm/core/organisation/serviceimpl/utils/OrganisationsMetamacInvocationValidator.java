package org.siemac.metamac.srm.core.organisation.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsInvocationValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsMetamacInvocationValidator extends OrganisationsInvocationValidator {

    // ORGANISATIONS SCHEME

    public static void checkCreateOrganisationScheme(OrganisationSchemeVersionMetamac organisationSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(organisationSchemeVersion, ServiceExceptionParameters.ORGANISATION_SCHEME, exceptions);
        if (organisationSchemeVersion != null) {
            checkOrganisationScheme(organisationSchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateOrganisationScheme(OrganisationSchemeVersionMetamac organisationSchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(organisationSchemeVersion, ServiceExceptionParameters.ORGANISATION_SCHEME, exceptions);
        if (organisationSchemeVersion != null) {
            checkOrganisationScheme(organisationSchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveOrganisationSchemeByOrganisationUrn(String organisationUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(organisationUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // ORGANISATIONS

    public static void checkCreateOrganisation(OrganisationSchemeVersionMetamac organisationSchemeVersion, OrganisationMetamac organisation, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(organisationSchemeVersion, ServiceExceptionParameters.ORGANISATION_SCHEME, exceptions);

        ValidationUtils.checkParameterRequired(organisation, ServiceExceptionParameters.ORGANISATION, exceptions);
        if (organisation != null && organisationSchemeVersion != null) {
            checkOrganisation(organisationSchemeVersion, organisation, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateOrganisation(OrganisationMetamac organisation, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(organisation, ServiceExceptionParameters.ORGANISATION, exceptions);
        if (organisation != null) {
            checkOrganisation(organisation.getItemSchemeVersion(), organisation, exceptions);
        }
        ValidationUtils.checkMetadataRequired(organisation.getNameableArtefact().getIsCodeUpdated(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_IS_CODE_UPDATED, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkOrganisationScheme(OrganisationSchemeVersionMetamac organisationSchemeVersion, List<MetamacExceptionItem> exceptions) {
        // SDMX metadata is checked in SDMX module
        if (organisationSchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(organisationSchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
        // AgencyScheme can be partial (example: agency scheme root with maintainer SDMX)
        if (!OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeVersion.getOrganisationSchemeType()) && BooleanUtils.isTrue(organisationSchemeVersion.getIsPartial())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL));
        }
    }

    private static void checkOrganisation(ItemSchemeVersion organisationSchemeVersion, OrganisationMetamac organisation, List<MetamacExceptionItem> exceptions) {
        // nothing additional
        // common metadata in sdmx are checked in Sdmx module
    }
}
