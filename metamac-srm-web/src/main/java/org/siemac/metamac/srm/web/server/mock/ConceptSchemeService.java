package org.siemac.metamac.internal.web.server.mock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

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

    public static ConceptSchemeDto retriveConceptSchemeByUuid(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        return index >= 0 ? conceptSchemeList.get(index) : null;
    }

    public static void deleteConceptScheme(String uuid) throws MetamacException {
        int chosen = getConceptSchemeIndexByUuid(uuid);
        conceptSchemeList.remove(chosen);
    }

    public static ConceptSchemeDto saveConceptScheme(ConceptSchemeDto schemeDto) throws MetamacException {
        if (schemeDto.getUuid() == null) {
            schemeDto.setUuid(UUID.randomUUID().toString());
            conceptSchemeList.add(schemeDto);
        } else {
            int index = getConceptSchemeIndexByUuid(schemeDto.getUuid());
            conceptSchemeList.set(index, schemeDto);
        }
        return schemeDto;
    }

    /* Lif cycle */
    public static ConceptSchemeDto sendToPendingPublication(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        // TODO: Change procstatus
        return scheme;
    }

    public static ConceptSchemeDto reject(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        // TODO: Change procstatus
        return scheme;
    }

    public static ConceptSchemeDto publishInternally(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        // TODO: Change procstatus
        return scheme;
    }

    public static ConceptSchemeDto publishExternally(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        // TODO: Change procstatus
        return scheme;
    }

    public static ConceptSchemeDto versioning(String uuid) throws MetamacException {
        int index = getConceptSchemeIndexByUuid(uuid);
        ConceptSchemeDto scheme = conceptSchemeList.get(index);
        // TODO: Change procstatus
        return scheme;
    }

    private static int getConceptSchemeIndexByUuid(String uuid) {
        int foundIndex = -1;
        for (int i = 0; i < conceptSchemeList.size() && foundIndex < 0; i++) {
            if (conceptSchemeList.get(i).getUuid().equals(uuid)) {
                foundIndex = i;
            }
        }
        return foundIndex;
    }

    private static void fillConceptSchemes() {
        conceptSchemeList = new LinkedList<ConceptSchemeDto>();
        for (int i = 0; i < 100; i++) {
            ConceptSchemeDto conceptScheme = new ConceptSchemeDto();
            conceptScheme.setUuid(UUID.randomUUID().toString());
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
