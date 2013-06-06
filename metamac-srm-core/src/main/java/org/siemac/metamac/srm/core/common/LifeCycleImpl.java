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
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmValidationUtils;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

public abstract class LifeCycleImpl implements LifeCycle {

    protected static final ProcStatusEnum[] procStatusToSendToProductionValidation = {ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED};
    protected static final ProcStatusEnum[] procStatusToSendToDiffusionValidation  = {ProcStatusEnum.PRODUCTION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusToRejectProductionValidation = {ProcStatusEnum.PRODUCTION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusToRejectDiffusionValidation  = {ProcStatusEnum.DIFFUSION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusToPublishInternally          = {ProcStatusEnum.DIFFUSION_VALIDATION};
    protected static final ProcStatusEnum[] procStatusToPublishExternally          = {ProcStatusEnum.INTERNALLY_PUBLISHED};

    // Due to java restrictions, this must be inialized out of constructor of LifeCycle
    protected LifeCycleCallback             callback                               = null;

    @Autowired
    private OrganisationsMetamacService     organisationsService;

    @Autowired
    private CategoriesMetamacService        categoriesService;

    @Autowired
    private SrmConfiguration                srmConfiguration;

    public LifeCycleImpl() {
    }

    @Override
    public Object sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToSendToProductionValidation);
        ProcStatusEnum targetStatus = ProcStatusEnum.PRODUCTION_VALIDATION;

        // Previous update actions
        callback.executeBeforeSendProductionValidation(ctx, srmResourceVersion);

        // Validate to send to production
        checkResourceInProductionValidation(ctx, urn, srmResourceVersion, targetStatus);

        // Update life cycle metadata
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifeCycle.setProcStatus(targetStatus);
        lifeCycle.setProductionValidationDate(new DateTime());
        lifeCycle.setProductionValidationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);

        return srmResourceVersion;
    }

    @Override
    public Object sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToSendToDiffusionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.DIFFUSION_VALIDATION;

        // Validate to send to Diffusion
        checkResourceInDiffusionValidation(ctx, urn, srmResourceVersion, targetStatus);

