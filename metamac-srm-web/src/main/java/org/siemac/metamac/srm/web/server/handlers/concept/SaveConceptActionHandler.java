package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptActionHandler extends SecurityActionHandler<SaveConceptAction, SaveConceptResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveConceptActionHandler() {
        super(SaveConceptAction.class);
    }

    @Override
    public SaveConceptResult executeSecurityAction(SaveConceptAction action) throws ActionException {
        try {
            ConceptMetamacDto conceptToSave = action.getConceptToSave();
            ConceptMetamacDto savedConcept = null;

            //
            // Save concept
            //

            if (conceptToSave.getId() == null) {
                // Create
                savedConcept = srmCoreServiceFacade.createConcept(ServiceContextHolder.getCurrentServiceContext(), conceptToSave);
            } else {
                // Update
                savedConcept = srmCoreServiceFacade.updateConcept(ServiceContextHolder.getCurrentServiceContext(), conceptToSave);
            }

            //
            // Save roles
            //

            List<ConceptMetamacDto> oldRoles = srmCoreServiceFacade.retrieveRoleConcepts(ServiceContextHolder.getCurrentServiceContext(), savedConcept.getUrn());
            Set<String> oldRolesUrn = getConceptUrnsSet(oldRoles);

            if (action.getRolesToSave() == null || action.getRolesToSave().isEmpty()) {
                if (oldRoles != null && !oldRoles.isEmpty()) {
                    // Remove all roles
                    for (String urn : oldRolesUrn) {
                        srmCoreServiceFacade.deleteRoleConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }
            } else {

                // Concepts to add
                for (String urn : action.getRolesToSave()) {
                    if (!oldRolesUrn.contains(urn)) {
                        srmCoreServiceFacade.addRoleConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }
                // Concepts to remove
                for (String urn : oldRolesUrn) {
                    if (!action.getRolesToSave().contains(urn)) {
                        srmCoreServiceFacade.deleteRoleConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }

            }
            List<ConceptMetamacDto> roles = srmCoreServiceFacade.retrieveRoleConcepts(ServiceContextHolder.getCurrentServiceContext(), savedConcept.getUrn());
            List<RelatedResourceDto> relatedResourceRoles = RelatedResourceUtils.getRelatedResourceDtosFromConceptMetamacDtos(roles);

            //
            // Save related concepts
            //

            List<ConceptMetamacDto> oldRelatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(ServiceContextHolder.getCurrentServiceContext(), savedConcept.getUrn());
            Set<String> oldConceptsUrn = getConceptUrnsSet(oldRelatedConcepts);

            if (action.getRelatedConceptsToSave() == null || action.getRelatedConceptsToSave().isEmpty()) {
                if (oldRelatedConcepts != null && !oldRelatedConcepts.isEmpty()) {
                    // Remove all related concepts
                    for (String urn : oldConceptsUrn) {
                        srmCoreServiceFacade.deleteRelatedConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }
            } else {

                // Concepts to add
                for (String urn : action.getRelatedConceptsToSave()) {
                    if (!oldConceptsUrn.contains(urn)) {
                        srmCoreServiceFacade.addRelatedConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }
                // Concepts to remove
                for (String urn : oldConceptsUrn) {
                    if (!action.getRelatedConceptsToSave().contains(urn)) {
                        srmCoreServiceFacade.deleteRelatedConcept(ServiceContextHolder.getCurrentServiceContext(), action.getConceptToSave().getUrn(), urn);
                    }
                }

            }
            List<ConceptMetamacDto> relatedConcepts = srmCoreServiceFacade.retrieveRelatedConcepts(ServiceContextHolder.getCurrentServiceContext(), savedConcept.getUrn());

            // Reload the saved concept
            savedConcept = srmCoreServiceFacade.retrieveConceptByUrn(ServiceContextHolder.getCurrentServiceContext(), savedConcept.getUrn());

            return new SaveConceptResult(savedConcept, relatedResourceRoles, relatedConcepts);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private Set<String> getConceptUrnsSet(List<ConceptMetamacDto> conceptMetamacDtos) {
        if (conceptMetamacDtos != null) {
            Set<String> urns = new HashSet<String>();
            for (ConceptMetamacDto conceptMetamacDto : conceptMetamacDtos) {
                urns.add(conceptMetamacDto.getUrn());
            }
            return urns;
        } else {
            return new HashSet<String>();
        }
    }
}
