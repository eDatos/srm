{
  "swagger": "2.0",
  "info" : {
    "description" : "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis\n\t\tdis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec\n\t\tpede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede\n\t\tmollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat\n\t\tvitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum.\n\t\tAenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum\n\t\trhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio\n\t\tet ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed\n\t\tfringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,",
    "version" : "1.2.2-SNAPSHOT",
    "title" : "API de recursos estructurales"
  },
  "host" : "<%=org.siemac.metamac.srm.web.external.WebUtils.getApiBaseURL()%>",
  "schemes" : [],
  "tags" : [
    {
      "name" : "\/v1.0/agencyschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/agencyschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/agencyschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categorisations",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categorisations/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categorisations/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categorisations/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelistfamilies",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelistfamilies/{id}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptTypes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/contentConstraints",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/contentConstraints/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/contentConstraints/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}/regions/{regionCode}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/datastructures",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/datastructures/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/datastructures/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/datastructures/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes/{agencyID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variablefamilies",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variablefamilies/{id}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variablefamilies/{id}/variables",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variables",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variables/{id}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variables/{variableID}/variableelements",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variables/{variableID}/variableelements/{resourceID}",
      "description" : ""
    }
    ,
    {
      "name" : "\/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo",
      "description" : ""
    }
  ],
  "definitions" : {
    "xml_ns0_Error" : {
      "type" : "object",
      "title" : "Error",
      "allOf" : [
        {
          "properties" : {
            "code" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "message" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "parameters" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_ErrorParameters"
            }
          }
        }
      ],
      "description" : "<p>Java class for Error complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Error\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"code\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"message\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"parameters\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ErrorParameters\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_ErrorParameters" : {
      "type" : "object",
      "title" : "ErrorParameters",
      "allOf" : [
        {
          "properties" : {
            "total" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "parameter" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for ErrorParameters complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"ErrorParameters\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"parameter\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"total\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Errors" : {
      "type" : "object",
      "title" : "Errors",
      "allOf" : [
        {
          "properties" : {
            "total" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "error" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Error"
            }
          }
        }
      ],
      "description" : "<p>Java class for Errors complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Errors\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"error\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Error\" maxOccurs=\"unbounded\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"total\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Exception" : {
      "type" : "object",
      "title" : "Exception",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_Error"
        },
        {
          "properties" : {
            "errors" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Errors"
            }
          }
        }
      ],
      "description" : "<p>Java class for Exception complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Exception\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Error\">\r\n       &lt;sequence>\r\n         &lt;element name=\"errors\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Errors\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_InternationalString" : {
      "type" : "object",
      "title" : "InternationalString",
      "allOf" : [
        {
          "properties" : {
            "text" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_LocalisedString"
            }
          }
        }
      ],
      "description" : "<p>Java class for InternationalString complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"InternationalString\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"text\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}LocalisedString\" maxOccurs=\"unbounded\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_ListBase" : {
      "type" : "object",
      "title" : "ListBase",
      "allOf" : [
        {
          "properties" : {
            "limit" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "offset" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "total" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            }
          }
        }
      ],
      "description" : "<p>Java class for ListBase complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"ListBase\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;attribute name=\"total\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n       &lt;attribute name=\"limit\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n       &lt;attribute name=\"offset\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_LocalisedString" : {
      "type" : "object",
      "title" : "LocalisedString",
      "allOf" : [
        {
          "properties" : {
            "lang" : {
              "xml" : {
                "attribute" : true,
                "namespace" : "http://www.w3.org/XML/1998/namespace"
              },
"description" : "",
"type" : "string"
            },
            "(value)" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for LocalisedString complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"LocalisedString\">\r\n   &lt;simpleContent>\r\n     &lt;extension base=\"&lt;http://www.w3.org/2001/XMLSchema>string\">\r\n       &lt;attribute ref=\"{http://www.w3.org/XML/1998/namespace}lang default=\"es\"\"/>\r\n     &lt;/extension>\r\n   &lt;/simpleContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteria" : {
      "type" : "object",
      "title" : "MetamacCriteria",
      "allOf" : [
        {
          "properties" : {
            "countTotalResults" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "firstResult" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "maximumResultSize" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "ordersBy" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_MetamacCriteriaOrders"
            },
            "restriction" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_MetamacCriteriaRestriction"
            }
          }
        }
      ],
      "description" : "<p>Java class for MetamacCriteria complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteria\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"ordersBy\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaOrders\" minOccurs=\"0\"/>\r\n         &lt;element name=\"restriction\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaRestriction\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"firstResult\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n       &lt;attribute name=\"maximumResultSize\" type=\"{http://www.w3.org/2001/XMLSchema}unsignedLong\" />\r\n       &lt;attribute name=\"countTotalResults\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" default=\"true\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteriaDisjunctionRestriction" : {
      "type" : "object",
      "title" : "MetamacCriteriaDisjunctionRestriction",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_MetamacCriteriaRestriction"
        },
        {
          "properties" : {
            "restrictions" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_MetamacCriteriaRestrictions"
            }
          }
        }
      ],
      "description" : "Restriction of the type \"OR\"\r\n\r\n<p>Java class for MetamacCriteriaDisjunctionRestriction complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteriaDisjunctionRestriction\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaRestriction\">\r\n       &lt;sequence>\r\n         &lt;element name=\"restrictions\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaRestrictions\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteriaOrder" : {
      "type" : "object",
      "title" : "MetamacCriteriaOrder",
      "allOf" : [
        {
          "properties" : {
            "propertyName" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "type" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_OrderType"
            }
          }
        }
      ],
      "description" : "<p>Java class for MetamacCriteriaOrder complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteriaOrder\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"propertyName\" type=\"{http://www.w3.org/2001/XMLSchema}token\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}OrderType\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteriaOrders" : {
      "type" : "object",
      "title" : "MetamacCriteriaOrders",
      "allOf" : [
        {
          "properties" : {
            "order" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_MetamacCriteriaOrder"
            }
          }
        }
      ],
      "description" : "<p>Java class for MetamacCriteriaOrders complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteriaOrders\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"order\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaOrder\" maxOccurs=\"unbounded\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteriaRestriction" : {
      "type" : "object",
      "title" : "MetamacCriteriaRestriction",
      "allOf" : [
        {
        }
      ],
      "description" : "<p>Java class for MetamacCriteriaRestriction complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteriaRestriction\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_MetamacCriteriaRestrictions" : {
      "type" : "object",
      "title" : "MetamacCriteriaRestrictions",
      "allOf" : [
        {
          "properties" : {
            "restriction" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_MetamacCriteriaRestriction"
            }
          }
        }
      ],
      "description" : "<p>Java class for MetamacCriteriaRestrictions complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"MetamacCriteriaRestrictions\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"restriction\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}MetamacCriteriaRestriction\" maxOccurs=\"unbounded\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_OrderType" : {
      "type" : "string",
      "title" : "OrderType",
          "enum" : [
            "DESC",
            "ASC"
          ],
      "description" : "<p>Java class for OrderType.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"OrderType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"DESC\"/>\r\n     &lt;enumeration value=\"ASC\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Resource" : {
      "type" : "object",
      "title" : "Resource",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "urn" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for Resource complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Resource\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"urn\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"id\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Codelist" : {
      "type" : "object",
      "title" : "Codelist",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns3_CodelistType"
        },
        {
          "properties" : {
            "descriptionSource" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "family" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            },
            "isRecommended" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "replaceTo" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_ReplaceToCodelist"
            },
            "replaceToVersion" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "replacedBy" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            },
            "replacedByVersion" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "shortName" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "urnProvider" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "variable" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            }
          }
        }
      ],
      "description" : "<p>Java class for Codelist complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Codelist\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure}CodelistType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"urnProvider\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"shortName\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"descriptionSource\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"isRecommended\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}ReplaceToCodelist\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replacedBy\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceToVersion\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replacedByVersion\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"family\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_CodelistFamilies" : {
      "type" : "object",
      "title" : "CodelistFamilies",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "codelistFamily" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_CodelistFamily"
            }
          }
        }
      ],
      "description" : "<p>Java class for CodelistFamilies complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"CodelistFamilies\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"codelistFamily\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}CodelistFamily\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_CodelistFamily" : {
      "type" : "object",
      "title" : "CodelistFamily",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "urnProvider" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for CodelistFamily complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"CodelistFamily\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"urnProvider\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"id\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n       &lt;attribute name=\"urn\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Codelists" : {
      "type" : "object",
      "title" : "Codelists",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "codelist" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            }
          }
        }
      ],
      "description" : "<p>Java class for Codelists complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Codelists\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"codelist\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_ReplaceToCodelist" : {
      "type" : "object",
      "title" : "ReplaceToCodelist",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "replaceTo" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            }
          }
        }
      ],
      "description" : "<p>Java class for ReplaceToCodelist complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"ReplaceToCodelist\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_ReplaceToVariable" : {
      "type" : "object",
      "title" : "ReplaceToVariable",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "replaceTo" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            }
          }
        }
      ],
      "description" : "<p>Java class for ReplaceToVariable complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"ReplaceToVariable\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Variable" : {
      "type" : "object",
      "title" : "Variable",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "families" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_VariableFamilyCodes"
            },
            "name" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "replaceTo" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_ReplaceToVariable"
            },
            "replacedBy" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            },
            "shortName" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "urnProvider" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "validFrom" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "validTo" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for Variable complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Variable\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"urnProvider\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\"/>\r\n         &lt;element name=\"shortName\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\"/>\r\n         &lt;element name=\"validFrom\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\" minOccurs=\"0\"/>\r\n         &lt;element name=\"validTo\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replacedBy\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}ReplaceToVariable\" minOccurs=\"0\"/>\r\n         &lt;element name=\"families\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}VariableFamilyCodes\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"id\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n       &lt;attribute name=\"urn\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_VariableFamilies" : {
      "type" : "object",
      "title" : "VariableFamilies",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "variableFamily" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_VariableFamily"
            }
          }
        }
      ],
      "description" : "<p>Java class for VariableFamilies complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamilies\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variableFamily\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}VariableFamily\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_VariableFamily" : {
      "type" : "object",
      "title" : "VariableFamily",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_InternationalString"
            },
            "urnProvider" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Java class for VariableFamily complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamily\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}InternationalString\"/>\r\n         &lt;element name=\"urnProvider\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"id\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n       &lt;attribute name=\"urn\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_VariableFamilyCodes" : {
      "type" : "object",
      "title" : "VariableFamilyCodes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "family" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Resource"
            }
          }
        }
      ],
      "description" : "<p>Java class for VariableFamilyCodes complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamilyCodes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"family\" type=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns0_Variables" : {
      "type" : "object",
      "title" : "Variables",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns0_ListBase"
        },
        {
          "properties" : {
            "variable" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns0_Variable"
            }
          }
        }
      ],
      "description" : "<p>Java class for Variables complex type.\r\n\r\n<p>The following schema fragment specifies the expected content contained within this class.\r\n\r\n<pre>\r\n &lt;complexType name=\"Variables\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/soap/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/soap/structural-resources/v1.0/domain}Variable\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Agencies" : {
      "type" : "object",
      "title" : "Agencies",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "agency" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Agencies complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Agencies\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"agency\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Agency" : {
      "type" : "object",
      "title" : "Agency",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "$ref" : "#/definitions/xml_stat_Organisation"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para Agency complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Agency\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Organisation\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AgencyScheme" : {
      "type" : "object",
      "title" : "AgencyScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "$ref" : "#/definitions/xml_stat_OrganisationScheme"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para AgencyScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"AgencyScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}OrganisationScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AgencySchemes" : {
      "type" : "object",
      "title" : "AgencySchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "agencyScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para AgencySchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"AgencySchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"agencyScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AnnotableArtefact" : {
      "type" : "object",
      "title" : "AnnotableArtefact",
      "allOf" : [
        {
          "properties" : {
            "annotations" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Annotations"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para AnnotableArtefact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"AnnotableArtefact\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"annotations\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Annotations\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Annotation" : {
      "type" : "object",
      "title" : "Annotation",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "text" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "title" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "url" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Annotation complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Annotation\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"id\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"title\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"url\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"text\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Annotations" : {
      "type" : "object",
      "title" : "Annotations",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "annotation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Annotation"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Annotations complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Annotations\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"annotation\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Annotation\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Attribute" : {
      "type" : "object",
      "title" : "Attribute",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Component"
        },
        {
          "$ref" : "#/definitions/xml_stat_AttributeBase"
        },
        {
          "properties" : {
            "assignmentStatus" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_AttributeUsageStatusType"
            },
            "attributeRelationship" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_AttributeRelationship"
            },
            "roleConcepts" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_RoleConcepts"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Attribute complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Attribute\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AttributeBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"roleConcepts\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}RoleConcepts\" minOccurs=\"0\"/>\r\n         &lt;element name=\"attributeRelationship\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AttributeRelationship\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"assignmentStatus\" use=\"required\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AttributeUsageStatusType\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AttributeBase" : {
      "type" : "object",
      "title" : "AttributeBase",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Component"
        },
        {
          "properties" : {
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_AttributeQualifierType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para AttributeBase complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"AttributeBase\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Component\">\r\n       &lt;sequence>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AttributeQualifierType\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AttributeQualifierType" : {
      "type" : "string",
      "title" : "AttributeQualifierType",
          "enum" : [
            "MEASURE",
            "SPATIAL",
            "TIME"
          ],
      "description" : "<p>Clase Java para AttributeQualifierType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"AttributeQualifierType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"MEASURE\"/>\r\n     &lt;enumeration value=\"SPATIAL\"/>\r\n     &lt;enumeration value=\"TIME\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AttributeRelationship" : {
      "type" : "object",
      "title" : "AttributeRelationship",
      "allOf" : [
        {
          "properties" : {
            "attachmentGroup" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "dimension" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "group" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "none" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Empty"
            },
            "primaryMeasure" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para AttributeRelationship complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"AttributeRelationship\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;choice>\r\n         &lt;sequence>\r\n           &lt;element name=\"dimension\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\"/>\r\n           &lt;element name=\"attachmentGroup\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n         &lt;/sequence>\r\n         &lt;element name=\"group\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"primaryMeasure\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"none\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Empty\"/>\r\n       &lt;/choice>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_AttributeUsageStatusType" : {
      "type" : "string",
      "title" : "AttributeUsageStatusType",
          "enum" : [
            "MANDATORY",
            "CONDITIONAL"
          ],
      "description" : "<p>Clase Java para AttributeUsageStatusType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"AttributeUsageStatusType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"MANDATORY\"/>\r\n     &lt;enumeration value=\"CONDITIONAL\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Attributes" : {
      "type" : "object",
      "title" : "Attributes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Components"
        },
        {
          "properties" : {
            "attribute" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_AttributeBase"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Attributes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Attributes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Components\">\r\n       &lt;sequence>\r\n         &lt;element name=\"attribute\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AttributeBase\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Categories" : {
      "type" : "object",
      "title" : "Categories",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "category" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Categories complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Categories\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"category\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Categorisation" : {
      "type" : "object",
      "title" : "Categorisation",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "source" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "target" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Categorisation complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Categorisation\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}MaintainableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"source\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"target\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Categorisations" : {
      "type" : "object",
      "title" : "Categorisations",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "categorisation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Categorisations complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Categorisations\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"categorisation\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Category" : {
      "type" : "object",
      "title" : "Category",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Category complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Category\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Item\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_CategoryScheme" : {
      "type" : "object",
      "title" : "CategoryScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para CategoryScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"CategoryScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_CategorySchemes" : {
      "type" : "object",
      "title" : "CategorySchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "categoryScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para CategorySchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"CategorySchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"categoryScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Code" : {
      "type" : "object",
      "title" : "Code",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "shortName" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Code complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Code\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Item\">\r\n       &lt;sequence>\r\n         &lt;element name=\"shortName\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_CodeResource" : {
      "type" : "object",
      "title" : "CodeResource",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_Resource"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemResource"
        },
        {
          "properties" : {
            "open" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "order" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "variableElement" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para CodeResource complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"CodeResource\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variableElement\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"order\" type=\"{http://www.w3.org/2001/XMLSchema}int\" minOccurs=\"0\"/>\r\n         &lt;element name=\"open\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Codelist" : {
      "type" : "object",
      "title" : "Codelist",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "descriptionSource" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "isRecommended" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "opennessConfigurations" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VisualisationConfigurations"
            },
            "orderConfigurations" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VisualisationConfigurations"
            },
            "replaceTo" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ReplaceToResources"
            },
            "shortName" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "variable" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Codelist complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Codelist\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemScheme\">\r\n       &lt;sequence>\r\n         &lt;element name=\"shortName\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"descriptionSource\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"isRecommended\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ReplaceToResources\" minOccurs=\"0\"/>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\"/>\r\n         &lt;element name=\"orderConfigurations\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VisualisationConfigurations\"/>\r\n         &lt;element name=\"opennessConfigurations\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VisualisationConfigurations\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_CodelistFamilies" : {
      "type" : "object",
      "title" : "CodelistFamilies",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "codelistFamily" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para CodelistFamilies complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"CodelistFamilies\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"codelistFamily\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_CodelistFamily" : {
      "type" : "object",
      "title" : "CodelistFamily",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para CodelistFamily complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"CodelistFamily\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Codelists" : {
      "type" : "object",
      "title" : "Codelists",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "codelist" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Codelists complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Codelists\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"codelist\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Codes" : {
      "type" : "object",
      "title" : "Codes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "code" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_CodeResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Codes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Codes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"code\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}CodeResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Component" : {
      "type" : "object",
      "title" : "Component",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "properties" : {
            "conceptIdentity" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            },
            "localRepresentation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Representation"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Component complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Component\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}IdentifiableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"conceptIdentity\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"localRepresentation\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Representation\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Components" : {
      "type" : "object",
      "title" : "Components",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para Components complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Components\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}IdentifiableArtefact\">\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Concept" : {
      "type" : "object",
      "title" : "Concept",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "acronym" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "context" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "coreRepresentation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Representation"
            },
            "derivation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "descriptionSource" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "docMethod" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "extends" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            },
            "legalActs" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "pluralName" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "quantity" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Quantity"
            },
            "relatedConcepts" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_RelatedConcepts"
            },
            "roles" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_RoleConcepts"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Item"
            },
            "variable" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Concept complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Concept\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Item\">\r\n       &lt;sequence>\r\n         &lt;element name=\"coreRepresentation\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Representation\" minOccurs=\"0\"/>\r\n         &lt;element name=\"pluralName\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"acronym\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"descriptionSource\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"context\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"docMethod\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Item\" minOccurs=\"0\"/>\r\n         &lt;element name=\"roles\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}RoleConcepts\" minOccurs=\"0\"/>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"derivation\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"extends\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"relatedConcepts\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}RelatedConcepts\" minOccurs=\"0\"/>\r\n         &lt;element name=\"legalActs\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"quantity\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Quantity\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ConceptScheme" : {
      "type" : "object",
      "title" : "ConceptScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "statisticalOperation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ConceptSchemeType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ConceptScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ConceptScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemScheme\">\r\n       &lt;sequence>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ConceptSchemeType\"/>\r\n         &lt;element name=\"statisticalOperation\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ConceptSchemeType" : {
      "type" : "string",
      "title" : "ConceptSchemeType",
          "enum" : [
            "GLOSSARY",
            "TRANSVERSAL",
            "OPERATION",
            "ROLE",
            "MEASURE"
          ],
      "description" : "<p>Clase Java para ConceptSchemeType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"ConceptSchemeType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"GLOSSARY\"/>\r\n     &lt;enumeration value=\"TRANSVERSAL\"/>\r\n     &lt;enumeration value=\"OPERATION\"/>\r\n     &lt;enumeration value=\"ROLE\"/>\r\n     &lt;enumeration value=\"MEASURE\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ConceptSchemes" : {
      "type" : "object",
      "title" : "ConceptSchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "conceptScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ConceptSchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ConceptSchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"conceptScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ConceptTypes" : {
      "type" : "object",
      "title" : "ConceptTypes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "conceptType" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Item"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ConceptTypes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ConceptTypes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"conceptType\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Item\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Concepts" : {
      "type" : "object",
      "title" : "Concepts",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "concept" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Concepts complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Concepts\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"concept\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Contact" : {
      "type" : "object",
      "title" : "Contact",
      "allOf" : [
        {
          "properties" : {
            "emails" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "faxes" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "organisationUnit" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "responsibility" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "telephones" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "urls" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Contact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Contact\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"id\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"organisationUnit\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"responsibility\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n         &lt;element name=\"telephones\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n         &lt;element name=\"faxes\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n         &lt;element name=\"urls\" type=\"{http://www.w3.org/2001/XMLSchema}anyURI\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n         &lt;element name=\"emails\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Contacts" : {
      "type" : "object",
      "title" : "Contacts",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "contact" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Contact"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Contacts complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Contacts\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"contact\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Contact\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ContentConstraint" : {
      "type" : "object",
      "title" : "ContentConstraint",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "constraintAttachment" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "regions" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Regions"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ContentConstraintType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ContentConstraint complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ContentConstraint\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}MaintainableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ContentConstraintType\"/>\r\n         &lt;element name=\"constraintAttachment\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\"/>\r\n         &lt;element name=\"regions\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Regions\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ContentConstraintType" : {
      "type" : "string",
      "title" : "ContentConstraintType",
          "enum" : [
            "ALLOWED",
            "ACTUAL"
          ],
      "description" : "<p>Clase Java para ContentConstraintType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"ContentConstraintType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"ALLOWED\"/>\r\n     &lt;enumeration value=\"ACTUAL\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ContentConstraints" : {
      "type" : "object",
      "title" : "ContentConstraints",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "contentConstraint" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ContentConstraints complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ContentConstraints\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"contentConstraint\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataConsumer" : {
      "type" : "object",
      "title" : "DataConsumer",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "$ref" : "#/definitions/xml_stat_Organisation"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para DataConsumer complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataConsumer\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Organisation\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataConsumerScheme" : {
      "type" : "object",
      "title" : "DataConsumerScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "$ref" : "#/definitions/xml_stat_OrganisationScheme"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para DataConsumerScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataConsumerScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}OrganisationScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataConsumerSchemes" : {
      "type" : "object",
      "title" : "DataConsumerSchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dataConsumerScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataConsumerSchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataConsumerSchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataConsumerScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataConsumers" : {
      "type" : "object",
      "title" : "DataConsumers",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dataConsumer" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataConsumers complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataConsumers\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataConsumer\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataProvider" : {
      "type" : "object",
      "title" : "DataProvider",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "$ref" : "#/definitions/xml_stat_Organisation"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para DataProvider complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataProvider\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Organisation\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataProviderScheme" : {
      "type" : "object",
      "title" : "DataProviderScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "$ref" : "#/definitions/xml_stat_OrganisationScheme"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para DataProviderScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataProviderScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}OrganisationScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataProviderSchemes" : {
      "type" : "object",
      "title" : "DataProviderSchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dataProviderScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataProviderSchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataProviderSchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataProviderScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataProviders" : {
      "type" : "object",
      "title" : "DataProviders",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dataProvider" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataProviders complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataProviders\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataProvider\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataStructure" : {
      "type" : "object",
      "title" : "DataStructure",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "autoOpen" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "dataStructureComponents" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DataStructureComponents"
            },
            "dimensionVisualisations" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionVisualisations"
            },
            "heading" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionReferences"
            },
            "showDecimals" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "showDecimalsPrecisions" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ShowDecimalPrecisions"
            },
            "statisticalOperation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "stub" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionReferences"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataStructure complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataStructure\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}MaintainableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataStructureComponents\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DataStructureComponents\" minOccurs=\"0\"/>\r\n         &lt;element name=\"statisticalOperation\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"autoOpen\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"heading\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionReferences\"/>\r\n         &lt;element name=\"stub\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionReferences\"/>\r\n         &lt;element name=\"showDecimals\" type=\"{http://www.w3.org/2001/XMLSchema}int\" minOccurs=\"0\"/>\r\n         &lt;element name=\"showDecimalsPrecisions\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ShowDecimalPrecisions\" minOccurs=\"0\"/>\r\n         &lt;element name=\"dimensionVisualisations\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionVisualisations\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataStructureComponents" : {
      "type" : "object",
      "title" : "DataStructureComponents",
      "allOf" : [
        {
          "properties" : {
            "attributes" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Attributes"
            },
            "dimensions" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Dimensions"
            },
            "groups" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Groups"
            },
            "measure" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Measure"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataStructureComponents complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataStructureComponents\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimensions\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Dimensions\"/>\r\n         &lt;element name=\"groups\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Groups\" minOccurs=\"0\"/>\r\n         &lt;element name=\"attributes\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Attributes\" minOccurs=\"0\"/>\r\n         &lt;element name=\"measure\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Measure\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataStructures" : {
      "type" : "object",
      "title" : "DataStructures",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dataStructure" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DataStructures complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DataStructures\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dataStructure\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DataType" : {
      "type" : "string",
      "title" : "DataType",
          "enum" : [
            "STRING",
            "ALPHA",
            "ALPHA_NUMERIC",
            "NUMERIC",
            "BIG_INTEGER",
            "INTEGER",
            "LONG",
            "SHORT",
            "DECIMAL",
            "FLOAT",
            "DOUBLE",
            "BOOLEAN",
            "URI",
            "COUNT",
            "INCLUSIVE_VALUE_RANGE",
            "EXCLUSIVE_VALUE_RANGE",
            "INCREMENTAL",
            "OBSERVATIONAL_TIME_PERIOD",
            "STANDARD_TIME_PERIOD",
            "BASIC_TIME_PERIOD",
            "GREGORIAN_TIME_PERIOD",
            "GREGORIAN_YEAR",
            "GREGORIAN_YEAR_MONTH",
            "GREGORIAN_DAY",
            "REPORTING_TIME_PERIOD",
            "REPORTING_YEAR",
            "REPORTING_SEMESTER",
            "REPORTING_TRIMESTER",
            "REPORTING_QUARTER",
            "REPORTING_MONTH",
            "REPORTING_WEEK",
            "REPORTING_DAY",
            "DATE_TIME",
            "TIME_RANGE",
            "MONTH",
            "MONTH_DAY",
            "DAY",
            "TIME",
            "XHTML"
          ],
      "description" : "<p>Clase Java para DataType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"DataType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"String\"/>\r\n     &lt;enumeration value=\"Alpha\"/>\r\n     &lt;enumeration value=\"AlphaNumeric\"/>\r\n     &lt;enumeration value=\"Numeric\"/>\r\n     &lt;enumeration value=\"BigInteger\"/>\r\n     &lt;enumeration value=\"Integer\"/>\r\n     &lt;enumeration value=\"Long\"/>\r\n     &lt;enumeration value=\"Short\"/>\r\n     &lt;enumeration value=\"Decimal\"/>\r\n     &lt;enumeration value=\"Float\"/>\r\n     &lt;enumeration value=\"Double\"/>\r\n     &lt;enumeration value=\"Boolean\"/>\r\n     &lt;enumeration value=\"URI\"/>\r\n     &lt;enumeration value=\"Count\"/>\r\n     &lt;enumeration value=\"InclusiveValueRange\"/>\r\n     &lt;enumeration value=\"ExclusiveValueRange\"/>\r\n     &lt;enumeration value=\"Incremental\"/>\r\n     &lt;enumeration value=\"ObservationalTimePeriod\"/>\r\n     &lt;enumeration value=\"StandardTimePeriod\"/>\r\n     &lt;enumeration value=\"BasicTimePeriod\"/>\r\n     &lt;enumeration value=\"GregorianTimePeriod\"/>\r\n     &lt;enumeration value=\"GregorianYear\"/>\r\n     &lt;enumeration value=\"GregorianYearMonth\"/>\r\n     &lt;enumeration value=\"GregorianDay\"/>\r\n     &lt;enumeration value=\"ReportingTimePeriod\"/>\r\n     &lt;enumeration value=\"ReportingYear\"/>\r\n     &lt;enumeration value=\"ReportingSemester\"/>\r\n     &lt;enumeration value=\"ReportingTrimester\"/>\r\n     &lt;enumeration value=\"ReportingQuarter\"/>\r\n     &lt;enumeration value=\"ReportingMonth\"/>\r\n     &lt;enumeration value=\"ReportingWeek\"/>\r\n     &lt;enumeration value=\"ReportingDay\"/>\r\n     &lt;enumeration value=\"DateTime\"/>\r\n     &lt;enumeration value=\"TimeRange\"/>\r\n     &lt;enumeration value=\"Month\"/>\r\n     &lt;enumeration value=\"MonthDay\"/>\r\n     &lt;enumeration value=\"Day\"/>\r\n     &lt;enumeration value=\"Time\"/>\r\n     &lt;enumeration value=\"xhtml\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DimensionBase" : {
      "type" : "object",
      "title" : "DimensionBase",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Component"
        },
        {
          "properties" : {
            "type" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DimensionBase complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DimensionBase\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Component\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionType\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DimensionReferences" : {
      "type" : "object",
      "title" : "DimensionReferences",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dimension" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DimensionReferences complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DimensionReferences\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimension\" type=\"{http://www.w3.org/2001/XMLSchema}string\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DimensionType" : {
      "type" : "string",
      "title" : "DimensionType",
          "enum" : [
            "DIMENSION",
            "MEASURE_DIMENSION",
            "TIME_DIMENSION"
          ],
      "description" : "<p>Clase Java para DimensionType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"DimensionType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"DIMENSION\"/>\r\n     &lt;enumeration value=\"MEASURE_DIMENSION\"/>\r\n     &lt;enumeration value=\"TIME_DIMENSION\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DimensionVisualisation" : {
      "type" : "object",
      "title" : "DimensionVisualisation",
      "allOf" : [
        {
          "properties" : {
            "dimension" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DimensionVisualisation complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DimensionVisualisation\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimension\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_DimensionVisualisations" : {
      "type" : "object",
      "title" : "DimensionVisualisations",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "dimensionVisualisation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionVisualisation"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para DimensionVisualisations complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"DimensionVisualisations\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimensionVisualisation\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionVisualisation\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Dimensions" : {
      "type" : "object",
      "title" : "Dimensions",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Components"
        },
        {
          "properties" : {
            "dimension" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionBase"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Dimensions complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Dimensions\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Components\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimension\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionBase\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Empty" : {
      "type" : "object",
      "title" : "Empty",
      "allOf" : [
        {
        }
      ],
      "description" : "<p>Clase Java para Empty complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Empty\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Group" : {
      "type" : "object",
      "title" : "Group",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Components"
        },
        {
          "properties" : {
            "dimensions" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DimensionReferences"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Group complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Group\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Components\">\r\n       &lt;sequence>\r\n         &lt;element name=\"dimensions\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionReferences\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Groups" : {
      "type" : "object",
      "title" : "Groups",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "group" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Group"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Groups complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Groups\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"group\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Group\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_IdentifiableArtefact" : {
      "type" : "object",
      "title" : "IdentifiableArtefact",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "properties" : {
            "childLinks" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_ChildLinks"
            },
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "managementAppLink" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "nestedId" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "parentLink" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_ResourceLink"
            },
            "selfLink" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_ResourceLink"
            },
            "uri" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "urnProvider" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para IdentifiableArtefact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"IdentifiableArtefact\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}AnnotableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"id\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"nestedId\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"urn\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"urnProvider\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"uri\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"selfLink\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ResourceLink\"/>\r\n         &lt;element name=\"parentLink\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ResourceLink\"/>\r\n         &lt;element name=\"childLinks\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ChildLinks\"/>\r\n         &lt;element name=\"managementAppLink\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Item" : {
      "type" : "object",
      "title" : "Item",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "parent" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Item complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Item\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"parent\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ItemResource" : {
      "type" : "object",
      "title" : "ItemResource",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_Resource"
        },
        {
          "properties" : {
            "parent" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ItemResource complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ItemResource\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\">\r\n       &lt;sequence>\r\n         &lt;element name=\"parent\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ItemScheme" : {
      "type" : "object",
      "title" : "ItemScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "properties" : {
            "isPartial" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ItemScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ItemScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}MaintainableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"isPartial\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Key" : {
      "type" : "object",
      "title" : "Key",
      "allOf" : [
        {
          "properties" : {
            "included" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "keyParts" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_KeyParts"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Key complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Key\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"included\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\"/>\r\n         &lt;element name=\"keyParts\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}KeyParts\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_KeyPart" : {
      "type" : "object",
      "title" : "KeyPart",
      "allOf" : [
        {
          "properties" : {
            "afterPeriod" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "afterPeriodInclusive" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "beforePeriod" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "beforePeriodInclusive" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "cascadeValues" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "endPeriod" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "endPeriodInclusive" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "identifier" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "position" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "startPeriod" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "startPeriodInclusive" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_KeyPartType"
            },
            "value" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para KeyPart complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"KeyPart\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"identifier\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}KeyPartType\"/>\r\n         &lt;element name=\"value\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"position\" type=\"{http://www.w3.org/2001/XMLSchema}int\"/>\r\n         &lt;element name=\"cascadeValues\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\"/>\r\n         &lt;element name=\"beforePeriod\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"beforePeriodInclusive\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"afterPeriod\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"afterPeriodInclusive\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"startPeriod\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"startPeriodInclusive\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"endPeriod\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"endPeriodInclusive\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_KeyPartType" : {
      "type" : "string",
      "title" : "KeyPartType",
          "enum" : [
            "TIME_RANGE",
            "NORMAL"
          ],
      "description" : "<p>Clase Java para KeyPartType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"KeyPartType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"TIME_RANGE\"/>\r\n     &lt;enumeration value=\"NORMAL\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_KeyParts" : {
      "type" : "object",
      "title" : "KeyParts",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "keyPart" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_KeyPart"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para KeyParts complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"KeyParts\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"keyPart\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}KeyPart\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Keys" : {
      "type" : "object",
      "title" : "Keys",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "key" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Key"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Keys complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Keys\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"key\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Key\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_LifeCycle" : {
      "type" : "object",
      "title" : "LifeCycle",
      "allOf" : [
        {
          "properties" : {
            "lastUpdatedDate" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para LifeCycle complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"LifeCycle\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"lastUpdatedDate\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_MaintainableArtefact" : {
      "type" : "object",
      "title" : "MaintainableArtefact",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "properties" : {
            "agencyID" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "isExternalReference" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "isFinal" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "lifeCycle" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_LifeCycle"
            },
            "serviceUrl" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "structureUrl" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para MaintainableArtefact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"MaintainableArtefact\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VersionableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"agencyID\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"isFinal\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\"/>\r\n         &lt;element name=\"isExternalReference\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"serviceUrl\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"structureUrl\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"lifeCycle\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}LifeCycle\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Measure" : {
      "type" : "object",
      "title" : "Measure",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Components"
        },
        {
          "properties" : {
            "primaryMeasure" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_PrimaryMeasure"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Measure complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Measure\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Components\">\r\n       &lt;sequence>\r\n         &lt;element name=\"primaryMeasure\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}PrimaryMeasure\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_MeasureDimension" : {
      "type" : "object",
      "title" : "MeasureDimension",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Component"
        },
        {
          "$ref" : "#/definitions/xml_stat_DimensionBase"
        },
        {
          "properties" : {
            "roleConcepts" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_RoleConcepts"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para MeasureDimension complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"MeasureDimension\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DimensionBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"roleConcepts\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}RoleConcepts\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_NameableArtefact" : {
      "type" : "object",
      "title" : "NameableArtefact",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "properties" : {
            "description" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "name" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para NameableArtefact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"NameableArtefact\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}IdentifiableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"name\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\"/>\r\n         &lt;element name=\"description\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Organisation" : {
      "type" : "object",
      "title" : "Organisation",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "contacts" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Contacts"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Organisation complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Organisation\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Item\">\r\n       &lt;sequence>\r\n         &lt;element name=\"contacts\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Contacts\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationScheme" : {
      "type" : "object",
      "title" : "OrganisationScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para OrganisationScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationSchemes" : {
      "type" : "object",
      "title" : "OrganisationSchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "organisationScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para OrganisationSchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationSchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"organisationScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationUnit" : {
      "type" : "object",
      "title" : "OrganisationUnit",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Item"
        },
        {
          "$ref" : "#/definitions/xml_stat_Organisation"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para OrganisationUnit complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationUnit\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Organisation\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationUnitScheme" : {
      "type" : "object",
      "title" : "OrganisationUnitScheme",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_VersionableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_MaintainableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_ItemScheme"
        },
        {
          "$ref" : "#/definitions/xml_stat_OrganisationScheme"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para OrganisationUnitScheme complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationUnitScheme\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}OrganisationScheme\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationUnitSchemes" : {
      "type" : "object",
      "title" : "OrganisationUnitSchemes",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "organisationUnitScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para OrganisationUnitSchemes complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationUnitSchemes\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"organisationUnitScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_OrganisationUnits" : {
      "type" : "object",
      "title" : "OrganisationUnits",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "organisationUnit" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para OrganisationUnits complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"OrganisationUnits\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"organisationUnit\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Organisations" : {
      "type" : "object",
      "title" : "Organisations",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "organisation" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Organisations complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Organisations\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"organisation\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_PrimaryMeasure" : {
      "type" : "object",
      "title" : "PrimaryMeasure",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_Component"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para PrimaryMeasure complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"PrimaryMeasure\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Component\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Quantity" : {
      "type" : "object",
      "title" : "Quantity",
      "allOf" : [
        {
          "properties" : {
            "decimalPlaces" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "significantDigits" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "unitCode" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            },
            "unitMultiplier" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "unitSymbolPosition" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_QuantityUnitSymbolPosition"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Quantity complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Quantity\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"unitCode\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\"/>\r\n         &lt;element name=\"unitSymbolPosition\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}QuantityUnitSymbolPosition\"/>\r\n         &lt;element name=\"significantDigits\" type=\"{http://www.w3.org/2001/XMLSchema}int\" minOccurs=\"0\"/>\r\n         &lt;element name=\"decimalPlaces\" type=\"{http://www.w3.org/2001/XMLSchema}int\" minOccurs=\"0\"/>\r\n         &lt;element name=\"unitMultiplier\" type=\"{http://www.w3.org/2001/XMLSchema}int\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_QuantityAmount" : {
      "type" : "object",
      "title" : "QuantityAmount",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_Quantity"
        },
        {
        }
      ],
      "description" : "<p>Clase Java para QuantityAmount complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"QuantityAmount\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Quantity\">\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_QuantityUnitSymbolPosition" : {
      "type" : "string",
      "title" : "QuantityUnitSymbolPosition",
          "enum" : [
            "START",
            "END"
          ],
      "description" : "<p>Clase Java para QuantityUnitSymbolPosition.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"QuantityUnitSymbolPosition\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"START\"/>\r\n     &lt;enumeration value=\"END\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Region" : {
      "type" : "object",
      "title" : "Region",
      "allOf" : [
        {
          "properties" : {
            "code" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "keys" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Keys"
            },
            "regionValueType" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_RegionValueType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Region complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Region\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"regionValueType\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}RegionValueType\"/>\r\n         &lt;element name=\"code\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"keys\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Keys\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_RegionReference" : {
      "type" : "object",
      "title" : "RegionReference",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_Region"
        },
        {
          "properties" : {
            "contentConstraintUrn" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para RegionReference complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"RegionReference\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Region\">\r\n       &lt;sequence>\r\n         &lt;element name=\"contentConstraintUrn\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_RegionValueType" : {
      "type" : "string",
      "title" : "RegionValueType",
          "enum" : [
            "KEY_SET",
            "CUBE"
          ],
      "description" : "<p>Clase Java para RegionValueType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"RegionValueType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"KEY_SET\"/>\r\n     &lt;enumeration value=\"CUBE\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Regions" : {
      "type" : "object",
      "title" : "Regions",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "region" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_Region"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Regions complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Regions\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"region\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}Region\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_RelatedConcepts" : {
      "type" : "object",
      "title" : "RelatedConcepts",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "relatedConcept" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para RelatedConcepts complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"RelatedConcepts\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"relatedConcept\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ReplaceToResources" : {
      "type" : "object",
      "title" : "ReplaceToResources",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "replaceTo" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ReplaceToResources complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ReplaceToResources\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Representation" : {
      "type" : "object",
      "title" : "Representation",
      "allOf" : [
        {
          "properties" : {
            "enumerationCodelist" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "enumerationConceptScheme" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "textFormat" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_TextFormat"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Representation complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Representation\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;choice>\r\n         &lt;element name=\"textFormat\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}TextFormat\"/>\r\n         &lt;choice>\r\n           &lt;element name=\"enumerationCodelist\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\"/>\r\n           &lt;element name=\"enumerationConceptScheme\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\"/>\r\n         &lt;/choice>\r\n       &lt;/choice>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_RoleConcepts" : {
      "type" : "object",
      "title" : "RoleConcepts",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "role" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para RoleConcepts complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"RoleConcepts\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"role\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ShowDecimalPrecision" : {
      "type" : "object",
      "title" : "ShowDecimalPrecision",
      "allOf" : [
        {
          "properties" : {
            "concept" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            },
            "showDecimals" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ShowDecimalPrecision complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ShowDecimalPrecision\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"concept\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\"/>\r\n         &lt;element name=\"showDecimals\" type=\"{http://www.w3.org/2001/XMLSchema}int\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_ShowDecimalPrecisions" : {
      "type" : "object",
      "title" : "ShowDecimalPrecisions",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "showDecimalPrecision" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ShowDecimalPrecision"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para ShowDecimalPrecisions complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"ShowDecimalPrecisions\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"showDecimalPrecision\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ShowDecimalPrecision\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_TextFormat" : {
      "type" : "object",
      "title" : "TextFormat",
      "allOf" : [
        {
          "properties" : {
            "decimals" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "endTime" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "endValue" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "interval" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "isMultiLingual" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "isSequence" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            },
            "maxLength" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "maxValue" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "minLength" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "minValue" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "pattern" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "startTime" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "startValue" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "textType" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_DataType"
            },
            "timeInterval" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para TextFormat complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"TextFormat\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"textType\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}DataType\"/>\r\n         &lt;element name=\"isSequence\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n         &lt;element name=\"interval\" type=\"{http://www.w3.org/2001/XMLSchema}decimal\" minOccurs=\"0\"/>\r\n         &lt;element name=\"startValue\" type=\"{http://www.w3.org/2001/XMLSchema}decimal\" minOccurs=\"0\"/>\r\n         &lt;element name=\"endValue\" type=\"{http://www.w3.org/2001/XMLSchema}decimal\" minOccurs=\"0\"/>\r\n         &lt;element name=\"timeInterval\" type=\"{http://www.w3.org/2001/XMLSchema}duration\" minOccurs=\"0\"/>\r\n         &lt;element name=\"startTime\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"endTime\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"minLength\" type=\"{http://www.w3.org/2001/XMLSchema}positiveInteger\" minOccurs=\"0\"/>\r\n         &lt;element name=\"maxLength\" type=\"{http://www.w3.org/2001/XMLSchema}positiveInteger\" minOccurs=\"0\"/>\r\n         &lt;element name=\"minValue\" type=\"{http://www.w3.org/2001/XMLSchema}decimal\" minOccurs=\"0\"/>\r\n         &lt;element name=\"maxValue\" type=\"{http://www.w3.org/2001/XMLSchema}decimal\" minOccurs=\"0\"/>\r\n         &lt;element name=\"decimals\" type=\"{http://www.w3.org/2001/XMLSchema}positiveInteger\" minOccurs=\"0\"/>\r\n         &lt;element name=\"pattern\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"isMultiLingual\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Variable" : {
      "type" : "object",
      "title" : "Variable",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "families" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableFamiliesMetadata"
            },
            "replaceTo" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ReplaceToResources"
            },
            "replacedBy" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "shortName" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableType"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Variable complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Variable\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"shortName\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}InternationalString\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VariableType\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replacedBy\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ReplaceToResources\" minOccurs=\"0\"/>\r\n         &lt;element name=\"families\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VariableFamiliesMetadata\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElement" : {
      "type" : "object",
      "title" : "VariableElement",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "replaceTo" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ReplaceToResources"
            },
            "replacedBy" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            },
            "variable" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElement complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElement\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"replacedBy\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceTo\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ReplaceToResources\" minOccurs=\"0\"/>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\"/>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElements" : {
      "type" : "object",
      "title" : "VariableElements",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "variableElement" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElements complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElements\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variableElement\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElementsGeoInfo" : {
      "type" : "object",
      "title" : "VariableElementsGeoInfo",
      "allOf" : [
        {
          "properties" : {
            "features" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableElementsGeoInfoFeatures"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElementsGeoInfo complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElementsGeoInfo\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"type\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"features\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VariableElementsGeoInfoFeatures\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElementsGeoInfoFeature" : {
      "type" : "object",
      "title" : "VariableElementsGeoInfoFeature",
      "allOf" : [
        {
          "properties" : {
            "geometryWKT" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "properties" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableElementsGeoInfoFeatureProperties"
            },
            "type" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElementsGeoInfoFeature complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElementsGeoInfoFeature\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"id\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"type\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"geometryWKT\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"properties\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VariableElementsGeoInfoFeatureProperties\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElementsGeoInfoFeatureProperties" : {
      "type" : "object",
      "title" : "VariableElementsGeoInfoFeatureProperties",
      "allOf" : [
        {
          "properties" : {
            "geographicalGranularity" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_ItemResource"
            },
            "lastUpdatedDate" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "latitude" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "longitude" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "number"
            },
            "urn" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElementsGeoInfoFeatureProperties complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElementsGeoInfoFeatureProperties\">\r\n   &lt;complexContent>\r\n     &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}anyType\">\r\n       &lt;sequence>\r\n         &lt;element name=\"urn\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"longitude\" type=\"{http://www.w3.org/2001/XMLSchema}double\" minOccurs=\"0\"/>\r\n         &lt;element name=\"latitude\" type=\"{http://www.w3.org/2001/XMLSchema}double\" minOccurs=\"0\"/>\r\n         &lt;element name=\"geographicalGranularity\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}ItemResource\" minOccurs=\"0\"/>\r\n         &lt;element name=\"lastUpdatedDate\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/restriction>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableElementsGeoInfoFeatures" : {
      "type" : "object",
      "title" : "VariableElementsGeoInfoFeatures",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "feature" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableElementsGeoInfoFeature"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableElementsGeoInfoFeatures complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableElementsGeoInfoFeatures\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"feature\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VariableElementsGeoInfoFeature\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableFamilies" : {
      "type" : "object",
      "title" : "VariableFamilies",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "variableFamily" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableFamilies complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamilies\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variableFamily\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableFamiliesMetadata" : {
      "type" : "object",
      "title" : "VariableFamiliesMetadata",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "family" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableFamiliesMetadata complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamiliesMetadata\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"family\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableFamily" : {
      "type" : "object",
      "title" : "VariableFamily",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VariableFamily complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VariableFamily\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n       &lt;/sequence>\r\n       &lt;attribute name=\"kind\" use=\"required\" type=\"{http://www.w3.org/2001/XMLSchema}string\" />\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VariableType" : {
      "type" : "string",
      "title" : "VariableType",
          "enum" : [
            "GEOGRAPHICAL"
          ],
      "description" : "<p>Clase Java para VariableType.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n<p>\r\n<pre>\r\n &lt;simpleType name=\"VariableType\">\r\n   &lt;restriction base=\"{http://www.w3.org/2001/XMLSchema}token\">\r\n     &lt;enumeration value=\"GEOGRAPHICAL\"/>\r\n   &lt;/restriction>\r\n &lt;/simpleType>\r\n <\/pre>"
    }
    ,
    "xml_stat_Variables" : {
      "type" : "object",
      "title" : "Variables",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "variable" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_Resource"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para Variables complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"Variables\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"variable\" type=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Resource\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VersionableArtefact" : {
      "type" : "object",
      "title" : "VersionableArtefact",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_stat_AnnotableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_IdentifiableArtefact"
        },
        {
          "$ref" : "#/definitions/xml_stat_NameableArtefact"
        },
        {
          "properties" : {
            "replaceToVersion" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "replacedByVersion" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "validFrom" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "validTo" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "version" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VersionableArtefact complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VersionableArtefact\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}NameableArtefact\">\r\n       &lt;sequence>\r\n         &lt;element name=\"version\" type=\"{http://www.w3.org/2001/XMLSchema}string\"/>\r\n         &lt;element name=\"validFrom\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\"/>\r\n         &lt;element name=\"validTo\" type=\"{http://www.w3.org/2001/XMLSchema}dateTime\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replaceToVersion\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n         &lt;element name=\"replacedByVersion\" type=\"{http://www.w3.org/2001/XMLSchema}string\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VisualisationConfiguration" : {
      "type" : "object",
      "title" : "VisualisationConfiguration",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_Item"
        },
        {
          "properties" : {
            "default" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"type" : "boolean"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VisualisationConfiguration complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VisualisationConfiguration\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}Item\">\r\n       &lt;sequence>\r\n         &lt;element name=\"default\" type=\"{http://www.w3.org/2001/XMLSchema}boolean\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_stat_VisualisationConfigurations" : {
      "type" : "object",
      "title" : "VisualisationConfigurations",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_cdomain_ListBase"
        },
        {
          "properties" : {
            "visualisationConfiguration" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_stat_VisualisationConfiguration"
            }
          }
        }
      ],
      "description" : "<p>Clase Java para VisualisationConfigurations complex type.\r\n\r\n<p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.\r\n\r\n<pre>\r\n &lt;complexType name=\"VisualisationConfigurations\">\r\n   &lt;complexContent>\r\n     &lt;extension base=\"{http://www.siemac.org/metamac/rest/common/v1.0/domain}ListBase\">\r\n       &lt;sequence>\r\n         &lt;element name=\"visualisationConfiguration\" type=\"{http://www.siemac.org/metamac/rest/structural-resources/v1.0/domain}VisualisationConfiguration\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n       &lt;/sequence>\r\n     &lt;/extension>\r\n   &lt;/complexContent>\r\n &lt;/complexType>\r\n <\/pre>"
    }
    ,
    "xml_ns5_QueryTextType" : {
      "type" : "object",
      "title" : "QueryTextType",
      "allOf" : [
        {
          "$ref" : "#/definitions/xml_ns4_TextType"
        },
        {
          "properties" : {
            "operator" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns3_CodeType" : {
      "type" : "object",
      "title" : "CodeType",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "uri" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "Annotations" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_AnnotationsType"
            },
            "Description" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_TextType"
            },
            "Name" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_TextType"
            },
            "Parent" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_LocalCodeReferenceType"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns3_CodelistType" : {
      "type" : "object",
      "title" : "CodelistType",
      "allOf" : [
        {
          "properties" : {
            "agencyID" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "isExternalReference" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "isFinal" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "isPartial" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "serviceURL" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "structureURL" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "uri" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "urn" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "validFrom" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "validTo" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "version" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "Annotations" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_AnnotationsType"
            },
            "Code" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns3_CodeType"
            },
            "Description" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_TextType"
            },
            "Name" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_TextType"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_ChildLinks" : {
      "type" : "object",
      "title" : "ChildLinks",
      "allOf" : [
        {
          "properties" : {
            "total" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "childLink" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_ResourceLink"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_InternationalString" : {
      "type" : "object",
      "title" : "InternationalString",
      "allOf" : [
        {
          "properties" : {
            "text" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_LocalisedString"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_Item" : {
      "type" : "object",
      "title" : "Item",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_ListBase" : {
      "type" : "object",
      "title" : "ListBase",
      "allOf" : [
        {
          "properties" : {
            "firstLink" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "lastLink" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "limit" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "nextLink" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "offset" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            },
            "previousLink" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "selfLink" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "total" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "number"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_LocalisedString" : {
      "type" : "object",
      "title" : "LocalisedString",
      "allOf" : [
        {
          "properties" : {
            "lang" : {
              "xml" : {
                "attribute" : true,
                "namespace" : "http://www.w3.org/XML/1998/namespace"
              },
"description" : "",
"type" : "string"
            },
            "(value)" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_Resource" : {
      "type" : "object",
      "title" : "Resource",
      "allOf" : [
        {
          "properties" : {
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "id" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "name" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_InternationalString"
            },
            "nestedId" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"type" : "string"
            },
            "selfLink" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"$ref" : "#/definitions/xml_cdomain_ResourceLink"
            },
            "urn" : {
              "xml" : {
                "namespace" : "http://www.siemac.org/metamac/rest/common/v1.0/domain"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_cdomain_ResourceLink" : {
      "type" : "object",
      "title" : "ResourceLink",
      "allOf" : [
        {
          "properties" : {
            "href" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "kind" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns4_AnnotationType" : {
      "type" : "object",
      "title" : "AnnotationType",
      "allOf" : [
        {
          "properties" : {
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "AnnotationText" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_TextType"
            },
            "AnnotationTitle" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"type" : "string"
            },
            "AnnotationType" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"type" : "string"
            },
            "AnnotationURL" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns4_AnnotationsType" : {
      "type" : "object",
      "title" : "AnnotationsType",
      "allOf" : [
        {
          "properties" : {
            "Annotation" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_AnnotationType"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns4_ItemSchemePackageTypeCodelistType" : {
      "type" : "string",
      "title" : "ItemSchemePackageTypeCodelistType",
          "enum" : [
            "BASE",
            "CODELIST",
            "CATEGORYSCHEME",
            "CONCEPTSCHEME"
          ],
      "description" : ""
    }
    ,
    "xml_ns4_ItemTypeCodelistType" : {
      "type" : "string",
      "title" : "ItemTypeCodelistType",
          "enum" : [
            "AGENCY",
            "CATEGORY",
            "CODE",
            "CONCEPT",
            "DATA_CONSUMER",
            "DATA_PROVIDER",
            "ORGANISATION_UNIT",
            "REPORTING_CATEGORY"
          ],
      "description" : ""
    }
    ,
    "xml_ns4_LocalCodeRefType" : {
      "type" : "object",
      "title" : "LocalCodeRefType",
      "allOf" : [
        {
          "properties" : {
            "class" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_ItemTypeCodelistType"
            },
            "id" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "string"
            },
            "local" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"type" : "boolean"
            },
            "package" : {
              "xml" : {
                "attribute" : true,
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_ItemSchemePackageTypeCodelistType"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns4_LocalCodeReferenceType" : {
      "type" : "object",
      "title" : "LocalCodeReferenceType",
      "allOf" : [
        {
          "properties" : {
            "Ref" : {
              "xml" : {
                "namespace" : ""
              },
"description" : "",
"$ref" : "#/definitions/xml_ns4_LocalCodeRefType"
            }
          }
        }
      ],
      "description" : ""
    }
    ,
    "xml_ns4_TextType" : {
      "type" : "object",
      "title" : "TextType",
      "allOf" : [
        {
          "properties" : {
            "lang" : {
              "xml" : {
                "attribute" : true,
                "namespace" : "http://www.w3.org/XML/1998/namespace"
              },
"description" : "",
"type" : "string"
            },
            "(value)" : {
              "xml" : {
                "namespace" : "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
              },
"description" : "",
"type" : "string"
            }
          }
        }
      ],
      "description" : ""
    }
  },
  "paths": {
    "/v1.0/agencyschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes_findAgencySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/agencyschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes__agencyID__findAgencySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/agencyschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes__agencyID___resourceID__findAgencySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes__agencyID___resourceID___version__retrieveAgencyScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies_findAgencies_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}" : {
      "get" : {
        "tags" : [ "\/v1.0/agencyschemes/{agencyID}/{resourceID}/{version}/agencies/{organisationID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_agencyschemes__agencyID___resourceID___version__agencies__organisationID__retrieveAgency_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "organisationID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categorisations" : {
      "get" : {
        "tags" : [ "\/v1.0/categorisations" ],
        "description" : "",
        "operationId" : "resource__v1.0_categorisations_findCategorisations_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categorisations/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/categorisations/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categorisations__agencyID__findCategorisations_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categorisations/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/categorisations/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categorisations__agencyID___resourceID__findCategorisations_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categorisations/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/categorisations/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categorisations__agencyID___resourceID___version__retrieveCategorisation_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes_findCategorySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes__agencyID__findCategorySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes__agencyID___resourceID__findCategorySchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes__agencyID___resourceID___version__retrieveCategoryScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes__agencyID___resourceID___version__categories_findCategories_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}" : {
      "get" : {
        "tags" : [ "\/v1.0/categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_categoryschemes__agencyID___resourceID___version__categories__categoryID__retrieveCategory_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "categoryID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelistfamilies" : {
      "get" : {
        "tags" : [ "\/v1.0/codelistfamilies" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelistfamilies_findCodelistFamilies_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelistfamilies/{id}" : {
      "get" : {
        "tags" : [ "\/v1.0/codelistfamilies/{id}" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelistfamilies__id__retrieveCodelistFamilyById_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "id",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists_findCodelists_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists__agencyID__findCodelists_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists__agencyID___resourceID__findCodelists_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists__agencyID___resourceID___version__retrieveCodelist_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists__agencyID___resourceID___version__codes_findCodes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "openness",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "order",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}" : {
      "get" : {
        "tags" : [ "\/v1.0/codelists/{agencyID}/{resourceID}/{version}/codes/{codeID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_codelists__agencyID___resourceID___version__codes__codeID__retrieveCode_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "codeID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptTypes" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptTypes" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptTypes_retrieveConceptTypes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes_findConceptSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes__agencyID__findConceptSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes__agencyID___resourceID__findConceptSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes__agencyID___resourceID___version__retrieveConceptScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts_findConcepts_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}" : {
      "get" : {
        "tags" : [ "\/v1.0/conceptschemes/{agencyID}/{resourceID}/{version}/concepts/{conceptID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_conceptschemes__agencyID___resourceID___version__concepts__conceptID__retrieveConcept_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "conceptID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/contentConstraints" : {
      "get" : {
        "tags" : [ "\/v1.0/contentConstraints" ],
        "description" : "",
        "operationId" : "resource__v1.0_contentConstraints_findContentConstraints_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/contentConstraints/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/contentConstraints/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_contentConstraints__agencyID__findContentConstraints_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/contentConstraints/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/contentConstraints/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_contentConstraints__agencyID___resourceID__findContentConstraints_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_contentConstraints__agencyID___resourceID___version__retrieveContentConstraint_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}/regions/{regionCode}" : {
      "get" : {
        "tags" : [ "\/v1.0/contentConstraints/{agencyID}/{resourceID}/{version}/regions/{regionCode}" ],
        "description" : "",
        "operationId" : "resource__v1.0_contentConstraints__agencyID___resourceID___version__regions__regionCode__retrieveRegionForContentConstraint_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "regionCode",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes_findDataConsumerSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes__agencyID__findDataConsumerSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes__agencyID___resourceID__findDataConsumerSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__retrieveDataConsumerScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers_findDataConsumers_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers/{organisationID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataconsumerschemes__agencyID___resourceID___version__dataconsumers__organisationID__retrieveDataConsumer_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "organisationID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes_findDataProviderSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes__agencyID__findDataProviderSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes__agencyID___resourceID__findDataProviderSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes__agencyID___resourceID___version__retrieveDataProviderScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders_findDataProviders_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}" : {
      "get" : {
        "tags" : [ "\/v1.0/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders/{organisationID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_dataproviderschemes__agencyID___resourceID___version__dataproviders__organisationID__retrieveDataProvider_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "organisationID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/datastructures" : {
      "get" : {
        "tags" : [ "\/v1.0/datastructures" ],
        "description" : "",
        "operationId" : "resource__v1.0_datastructures_findDataStructures_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/datastructures/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/datastructures/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_datastructures__agencyID__findDataStructures_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/datastructures/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/datastructures/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_datastructures__agencyID___resourceID__findDataStructures_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/datastructures/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/datastructures/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_datastructures__agencyID___resourceID___version__retrieveDataStructure_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes_findOrganisationSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes__agencyID__findOrganisationSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes__agencyID___resourceID__findOrganisationSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes__agencyID___resourceID___version__retrieveOrganisationScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations_findOrganisations_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationschemes/{agencyID}/{resourceID}/{version}/organisations/{organisationID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationschemes__agencyID___resourceID___version__organisations__organisationID__retrieveOrganisation_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "organisationID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes_findOrganisationUnitSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes/{agencyID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes/{agencyID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes__agencyID__findOrganisationUnitSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes/{agencyID}/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes__agencyID___resourceID__findOrganisationUnitSchemes_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes__agencyID___resourceID___version__retrieveOrganisationUnitScheme_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits_findOrganisationUnits_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}" : {
      "get" : {
        "tags" : [ "\/v1.0/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits/{organisationID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_organisationunitschemes__agencyID___resourceID___version__organisationunits__organisationID__retrieveOrganisationUnit_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "agencyID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "organisationID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "version",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variablefamilies" : {
      "get" : {
        "tags" : [ "\/v1.0/variablefamilies" ],
        "description" : "",
        "operationId" : "resource__v1.0_variablefamilies_findVariableFamilies_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variablefamilies/{id}" : {
      "get" : {
        "tags" : [ "\/v1.0/variablefamilies/{id}" ],
        "description" : "",
        "operationId" : "resource__v1.0_variablefamilies__id__retrieveVariableFamilyById_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "id",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variablefamilies/{id}/variables" : {
      "get" : {
        "tags" : [ "\/v1.0/variablefamilies/{id}/variables" ],
        "description" : "",
        "operationId" : "resource__v1.0_variablefamilies__id__variables_findVariablesByFamily_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "id",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variables" : {
      "get" : {
        "tags" : [ "\/v1.0/variables" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables_findVariables_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variables/{id}" : {
      "get" : {
        "tags" : [ "\/v1.0/variables/{id}" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables__id__retrieveVariableById_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "id",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variables/{variableID}/variableelements" : {
      "get" : {
        "tags" : [ "\/v1.0/variables/{variableID}/variableelements" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables__variableID__variableelements_findVariableElements_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "variableID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variables/{variableID}/variableelements/{resourceID}" : {
      "get" : {
        "tags" : [ "\/v1.0/variables/{variableID}/variableelements/{resourceID}" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables__variableID__variableelements__resourceID__retrieveVariableElementById_GET",
        "produces" : [ "application/json", "application/xml" ],
        "parameters" : [
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "variableID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
    ,
    "/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo" : {
      "get" : {
        "tags" : [ "\/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables__variableID__variableelements__resourceID__geoinfo_findVariableElementsGeoInfoXml_GET",
        "produces" : [ "application/xml" ],
        "parameters" : [
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "variableID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "fields",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "schema" : {
"description" : "",
"$ref" : "#/definitions/xml_stat_VariableElementsGeoInfo"
            },
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
      ,
      "get" : {
        "tags" : [ "\/v1.0/variables/{variableID}/variableelements/{resourceID}/geoinfo" ],
        "description" : "",
        "operationId" : "resource__v1.0_variables__variableID__variableelements__resourceID__geoinfo_findVariableElementsGeoInfoJson_GET",
        "produces" : [ "application/json" ],
        "parameters" : [
          {
            "name" : "resourceID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "variableID",
            "in" : "path",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "fields",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "limit",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "offset",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "orderBy",
            "in" : "query",
            "type" : "string",
            "description" : ""
          },
          {
            "name" : "query",
            "in" : "query",
            "type" : "string",
            "description" : ""
          }
        ],
        "responses" : {
          "200" : {
            "headers" : {
            },
            "description" : "Success"
          },
          "default" : {
            "description" : "Unexpected error."
          }
        }
      }
    }
  }
}
