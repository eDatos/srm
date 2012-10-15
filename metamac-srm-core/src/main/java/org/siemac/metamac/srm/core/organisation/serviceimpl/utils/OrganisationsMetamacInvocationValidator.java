package org.siemac.metamac.srm.core.organisation.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.serviceimpl.utils.OrganisationsInvocationValidator;

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

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkOrganisationScheme(OrganisationSchemeVersionMetamac organisationSchemeVersion, List<MetamacExceptionItem> exceptions) {
        // SDMX metadata is checked in SDMX module
        if (organisationSchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(organisationSchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
    }

    private static void checkOrganisation(ItemSchemeVersion organisationSchemeVersion, OrganisationMetamac organisation, List<MetamacExceptionItem> exceptions) {
        // nothing additional
        // common metadata in sdmx are checked in Sdmx module
    }
}
