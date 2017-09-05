<%@page pageEncoding="UTF-8"%>
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
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>DESCRIPTION_SOURCE</li><li>VALID_FROM</li><li>VALID_TO</li><li>LAST_UPDATED_DATE</li><li>LATEST</li><li>VARIABLE_URN</li><li>VARIABLE_FAMILY_URN</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "codelistOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "conceptSchemasQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>TYPE</li><li>STATISTICAL_OPERATION_URN</li><li>VALID_FROM</li><li>VALID_TO</li><li>LAST_UPDATED_DATE</li><li>LATEST</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "conceptSchemasOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "conceptQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>DESCRIPTION_SOURCE</li><li>ACRONYM</li><li>EXTENDS_URN</li><li>RELATED_CONCEPT_URN</li><li>CONCEPT_SCHEME_URN</li><li>CONCEPT_SCHEME_TYPE</li><li>CONCEPT_SCHEME_STATISTICAL_OPERATION_URN</li><li>CONCEPT_SCHEME_LATEST</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "conceptOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "contentConstraintQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li><li>URN</li><li>NAME</li><li>ARTEFACT_URN</li><li>LATEST</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "contentConstraintOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "dataStructuresQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>VALID_FROM</li><li>VALID_TO</li><li>LAST_UPDATED_DATE</li><li>LATEST</li><li>STATISTICAL_OPERATION_URN</li><li>DIMENSION_CONCEPT_URN</li><li>ATTRIBUTE_CONCEPT_URN</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "dataStructuresOrderByParam": {
            "name":"orderBy",
            "in":"query",
            "type":"string",
            "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
        "organizationSchemeQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos: <ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>VALID_FROM</li><li>VALID_TO</li><li>LAST_UPDATED_DATE</li><li>LATEST</li><li>TYPE</li></ul>Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "organizationSchemeOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
         "organizationQueryParam": {
            "name":"query",
            "in":"query",
            "type":"string",
            "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:   <ul><li>ID</li><li>URN</li><li>NAME</li><li>DESCRIPTION</li><li>ORGANISATION_SCHEME_URN</li><li>ORGANISATION_SCHEME_LATEST</li><li>TYPE</li></ul> Ejemplo: query=\"ID EQ 2090\"</div>"
        },
        "organizationOrderByParam": {
          "name":"orderBy",
          "in":"query",
          "type":"string",
          "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
        },
    }
},