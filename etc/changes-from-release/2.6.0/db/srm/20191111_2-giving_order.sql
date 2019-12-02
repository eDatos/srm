-- --------------------------------------------------------------------------------------------------
-- EDATOS-2872 - Permitir crear un orden de visualización para esquemas de conceptosa
-- --------------------------------------------------------------------------------------------------

-- TODO EDATOS-2872 take care! this script isn't finished, just for one scheme concept

DECLARE
    CURSOR c_tb_concepts IS
    SELECT
        cb.id
    FROM
        tb_m_concepts c,
        tb_concepts cb,
        tb_annotable_artefacts a
    WHERE
        c.tb_concepts = cb.id
        AND cb.item_scheme_version_fk = '26852'
        AND cb.nameable_artefact_fk = a.id
        AND cb.parent_fk IS NULL
    ORDER BY
        a.code ASC;

    CURSOR c_tb_concepts_children (
        parent_id IN   NUMBER
    ) IS
    SELECT
        cb.id
    FROM
        tb_concepts cb,
        tb_annotable_artefacts a
    WHERE
        cb.nameable_artefact_fk = a.id
        AND cb.parent_fk = parent_id
    ORDER BY
        a.code ASC;

    v_count   NUMBER(10) := 0;

    FUNCTION haschildren (
        x IN   INT
    ) RETURN INTEGER AS
        total   INTEGER := 0;
    BEGIN
        SELECT
            COUNT(1)
        INTO total
        FROM
            tb_concepts cb
        WHERE
            cb.parent_fk = x;

        RETURN total;
    END;

    PROCEDURE processchildren (
        x            IN           INT,
        ordervalue   IN           NUMBER
    ) IS
        v_count_2   NUMBER(10);
    BEGIN
        IF ( haschildren(x) > 0 ) THEN
            dbms_output.put_line(x || ' tiene hijos');
            v_count_2 := 0;
            FOR r_tb_concepts_children IN c_tb_concepts_children(x) LOOP
                processchildren(r_tb_concepts_children.id, v_count_2);
                v_count_2 := v_count_2 + 1;
            END LOOP;

        END IF;

        dbms_output.put_line(x
                             || ' numero de orden: '
                             || ordervalue);
        UPDATE tb_m_concepts
        SET
            order_value = ordervalue
        WHERE
            tb_concepts = x;

    END;

BEGIN
    FOR r_tb_concepts IN c_tb_concepts LOOP
        processchildren(r_tb_concepts.id, v_count);
        v_count := v_count + 1;
    END LOOP;

    COMMIT;
END;