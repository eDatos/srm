<%@page import="org.siemac.metamac.core.common.util.swagger.SwaggerUtils"%>
<%@page pageEncoding="UTF-8"%>
{
   "swagger":"2.0",
   "info":{
      "description":"Los recursos estructurales son aquellos que sirven de apoyo para normalizar a los recursos estadísticos que produce la organización. Los principales recursos estructurales existente son los esquemas de organizaciones, esquemas de temas, esquemas de conceptos, clasificaciones y definiciones de estructuras de datos. Esta API permite consultar el inventario completo de los recursos estructurales que maneja la organización.",
      "version":"1.0",
      "title":"API de recursos estructurales"
   },
   "host":"<%=SwaggerUtils.getApiBaseURLForSwagger()%>",
   "schemes":[

   ],
   "tags":[
      {
         "name":"Organizaciones",
         "description":""
      },
      {
         "name":"Temas",
         "description":""
      },
      {
         "name":"Clasificaciones",
         "description":""
      },
      {
         "name":"Conceptos",
         "description":""
      },
      {
         "name":"Definiciones de Estructuras de Datos (DSD)",
         "description":""
      },
      {
         "name":"Variables",
         "description":""
      }
   ],
   "definitions": <jsp:include page="definitions.jsp" /> ,
   "components":  <jsp:include page="components.jsp" /> 
   "paths":{
      "/v1.0/agencyschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes_findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                  "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal" 
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes__agencyID__findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID__findAgencySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/AgencySchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__retrieveAgencyScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/AgencyScheme"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies_findAgencies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Agencies"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies__organisationID__retrieveAgency_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":""
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Agency"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categorisations":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categorisations_findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categorisations__agencyID__findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categorisations__agencyID___resourceID__findCategorisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorisationQueryParam"},
                {"$ref":"#/components/parameters/categorisationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Categorisations"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categorisations/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categorisations__agencyID___resourceID___version__retrieveCategorisation_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Categorisation"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes_findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CategorySchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes__agencyID__findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CategorySchemes"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID__findCategorySchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categorySchemeQueryParam"},
                {"$ref":"#/components/parameters/categorySchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CategorySchemes"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__retrieveCategoryScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CategoryScheme"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__categories_findCategories_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/categoryQueryParam"},
                {"$ref":"#/components/parameters/categoryOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Categories"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}":{
         "get":{
            "tags":[
               "Temas"
            ],
            "description":"",
            "operationId":"resource__v1.0_categoryschemes__agencyID___resourceID___version__categories__categoryID__retrieveCategory_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"categoryID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la categoría"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Category"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelistfamilies":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite obtener el listado de familias de clasificaciones",
            "operationId":"resource__v1.0_codelistfamilies_findCodelistFamilies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {"$ref":"#/components/parameters/limitParam"},
               {"$ref":"#/components/parameters/offsetParam"},
               {
                  "name":"orderBy",
                  "in":"query",
                  "type":"string",
                  "description":"Campo por el que ordenar los resultados<br>Campos válidos<ul><li>ID</li></ul>Criterios de Orden<ul><li>ASC: ascendente</li><li>DESC: descendente</li></ul><br>Ejemplo: orderBy=\"ID ASC\""
               },
               {
                  "name":"query",
                  "in":"query",
                  "type":"string",
                  "description":"<div>Consulta para filtrar los resultados</div><div>Operadores válidos:<ul><li>EQ: igual, </li><li>IEQ: igual sin distinción de mayúsculas, </li><li>LIKE: patrón de búsqueda, </li><li>ILIKE: pátron sin distinción de mayúsculas, </li><li>NE: no igual, </li><li>LT: menor que, </li><li>LE: menor o igual, </li><li>GT: mayor que, </li><li>GE: mayor o igual, </li><li>IS_NULL. valor nulo, </li><li>IS_NOT_NULL: valor no nulo, </li><li>IN: valor dentro de un conjunto</li></ul><div>Campos válidos:<ul><li>ID</li> <li>URN</li> <li>NAME</li></ul>Ejemplo: query=\"ID EQ 2090\"</div>"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CodelistFamilies"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelistfamilies/{id}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite obtener una familia de clasificaciones en particular",
            "operationId":"resource__v1.0_codelistfamilies__id__retrieveCodelistFamilyById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la familia de codelist"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/CodelistFamily"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite obtener el listado de clasificaciones",
            "operationId":"resource__v1.0_codelists_findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite obtener el listado de todas las clasificaciones mantenidas por una determinada organización.",
            "operationId":"resource__v1.0_codelists__agencyID__findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite obtener todas las versiones de una clasificación con un determinado identificador y que además es matenida por una organización determinada.",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID__findCodelists_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Codelists"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite consultar una versión en particular de una clasificación",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__retrieveCodelist_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Codelist"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite consultar los códigos de una versión de una clasificación. Nótese que si se hace uso de <i>wildcards</i> como \"~all\" o de alguno de los parámetros <i>limit, offset, query u orderBy</i>, el listado se devolverá paginado automáticamente.",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__codes_findCodes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/codelistQueryParam"},
                {"$ref":"#/components/parameters/codelistOrderByParam"},
               {
                  "name":"openness",
                  "in":"query",
                  "type":"string",
                  "description":"Apertura establecida para la visualización"
               },
               {
                  "name":"order",
                  "in":"query",
                  "type":"string",
                  "description":"Orden establecido para la visualización"
               },
              {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"Campos adicionales que se desean mostrar en la respuesta. Los valores pueden ser: <ul><li>+open: muestra la apertura del elemento. Sólo puede obtenerse en el caso de que no se esté haciendo uso del wildcard \"~all\".</li><li>+order: muestra el orden establecido para el elemento. Sólo puede obtenerse en el caso de que no se esté haciendo uso del wildcard \"~all\".</li><li>+variableElement: muestra su variable de elemento </li><li>+description: muestra la descripción de los códigos</li></ul> Ejemplo: fields=\"+open,+order\""
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Codes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}":{
         "get":{
            "tags":[
               "Clasificaciones"
            ],
            "description":"Permite consultar un código concreto de una versión de una clasificación",
            "operationId":"resource__v1.0_codelists__agencyID___resourceID___version__codes__codeID__retrieveCode_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
             {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"codeID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador del código"
               },
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Code"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptTypes":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptTypes_retrieveConceptTypes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[

            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ConceptTypes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes_findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ConceptSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes__agencyID__findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ConceptSchemes"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID__findConceptSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptSchemasQueryParam"},
                {"$ref":"#/components/parameters/conceptSchemasOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ConceptSchemes"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__retrieveConceptScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ConceptScheme"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts_findConcepts_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/conceptQueryParam"},
                {"$ref":"#/components/parameters/conceptOrderByParam"},
              {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"Campos adicionales que se desean mostrar en la respuesta. Los valores pueden ser: <ul><li>+description: muestra la descripción de los códigos</li></ul> Ejemplo: fields=\"+description\""
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Concepts"
                  },                   
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}":{
         "get":{
            "tags":[
               "Conceptos"
            ],
            "description":"",
            "operationId":"resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts__conceptID__retrieveConcept_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {
                  "name":"conceptID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador del concepto"
               },
             
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Concept"
                  },                   
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/contentConstraints":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_contentConstraints_findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_contentConstraints__agencyID__findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID__findContentConstraints_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/contentConstraintQueryParam"},
                {"$ref":"#/components/parameters/contentConstraintOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ContentConstraints"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID___version__retrieveContentConstraint_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/ContentConstraint"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}/regions/{regionCode}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_contentConstraints__agencyID___resourceID___version__regions__regionCode__retrieveRegionForContentConstraint_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
             {
                  "name":"regionCode",
                  "in":"path",
                  "type":"string",
                  "description":"Código de la región"
               },
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/RegionReference"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes_findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID__findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },                  
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID__findDataConsumerSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumerSchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__retrieveDataConsumerScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumerScheme"
                  },                  
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers_findDataConsumers_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumers"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers__organisationID__retrieveDataConsumer_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la organización"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataConsumer"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes_findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID__findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID__findDataProviderSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProviderSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__retrieveDataProviderScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProviderScheme"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders_findDataProviders_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProviders"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders__organisationID__retrieveDataProvider_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la organización"
               },
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataProvider"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/datastructures":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_datastructures_findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_datastructures__agencyID__findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_datastructures__agencyID___resourceID__findDataStructures_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/dataStructuresQueryParam"},
                {"$ref":"#/components/parameters/dataStructuresOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataStructures"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/datastructures/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Definiciones de Estructuras de Datos (DSD)"
            ],
            "description":"",
            "operationId":"resource__v1.0_datastructures__agencyID___resourceID___version__retrieveDataStructure_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/DataStructure"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes_findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes__agencyID__findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID__findOrganisationSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__retrieveOrganisationScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationScheme"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations_findOrganisations_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationQueryParam"},
                {"$ref":"#/components/parameters/organizationOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Organisations"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations__organisationID__retrieveOrganisation_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la organización"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Organisation"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes_findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID__findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID__findOrganisationUnitSchemes_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnitSchemes"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__retrieveOrganisationUnitScheme_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnitScheme"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits_findOrganisationUnits_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/organizationSchemeQueryParam"},
                {"$ref":"#/components/parameters/organizationSchemeOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnits"
                  },                      
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}":{
         "get":{
            "tags":[
               "Organizaciones"
            ],
            "description":"",
            "operationId":"resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits__organisationID__retrieveOrganisationUnit_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/agencyIdParam"},
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/versionParam"},
               {
                  "name":"organisationID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la organización"
               },
              
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/OrganisationUnit"
                  },                  
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variablefamilies":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variablefamilies_findVariableFamilies_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableFamilyQueryParam"},
                {"$ref":"#/components/parameters/variableFamilyOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/VariableFamilies"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variablefamilies/{id}":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variablefamilies__id__retrieveVariableFamilyById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la familia de variables"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/VariableFamily"
                  },                 
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variablefamilies/{id}/variables":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variablefamilies__id__variables_findVariablesByFamily_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la familia de variables"
               },
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableQueryParam"},
                {"$ref":"#/components/parameters/variableOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Variables"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variables":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variables_findVariables_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableQueryParam"},
                {"$ref":"#/components/parameters/variableOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Variables"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variables/{id}":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variables__id__retrieveVariableById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"id",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la variable"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/Variable"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variables__variableID__variableelements_findVariableElements_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
               {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la variable"
               },
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableElementQueryParam"},
                {"$ref":"#/components/parameters/variableElementOrderByParam"},
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/VariableElements"
                  },                
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements/{resourceID}":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variables__variableID__variableelements__resourceID__retrieveVariableElementById_GET",
            "produces":[
               "application/json",
               "application/xml"
            ],
            "parameters":[
                {"$ref":"#/components/parameters/resourceIdParam"},
               {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la variable"
               }
            ],
            "responses":{
               "200":{
               	  "schema":{
                     "description":"",
                     "$ref":"#/definitions/VariableElement"
                  },               
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      },
      "/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo":{
         "get":{
            "tags":[
               "Variables"
            ],
            "description":"",
            "operationId":"resource__v1.0_variables__variableID__variableelements__resourceID__geoinfo_findVariableElementsGeoInfoXml_GET",
            "produces":[
               "application/xml",
               "application/json"
            ],
            "parameters":[
                 {
                  "name":"variableID",
                  "in":"path",
                  "type":"string",
                  "description":"Identificador de la variable"
               },
               {
                  "name":"fields",
                  "in":"query",
                  "type":"string",
                  "description":"Campos en la respuesta que pueden mostrarse/ocultarse. <br>Valores válidos:<ul><li>\"-point\": Ocultar campo punto</li><li>\"-geometry\": Ocultar el campo geometría</li><li>\"-geographicalGranularity\": Ocultar la granularidad geográfica</li></ul>Ejemplo: fields=\"-geometry\""
               },
                {"$ref":"#/components/parameters/resourceIdParam"},
                {"$ref":"#/components/parameters/limitParam"},
                {"$ref":"#/components/parameters/offsetParam"},
                {"$ref":"#/components/parameters/variableElementQueryParam"},
                {"$ref":"#/components/parameters/variableElementOrderByParam"}
            ],
            "responses":{
               "200":{
                  "schema":{
                     "description":"",
                     "$ref":"#/definitions/VariableElementsGeoInfo"
                  },
                  "headers":{

                  },
                  "description":"Éxito. Indica que la petición ha sido resuelta correctamente"
               },
               "406":{
                  "description":"No aceptable. El formato solicitado no es válido"
               },
               "500":{
                  "description":"Error interno del servidor. Se ha producido un error que impide que se obtenga el recurso solicitado"
               },
               "503":{
                   "description":"Servicio no disponible. Indica que actualmente el servidor no está disponible y por tanto, la solicitud no puede procesarse. El error puede deberse a una sobrecarga temporal o a labores de mantenimiento del servidor. Se trata de una situación temporal"
               }
            }
         }
      }
   }
}