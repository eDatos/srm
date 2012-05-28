package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.ItemScheme;
import org.siemac.metamac.srm.core.concept.domain.ConceptScheme;
import org.siemac.metamac.srm.core.error.ServiceExceptionParameters;

public class InvocationValidator {

    public static void checkFindConceptSchemeById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        
        checkConceptScheme(conceptScheme, exceptions);
        
        ExceptionUtils.throwIfException(exceptions);
    }


    public static void checkUpdateConceptScheme(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public static void checkDeleteConceptScheme(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkFindAllConceptSchemes(List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public static void checkFindConceptSchemeByCondition(MetamacCriteria criteria, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    // ------------------------------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------------------------------

    public static void checkFindConceptById(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkCreateConcept(Long conceptSchemeId, ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public static void  checkUpdateConcept(ConceptScheme entity, List<MetamacExceptionItem> exceptions) throws MetamacException {
     // TODO
    }

    public void checkDeleteConcept(Long id, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(id, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }

    public static void checkFindConceptSchemeConcepts(Long conceptSchemeId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(conceptSchemeId, ServiceExceptionParameters.CONCEPT_SCHEME_ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);

    }
    
    private static void checkConceptScheme(ConceptScheme conceptScheme, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(conceptScheme, ServiceExceptionParameters.CONCEPT_SCHEME, exceptions);
        checkItemScheme(conceptScheme.getItemScheme(), exceptions);
    }


    private static void checkItemScheme(ItemScheme itemScheme, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(itemScheme, ServiceExceptionParameters.ITEM_SCHEME, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getIdLogic(), ServiceExceptionParameters.ITEM_SCHEME_ID_LOGIC, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getVersionLogic(), ServiceExceptionParameters.ITEM_SCHEME_VERSION_LOGIC, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getName(), ServiceExceptionParameters.ITEM_SCHEME_NAME, exceptions);
        ValidationUtils.checkMetadataRequired(itemScheme.getFinalLogic(), ServiceExceptionParameters.ITEM_SCHEM_FINAL_LOGIC, exceptions);
        checkMantainer(itemScheme.getMaintainer(), exceptions);
    }
    
    private static void checkMantainer(ExternalItemBt maintainer, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkMetadataRequired(maintainer.getCodeId(), ServiceExceptionParameters.MANTAINER_CODE_ID, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getType(), ServiceExceptionParameters.MANTAINER_TYPE, exceptions);
        ValidationUtils.checkMetadataRequired(maintainer.getUriInt(), ServiceExceptionParameters.MANTAINER_URI_INT, exceptions);
    }
    
   

}
