package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;

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
    // TODO CATEGORISATIONS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // @GET
    // @Produces("application/xml")
    // @Path("categorisations")
    // Categorisations findCategorisations(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);
    //
    // @GET
    // @Produces("application/xml")
    // @Path("categorisations/{agencyID}")
    // Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
    // @QueryParam("offset") String offset);
    //
    // @GET
    // @Produces("application/xml")
    // @Path("categorisations/{agencyID}/{resourceID}")
    // Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
    // @QueryParam("limit") String limit, @QueryParam("offset") String offset);
    //
    // @GET
    // @Produces("application/xml")
    // @Path("categorisations/{agencyID}/{resourceID}/{version}")
    // Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

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

    // <resource id="organisationscheme" path="organisationscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact">
    // <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>
    // <param name="resourceID" type="types:IDType" style="template" required="false" default="all"/>
    // <param name="version" type="types:VersionType" style="template" required="false" default="latest"/>
    // </resource>
    // <resource id="dataproviderscheme" path="dataproviderscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact">
    // <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>
    // <param name="resourceID" type="types:IDType" style="template" required="false" fixed="DATA_PROVIDERS"/>
    // <param name="version" type="types:VersionType" style="template" required="false" fixed="1.0"/>
    // </resource>
    // <resource id="dataconsumerscheme" path="dataconsumerscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact">
    // <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>
    // <param name="resourceID" type="types:IDType" style="template" required="false" fixed="DATA_CONSUMERS"/>
    // <param name="version" type="types:VersionType" style="template" required="false" fixed="1.0"/>
    // </resource>

}