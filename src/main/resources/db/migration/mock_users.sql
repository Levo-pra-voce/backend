create function mock_usuario() returns void as
$$
declare
    i integer;
begin
    i := 0;
    while i < 500000
        loop
            insert into usuario (email, senha, nome, tipo, status)
            values ('test'::text || i::text || '@gmail.com'::text,
                    '$2a$10$RLb6W1yRuqzjurauXUcaB.Yx938k37xSV9UxM8kZUxBXgytPVhoC2',
                    'test'::text || i::text, 'CLIENTE', 'ACTIVE');
            i := i + 1;
        end loop;
end;
$$ language plpgsql;

create function mock_grupo() returns void as
$$
declare
    i integer;
begin
    i := 0;
    while i < 500000
        loop
            insert into grupo (nome, ativo) values ('test'::text || i::text, true);
            i := i + 1;
        end loop;
end;
$$ language plpgsql;

do
$$
    declare
        i integer;
    begin
        i := 0;
        while i < 500000
            loop
                insert into grupo (nome, ativo) values ('test'::text || i::text, true);
                i := i + 1;
            end loop;
    end
$$;


create function mock_usuario_grupo() returns void as
$$
declare
    i             integer;
    base_user_id  integer;
    base_group_id integer;
begin
    i := 0;
    base_user_id := (select min(id) from usuario where email like 'test%');
    base_group_id := (select min(id) from grupo where nome like 'test%');
    raise notice 'base_user_id: %', base_user_id;
    raise notice 'base_group_id: %', base_group_id;
    while i < 500000
        loop
            insert into usuario_grupo (id_usuario, id_grupo, ativo)
            values (base_user_id + i, base_group_id + i, true);
            insert into usuario_grupo (id_usuario, id_grupo)
            values (base_user_id + i + 1, base_group_id + i);
            i := i + 1;
        end loop;
end;
$$ language plpgsql;


-- alter sequence usuario_id_seq restart with 1;
-- alter sequence grupo_id_seq restart with 1;
-- truncate table usuario_grupo;
-- truncate table grupo cascade;
-- truncate table usuario cascade;
-- select mock_usuario();
-- select mock_grupo();
-- select mock_usuario_grupo();