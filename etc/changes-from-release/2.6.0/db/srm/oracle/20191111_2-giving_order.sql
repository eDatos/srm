-- --------------------------------------------------------------------------------------------------
-- EDATOS-2872 - Permitir crear un orden de visualización para esquemas de conceptosa
-- --------------------------------------------------------------------------------------------------

DECLARE
    ------------------------------------------------------------------------
    -- Esta variable booleana permite habilitar los logs de debug del script.
    -- Si se habilitan los logs, es necesario activar la salida DMBS y asignarle
    -- un tamaño de buffer ilimitado
    enable_debug BOOLEAN := TRUE;
    ------------------------------------------------------------------------

    CURSOR c_tb_concepts (item_scheme_id IN   NUMBER) IS
        SELECT cb.id, a.code
        FROM tb_m_concepts c, tb_concepts cb, tb_annotable_artefacts a
        WHERE c.tb_concepts = cb.id AND cb.item_scheme_version_fk = item_scheme_id AND cb.nameable_artefact_fk = a.id AND cb.parent_fk IS NULL
        ORDER BY a.code ASC;
    
    CURSOR c_item_scheme_version is
        select distinct(item_scheme_version_fk) item_scheme_id, a.code
        from tb_concepts c, tb_item_schemes_versions isv, TB_ANNOTABLE_ARTEFACTS a 
        where c.item_scheme_version_fk = isv.id and a.id = isv.MAINTAINABLE_ARTEFACT_FK 
        order by 1 asc;
    
    v_count   NUMBER(10) := 0;

    FUNCTION haschildren (x IN INT)
    RETURN INTEGER AS
        total   INTEGER := 0;
    BEGIN
        SELECT COUNT(1) INTO total FROM tb_concepts cb WHERE cb.parent_fk = x;
        RETURN total;
    END;

    PROCEDURE processchildren (
        x            IN           INT,
        code IN VARCHAR2,
        ordervalue   IN           NUMBER
    ) IS
        v_count_2   NUMBER(10);

        CURSOR c_tb_concepts_children (parent_id IN   NUMBER) IS
            SELECT cb.id, a.code
            FROM tb_concepts cb, tb_annotable_artefacts a
            WHERE cb.nameable_artefact_fk = a.id AND cb.parent_fk = parent_id
            ORDER BY a.code ASC;

    BEGIN
        IF ( haschildren(x) > 0 ) THEN
            v_count_2 := 0;
            FOR r_tb_concepts_children IN c_tb_concepts_children(x) LOOP
                processchildren(r_tb_concepts_children.id,r_tb_concepts_children.code, v_count_2);
                v_count_2 := v_count_2 + 1;
            END LOOP;
        END IF;

        IF (enable_debug = TRUE) THEN 
            dbms_output.put_line('concepto: ' || code || ' numero de orden asignado: ' || ordervalue); 
        END IF;
        
        UPDATE tb_m_concepts SET order_value = ordervalue WHERE tb_concepts = x;
    END;

BEGIN
    dbms_output.put_line('Inicio script poblado campo order value');
    dbms_output.put_line('');
    FOR r_item_scheme_version in c_item_scheme_version LOOP
        v_count := 0;
        
        IF (enable_debug = TRUE) THEN 
            dbms_output.put_line('Poblando campo order value para los conceptos del esquema de conceptos: ' || r_item_scheme_version.code); 
        END IF;
        
        FOR r_tb_concepts IN c_tb_concepts(r_item_scheme_version.item_scheme_id) LOOP
            processchildren(r_tb_concepts.id, r_tb_concepts.code, v_count);
            v_count := v_count + 1;
        END LOOP;
        
        COMMIT;
        
        IF (enable_debug = TRUE) THEN 
            dbms_output.put_line('Fin del poblado del campo order value para los conceptos del esquema de conceptos: ' || r_item_scheme_version.code);
            dbms_output.put_line(''); 
        END IF;
    END LOOP;
    
    dbms_output.put_line('');
    dbms_output.put_line('Fin script poblado campo order value');
END;