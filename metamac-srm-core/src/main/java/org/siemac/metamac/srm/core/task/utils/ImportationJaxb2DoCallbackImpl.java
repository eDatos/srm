package org.siemac.metamac.srm.core.task.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureJaxb2DoCallback;
import com.arte.statistic.sdmx.srm.core.task.serviceimpl.utils.ImportationJaxb2DoCallback;

@Component(ImportationJaxb2DoCallbackImpl.BEAN_ID)
public class ImportationJaxb2DoCallbackImpl implements ImportationJaxb2DoCallback {

    public static final String           BEAN_ID = "importationMetamacJaxb2DoCallback";

    @Autowired
    @Qualifier("organisationsMetamacJaxb2DoCallback")
    private OrganisationsJaxb2DoCallback organisationsJaxb2DoCallback;

    @Autowired
    @Qualifier("categoriesMetamacJaxb2DoCallback")
    private CategoriesJaxb2DoCallback    categoriesJaxb2DoCallback;

    @Autowired
    @Qualifier("codesMetamacJaxb2DoCallback")
    private CodesJaxb2DoCallback         codesJaxb2DoCallback;

    @Autowired
    @Qualifier("conceptsMetamacJaxb2DoCallback")
    private ConceptsJaxb2DoCallback      conceptsJaxb2DoCallback;

    @Autowired
    @Qualifier("structureMetamacJaxb2DoCallback")
    private StructureJaxb2DoCallback     structureJaxb2DoCallback;

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

    @Override
    public String getBeanName() {
        return BEAN_ID;
    }
}
