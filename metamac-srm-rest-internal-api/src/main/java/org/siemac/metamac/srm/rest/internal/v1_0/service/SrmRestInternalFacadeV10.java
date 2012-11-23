package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants;
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

@Path("v1.0")
public interface SrmRestInternalFacadeV10 {

    // TODO Documentation

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("conceptschemes")
    ConceptSchemes findConceptSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{version: \\d.*}")
    ConceptScheme retrieveConceptScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID}/{version}/concepts")
    Concepts findConcepts(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER
            + "}/{version: \\d.*}/concepts/{conceptID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
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
    @Path("categoryschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    CategorySchemes findCategorySchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    CategorySchemes findCategorySchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{version: \\d.*}")
    CategoryScheme retrieveCategoryScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID}/{resourceID}/{version}/categories")
    Categories findCategories(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    
    @GET
    @Produces("application/xml")
    @Path("categoryschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER
            + "}/{version: \\d.*}/categories/{categoryID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    Category retrieveCategory(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("categoryID") String categoryID);
    
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // TODO CATEGORISATIONS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//    @GET
//    @Produces("application/xml")
//    @Path("categorisations")
//    Categorisations findCategorisations(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);
//
//    @GET
//    @Produces("application/xml")
//    @Path("categorisations/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
//    Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
//            @QueryParam("offset") String offset);
//
//    @GET
//    @Produces("application/xml")
//    @Path("categorisations/{agencyID}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
//    Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
//            @QueryParam("limit") String limit, @QueryParam("offset") String offset);
//
//    @GET
//    @Produces("application/xml")
//    @Path("categorisations/{agencyID}/{resourceID}/{version: \\d.*}")
//    Categorisations findCategorisations(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);
    
    
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces("application/xml")
    @Path("agencyschemes")
    AgencySchemes findAgencySchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    AgencySchemes findAgencySchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    @GET
    @Produces("application/xml")
    @Path("agencyschemes/{agencyID}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    AgencySchemes findAgencySchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    // TODO
//    @GET
//    @Produces("application/xml")
//    @Path("agencyschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{version: \\d.*}")
//    AgencyScheme retrieveAgencyScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);
//
//    @GET
//    @Produces("application/xml")
//    @Path("agencyschemes/{agencyID}/{resourceID}/{version}/categories")
//    Agencies findAgencies(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @QueryParam("query") String query,
//            @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);
//
//    
//    @GET
//    @Produces("application/xml")
//    @Path("agencyschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER
//            + "}/{version: \\d.*}/categories/{categoryID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
//    Agency retrieveAgency(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version, @PathParam("categoryID") String categoryID);
    
//    <resource id="organisationscheme" path="organisationscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact"> 
//    <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>      
//    <param name="resourceID" type="types:IDType" style="template" required="false" default="all"/> 
//    <param name="version" type="types:VersionType" style="template" required="false" default="latest"/>
//</resource>
//<resource id="agencyscheme" path="agencyscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact"> 
//    <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>      
//    <param name="resourceID" type="types:IDType" style="template" required="false" fixed="AGENCIES"/> 
//    <param name="version" type="types:VersionType" style="template" required="false" fixed="1.0"/>
//</resource>
//<resource id="dataproviderscheme" path="dataproviderscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact"> 
//    <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>      
//    <param name="resourceID" type="types:IDType" style="template" required="false" fixed="DATA_PROVIDERS"/> 
//    <param name="version" type="types:VersionType" style="template" required="false" fixed="1.0"/>
//</resource>
//<resource id="dataconsumerscheme" path="dataconsumerscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact"> 
//    <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>      
//    <param name="resourceID" type="types:IDType" style="template" required="false" fixed="DATA_CONSUMERS"/> 
//    <param name="version" type="types:VersionType" style="template" required="false" fixed="1.0"/>
//</resource>
//<resource id="organisationunitscheme" path="organisationunitscheme/{agencyID}/{resourceID}/{version}" type="#MaintainableArtefact"> 
//    <param name="agencyID" type="types:NCNameIDType" style="template" required="false" default="all"/>      
//    <param name="resourceID" type="types:IDType" style="template" required="false" default="all"/> 
//    <param name="version" type="types:VersionType" style="template" required="false" default="latest"/>
//</resource>

}