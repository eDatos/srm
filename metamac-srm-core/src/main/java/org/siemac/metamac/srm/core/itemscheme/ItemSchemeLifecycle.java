package org.siemac.metamac.srm.core.itemscheme;

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
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;

public class ItemSchemeLifecycle {

    // TODO eliminar el enumerado validation_rejected y poner una marca?? así se evitarían los []
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToSendToProductionValidation = {ItemSchemeMetamacProcStatusEnum.DRAFT, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED};
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToSendToDiffusionValidation  = {ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToRejectProductionValidation = {ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION};
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToRejectDiffusionValidation  = {ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToPublishInternally          = {ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION};
    private static final ItemSchemeMetamacProcStatusEnum[] procStatusToPublishExternally          = {ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED};

    private ItemSchemeLifecycleCallback                    callback                               = null;

    public ItemSchemeLifecycle(ItemSchemeLifecycleCallback callback) {
        this.callback = callback;
    }

    public ItemSchemeVersion sendToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkSendItemSchemeToProductionValidation(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToSendToProductionValidation);

        // Validate to send to production
        checkItemSchemeToSendToProductionValidation(urn, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);

        return itemSchemeVersion;
    }

    public ItemSchemeVersion sendToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkSendItemSchemeToDiffusionValidation(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToSendToDiffusionValidation);

        // Validate to send to Diffusion
        checkItemSchemeToSendToDiffusionValidation(urn, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);

        return itemSchemeVersion;
    }

    public ItemSchemeVersion rejectProductionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkRejectItemSchemeProductionValidation(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToRejectProductionValidation);

        // Validate to reject
        checkItemSchemeToRejectProductionValidation(urn, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);

