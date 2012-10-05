package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class LifecycleImpl implements Lifecycle {

    private static final ProcStatusEnum[] procStatusToSendToProductionValidation = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    private static final ProcStatusEnum[] procStatusToSendToDiffusionValidation  = {ProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToRejectProductionValidation = {ProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToRejectDiffusionValidation  = {ProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToPublishInternally          = {ProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ProcStatusEnum[] procStatusToPublishExternally          = {ProcStatusEnum.INTERNALLY_PUBLISHED};

    // Due to java restrictions, this must be inialized out of constructor of Lifecycle
    protected LifecycleCallback           callback                               = null;

    public LifecycleImpl() {
    }

    public Object sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToSendToProductionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.PRODUCTION_VALIDATION;

        // Validate to send to production
        checkResourceInProductionValidation(urn, srmResourceVersion, targetStatus);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setProductionValidationDate(new DateTime());
        lifecycle.setProductionValidationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);

        return srmResourceVersion;
    }

    public Object sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToSendToDiffusionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.DIFFUSION_VALIDATION;

        // Validate to send to Diffusion
        checkResourceInDiffusionValidation(urn, srmResourceVersion, targetStatus);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setDiffusionValidationDate(new DateTime());
        lifecycle.setDiffusionValidationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);

        return srmResourceVersion;
    }

    public Object rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToRejectProductionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.VALIDATION_REJECTED;

        // Validate to reject
        checkResourceInRejectProductionValidation(urn, srmResourceVersion, targetStatus);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setProductionValidationDate(null);
        lifecycle.setProductionValidationUser(null);
        lifecycle.setDiffusionValidationDate(null);
        lifecycle.setDiffusionValidationUser(null);
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);

        return srmResourceVersion;
    }

    public Object rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToRejectDiffusionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.VALIDATION_REJECTED;

        // Validate to reject
        checkResourceInRejectDiffusionValidation(urn, srmResourceVersion, targetStatus);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setProductionValidationDate(null);
        lifecycle.setProductionValidationUser(null);
        lifecycle.setDiffusionValidationDate(null);
        lifecycle.setDiffusionValidationUser(null);
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);

        return srmResourceVersion;
    }

    public Object publishInternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToPublishInternally);

        ProcStatusEnum targetStatus = ProcStatusEnum.INTERNALLY_PUBLISHED;

        // Validate to publish internally
        checkResourceInInternallyPublished(urn, srmResourceVersion, targetStatus);

        // Update lifecycle metadata
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setInternalPublicationDate(new DateTime());
        lifecycle.setInternalPublicationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);
        srmResourceVersion = callback.markSrmResourceAsFinal(ctx, srmResourceVersion);

        return srmResourceVersion;
    }

    public Object publishExternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToPublishExternally);

        ProcStatusEnum targetStatus = ProcStatusEnum.EXTERNALLY_PUBLISHED;

        // Validate to publish externally
        checkResourceInExternallyPublished(urn, srmResourceVersion, targetStatus);

        // Start concept scheme validity
        srmResourceVersion = callback.startSrmResourceValidity(ctx, srmResourceVersion);
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);

        // Fill validTo in previous externally published versions without end validity
        List<Object> versionsExternallyPublished = callback.findSrmResourceVersionsOfSrmResourceInProcStatus(ctx, srmResourceVersion, ProcStatusEnum.EXTERNALLY_PUBLISHED);
        for (Object versionExternallyPublished : versionsExternallyPublished) {
            MaintainableArtefact maintainableArtefact = callback.getMaintainableArtefact(versionExternallyPublished);
            if (maintainableArtefact.getValidTo() == null) {
                callback.endSrmResourceValidity(ctx, versionExternallyPublished);
            }
        }

        // Update lifecycle metadata to publish externally
        lifecycle.setProcStatus(targetStatus);
        lifecycle.setExternalPublicationDate(new DateTime());
        lifecycle.setExternalPublicationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(srmResourceVersion);

        return srmResourceVersion;
    }

    /**
     * Makes validations to sent to production validation. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInProductionValidation(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is production validation
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToSendToProductionValidation);
        }

        // Conditions for concrete resource
        callback.checkConcreteResourceInProductionValidation(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to send to diffusion validation. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInDiffusionValidation(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is diffusion validation
        if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToSendToDiffusionValidation);
        }

        // Check other conditions
        checkResourceInProductionValidation(urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInDiffusionValidation(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     */
    private void checkResourceInRejectProductionValidation(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is reject
        if (ProcStatusEnum.VALIDATION_REJECTED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToRejectProductionValidation);
        }

        // Check other conditions
        callback.checkConcreteResourceInRejectProductionValidation(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject diffusion validation
     */
    private void checkResourceInRejectDiffusionValidation(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is reject
        if (ProcStatusEnum.VALIDATION_REJECTED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToRejectDiffusionValidation);
        }

        // Check other conditions
        callback.checkConcreteResourceInRejectDiffusionValidation(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish internally. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInInternallyPublished(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is publish internally
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToPublishInternally);
        }

        // Check other conditions
        checkResourceInDiffusionValidation(urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInInternallyPublished(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish externally. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInExternallyPublished(String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is publish externally
        if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToPublishExternally);
        }

        // Check other conditions
        checkResourceInInternallyPublished(urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInExternallyPublished(srmResourceVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Checks proc status resource in proc status expected and throws exceptions if it is incorrect
     */
    private void checkProcStatus(Object srmResourceVersion, ProcStatusEnum[] expecteds) throws MetamacException {
        SrmLifeCycleMetadata lifecycle = callback.getLifeCycleMetadata(srmResourceVersion);
        ProcStatusEnum actual = lifecycle.getProcStatus();
        if (!ArrayUtils.contains(expecteds, actual)) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(expecteds);
            List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
            exceptionItems.add(callback.buildExceptionItemWrongProcStatus(srmResourceVersion, procStatusString));
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }

    private void checkInvocation(String urn) throws MetamacException {
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
        ValidationUtils.checkParameterRequired(urn, ServiceExceptionParameters.URN, exceptions);
        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Callback to implement the specific operations in concrete class
     */
    public interface LifecycleCallback {

        // Operations to retrieve, find...
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException;
        public Object updateSrmResource(Object srmResourceVersion);
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion);
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion);
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus);

        // Conditions to update state
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInInternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, List<MetamacExceptionItem> exceptions);

        // Validity, final
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;

        // Other
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds);
    }
}