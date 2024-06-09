drop table if exists usuario cascade;

create table usuario (
                         id            bigserial primary key,
                         email         text unique,
                         senha         text,
                         cpf           text,
                         cnh           text,

                         nome          text,
                         contato       text,
                         tipo          text,
                         data_criacao  timestamp default now(),
                         ativo         boolean,
                         status        text,
                         foto          bytea
);

drop table if exists endereco cascade;

create table endereco (
                          id           bigserial primary key,
                          id_usuario   bigint,
                          cep          text,
                          logradouro   text,
                          numero       text,
                          complemento  text,
                          bairro       text,
                          cidade       text,
                          estado       text,
                          data_criacao timestamp default now(),
                          ativo        boolean,
                          foreign key (id_usuario) references usuario (id)
);

drop table if exists permissao cascade;

create table permissao (
                           id           bigserial primary key,
                           nome         text,
                           data_criacao timestamp default now(),
                           ativo        boolean
);

drop table if exists perfil cascade;

create table perfil (
                        id           bigserial primary key,
                        nome         text,
                        data_criacao timestamp default now(),
                        ativo        boolean
);

drop table if exists perfil_permissao;

create table perfil_permissao (
                                  id_perfil    bigint,
                                  id_permissao bigint,
                                  data_criacao timestamp default now(),
                                  ativo        boolean,
                                  primary key (id_perfil, id_permissao),
                                  foreign key (id_perfil) references perfil (id),
                                  foreign key (id_permissao) references permissao (id)
);

drop table if exists perfil_usuario;

create table perfil_usuario(
                               id_perfil    bigint,
                               id_usuario   bigint,
                               data_criacao timestamp default now(),
                               ativo        boolean,
                               primary key (id_perfil, id_usuario),
                               foreign key (id_perfil) references perfil (id),
                               foreign key (id_usuario) references usuario (id)
);

insert into perfil (nome, data_criacao, ativo) values ('CLIENTE', now(), true);
insert into perfil (nome, data_criacao, ativo) values ('ENTREGADOR', now(), true);

drop table if exists tipo_veiculo cascade;

create table tipo_veiculo (
                              id           bigserial primary key,
                              nome         text,
                              data_criacao timestamp default now(),
                              ativo        boolean
);

drop table if exists veiculo cascade;

create table veiculo (
                         id              bigserial primary key,
                         id_usuario      bigint,
                         id_tipo_veiculo bigint,
                         placa           text,
                         modelo          text,
                         montadora text,
                         cor             text,
                         renavam         text,
                         data_criacao    timestamp default now(),
                         ativo           boolean,
                         cnh             text,
                         foto            bytea,
                         altura          DOUBLE PRECISION,
                         largura         DOUBLE PRECISION,
                         peso_maximo     DOUBLE PRECISION,
                         preco_base      DOUBLE PRECISION,
                         preco_por_km    DOUBLE PRECISION,
                         foreign key (id_usuario) references usuario (id),
                         foreign key (id_tipo_veiculo) references tipo_veiculo (id)
);

drop table if exists avaliacao;

create table avaliacao (
                           id           bigserial primary key,
                           id_usuario   bigint,
                           id_veiculo   bigint,
                           nota         numeric(2, 1),
                           data_criacao timestamp default now(),
                           ativo        boolean,
                           foreign key (id_usuario) references usuario (id),
                           foreign key (id_veiculo) references veiculo (id)
);

drop table if exists pagamento cascade;

create table pagamento (
                           id           bigserial primary key,
                           id_cliente   bigint,
                           id_entregador bigint,
                           id_veiculo   bigint,
                           valor        numeric(10, 2),
                           data_criacao timestamp default now(),
                           ativo        boolean,
                           status       text,
                           foreign key (id_cliente) references usuario (id),
                           foreign key (id_veiculo) references veiculo (id),
                           foreign key (id_entregador) references usuario (id)
);

drop table if exists pedido cascade;

create table pedido
(
    id                  bigserial primary key,
    id_entregador       bigint,
    id_cliente          bigint NOT NULL,
    id_veiculo          bigint,
    id_pagamento        bigint,
    data_entrega        timestamp default now(),
    altura              DOUBLE PRECISION NOT NULL ,
    largura             DOUBLE PRECISION NOT NULL ,
    peso_maximo         DOUBLE PRECISION NOT NULL,
    seguro              boolean default false,
    valor               DOUBLE PRECISION,
    status              text,
    foreign key (id_entregador) references usuario (id),
    foreign key (id_cliente) references usuario (id),
    foreign key (id_veiculo) references veiculo (id),
    foreign key (id_pagamento) references pagamento (id)
);
