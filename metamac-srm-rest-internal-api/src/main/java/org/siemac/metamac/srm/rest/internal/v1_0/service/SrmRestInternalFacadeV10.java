package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;

@Path("v1.0")
public interface SrmRestInternalFacadeV10 {

    // TODO Documentación
    // TODO Documentación: indicar dónde se puede indicar wildcard, latest...

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("conceptschemes")
    ConceptSchemes findConceptSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID}/{version}")
    ConceptScheme retrieveConceptScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID}/{version}/concepts")
    Concepts findConcepts(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}")
    Concept retrieveConcept(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("conceptID") String conceptID);

    @GET
    @Produces("application/xml")
    @Path("conceptTypes")
    ConceptTypes retrieveConceptTypes();

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("categoryschemes")
    CategorySchemes findCategorySchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}")
    CategorySchemes findCategorySchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID}")
    CategorySchemes findCategorySchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID}/{version}")
    CategoryScheme retrieveCategoryScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID}/{version}/categories")
    Categories findCategories(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}")
    Category retrieveCategory(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("categoryID") String categoryID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // CATEGORISATIONS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("categorisations")
    Categorisations findCategorisations(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categorisations/{agencyID}")
    Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categorisations/{agencyID}/{resourceID}")
    Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categorisations/{agencyID}/{resourceID}/{version}")
    Categorisation retrieveCategorisation(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS - ALL ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("organisationschemes")
    OrganisationSchemes findOrganisationSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationschemes/{agencyID}")
    OrganisationSchemes findOrganisationSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationschemes/{agencyID}/{resourceID}")
    OrganisationSchemes findOrganisationSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationschemes/{agencyID}/{resourceID}/{version}")
    OrganisationScheme retrieveOrganisationScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("organisationschemes/{agencyID}/{resourceID}/{version}/organisations")
    Organisations findOrganisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}")
    Organisation retrieveOrganisation(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version,
            @PathParam("organisationID") String organisationID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS - AGENCIES
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("agencyschemes")
    AgencySchemes findAgencySchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}")
    AgencySchemes findAgencySchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}/{resourceID}")
    AgencySchemes findAgencySchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}/{resourceID}/{version}")
    AgencyScheme retrieveAgencyScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}/{resourceID}/{version}/agencies")
    Agencies findAgencies(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}")
    Agency retrieveAgency(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("organisationID") String organisationID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS - ORGANISATION UNITS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes")
    OrganisationUnitSchemes findOrganisationUnitSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes/{agencyID}")
    OrganisationUnitSchemes findOrganisationUnitSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes/{agencyID}/{resourceID}")
    OrganisationUnitSchemes findOrganisationUnitSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes/{agencyID}/{resourceID}/{version}")
    OrganisationUnitScheme retrieveOrganisationUnitScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits")
    OrganisationUnits findOrganisationUnits(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version,
            @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}")
    OrganisationUnit retrieveOrganisationUnit(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version,
            @PathParam("organisationID") String organisationID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS - DATA PROVIDERS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes")
    DataProviderSchemes findDataProviderSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes/{agencyID}")
    DataProviderSchemes findDataProviderSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes/{agencyID}/{resourceID}")
    DataProviderSchemes findDataProviderSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes/{agencyID}/{resourceID}/{version}")
    DataProviderScheme retrieveDataProviderScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders")
    DataProviders findDataProviders(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}")
    DataProvider retrieveDataProvider(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version,
            @PathParam("organisationID") String organisationID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS - DATA CONSUMERS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes")
    DataConsumerSchemes findDataConsumerSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes/{agencyID}")
    DataConsumerSchemes findDataConsumerSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes/{agencyID}/{resourceID}")
    DataConsumerSchemes findDataConsumerSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes/{agencyID}/{resourceID}/{version}")
    DataConsumerScheme retrieveDataConsumerScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers")
    DataConsumers findDataConsumers(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}")
    DataConsumer retrieveDataConsumer(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version,
            @PathParam("organisationID") String organisationID);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // CODELISTS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("codelists")
    Codelists findCodelists(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("codelists/{agencyID}")
    Codelists findCodelists(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("codelists/{agencyID}/{resourceID}")
    Codelists findCodelists(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("codelists/{agencyID}/{resourceID}/{version}")
    Codelist retrieveCodelist(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("codelists/{agencyID}/{resourceID}/{version}/codes")
    Codes findCodes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}")
    Code retrieveCode(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("codeID") String codeID);

    @GET
    @Produces("application/xml")
    @Path("variablefamilies")
    VariableFamilies findVariableFamilies(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("variablefamilies/{id}")
    VariableFamily retrieveVariableFamilyById(@PathParam("id") String id);

    @GET
    @Produces("application/xml")
    @Path("variables")
    Variables findVariables(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("variables/{id}")
    Variable retrieveVariableById(@PathParam("id") String id);

    @GET
    @Produces("application/xml")
    @Path("codelistfamilies")
    CodelistFamilies findCodelistFamilies(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("codelistfamilies/{id}")
    CodelistFamily retrieveCodelistFamilyById(@PathParam("id") String id);

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // DATASTRUCTURES
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("datastructures")
    DataStructures findDataStructures(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("datastructures/{agencyID}")
    DataStructures findDataStructures(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("datastructures/{agencyID}/{resourceID}")
    DataStructures findDataStructures(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("datastructures/{agencyID}/{resourceID}/{version}")
    DataStructure retrieveDataStructure(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

}