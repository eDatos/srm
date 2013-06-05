package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import java.util.ArrayList;
import java.util.Map;

import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;

public class CategorisationsUtils {

    public static boolean addCategorisationsTemporalToItemScheme(ItemSchemeVersion itemSchemeTemporalVersion, ItemSchemeVersion itemSchemeVersion) {
        boolean thereAreNewCategorisations = false;
        Map<String, Categorisation> currentCategorisationMap = SrmServiceUtils.createMapOfCategorisationsByUrn(itemSchemeVersion.getMaintainableArtefact().getCategorisations());
        for (Categorisation categorisationTemp : new ArrayList<Categorisation>(itemSchemeTemporalVersion.getMaintainableArtefact().getCategorisations())) {
            String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(categorisationTemp.getMaintainableArtefact().getUrn());
            if (!currentCategorisationMap.containsKey(urnFromTemporal)) {
                // Add
                itemSchemeVersion.getMaintainableArtefact().addCategorisation(categorisationTemp);

                categorisationTemp.getMaintainableArtefact().setUrn(urnFromTemporal);
                categorisationTemp.getMaintainableArtefact().setUrnProvider(urnFromTemporal);
                thereAreNewCategorisations = true;
            }
        }
        return thereAreNewCategorisations;
    }
}
