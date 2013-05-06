package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public abstract class SrmRestInternalFacadeV10OrganisationsTest extends SrmRestInternalFacadeV10BaseTest {

    protected OrganisationsMetamacService organisationsService;
    private ItemSchemeVersionRepository   itemSchemeVersionRepository;

    public static String                  ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE            = "itemSchemeAgency1";
    public static String                  ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE = "itemSchemeOrganisationUnit1";

    public static String                  ITEM_ORGANISATION_TYPE_AGENCY_1_CODE                   = "itemAgency1";
    public static String                  ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE        = "itemOrganisationUnit1";

    @Override
    protected void resetMocks() throws MetamacException {
        organisationsService = applicationContext.getBean(OrganisationsMetamacService.class);
        reset(organisationsService);
        itemSchemeVersionRepository = applicationContext.getBean(ItemSchemeVersionRepository.class);
        reset(itemSchemeVersionRepository);

        mockRetrieveItemSchemeVersionByVersion();
        mockFindOrganisationSchemesByCondition();
        mockFindOrganisationsByCondition();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Boolean getVersionLatestFromConditionalCriteria(List<ConditionalCriteria> conditions, MaintainableArtefactProperty maintainableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Or, maintainableArtefactProperty.latestFinal());
        return conditionalCriteria != null ? (Boolean) conditionalCriteria.getFirstOperant() : null;
    }

    private void mockRetrieveItemSchemeVersionByVersion() throws MetamacException {
        when(itemSchemeVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<ItemSchemeVersion>() {

            @Override
            public ItemSchemeVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return OrganisationsDoMocks.mockOrganisationScheme("agencyID", version, version, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationSchemesByCondition() throws MetamacException {
        when(organisationsService.findOrganisationSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<OrganisationSchemeVersionMetamac>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, OrganisationSchemeVersionMetamacProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, OrganisationSchemeVersionMetamacProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, OrganisationSchemeVersionMetamacProperties.maintainableArtefact());
                        ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.organisationSchemeType());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, OrganisationSchemeVersionMetamacProperties.maintainableArtefact());

                        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = conditionalCriteriaType != null ? (OrganisationSchemeTypeEnum) conditionalCriteriaType.getFirstOperant() : null;
                        if (version == null && latest == null) {
                            // check special schemes
                            if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeTypeEnum) || OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.equals(organisationSchemeTypeEnum)
                                    || OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.equals(organisationSchemeTypeEnum)) {
                                latest = Boolean.TRUE; // always in version 01.000
                            }
                        }
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            OrganisationSchemeVersionMetamac itemSchemeVersion = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                itemSchemeVersion = null;
                            } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, organisationSchemeTypeEnum);
                            } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE, VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                            } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE.equals(resourceID)
                                    && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE, VERSION_1,
                                        OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
                            } else {
                                fail();
                            }
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (itemSchemeVersion != null) {
                                itemSchemes.add(itemSchemeVersion);
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, 0, itemSchemes.size(), itemSchemes.size());
                        } else {
                            // any
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (organisationSchemeTypeEnum != null) {
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1, organisationSchemeTypeEnum));
                            } else {
                                // different types
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, itemSchemes.size(), itemSchemes.size(), itemSchemes.size(), itemSchemes.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationsByCondition() throws MetamacException {
        when(organisationsService.findOrganisationsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<OrganisationMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String itemID = getItemIdFromConditionalCriteria(conditions, OrganisationMetamacProperties.nameableArtefact());
                ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                        OrganisationMetamacProperties.organisationType());

                OrganisationTypeEnum organisationTypeEnum = conditionalCriteriaType != null ? (OrganisationTypeEnum) conditionalCriteriaType.getFirstOperant() : null;

                if (agencyID != null && resourceID != null && version != null && itemID != null) {
                    // Retrieve one
                    OrganisationMetamac item = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version) || NOT_EXISTS.equals(itemID)) {
                        item = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                        OrganisationMetamac parent = null;
                        if (OrganisationTypeEnum.ORGANISATION_UNIT.equals(organisationTypeEnum)) {
                            parent = OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum);;
                        }
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, parent, organisationTypeEnum);
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_ORGANISATION_TYPE_AGENCY_1_CODE.equals(itemID)) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_ORGANISATION_TYPE_AGENCY_1_CODE, itemScheme1, null, OrganisationTypeEnum.AGENCY);
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE.equals(itemID)) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1,
                                OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
                        OrganisationMetamac parent = OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum);;
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE, itemScheme1, parent, OrganisationTypeEnum.ORGANISATION_UNIT);
                    } else {
                        fail();
                    }
                    List<OrganisationMetamac> items = new ArrayList<OrganisationMetamac>();
                    if (item != null) {
                        items.add(item);
                    }
                    return new PagedResult<OrganisationMetamac>(items, 0, items.size(), items.size());
                } else {
                    // any
                    List<OrganisationMetamac> items = new ArrayList<OrganisationMetamac>();
                    if (organisationTypeEnum != null) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                        OrganisationSchemeVersionMetamac itemScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));

                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_3_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme2, null, organisationTypeEnum));
                    } else {
                        // different types
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                        OrganisationSchemeVersionMetamac itemScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1,
                                OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);

                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, null, OrganisationTypeEnum.AGENCY));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, OrganisationTypeEnum.AGENCY));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_3_CODE, itemScheme1, null, OrganisationTypeEnum.ORGANISATION_UNIT));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme2, null, OrganisationTypeEnum.ORGANISATION_UNIT));
                    }
                    return new PagedResult<OrganisationMetamac>(items, items.size(), items.size(), items.size(), items.size() * 10, 0);
                }
            };
        });
    }
}