-- --------------------------------------------------------------------------------------------------
-- EDATOS-3482 - Mejoras sobre el visualizador del portal. En los gráficos, mostrar cada categoría con un color en concreto
-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de elementos de variable para almacenar el color de representación

ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD RENDERING_COLOR VARCHAR(255);

commit;