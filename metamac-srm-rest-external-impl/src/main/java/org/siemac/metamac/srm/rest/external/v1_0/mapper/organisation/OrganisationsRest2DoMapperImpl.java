package org.siemac.metamac.srm.rest.external.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationSchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class OrganisationsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements OrganisationsRest2DoMapper {

    private RestCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> organisationSchemeCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<OrganisationMetamac>              organisationCriteriaMapper       = null;

    public OrganisationsRest2DoMapperImpl() {
        organisationSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<>(OrganisationSchemeVersionMetamac.class, OrganisationSchemeCriteriaPropertyOrder.class,
                OrganisationSchemeCriteriaPropertyRestriction.class, new OrganisationSchemeCriteriaCallback());
        organisationCriteriaMapper = new RestCriteria2SculptorCriteria<>(OrganisationMetamac.class, OrganisationCriteriaPropertyOrder.class,
                OrganisationCriteriaPropertyRestriction.class, new OrganisationCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> getOrganisationSchemeCriteriaMapper() {
        return organisationSchemeCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<OrganisationMetamac> getOrganisationCriteriaMapper() {
        return organisationCriteriaMapper;
    }

    private class OrganisationSchemeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            OrganisationSchemeCriteriaPropertyRestriction propertyNameCriteria = OrganisationSchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, OrganisationSchemeVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().validFrom(),
                            OrganisationSchemeVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().validTo(),
                            OrganisationSchemeVersionMetamac.class, false);
                case LAST_UPDATED_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            OrganisationSchemeVersionMetamac.class, true);
                case LATEST:
                    // Note: AgencyScheme, DataProvider... are note marked as final, but they are marked as latest when published.
                    return buildSculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().latestPublic(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                case TYPE:
                    return buildSculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.organisationSchemeType(), PropertyTypeEnum.ORGANISATION_SCHEME_TYPE, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            OrganisationSchemeCriteriaPropertyOrder propertyNameCriteria = OrganisationSchemeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class OrganisationCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            OrganisationCriteriaPropertyRestriction propertyNameCriteria = OrganisationCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, OrganisationMetamacProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case ORGANISATION_SCHEME_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case ORGANISATION_SCHEME_LATEST:
                    return buildSculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().latestPublic(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                case TYPE:
                    return buildSculptorPropertyCriteria(OrganisationMetamacProperties.organisationType(), PropertyTypeEnum.ORGANISATION_TYPE, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            OrganisationCriteriaPropertyOrder propertyNameCriteria = OrganisationCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return OrganisationMetamacProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return OrganisationMetamacProperties.nameableArtefact().code();
        }
    }
}