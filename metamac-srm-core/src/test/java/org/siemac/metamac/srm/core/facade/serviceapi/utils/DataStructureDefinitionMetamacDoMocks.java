package org.siemac.metamac.srm.core.facade.serviceapi.utils;

import java.util.UUID;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDoMocks;

public class DataStructureDefinitionMetamacDoMocks extends ConceptsDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamac() {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = new DataStructureDefinitionVersionMetamac();
        
        return dataStructureDefinitionVersionMetamac;
    }
    
}
