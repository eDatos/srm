package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;

@Path("v1.0")
public interface SrmRestInternalFacadeV10 {
    
    /**
     * TODO Documentation
     */
    @GET
    @Produces("application/xml")
    @Path("conceptscheme/{agencyID}/{resourceID}/{version}")
    ConceptScheme retrieveConceptSchemeById(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);
}