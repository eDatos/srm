package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.siemac.metamac.core.common.constants.shared.RegularExpressionConstants;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;

@Path("v1.0")
public interface SrmRestInternalFacadeV10 {

    /**
     * TODO Documentation
     */
    @GET
    @Produces("application/xml")
    @Path("conceptschemes")
    ConceptSchemes findConceptSchemes(@QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    /**
     * TODO Documentation
     */
    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy, @QueryParam("limit") String limit,
            @QueryParam("offset") String offset);

    /**
     * TODO Documentation
     * 
     * agencyID can be ~all
     */
    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}")
    ConceptSchemes findConceptSchemes(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @QueryParam("query") String query, @QueryParam("orderBy") String orderBy,
            @QueryParam("limit") String limit, @QueryParam("offset") String offset);

    /**
     * TODO Documentation
     * TODO latest
     */
    @GET
    @Produces("application/xml")
    @Path("conceptschemes/{agencyID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{resourceID: " + RegularExpressionConstants.REG_EXP_SEMANTIC_IDENTIFIER + "}/{version: \\d.*}")
    ConceptScheme retrieveConceptScheme(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);

}