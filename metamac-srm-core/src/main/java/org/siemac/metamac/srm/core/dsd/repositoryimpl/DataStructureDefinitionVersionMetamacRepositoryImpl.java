package org.siemac.metamac.srm.core.dsd.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.serviceimpl.SrmServiceUtils;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for DataStructureDefinitionVersionMetamac
 */
@Repository("dataStructureDefinitionVersionMetamacRepository")
public class DataStructureDefinitionVersionMetamacRepositoryImpl extends DataStructureDefinitionVersionMetamacRepositoryBase {

    public DataStructureDefinitionVersionMetamacRepositoryImpl() {
    }

    @Override
    public DataStructureDefinitionVersionMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<DataStructureDefinitionVersionMetamac> result = findByQuery("from DataStructureDefinitionVersionMetamac where maintainableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public DataStructureDefinitionVersionMetamac retrieveDataStructureDefinitionVersionByProcStatus(String urn, ItemSchemeMetamacProcStatusEnum[] procStatusArray) throws MetamacException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        parameters.put("procStatus", SrmServiceUtils.procStatusEnumToList(procStatusArray));
        List<DataStructureDefinitionVersionMetamac> result = findByQuery("from DataStructureDefinitionVersionMetamac where maintainableArtefact.urn = :urn and procStatus in (:procStatus)", parameters, 1);
        if (result == null || result.isEmpty()) {
            // check concept scheme exists to throws specific exception
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = findByUrn(urn);
            if (dataStructureDefinitionVersion == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(urn).build();
            } else {
                // if exists, throw exception about wrong proc status
                String[] procStatusString = SrmServiceUtils.procStatusEnumToString(procStatusArray);
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WRONG_PROC_STATUS).withMessageParameters(urn, procStatusString).build();

            }
        }
        return result.get(0);
    }

}
