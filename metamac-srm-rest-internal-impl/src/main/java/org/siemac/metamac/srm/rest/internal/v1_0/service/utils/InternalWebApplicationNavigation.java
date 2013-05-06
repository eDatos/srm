package org.siemac.metamac.srm.rest.internal.v1_0.service.utils;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.navigation.shared.PlaceRequestParams;
import org.springframework.web.util.UriTemplate;

public class InternalWebApplicationNavigation {

    private final String      PATH_STRUCTURAL_RESOURCES = "#" + NameTokens.structuralResourcesPage;
    private final String      SEPARATOR                 = "/";
    private final String      ITEM_SCHEME_PARAMETER     = "itemScheme";
    private final String      ITEM_PARAMETER            = "item";

    private final UriTemplate codelistTemplate;
    private final UriTemplate codeTemplate;

    public InternalWebApplicationNavigation(String webApplicationPath) {
        // Codes
        codelistTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.codelistListPage + SEPARATOR + NameTokens.codelistPage + ";"
                + PlaceRequestParams.codelistParamId + "=" + "{" + ITEM_SCHEME_PARAMETER + "}");
        codeTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.codelistListPage + SEPARATOR + NameTokens.codelistPage + ";"
                + PlaceRequestParams.codelistParamId + "=" + "{" + ITEM_SCHEME_PARAMETER + "}/" + NameTokens.codePage + ";" + PlaceRequestParams.codeParamId + "={" + ITEM_PARAMETER + "}");
    }

    public String buildCodelistUrl(String codelistUrn) {
        Map<String, String> uriVariables = new HashMap<String, String>(1);
        String codelistUrlPart = UrnUtils.removePrefix(codelistUrn);
        uriVariables.put(ITEM_SCHEME_PARAMETER, codelistUrlPart);
        return codelistTemplate.expand(uriVariables).toString();
    }

    public String buildCodeUrl(String codeUrn) {
        String[] codeUrnSplited = UrnUtils.splitUrnByDots(UrnUtils.removePrefix(codeUrn));
        String codelistUrlPart = codeUrnSplited[0];
        String codeUrlPart = codeUrnSplited[1];
        Map<String, String> uriVariables = new HashMap<String, String>(2);
        uriVariables.put(ITEM_SCHEME_PARAMETER, codelistUrlPart);
        uriVariables.put(ITEM_PARAMETER, codeUrlPart);
        return codeTemplate.expand(uriVariables).toString();
    }
}