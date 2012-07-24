package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import java.util.List;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.AnnotableArtefact;
import org.siemac.metamac.srm.core.base.domain.Annotation;
import org.siemac.metamac.srm.core.base.domain.IdentifiableArtefact;
import org.siemac.metamac.srm.core.base.domain.ItemSchemeVersion;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.domain.NameableArtefact;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;

public class BaseInvocationValidator {

    // ------------------------------------------------------------------------------------
    // ITEM SCHEME
    // ------------------------------------------------------------------------------------
    public static void checkItemScheme(ItemSchemeVersion itemSchemeVersion, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(itemSchemeVersion, ServiceExceptionParameters.ITEM_SCHEME, exceptions);
        if (itemSchemeVersion == null) {
            return;
        }
        ValidationUtils.checkParameterRequired(itemSchemeVersion.getItemScheme(), ServiceExceptionParameters.ITEM_SCHEME, exceptions);
        if (itemSchemeVersion.getItemScheme() == null) {
            return;
        }
        if (itemSchemeVersion.getId() == null) {
            ValidationUtils.checkMetadataEmpty(itemSchemeVersion.getItems(), ServiceExceptionParameters.ITEM_SCHEME_ITEMS, exceptions);
        }

        checkMaintainableArtefact(itemSchemeVersion.getMaintainableArtefact(), exceptions);
    }

    // ------------------------------------------------------------------------------------
    // MAINTAINABLE ARTEFACT
    // ------------------------------------------------------------------------------------
    public static void checkMaintainableArtefact(MaintainableArtefact maintainableArtefact, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(maintainableArtefact, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT, exceptions);
        if (maintainableArtefact == null) {
            return;
        }
        if (maintainableArtefact.getId() != null) {
            ValidationUtils.checkMetadataRequired(maintainableArtefact.getVersionLogic(), ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_VERSION_LOGIC, exceptions);
        } else {
            // metadata must be empty when the entity is created
            ValidationUtils.checkMetadataEmpty(maintainableArtefact.getVersionLogic(), ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_VERSION_LOGIC, exceptions);
            ValidationUtils.checkMetadataEmpty(maintainableArtefact.getValidFrom(), ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_VALID_FROM, exceptions);
            ValidationUtils.checkMetadataEmpty(maintainableArtefact.getValidTo(), ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_VALID_TO, exceptions);
        }
        checkNameableArtefact(maintainableArtefact, exceptions);
        checkMantainer(maintainableArtefact.getMaintainer(), exceptions);
    }

    // ------------------------------------------------------------------------------------
    // NAMEABLE ARTEFACT
    // ------------------------------------------------------------------------------------
    public static void checkNameableArtefact(NameableArtefact nameableArtefact, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(nameableArtefact, ServiceExceptionParameters.NAMEABLE_ARTEFACT, exceptions);
        if (nameableArtefact == null) {
            return;
        }
        ValidationUtils.checkMetadataRequired(nameableArtefact.getName(), ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME, exceptions);
        checkIdentifiableArtefact(nameableArtefact, exceptions);
    }

    // ------------------------------------------------------------------------------------
    // IDENTIFIABLE ARTEFACT
    // ------------------------------------------------------------------------------------
    public static void checkIdentifiableArtefact(IdentifiableArtefact identifiableArtefact, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(identifiableArtefact, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT, exceptions);
        if (identifiableArtefact == null) {
            return;
        }
        ValidationUtils.checkMetadataRequired(identifiableArtefact.getIdLogic(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_ID_LOGIC, exceptions);
        if (identifiableArtefact.getIdLogic() != null && !CoreCommonUtil.isSemanticIdentifier(identifiableArtefact.getIdLogic())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_ID_LOGIC));
        }
        // TODO uri?
        checkAnnotableArtefact(identifiableArtefact, exceptions);
    }

    // ------------------------------------------------------------------------------------
    // ANNOTABLE ARTEFACT
    // ------------------------------------------------------------------------------------
    public static void checkAnnotableArtefact(AnnotableArtefact annotableArtefact, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(annotableArtefact, ServiceExceptionParameters.ANNOTABLE_ARTEFACT, exceptions);
        if (annotableArtefact == null) {
            return;
        }
        for (Annotation annotation : annotableArtefact.getAnnotations()) {
            checkAnnotation(annotation, exceptions);
        }
    }

    public static void checkAnnotation(Annotation annotation, List<MetamacExceptionItem> exceptions) {
        // nothing
    }

    // ------------------------------------------------------------------------------------
    // MANTAINER
    // ------------------------------------------------------------------------------------

    public static void checkMantainer(ExternalItem maintainer, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(maintainer, ServiceExceptionParameters.MANTAINER, exceptions);
        if (maintainer == null) {
            return;
        }
        ValidationUtils.checkMetadataRequired(maintainer.getCode(), ServiceExceptionParameters.MANTAINER_CODE, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getType(), ServiceExceptionParameters.MANTAINER_TYPE, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getUri(), ServiceExceptionParameters.MANTAINER_URI, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getUrn(), ServiceExceptionParameters.MANTAINER_URN, exceptions);
    }
}
