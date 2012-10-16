package org.siemac.metamac.srm.core.category.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CategorySchemeVersionMetamac
 */
@Repository("categorySchemeVersionMetamacRepository")
public class CategorySchemeVersionMetamacRepositoryImpl extends CategorySchemeVersionMetamacRepositoryBase {

    public CategorySchemeVersionMetamacRepositoryImpl() {
    }

    @Override
    public CategorySchemeVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategorySchemeVersionMetamac> result = findByQuery("from CategorySchemeVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public CategorySchemeVersionMetamac retrieveCategorySchemeVersionByProcStatus(String urn, ProcStatusEnum[] procStatusArray) throws MetamacException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<CategorySchemeVersionMetamac> result = findByQuery("from CategorySchemeVersionMetamac where maintainableArtefact.urn = :urn and lifeCycleMetadata.procStatus in (:procStatus)",
                parameters, 1);
        if (result == null || result.isEmpty()) {
            // check scheme exists to throws specific exception
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = findByUrn(urn);
            if (categorySchemeVersionMetamac == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND).withMessageParameters(urn).build();
            } else {
                // if exists, throw exception about wrong proc status
                String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatusArray);
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CATEGORY_SCHEME_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();

            }
        }
        return result.get(0);
    }
}
