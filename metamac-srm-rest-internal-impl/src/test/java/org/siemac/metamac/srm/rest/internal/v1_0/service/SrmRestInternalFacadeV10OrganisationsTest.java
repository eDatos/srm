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
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;

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

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public abstract class SrmRestInternalFacadeV10OrganisationsTest extends SrmRestInternalFacadeV10BaseTest {

    protected OrganisationsMetamacService organisationsService;

    public static String                  ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE            = "itemSchemeAgency1";
    public static String                  ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE = "itemSchemeOrganisationUnit1";

    public static String                  ITEM_ORGANISATION_TYPE_AGENCY_1_CODE                   = "itemAgency1";
    public static String                  ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE        = "itemOrganisationUnit1";

    @Override
    protected void resetMocks() throws MetamacException {
        organisationsService = applicationContext.getBean(OrganisationsMetamacService.class);
        reset(organisationsService);
        mockFindOrganisationSchemesByCondition();
        mockFindOrganisationsByCondition();
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationSchemesByCondition() throws MetamacException {
        when(organisationsService.findOrganisationSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<OrganisationSchemeVersionMetamac>>() {

                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer());
                        ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code());
                        ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().versionLogic());
                        ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.organisationSchemeType());

                        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = conditionalCriteriaType != null ? (OrganisationSchemeTypeEnum) conditionalCriteriaType.getFirstOperant() : null;
                        if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null) {
                            // Retrieve one
                            OrganisationSchemeVersionMetamac itemSchemeVersion = null;
                            if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = null;
                            } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum);
                            } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant())
                                    && ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_AGENCY_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                            } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant())
                                    && ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE,
                                        ITEM_SCHEME_1_VERSION_1, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
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
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1, organisationSchemeTypeEnum));
                            } else {
                                // different types
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2,
                                        OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1,
                                        OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, itemSchemes.size(), itemSchemes.size(), itemSchemes.size(), itemSchemes.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationsByCondition() throws MetamacException {
        when(organisationsService.findOrganisationsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<OrganisationMetamac>>() {

            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer());
                ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().code());
                ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .itemSchemeVersion().maintainableArtefact().versionLogic());
                ConditionalCriteria conditionalCriteriaItem = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
                        .nameableArtefact().code());
                ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                        OrganisationMetamacProperties.organisationType());

                OrganisationTypeEnum organisationTypeEnum = conditionalCriteriaType != null ? (OrganisationTypeEnum) conditionalCriteriaType.getFirstOperant() : null;

                if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaItem != null) {
                    // Retrieve one
                    OrganisationMetamac item = null;
                    if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                            || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaItem.getFirstOperant())) {
                        item = null;
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                        OrganisationMetamac parent = null;
                        if (OrganisationTypeEnum.ORGANISATION_UNIT.equals(organisationTypeEnum)) {
                            parent = OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum);;
                        }
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, parent, organisationTypeEnum);
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_ORGANISATION_TYPE_AGENCY_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                        item = OrganisationsDoMocks.mockOrganisation(ITEM_ORGANISATION_TYPE_AGENCY_1_CODE, itemScheme1, null, OrganisationTypeEnum.AGENCY);
                    } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                            && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())
                            && ITEM_ORGANISATION_TYPE_ORGANISATION_UNIT_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
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
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));
                        OrganisationSchemeVersionMetamac itemScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
                                SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeEnum));

                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_3_CODE, itemScheme1, null, organisationTypeEnum));
                        items.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme2, null, organisationTypeEnum));
                    } else {
                        // different types
                        OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                        OrganisationSchemeVersionMetamac itemScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
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