        return itemSchemeVersion;
    }

    public ItemSchemeVersion rejectDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkRejectItemSchemeDiffusionValidation(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToRejectDiffusionValidation);

        // Validate to reject
        checkItemSchemeToRejectDiffusionValidation(urn, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);

        return itemSchemeVersion;
    }

    public ItemSchemeVersion publishInternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkPublishInternallyItemScheme(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToPublishInternally);

        // Validate to publish internally
        checkItemSchemeToPublishInternally(ctx, urn, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);
        itemSchemeVersion = callback.markItemSchemeAsFinal(ctx, itemSchemeVersion);

        return itemSchemeVersion;
    }

    public ItemSchemeVersion publishExternally(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ItemSchemeInvocationValidator.checkPublishExternallyItemScheme(urn, null);

        // Retrieve version in specific proc status
        ItemSchemeVersion itemSchemeVersion = callback.retrieveItemSchemeByProcStatus(urn, procStatusToPublishExternally);

        // Validate to publish externally
        checkItemSchemeToPublishExternally(ctx, urn, itemSchemeVersion);

        // Start concept scheme validity
        itemSchemeVersion = callback.startItemSchemeValidity(ctx, itemSchemeVersion);

        // Update proc status
        callback.setItemSchemeProcStatusAndInformationStatus(itemSchemeVersion, ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED, ctx.getUserId());
        itemSchemeVersion = callback.updateItemScheme(itemSchemeVersion);

        // Fill validTo in previous internally published versions
        List<ItemSchemeVersion> versionsExternallyPublished = callback.findItemSchemeVersionsOfItemSchemeInProcStatus(ctx, itemSchemeVersion.getItemScheme(),
                ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        for (ItemSchemeVersion versionExternallyPublished : versionsExternallyPublished) {
            if (versionExternallyPublished.getId().equals(itemSchemeVersion.getId())) {
                continue;
            }
            if (versionExternallyPublished.getMaintainableArtefact().getValidTo() == null) {
                versionExternallyPublished.getMaintainableArtefact().setValidTo(callback.getExternalPublicationDateMetadataValue(itemSchemeVersion));
                callback.updateItemScheme(versionExternallyPublished);
            }
        }

        return itemSchemeVersion;
    }

    /**
     * Makes validations to sent to production validation
     */
    private void checkItemSchemeToSendToProductionValidation(String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToSendToProductionValidation);

        // Check other conditions
        checkConditionsSinceSendToProductionValidation(itemSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to sent to diffusion validation
     */
    private void checkItemSchemeToSendToDiffusionValidation(String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToSendToDiffusionValidation);

        // Check other conditions
        checkConditionsSinceSendToDiffusionValidation(itemSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject production validation
     */
    private void checkItemSchemeToRejectProductionValidation(String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToRejectProductionValidation);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to reject diffusion validation
     */
    private void checkItemSchemeToRejectDiffusionValidation(String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToRejectDiffusionValidation);

        // nothing more

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish internally
     */
    private void checkItemSchemeToPublishInternally(ServiceContext ctx, String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToPublishInternally);

        // Additional conditions of concrete classes
        callback.checkAdditionalConditionsToPublishInternally(itemSchemeVersion, exceptions);

        // Check other conditions
        checkConditionsSincePublishInternally(itemSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Makes validations to publish externally
     */
    private void checkItemSchemeToPublishExternally(ServiceContext ctx, String urn, ItemSchemeVersion itemSchemeVersion) throws MetamacException {

        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

        // Check proc status
        checkProcStatus(itemSchemeVersion, callback.getProcStatusMetadataValue(itemSchemeVersion), procStatusToPublishExternally);

        // Additional conditions of concrete classes
        callback.checkAdditionalConditionsToPublishExternally(itemSchemeVersion, exceptions);
        
        // Check other conditions
        checkConditionsSincePublishExternally(itemSchemeVersion, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    /**
     * Checks proc status of item scheme in proc status expected and throws exceptions if it is incorrect
     */
    private void checkProcStatus(ItemSchemeVersion itemSchemeVersion, ItemSchemeMetamacProcStatusEnum expected, ItemSchemeMetamacProcStatusEnum[] actuals) throws MetamacException {
        if (!ArrayUtils.contains(actuals, expected)) {
            String[] procStatusString = SrmServiceUtils.procStatusEnumToString(actuals);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_PROC_STATUS)
                    .withMessageParameters(itemSchemeVersion.getMaintainableArtefact().getUrn(), procStatusString).build();
        }
    }

    /**
     * Checks common conditions of item schemes to send to production validation
     */
    private void checkConditionsSinceSendToProductionValidation(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {

        // Metadata required
        ValidationUtils.checkMetadataRequired(itemSchemeVersion.getIsPartial(), ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL, exceptions);

        // Conditions for concrete resource
        callback.checkAdditionalConditionsSinceSendToProductionValidation(itemSchemeVersion, exceptions);
    }

    /**
     * Checks common conditions of item schemes to check states since item scheme was sent to diffusion validation
     */
    private void checkConditionsSinceSendToDiffusionValidation(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToProductionValidation(itemSchemeVersion, exceptions);
    }

    /**
     * Checks common conditions of item schemes to check states since item scheme was internally published
     */
    private void checkConditionsSincePublishInternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSinceSendToDiffusionValidation(itemSchemeVersion, exceptions);
    }

    /**
     * Checks common conditions of item schemes to check states since item scheme was externally published
     */
    private void checkConditionsSincePublishExternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {
        checkConditionsSincePublishInternally(itemSchemeVersion, exceptions);
    }

    /**
     * Callback to implement the specific operations in concrete class of item scheme (concepts, organisations...)
     */
    public static interface ItemSchemeLifecycleCallback {

        public ItemSchemeVersion retrieveItemSchemeByProcStatus(String urn, ItemSchemeMetamacProcStatusEnum[] procStatus) throws MetamacException;
        public ItemSchemeVersion updateItemScheme(ItemSchemeVersion itemSchemeVersion);
        public ItemSchemeMetamacProcStatusEnum getProcStatusMetadataValue(ItemSchemeVersion itemSchemeVersion);
        public DateTime getExternalPublicationDateMetadataValue(ItemSchemeVersion itemSchemeVersion);
        public void setItemSchemeProcStatusAndInformationStatus(ItemSchemeVersion itemSchemeVersion, ItemSchemeMetamacProcStatusEnum newProcStatus, String user);
        public void checkAdditionalConditionsSinceSendToProductionValidation(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions);
        public void checkAdditionalConditionsToPublishInternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions);
        public void checkAdditionalConditionsToPublishExternally(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions);
        public ItemSchemeVersion markItemSchemeAsFinal(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException;
        public ItemSchemeVersion startItemSchemeValidity(ServiceContext ctx, ItemSchemeVersion itemSchemeVersion) throws MetamacException;
        List<ItemSchemeVersion> findItemSchemeVersionsOfItemSchemeInProcStatus(ServiceContext ctx, ItemScheme itemScheme, ItemSchemeMetamacProcStatusEnum... procStatus);
    }
}
