package org.siemac.metamac.srm.core.importation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.common.mapper.CommonJaxb2DoMapper;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.importation.ImportationJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureJaxb2DoCallback;

@Component("importationWrapper")
public class ImportationMetamacJaxb2DoCallbackWrapperImpl implements ImportationJaxb2DoCallback {

    @Autowired
    @Qualifier("organisationsJaxb2DoCallback")
    private OrganisationsJaxb2DoCallback organisationsJaxb2DoCallback;

    @Autowired
    @Qualifier("categoriesJaxb2DoCallback")
    private CategoriesJaxb2DoCallback    categoriesJaxb2DoCallback;

    @Autowired
    @Qualifier("codesJaxb2DoCallback")
    private CodesJaxb2DoCallback         codesJaxb2DoCallback;

    @Autowired
    @Qualifier("conceptsJaxb2DoCallback")
    private ConceptsJaxb2DoCallback      conceptsJaxb2DoCallback;

    @Autowired
    @Qualifier("structureJaxb2DoCallback")
    private StructureJaxb2DoCallback     structureJaxb2DoCallback;

    @Autowired
    private CommonJaxb2DoMapper          commonJaxb2DoMapper;

    @Override
    public OrganisationsJaxb2DoCallback getOrganisationsJaxb2DoCallback() {
        return organisationsJaxb2DoCallback;
    }

    @Override
    public CategoriesJaxb2DoCallback getCategoriesJaxb2DoCallback() {
        return categoriesJaxb2DoCallback;
    }

    @Override
    public CodesJaxb2DoCallback getCodesJaxb2DoCallback() {
        return codesJaxb2DoCallback;
    }

    @Override
    public ConceptsJaxb2DoCallback getConceptsJaxb2DoCallback() {
        return conceptsJaxb2DoCallback;
    }

    @Override
    public StructureJaxb2DoCallback getStructureJaxb2DoCallback() {
        return structureJaxb2DoCallback;
    }

    public CommonJaxb2DoMapper getCommonJaxb2DoMapper() {
        return commonJaxb2DoMapper;
    }

}
