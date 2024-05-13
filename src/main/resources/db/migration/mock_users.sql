create schema if not exists mock;

create table if not exists mock.nome
(
    id   serial primary key,
    nome text
);

truncate table mock.nome;
alter sequence mock.nome_id_seq restart with 1;
insert into mock.nome (nome)
select nome
from (select trim(name) as nome
      from unnest(string_to_array(
              'roberto, amanda, luiz, eduardo, raquel, gabriel, renan, maria, joao, carlos, julia, ana, pedro, lucas, marcelo, fernando, marcos, rafael, ricardo, marcela, mariana, carla, carolina, camila, bruna, beatriz, barbara, bianca, bruno, caio, carlos, caique, daniel, diego, douglas, eduardo, elias, elton, emerson, felipe, fernando, flavio, gabriel, gustavo, guilherme, henrique, igor, isaque, ivan, jair, jeferson, joao, jonas, jorge, jose, julio, junior, leandro, leonardo, lucas, luis, marcelo, marcos, mario, mauricio, maycon, michel, nelson, nivaldo, paulo, pedro, rafael, ramon, renan, ricardo, roberto, rodrigo, rogerio, ronaldo, samuel, sergio, sidney, thiago, valdir, vanderlei, victor, vinicius, wagner, washington, wilson, yuri',
              ',')) as name) as nn
where (select count(*) from mock.nome) = 0;

create or replace procedure mock_usuario(
    batch_size integer default 1000,
    i integer default 0,
    random_names_length integer default 1,
    current_random_index integer default 1,
    current_random_name text default null
)
    language plpgsql as
$$
begin
    i := 0;
    current_random_index := 1;
    random_names_length := (select count(*) from mock.nome);
    while i < 500000
        loop
            if current_random_index > random_names_length then
                current_random_index := 1;
            end if;
            current_random_name := (select nome from mock.nome where id = current_random_index);
            raise notice 'current_random_name: %', current_random_name;
            insert into usuario (email, senha, nome, tipo, status)
            values ('test'::text || i::text || '@gmail.com'::text,
                    '$2a$10$RLb6W1yRuqzjurauXUcaB.Yx938k37xSV9UxM8kZUxBXgytPVhoC2',
                    current_random_name, 'CLIENTE',
                    'ACTIVE');
            -- WITHOUT NAME
--         insert into usuario (email, senha, nome, tipo, status) values ('test'::text || i::text || '@gmail.com'::text,'$2a$10$RLb6W1yRuqzjurauXUcaB.Yx938k37xSV9UxM8kZUxBXgytPVhoC2',null, 'CLIENTE', 'ACTIVE');
            i := i + 1;
            current_random_index := current_random_index + 1;

            if i % batch_size = 0 then
                commit;
            end if;
        end loop;
end;
$$;

create or replace function mock_grupo() returns void as
$$
declare
    i integer;
begin
    i := 0;
    while i < 500000
        loop
            insert into grupo (nome, ativo) values (null, true);
            i := i + 1;
        end loop;
end;
$$ language plpgsql;


create or replace function mock_usuario_grupo() returns void as
$$
declare
    i            integer;
    base_user_id integer;
    base_group_id integer;
begin
    i := 0;
    base_user_id := (select coalesce(min(id), 1) from usuario where email like 'test%');
    base_group_id := (select coalesce(min(id), 1) from grupo where nome like 'test%');
    raise notice 'base_user_id: %', base_user_id;
    raise notice 'base_group_id: %', base_group_id;
    while i < 499999
        loop
            insert into usuario_grupo (id_usuario, id_grupo, ativo)
            values (base_user_id + i, base_group_id + i, true);
            insert into usuario_grupo (id_usuario, id_grupo, ativo)
            values (base_user_id + i + 1, base_group_id + i, true);
            i := i + 1;
        end loop;
end;
$$ language plpgsql;

alter sequence usuario_id_seq restart with 1;
alter sequence grupo_id_seq restart with 1;
truncate table usuario_grupo;
truncate table grupo cascade;
truncate table usuario cascade;
vacuum full usuario;
vacuum full grupo;
vacuum full usuario_grupo;
call mock_usuario(10000, 0, 1, null, null);
select mock_grupo();
select mock_usuario_grupo();