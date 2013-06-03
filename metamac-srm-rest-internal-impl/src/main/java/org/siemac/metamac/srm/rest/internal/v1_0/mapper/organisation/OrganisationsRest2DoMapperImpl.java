package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@Component
public class OrganisationsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements OrganisationsRest2DoMapper {

    private RestCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> organisationSchemeCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<OrganisationMetamac>              organisationCriteriaMapper       = null;

    public OrganisationsRest2DoMapperImpl() {
        organisationSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac>(OrganisationSchemeVersionMetamac.class, OrganisationSchemeCriteriaPropertyOrder.class,
                OrganisationSchemeCriteriaPropertyRestriction.class, new OrganisationSchemeCriteriaCallback());
        organisationCriteriaMapper = new RestCriteria2SculptorCriteria<OrganisationMetamac>(OrganisationMetamac.class, OrganisationCriteriaPropertyOrder.class,
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
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            OrganisationSchemeCriteriaPropertyRestriction propertyNameCriteria = OrganisationSchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().validFrom(), OrganisationSchemeVersionMetamac.class,
                            false);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().validTo(), OrganisationSchemeVersionMetamac.class,
                            false);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(),
                            propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            OrganisationSchemeVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            OrganisationSchemeVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case TYPE:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.organisationSchemeType(),
                            propertyRestrictionValueToOrganisationSchemeTypeEnum(propertyRestriction.getValue()), propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            OrganisationSchemeCriteriaPropertyOrder propertyNameCriteria = OrganisationSchemeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class OrganisationCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            OrganisationCriteriaPropertyRestriction propertyNameCriteria = OrganisationCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_PROC_STATUS:
                    return new SculptorPropertyCriteria(new LeafProperty<OrganisationMetamac>(OrganisationMetamacProperties.itemSchemeVersion().getName(), OrganisationSchemeVersionMetamacProperties
                            .lifeCycleMetadata().procStatus().getName(), false, OrganisationMetamac.class), propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case TYPE:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.organisationType(), propertyRestrictionValueToOrganisationTypeEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            OrganisationCriteriaPropertyOrder propertyNameCriteria = OrganisationCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return OrganisationMetamacProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return OrganisationMetamacProperties.nameableArtefact().code();
        }
    }

    private OrganisationSchemeTypeEnum propertyRestrictionValueToOrganisationSchemeTypeEnum(String value) {
        return value != null ? OrganisationSchemeTypeEnum.valueOf(value) : null;
    }

    private OrganisationTypeEnum propertyRestrictionValueToOrganisationTypeEnum(String value) {
        return value != null ? OrganisationTypeEnum.valueOf(value) : null;
    }
}