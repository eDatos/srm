package org.siemac.metamac.srm.core.code.serviceimpl.utils;

import java.util.ArrayList;
import java.util.Map;

import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;

public class CodesUtils {

    public static boolean addCodelistOpennessVisualisationTemporalToItemScheme(CodelistVersionMetamac codelistTemporalVersion, CodelistVersionMetamac codelist,
            InternationalStringRepository internationalStringRepository) {
        boolean thereAreNewOpeness = false;
        Map<String, CodelistOpennessVisualisation> currentOpenessMap = SrmServiceUtils.createMapOfCodelistOpennessVisualisationByOriginalUrn(codelist.getOpennessVisualisations());

        for (CodelistOpennessVisualisation codelistOpenessVisualizationTemp : new ArrayList<CodelistOpennessVisualisation>(codelistTemporalVersion.getOpennessVisualisations())) {
            String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(codelistOpenessVisualizationTemp.getNameableArtefact().getUrn());
            if (!currentOpenessMap.containsKey(urnFromTemporal)) {
                // Add
                codelist.addOpennessVisualisation(codelistOpenessVisualizationTemp);

                codelistOpenessVisualizationTemp.getNameableArtefact().setUrn(urnFromTemporal);
                codelistOpenessVisualizationTemp.getNameableArtefact().setUrnProvider(urnFromTemporal);
                thereAreNewOpeness = true;
            } else {
                // Update International Strings is permitted
                BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCodelistOpennessVisualisation(currentOpenessMap.get(urnFromTemporal), codelistOpenessVisualizationTemp,
                        internationalStringRepository);
            }
        }
        return thereAreNewOpeness;
    }

    public static boolean addCodelistOrderVisualisationTemporalToItemScheme(CodelistVersionMetamac itemSchemeTemporalVersion, CodelistVersionMetamac itemSchemeVersion,
            InternationalStringRepository internationalStringRepository) {
        boolean thereAreNewOpeness = false;
        Map<String, CodelistOrderVisualisation> currentOrderMap = SrmServiceUtils.createMapOfCodelistOrderVisualisationByOriginalUrn(itemSchemeVersion.getOrderVisualisations());

        for (CodelistOrderVisualisation codelistOrderVisualisationTemp : new ArrayList<CodelistOrderVisualisation>(itemSchemeTemporalVersion.getOrderVisualisations())) {
            String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(codelistOrderVisualisationTemp.getNameableArtefact().getUrn());
            if (!currentOrderMap.containsKey(urnFromTemporal)) {
                // Add
                itemSchemeVersion.addOrderVisualisation(codelistOrderVisualisationTemp);

                codelistOrderVisualisationTemp.getNameableArtefact().setUrn(urnFromTemporal);
                codelistOrderVisualisationTemp.getNameableArtefact().setUrnProvider(urnFromTemporal);
                thereAreNewOpeness = true;
            } else {
                // Update International Strings is permitted
                BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCodelistOrderVisualisation(currentOrderMap.get(urnFromTemporal), codelistOrderVisualisationTemp,
                        internationalStringRepository);
            }
        }
        return thereAreNewOpeness;
    }

}
