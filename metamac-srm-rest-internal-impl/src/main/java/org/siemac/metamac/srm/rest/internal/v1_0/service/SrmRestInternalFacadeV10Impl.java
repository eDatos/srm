package org.siemac.metamac.srm.rest.internal.v1_0.service;

import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.v2_1.domain.jaxb.common.TextType;

@Service("srmRestInternalFacadeV10")
public class SrmRestInternalFacadeV10Impl implements SrmRestInternalFacadeV10 {

    @Override
    public ConceptScheme retrieveConceptSchemeById(String agencyID, String resourceID, String version) {
        ConceptScheme conceptScheme = new ConceptScheme();
        conceptScheme.setAgencyID("agencyID");
        conceptScheme.setType(ConceptSchemeType.OPERATION);
        TextType textType = new TextType();
        textType.setLang("es");
        textType.setValue("textType");
        conceptScheme.getDescriptions().add(textType);
        return conceptScheme;
    }
}