package org.siemac.metamac.srm.web.server.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;

import com.ibm.icu.text.DecimalFormat;

public class ConceptSchemeService {

    private static List<ConceptSchemeMetamacDto> conceptSchemeList = null;

    static {
        fillConceptSchemes();
    }

    public static ConceptSchemePage findAllConceptSchemes(int firstResult, int maxResults) throws MetamacException {
        ConceptSchemePage pageResult = new ConceptSchemePage();
        List<ConceptSchemeMetamacDto> concepts = new ArrayList<ConceptSchemeMetamacDto>();
        for (int i = firstResult; i < firstResult + maxResults && i < conceptSchemeList.size(); i++) {
            concepts.add(conceptSchemeList.get(i));
        }
        pageResult.resultList = concepts;
        pageResult.totalResults = conceptSchemeList.size();
        return pageResult;
    }

    public static ConceptSchemeMetamacDto retriveConceptSchemeByIdLogic(String idLogic) throws MetamacException {
        for (ConceptSchemeMetamacDto scheme : conceptSchemeList) {
            if (scheme.getCode().equals(idLogic)) {
                return scheme;
            }
        }
        return null;
    }

    public static void deleteConceptScheme(Long id) throws MetamacException {
        int chosen = getConceptSchemeIndexById(id);
        conceptSchemeList.remove(chosen);
    }

    public static ConceptSchemeMetamacDto saveConceptScheme(ConceptSchemeMetamacDto schemeDto) throws MetamacException {
        if (schemeDto.getUuid() == null) {
            schemeDto.setUuid(UUID.randomUUID().toString());
            schemeDto.setId(new Random().nextInt(Integer.MAX_VALUE) + 1L);
            schemeDto.setUrn(UUID.randomUUID().toString());
            schemeDto.setUri(UUID.randomUUID().toString());
            schemeDto.setVersionLogic("01.000");
            schemeDto.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
            conceptSchemeList.add(schemeDto);
        } else {
            int index = getConceptSchemeIndexById(schemeDto.getId());
            conceptSchemeList.set(index, schemeDto);
        }
        return schemeDto;
    }

    /* Life cycle */
    public static ConceptSchemeMetamacDto sendToProductionValidation(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION);
        return scheme;
    }

    public static ConceptSchemeMetamacDto sendToDiffusionValidation(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION);
        return scheme;
    }

    public static ConceptSchemeMetamacDto reject(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED);
        return scheme;
    }

    public static ConceptSchemeMetamacDto publishInternally(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED);
        return scheme;
    }

    public static ConceptSchemeMetamacDto publishExternally(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setValidFrom(new Date());
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED);
        return scheme;
    }

    public static ConceptSchemeMetamacDto versioning(Long id) throws MetamacException {
        ConceptSchemeMetamacDto scheme = conceptSchemeList.get(0);
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        return scheme;
    }

    private static int getConceptSchemeIndexById(Long id) {
        int foundIndex = -1;
        for (int i = 0; i < conceptSchemeList.size() && foundIndex < 0; i++) {
            if (conceptSchemeList.get(i).getId().equals(id)) {
                foundIndex = i;
            }
        }
        return foundIndex;
    }

    private static void fillConceptSchemes() {
        conceptSchemeList = new LinkedList<ConceptSchemeMetamacDto>();
        for (int i = 0; i < 100; i++) {
            ConceptSchemeMetamacDto conceptScheme = new ConceptSchemeMetamacDto();
            conceptScheme.setId(Long.valueOf(i * 1));
            conceptScheme.setUuid(UUID.randomUUID().toString());
            conceptScheme.setUrn("prefix=" + UUID.randomUUID().toString());
            conceptScheme.setUri(UUID.randomUUID().toString());
            conceptScheme.setVersionLogic("01.000");
            conceptScheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
            DecimalFormat nformat = new DecimalFormat("0000");
            conceptScheme.setCode("SCH" + nformat.format(i));

            InternationalStringDto intStr = new InternationalStringDto();
            LocalisedStringDto locStr = new LocalisedStringDto();
            locStr.setLocale("es");
            locStr.setLabel("SCH " + nformat.format(i));
            intStr.addText(locStr);
            conceptScheme.setName(intStr);

            conceptSchemeList.add(conceptScheme);
        }
    }

    public static class ConceptSchemePage {

        public List<ConceptSchemeMetamacDto> resultList;
        public Integer                       totalResults;

        public ConceptSchemePage() {
            // TODO Auto-generated constructor stub
        }
    }
}
