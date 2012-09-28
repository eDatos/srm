package org.siemac.metamac.srm.core.structure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.Structure;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;

// TODO unir con ItemSchemeLifecycle
public class StructureLifecycle {

    // TODO eliminar el enumerado validation_rejected y poner una marca?? así se evitarían los []
    private static final ProcStatusEnum[] procStatusToSendToProductionValidation = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    private static final ProcStatusEnum[] procStatusToSendToDiffusionValidation  = {ProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToRejectProductionValidation = {ProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToRejectDiffusionValidation  = {ProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToPublishInternally          = {ProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToPublishExternally          = {ProcStatusEnum.INTERNALLY_PUBLISHED};

    private StructureLifecycleCallback    callback                               = null;

    public StructureLifecycle(StructureLifecycleCallback callback) {
        this.callback = callback;
    }

    public StructureVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkSendItemSchemeToProductionValidation(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToSendToProductionValidation);

        // Validate to send to production
        checkStructureToSendToProductionValidation(urn, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
        lifecycle.setProductionValidationDate(new DateTime());
        lifecycle.setProductionValidationUser(ctx.getUserId());
        structureVersion = callback.updateStructure(structureVersion);

        return structureVersion;
    }

    public StructureVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkSendItemSchemeToDiffusionValidation(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToSendToDiffusionValidation);

        // Validate to send to Diffusion
        checkStructureToSendToDiffusionValidation(urn, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
        lifecycle.setDiffusionValidationDate(new DateTime());
        lifecycle.setDiffusionValidationUser(ctx.getUserId());
        structureVersion = callback.updateStructure(structureVersion);

        return structureVersion;
    }

    public StructureVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkRejectItemSchemeProductionValidation(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToRejectProductionValidation);

        // Validate to reject
        checkStructureToRejectProductionValidation(urn, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        lifecycle.setProductionValidationDate(null);
        lifecycle.setProductionValidationUser(null);
        lifecycle.setDiffusionValidationDate(null);
        lifecycle.setDiffusionValidationUser(null);
        structureVersion = callback.updateStructure(structureVersion);

        return structureVersion;
    }

    public StructureVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkRejectItemSchemeDiffusionValidation(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToRejectDiffusionValidation);

        // Validate to reject
        checkStructureToRejectDiffusionValidation(urn, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
        lifecycle.setProductionValidationDate(null);
        lifecycle.setProductionValidationUser(null);
        lifecycle.setDiffusionValidationDate(null);
        lifecycle.setDiffusionValidationUser(null);
        structureVersion = callback.updateStructure(structureVersion);

        return structureVersion;
    }

    public StructureVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkPublishInternallyItemScheme(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToPublishInternally);

        // Validate to publish internally
        checkStructureToPublishInternally(ctx, urn, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.INTERNALLY_PUBLISHED);
        lifecycle.setInternalPublicationDate(new DateTime());
        lifecycle.setInternalPublicationUser(ctx.getUserId());
        structureVersion = callback.updateStructure(structureVersion);
        structureVersion = callback.markStructureAsFinal(ctx, structureVersion);

        return structureVersion;
    }

    public StructureVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        StructureInvocationValidator.checkPublishExternallyItemScheme(urn, null);

        // Retrieve version in specific proc status
        StructureVersion structureVersion = callback.retrieveStructureByProcStatus(urn, procStatusToPublishExternally);

        // Validate to publish externally
        checkStructureToPublishExternally(ctx, urn, structureVersion);

        // Start concept scheme validity
        structureVersion = callback.startStructureValidity(ctx, structureVersion);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        lifecycle.setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
        lifecycle.setExternalPublicationDate(new DateTime());
        lifecycle.setExternalPublicationUser(ctx.getUserId());
        structureVersion = callback.updateStructure(structureVersion);

        // Fill validTo in previous internally published versions
        List<StructureVersion> versionsExternallyPublished = callback.findStructureVersionsOfItemSchemeInProcStatus(ctx, structureVersion.getStructure(), ProcStatusEnum.EXTERNALLY_PUBLISHED);
        for (StructureVersion versionExternallyPublished : versionsExternallyPublished) {
            if (versionExternallyPublished.getId().equals(structureVersion.getId())) {
                continue;
            }
            if (versionExternallyPublished.getMaintainableArtefact().getValidTo() == null) {
                versionExternallyPublished.getMaintainableArtefact().setValidTo(lifecycle.getExternalPublicationDate());
                callback.updateStructure(versionExternallyPublished);
            }
        }

        return structureVersion;
    }

    /**
     * Makes validations to sent to production validation
     */
    private void checkStructureToSendToProductionValidation(String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToSendToProductionValidation);

        // Check other conditions
        checkConditionsSinceSendToProductionValidation(structureVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to sent to diffusion validation
     */
    private void checkStructureToSendToDiffusionValidation(String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToSendToDiffusionValidation);

        // Check other conditions
        checkConditionsSinceSendToDiffusionValidation(structureVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     */
    private void checkStructureToRejectProductionValidation(String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToRejectProductionValidation);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject diffusion validation
     */
    private void checkStructureToRejectDiffusionValidation(String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToRejectDiffusionValidation);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish internally
     */
    private void checkStructureToPublishInternally(ServiceContext ctx, String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToPublishInternally);

        // Additional conditions of concrete classes
        callback.checkAdditionalConditionsToPublishInternally(structureVersion, exceptions);

        // Check other conditions
        checkConditionsSincePublishInternally(structureVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish externally
     */
    private void checkStructureToPublishExternally(ServiceContext ctx, String urn, StructureVersion structureVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(structureVersion, procStatusToPublishExternally);

        // Additional conditions of concrete classes
        callback.checkAdditionalConditionsToPublishExternally(structureVersion, exceptions);

        // Check other conditions
        checkConditionsSincePublishExternally(structureVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private void checkProcStatus(StructureVersion structureVersion, ProcStatusEnum[] expecteds) throws MetamacException {
        SrmLifeCycleMetadata lifecycle = callback.getSrmLifeCycleMetadata(structureVersion);
        ProcStatusEnum actual = lifecycle.getProcStatus();
        if (!ArrayUtils.contains(expecteds, actual)) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(expecteds);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WRONG_PROC_STATUS)
                    .withMessageParameters(structureVersion.getMaintainableArtefact().getUrn(), procStatusString).build();
        }
    }

    /**
     * Checks common conditions of item schemes to send to production validation
     */
    private void checkConditionsSinceSendToProductionValidation(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {
        // Metadata required

        // Conditions for concrete resource
        callback.checkAdditionalConditionsSinceSendToProductionValidation(structureVersion, exceptions);
    }

    /**
     * Checks common conditions of structure to check states since structure was sent to diffusion validation
     */
    private void checkConditionsSinceSendToDiffusionValidation(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToProductionValidation(structureVersion, exceptions);
    }

    /**
     * Checks common conditions of structure to check states since structure was internally published
     */
    private void checkConditionsSincePublishInternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToDiffusionValidation(structureVersion, exceptions);
    }

    /**
     * Checks common conditions of structure to check states since structure was externally published
     */
    private void checkConditionsSincePublishExternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSincePublishInternally(structureVersion, exceptions);
    }

    /**
     * Callback to implement the specific operations in concrete class of structure (dataStructureDefinition, metadataStructureDefinition...)
     */
    public static interface StructureLifecycleCallback {

        public StructureVersion retrieveStructureByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException;
        public StructureVersion updateStructure(StructureVersion structureVersion);
        public SrmLifeCycleMetadata getSrmLifeCycleMetadata(StructureVersion structureVersion);
        public void checkAdditionalConditionsSinceSendToProductionValidation(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions);
        public void checkAdditionalConditionsToPublishInternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions);
        public void checkAdditionalConditionsToPublishExternally(StructureVersion structureVersion, List<MetamacExceptionItem> exceptions);
        public StructureVersion markStructureAsFinal(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException;
        public StructureVersion startStructureValidity(ServiceContext ctx, StructureVersion structureVersion) throws MetamacException;
        List<StructureVersion> findStructureVersionsOfItemSchemeInProcStatus(ServiceContext ctx, Structure structure, ProcStatusEnum... procStatus);
    }
}
