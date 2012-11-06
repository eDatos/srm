package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("v1.0")
public interface SrmRestInternalFacadeV10 {
    
    /**
     * Retrieve concept scheme
     * 
     * @param agencyID Code of agency
     * @param resourceID Code of concept scheme
     * @param version Version of concept scheme
     * @return Structure
     * 
     * TODO qué devolver?
     */
    @GET
    @Produces("application/xml")
    @Path("conceptscheme/{agencyID}/{resourceID}/{version}")
    String retrieveConceptSchemeById(@PathParam("agencyID") String agencyID, @PathParam("resourceID") String resourceID, @PathParam("version") String version);
}