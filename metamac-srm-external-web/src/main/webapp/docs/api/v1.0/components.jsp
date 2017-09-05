<%@page pageEncoding="UTF-8"%>
<%! String id = "ID: identificador"; %>
<%! String urn = "URN: urn única"; %>
<%! String name = "NAME: nombre"; %>
<%! String description = "DESCRIPTION: descripción"; %>
<%! String descriptionSource = "DESCRIPTION_SOURCE: origen de la descripción"; %>
<%! String validFrom = "VALID_FROM: válido desde"; %>
<%! String validTo = "VALID_TO: válido hasta"; %>
<%! String lastUpdatedDate = "LAST_UPDATED_DATE: fecha de última actualización"; %>
<%! String latest = "LATEST: es último"; %>
<%! String variableUrn = "VARIABLE_URN: URN de variable"; %>
<%! String variableFamilyUrn = "VARIABLE_FAMILY_URN: URN de la familia de variables"; %>
<%! String statisticalOperationUrn = "STATISTICAL_OPERATION_URN: URN de operación estadística"; %>
<%! String conceptSchemeLatest = "CONCEPT_SCHEME_LATEST: Último esquema de concepto"; %>
<%! String acronym = "ACRONYM: Acrónimo"; %>
<%! String extendsUrn = "EXTENDS_URN: Extiende URN"; %>
<%! String artefactUrn = "ARTEFACT_URN: URN de artefacto"; %>
<%! String relatedConceptUrn = "RELATED_CONCEPT_URN: URN de concepto relacionado"; %>
<%! String organisationSchemeUrn = "ORGANISATION_SCHEME_URN: URN de esquema de organización"; %>
<%! String type = "TYPE: tipo"; %>
<%! String categoryUrn = "CATEGORY_URN: URN de categoría"; %>
<%! String categorySchemeUrn = "CATEGORY_SCHEME_URN: URN de esquema de categoría"; %>
<%! String categorySchemeLatest = "CATEGORY_SCHEME_LATEST: último esquema de categoría"; %>
<%! String variableType = "VARIABLE_TYPE: tipo de variable"; %>
<%! String familyUrn = "FAMILY_URN: URN de familia"; %>
<%! String shortName = "SHORT_NAME: Nombre corto"; %>
<%! String geographicalGranularityUrn = "GEOGRAPHICAL_GRANULARITY_URN: URN de granularidad geográfica"; %>
<%! String organisationSchemeLatest = "ORGANISATION_SCHEME_LATEST: último esquema de organización"; %>
<%! String conceptSchemeUrn = "CONCEPT_SCHEME_URN: URN de esquema de concepto"; %>
<%! String conceptSchemeStatisticalOperationUrn = "CONCEPT_SCHEME_STATISTICAL_OPERATION_URN"; %>
<%! String conceptSchemeType = "CONCEPT_SCHEME_TYPE"; %>
<%! String attributeConceptUrn = "ATTRIBUTE_CONCEPT_URN"; %>
<%! String dimensionConceptUrn = "DIMENSION_CONCEPT_URN"; %>

<%! String validOperators = "<div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul>"; %>
<%! String criteriosDeOrden = "Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul>"; %>
<%! String queryDescription = "<div>Consulta para filtrar los resultados</div>"; %>
<%! String orderByDescription = "<div>Campo por el que ordenar los resultados</div>"; %>

<%! public String queryDescriptionWithParams(String... a) {
    String result = "" + queryDescription + validOperators +"<div>Campos válidos:<ul>";
    for (int i = 0; i < a.length; i++) {
        result += "<li>" + a[i] + "</li>";
    }
    return result + "</ul> Ejemplo: query=\\\"ID EQ 2090\\\"</div>";    
}
%>


{
    "parameters": {
        "limitParam" : {
            "name":"limit",
            "in":"query",
            "type":"string",
            "description":"Límite de resultados (número máximo), por defecto 25.<br/>Ejemplo: limit=10."
        },
        "offsetParam":{
              "name":"offset",
              "in":"query",
              "type":"string",
              "description":" Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. Ejemplo: offset=2."
        },
        "agencyIdParam":{
              "name":"agencyID",
              "in":"path",
              "type":"string",
              "description":"Identificador de la agencia que publica"
         },
        "resourceIdParam": {
              "name":"resourceID",
              "in":"path",
              "type":"string",
              "description":"Identificador del recurso"
         },
         "versionParam":  {
              "name":"version",
              "in":"path",
              "type":"string",
              "description":"Versión específica del recurso"
          },
        "codelistQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description": "<%= queryDescriptionWithParams(id,urn,name,description, descriptionSource, validFrom, validTo, lastUpdatedDate, latest, variableUrn, variableFamilyUrn) %>"
        },
        "codelistOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "conceptSchemasQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id,urn,name,description,type,statisticalOperationUrn,validFrom, validTo,lastUpdatedDate,latest) %>"
        },
        "conceptSchemasOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "conceptQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id,urn,name,description,descriptionSource,acronym,extendsUrn,relatedConceptUrn,conceptSchemeUrn,conceptSchemeType,conceptSchemeStatisticalOperationUrn, conceptSchemeLatest) %>"
        },
        "conceptOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "contentConstraintQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id, urn ,name ,artefactUrn ,latest) %>"
        },
        "contentConstraintOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "dataStructuresQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id,urn, name, description, validFrom, validTo, lastUpdatedDate, latest, statisticalOperationUrn,dimensionConceptUrn,attributeConceptUrn) %>"
        },
        "dataStructuresOrderByParam": {
            "name":"orderBy",
            "in":"query",
            "type":"string",
            "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "organizationSchemeQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id, urn ,name , description ,validFrom ,validTo ,lastUpdatedDate ,latest ,type) %>"
        },
        "organizationSchemeOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
         "organizationQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name , description ,organisationSchemeUrn ,organisationSchemeLatest ,type) %>"
        },
        "organizationOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "categorySchemeQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name , description ,validFrom ,validTo ,lastUpdatedDate ,latest) %>"
        },
        "categorySchemeOrderByParam": {
            "name":"orderBy",
            "in":"query",
            "type":"string",
            "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
         "categorisationQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name ,artefactUrn ,categoryUrn ,latest) %>"
        },
        "categorisationOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
         "categoryQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name , description ,categorySchemeUrn ,categorySchemeLatest) %>"
        },
        "categoryOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "variableFamilyQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name) %>"
        },
        "variableFamilyOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "variableQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name ,shortName ,familyUrn ,variableType) %>"
        },
        "variableOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%=orderByDescription %><br>Campos válidos<ul><li><%=id %></li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "variableElementQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<%= queryDescriptionWithParams(id ,urn ,name ,geographicalGranularityUrn ,variableType)%>"
        },
        "variableElementOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"<%= orderByDescription %><br>Campos válidos<ul><li>ID</li></ul><%= criteriosDeOrden %><br>Ejemplo: orderBy=\"ID ASC\""
        },
        
    }
},