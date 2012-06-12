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
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.domain.concept.enums.domain.ConceptSchemeProcStatusEnum;

import com.ibm.icu.text.DecimalFormat;

public class ConceptSchemeService {

    private static List<ConceptSchemeDto> conceptSchemeList = null;

    static {
        fillConceptSchemes();
    }

    public static ConceptSchemePage findAllConceptSchemes(int firstResult, int maxResults) throws MetamacException {
        ConceptSchemePage pageResult = new ConceptSchemePage();
        List<ConceptSchemeDto> concepts = new ArrayList<ConceptSchemeDto>();
        for (int i = firstResult; i < firstResult + maxResults && i < conceptSchemeList.size(); i++) {
            concepts.add(conceptSchemeList.get(i));
        }
        pageResult.resultList = concepts;
        pageResult.totalResults = conceptSchemeList.size();
        return pageResult;
    }

    public static ConceptSchemeDto retriveConceptSchemeByIdLogic(String idLogic) throws MetamacException {
        for (ConceptSchemeDto scheme : conceptSchemeList) {
            if (scheme.getIdLogic().equals(idLogic)) {
                return scheme;
            }
        }
        return null;
    }

    public static void deleteConceptScheme(Long id) throws MetamacException {
        int chosen = getConceptSchemeIndexById(id);
        conceptSchemeList.remove(chosen);
    }

    public static ConceptSchemeDto saveConceptScheme(ConceptSchemeDto schemeDto) throws MetamacException {
        if (schemeDto.getUuid() == null) {
            schemeDto.setUuid(UUID.randomUUID().toString());
            schemeDto.setId(new Random().nextInt(Integer.MAX_VALUE)+1L);
            schemeDto.setUrn(UUID.randomUUID().toString());
            schemeDto.setUri(UUID.randomUUID().toString());
            schemeDto.setVersionLogic("01.000");
            schemeDto.setProcStatus(ConceptSchemeProcStatusEnum.DRAFT);
            conceptSchemeList.add(schemeDto);
        } else {
            int index = getConceptSchemeIndexById(schemeDto.getId());
            conceptSchemeList.set(index, schemeDto);
        }
        return schemeDto;
    }

    /* Life cycle */
    public static ConceptSchemeDto sendToPendingPublication(Long id) throws MetamacException {
        int index = getConceptSchemeIndexById(id);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        scheme.setProcStatus(ConceptSchemeProcStatusEnum.PENDING_PUBLICATION);
        return scheme;
    }

    public static ConceptSchemeDto reject(Long id) throws MetamacException {
        int index = getConceptSchemeIndexById(id);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        scheme.setProcStatus(ConceptSchemeProcStatusEnum.VALIDATION_REJECTED);
        return scheme;
    }

    public static ConceptSchemeDto publishInternally(Long id) throws MetamacException {
        int index = getConceptSchemeIndexById(id);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        scheme.setProcStatus(ConceptSchemeProcStatusEnum.INTERNALLY_PUBLISHED);
        return scheme;
    }

    public static ConceptSchemeDto publishExternally(Long id) throws MetamacException {
        int index = getConceptSchemeIndexById(id);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        scheme.setValidFrom(new Date());
        scheme.setProcStatus(ConceptSchemeProcStatusEnum.EXTERNALLY_PUBLISHED);
        return scheme;
    }

    public static ConceptSchemeDto versioning(Long id) throws MetamacException {
        int index = getConceptSchemeIndexById(id);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        scheme.setProcStatus(ConceptSchemeProcStatusEnum.DRAFT);
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
        conceptSchemeList = new LinkedList<ConceptSchemeDto>();
        for (int i = 0; i < 100; i++) {
            ConceptSchemeDto conceptScheme = new ConceptSchemeDto();
            conceptScheme.setId(Long.valueOf(i*1));
            conceptScheme.setUuid(UUID.randomUUID().toString());
            conceptScheme.setUrn(UUID.randomUUID().toString());
            conceptScheme.setUri(UUID.randomUUID().toString());
            conceptScheme.setVersionLogic("01.000");
            conceptScheme.setProcStatus(ConceptSchemeProcStatusEnum.DRAFT);
            DecimalFormat nformat = new DecimalFormat("0000");
            conceptScheme.setIdLogic("SCH" + nformat.format(i));

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

        public List<ConceptSchemeDto> resultList;
        public Integer                totalResults;

        public ConceptSchemePage() {
            // TODO Auto-generated constructor stub
        }
    }
}
