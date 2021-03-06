-- TB_FAM_CONCEPTOS
CREATE VIEW TB_FAM_CONCEPTOS AS
SELECT vf.ID as ID_FAM_CONCEPTO, ls.LABEL as NOMBRE
FROM TB_M_VARIABLE_FAMILIES vf
INNER JOIN TB_ANNOTABLE_ARTEFACTS a ON a.ID = vf.NAMEABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.LOCALE = 'es';


-- TB_CONC_FAMCONC
CREATE VIEW TB_CONC_FAMCONC AS
SELECT VARIABLE_FK as ID_CONCEPTO, VARIABLE_FAMILY_FK as ID_FAM_CONCEPTO
FROM TB_M_VAR_FAMILIES_VARIABLES;


-- TB_CONCEPTOS
CREATE VIEW TB_CONCEPTOS AS
SELECT v.ID as ID_CONCEPTO, lsName.LABEL as NOMBRE, lsShortName.LABEL as NOMBRE_CORTO,
CAST(v.VALID_FROM AS DATE) as FECHA_INICIO, CAST(v.VALID_TO AS DATE) as FECHA_FIN
FROM TB_M_VARIABLES v
INNER JOIN TB_ANNOTABLE_ARTEFACTS a ON a.ID = v.NAMEABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName ON lsName.INTERNATIONAL_STRING_FK = a.NAME_FK and lsName.LOCALE = 'es'
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsShortName ON lsShortName.INTERNATIONAL_STRING_FK = v.SHORT_NAME_FK and lsShortName.LOCALE = 'es';


-- TB_CODIFICACIONES
CREATE VIEW TB_CODIFICACIONES AS
SELECT isv.ID as ID_CODIF, VARIABLE_FK as ID_CONCEPTO, lsName.LABEL as NOMBRE, lsShortName.LABEL as NOMBRE_CORTO, cv.CODELIST_FAMILY_FK as ID_FAM_CODIF,
CAST(a.VALID_FROM AS DATE) as FECHA_INICIO, CAST(a.VALID_TO AS DATE) as FECHA_FIN
FROM TB_M_CODELISTS_VERSIONS cv
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON cv.TB_CODELISTS_VERSIONS = isv.ID
INNER JOIN TB_ANNOTABLE_ARTEFACTS a ON a.ID = isv.MAINTAINABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName ON lsName.INTERNATIONAL_STRING_FK = a.NAME_FK and lsName.LOCALE = 'es'
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsShortName ON lsShortName.INTERNATIONAL_STRING_FK = cv.SHORT_NAME_FK and lsShortName.LOCALE = 'es'
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED';


-- TB_CODIGOS
CREATE VIEW TB_CODIGOS AS
SELECT cb.ID as ID_CODIGO, ai.CODE as CODIGO, c.VARIABLE_ELEMENT_FK as ID_ITEM, aParent.CODE as CODIGO_COMPUESTO,
CAST(aisv.VALID_FROM AS DATE) as FECHA_INICIO, CAST(aisv.VALID_TO AS DATE) as FECHA_FIN, isv.ID as ID_CODIF
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_ANNOTABLE_ARTEFACTS ai ON ai.ID = cb.NAMEABLE_ARTEFACT_FK
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_ANNOTABLE_ARTEFACTS aisv ON aisv.ID = isv.MAINTAINABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_CODES cParent ON cb.PARENT_FK = cParent.ID
LEFT OUTER JOIN TB_ANNOTABLE_ARTEFACTS aParent ON aParent.ID = cParent.NAMEABLE_ARTEFACT_FK
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
ORDER BY ID_CODIF;


-- TB_ITEMS
CREATE VIEW TB_ITEMS AS
SELECT cb.ID as ID_ITEM, lsName.LABEL as NOMBRE_LARGO,  NVL(lsShortNameVe.LABEL, lsShortName.LABEL) as NOMBRE_CORTO, ve.VARIABLE_FK as ID_CONCEPTO
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ANNOTABLE_ARTEFACTS ai ON ai.ID = cb.NAMEABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName ON lsName.INTERNATIONAL_STRING_FK = ai.NAME_FK and lsName.LOCALE = 'es'
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsShortName ON lsShortName.INTERNATIONAL_STRING_FK = c.SHORT_NAME_FK and lsShortName.LOCALE = 'es'
LEFT OUTER JOIN TB_M_VARIABLE_ELEMENTS ve ON ve.ID = c.VARIABLE_ELEMENT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsShortNameVe ON lsShortNameVe.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK and lsShortNameVe.LOCALE = 'es'
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED';


-- TB_ORDENES
CREATE VIEW TB_ORDENES AS
SELECT o.ID as ID_ORDEN, lsName.LABEL as NOMBRE, o.CODELIST_FK as ID_CODIF,
CASE o.ID
  WHEN cv.DEFAULT_ORDER_VISUAL_FK THEN '1'
  ELSE '0'
END
AS POR_DEFECTO
FROM TB_M_CODELIST_ORDER_VIS o
INNER JOIN TB_ANNOTABLE_ARTEFACTS ao ON ao.ID = o.NAMEABLE_ARTEFACT_FK
LEFT OUTER JOIN TB_LOCALISED_STRINGS lsName ON lsName.INTERNATIONAL_STRING_FK = ao.NAME_FK and lsName.LOCALE = 'es'
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = o.CODELIST_FK
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED';


-- TB_ORDENES_CODIGOS
CREATE VIEW TB_ORDENES_CODIGOS AS
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER1 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 1
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER2 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 2
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER3 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 3
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER4 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 4
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER5 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 5
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER6 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 6
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER7 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 7
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER8 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 8
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER9 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 9
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER10 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 10
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER11 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 11
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER12 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 12
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER13 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 13
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER14 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 14
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER15 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 15
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER16 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 16
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER17 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 17
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER18 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 18
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER19 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 19
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
)
UNION
(
SELECT o.ID as ID_ORDEN, c.TB_CODES as ID_CODIGO, c.ORDER20 as ORDEN
FROM TB_M_CODES c
INNER JOIN TB_CODES cb ON c.TB_CODES = cb.ID
INNER JOIN TB_M_CODELISTS_VERSIONS cv ON cv.TB_CODELISTS_VERSIONS = cb.ITEM_SCHEME_VERSION_FK
INNER JOIN TB_ITEM_SCHEMES_VERSIONS isv ON isv.ID = cv.TB_CODELISTS_VERSIONS
INNER JOIN TB_M_CODELIST_ORDER_VIS o on o.CODELIST_FK = cv.TB_CODELISTS_VERSIONS AND o.COLUMN_INDEX = 20
WHERE cv.ACCESS_TYPE in ('PUBLIC', 'RESTRICTED') AND cv.PROC_STATUS = 'EXTERNALLY_PUBLISHED'
);