        // Update life cycle metadata
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifeCycle.setProcStatus(targetStatus);
        lifeCycle.setDiffusionValidationDate(new DateTime());
        lifeCycle.setDiffusionValidationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);

        return srmResourceVersion;
    }

    @Override
    public Object rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToRejectProductionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.VALIDATION_REJECTED;

        // Validate to reject
        checkResourceInRejectProductionValidation(urn, srmResourceVersion, targetStatus);

        // Update life cycle metadata
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifeCycle.setProcStatus(targetStatus);
        lifeCycle.setProductionValidationDate(null);
        lifeCycle.setProductionValidationUser(null);
        lifeCycle.setDiffusionValidationDate(null);
        lifeCycle.setDiffusionValidationUser(null);
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);

        return srmResourceVersion;
    }

    @Override
    public Object rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToRejectDiffusionValidation);

        ProcStatusEnum targetStatus = ProcStatusEnum.VALIDATION_REJECTED;

        // Validate to reject
        checkResourceInRejectDiffusionValidation(urn, srmResourceVersion, targetStatus);

        // Update life cycle metadata
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        lifeCycle.setProcStatus(targetStatus);
        lifeCycle.setProductionValidationDate(null);
        lifeCycle.setProductionValidationUser(null);
        lifeCycle.setDiffusionValidationDate(null);
        lifeCycle.setDiffusionValidationUser(null);
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);

        return srmResourceVersion;
    }

    @Override
    public Object publishInternally(ServiceContext ctx, String urn, Boolean forceLastestFinal) throws MetamacException {
        // Validation
        checkInvocation(urn);

        ProcStatusEnum targetStatus = ProcStatusEnum.INTERNALLY_PUBLISHED;
        Object srmResourceVersion = prePublishResourceInInternallyPublished(ctx, urn, targetStatus);

        // Merge temporal version if is needed
        srmResourceVersion = callback.mergeTemporal(ctx, srmResourceVersion);

        // Update life cycle metadata
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        boolean isTemporalVersionWithExternalPublished = false;
        if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(lifeCycle.getProcStatus())) {
            isTemporalVersionWithExternalPublished = true;
        }
        lifeCycle.setProcStatus(targetStatus);
        lifeCycle.setInternalPublicationDate(new DateTime());
        lifeCycle.setInternalPublicationUser(ctx.getUserId());
        MaintainableArtefact maintainableArtefact = callback.getMaintainableArtefact(srmResourceVersion);
        maintainableArtefact.setFinalLogicClient(Boolean.TRUE);
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);
        srmResourceVersion = callback.publishInternallyConcreteResource(ctx, srmResourceVersion);
        srmResourceVersion = callback.markSrmResourceAsFinal(ctx, srmResourceVersion, forceLastestFinal);

        // Mark categorisations as final
        if (callback.canHaveCategorisations()) {
            List<Categorisation> categorisations = categoriesService.retrieveCategorisationsByArtefact(ctx, urn);
            for (Categorisation categorisation : categorisations) {
                categoriesService.markCategorisationAsFinal(ctx, categorisation.getMaintainableArtefact().getUrn());
            }
        }

        // If the artefact to publishInternally is a temporal version (a merge was performed) and if the Proc Status of original version is PublishExternally, then merge changes to publish externally
        // version
        // if (callback.isTemporalToPublishExternally(ctx, srmResourceVersion)) {
        if (isTemporalVersionWithExternalPublished) {
            publishExternally(ctx, maintainableArtefact.getUrn());
        }

        return srmResourceVersion;
    }

    @Override
    public Object prePublishResourceInInternallyPublished(ServiceContext ctx, String urn, ProcStatusEnum targetStatus) throws MetamacException {
        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToPublishInternally);

        // Validate to publish internally
        checkResourceInInternallyPublished(ctx, urn, srmResourceVersion, targetStatus);

        return srmResourceVersion;
    }

    @Override
    public Object publishExternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        checkInvocation(urn);

        // Retrieve version in specific proc status
        Object srmResourceVersion = callback.retrieveSrmResourceByProcStatus(urn, procStatusToPublishExternally);

        ProcStatusEnum targetStatus = ProcStatusEnum.EXTERNALLY_PUBLISHED;

        // Validate to publish externally
        checkResourceInExternallyPublished(ctx, urn, srmResourceVersion, targetStatus);

        // Start validity and mark as public. Note: is imported, validFrom can not be override
        if (!callback.getMaintainableArtefact(srmResourceVersion).getIsImported()) {
            srmResourceVersion = callback.startSrmResourceValidity(ctx, srmResourceVersion);
        }
        srmResourceVersion = callback.markSrmResourceAsPublic(ctx, srmResourceVersion);
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);

        // Fill validTo in previous externally published versions without end validity. Note: is imported, validTo can not be override
        if (!callback.getMaintainableArtefact(srmResourceVersion).getIsImported()) {
            List<Object> versionsExternallyPublished = callback.findSrmResourceVersionsOfSrmResourceInProcStatus(ctx, srmResourceVersion, ProcStatusEnum.EXTERNALLY_PUBLISHED);
            for (Object versionExternallyPublished : versionsExternallyPublished) {
                MaintainableArtefact maintainableArtefact = callback.getMaintainableArtefact(versionExternallyPublished);
                if (maintainableArtefact.getValidTo() == null) {
                    callback.endSrmResourceValidity(ctx, versionExternallyPublished);
                }
            }
        }

        // Update life cycle metadata to publish externally
        lifeCycle.setProcStatus(targetStatus);
        DateTime externalPublicationDate = callback.getMaintainableArtefact(srmResourceVersion).getValidFrom();
        if (externalPublicationDate == null) {
            externalPublicationDate = new DateTime();
        }
        lifeCycle.setExternalPublicationDate(externalPublicationDate);
        lifeCycle.setExternalPublicationUser(ctx.getUserId());
        srmResourceVersion = callback.updateSrmResource(ctx, srmResourceVersion);

        // Start validity of categorisations and mark as public
        if (callback.canHaveCategorisations()) {
            List<Categorisation> categorisations = categoriesService.retrieveCategorisationsByArtefact(ctx, urn);
            for (Categorisation categorisation : categorisations) {
                categoriesService.startCategorisationValidity(ctx, categorisation.getMaintainableArtefact().getUrn(), null);
                categoriesService.markCategorisationAsPublic(ctx, categorisation.getMaintainableArtefact().getUrn());
            }
        }

        return srmResourceVersion;
    }

    /**
     * Makes validations to sent to production validation. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInProductionValidation(ServiceContext ctx, String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is production validation
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToSendToProductionValidation);
        }
        // There is not pending tasks
        Boolean isJobInvocation = (Boolean) ((ctx.getProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION) != null)
                ? ctx.getProperty(SdmxConstants.SERVICE_CONTEXT_PROP_IS_JOB_INVOCATION)
                : Boolean.FALSE);
        if (srmResourceVersion instanceof ItemSchemeVersion && !isJobInvocation) {
            SdmxSrmValidationUtils.checkArtefactWithoutTaskInBackground((ItemSchemeVersion) srmResourceVersion);
        }
        // Conditions for concrete resource
        callback.checkConcreteResourceInProductionValidation(ctx, srmResourceVersion, targetStatus, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
    /**
     * Makes validations to send to diffusion validation. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInDiffusionValidation(ServiceContext ctx, String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is diffusion validation
        if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToSendToDiffusionValidation);
        }

        // Check other conditions
        checkResourceInProductionValidation(ctx, urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInDiffusionValidation(srmResourceVersion, targetStatus, exceptions);

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
        callback.checkConcreteResourceInRejectProductionValidation(srmResourceVersion, targetStatus, exceptions);

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
        callback.checkConcreteResourceInRejectDiffusionValidation(srmResourceVersion, targetStatus, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish internally. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInInternallyPublished(ServiceContext ctx, String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is publish internally
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToPublishInternally);

            // Check translations
            String locale = srmConfiguration.retrieveLanguageDefault();
            List<MetamacExceptionItem> exceptionItems = callback.checkConcreteResourceTranslations(ctx, srmResourceVersion, locale);
            if (exceptionItems != null) {
                exceptions.addAll(exceptionItems);
            }
        }

        // Check other conditions
        checkResourceInDiffusionValidation(ctx, urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInInternallyPublished(ctx, srmResourceVersion, targetStatus, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish externally. Also revalidates conditions that were checked to go previous statuses.
     */
    private void checkResourceInExternallyPublished(ServiceContext ctx, String urn, Object srmResourceVersion, ProcStatusEnum targetStatus) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check current proc status if target status is publish externally
        if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(targetStatus)) {
            checkProcStatus(srmResourceVersion, procStatusToPublishExternally);
        }

        // Check maintainer is externally published
        if (!SdmxSrmUtils.isAgencySchemeSdmx(urn)) {
            Organisation maintainer = callback.getMaintainableArtefact(srmResourceVersion).getMaintainer();
            OrganisationSchemeVersionMetamac agencyScheme = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(ctx, maintainer.getNameableArtefact().getUrn());
            SrmValidationUtils.checkArtefactExternallyPublished(agencyScheme.getMaintainableArtefact().getUrn(), agencyScheme.getLifeCycleMetadata());
        }

        // Check other conditions
        checkResourceInInternallyPublished(ctx, urn, srmResourceVersion, targetStatus);
        callback.checkConcreteResourceInExternallyPublished(srmResourceVersion, targetStatus, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Checks proc status resource in proc status expected and throws exceptions if it is incorrect
     */
    private void checkProcStatus(Object srmResourceVersion, ProcStatusEnum[] expecteds) throws MetamacException {
        SrmLifeCycleMetadata lifeCycle = callback.getLifeCycleMetadata(srmResourceVersion);
        ProcStatusEnum actual = lifeCycle.getProcStatus();
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
    public interface LifeCycleCallback {

        // Operations to retrieve, find...
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException;
        public Object updateSrmResource(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion);
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion);
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus);

        // Conditions to update state
        public void checkConcreteResourceInProductionValidation(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions)
                throws MetamacException;
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInInternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions);
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions);
        public List<MetamacExceptionItem> checkConcreteResourceTranslations(ServiceContext ctx, Object srmResourceVersion, String locale) throws MetamacException;

        // Validity, final, public, additional actions
        public Object executeBeforeSendProductionValidation(ServiceContext ctx, Object srmResourceVersion);
        public Object publishInternallyConcreteResource(ServiceContext ctx, Object srmResourceVersion);
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException;
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;

        // Other
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds);
        public Boolean canHaveCategorisations();
        public Object mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
        public Boolean isTemporalToPublishExternally(ServiceContext ctx, Object srmResourceVersion) throws MetamacException;
    }
}