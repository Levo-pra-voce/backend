drop table if exists usuario cascade;

create table usuario (
                         id            serial primary key,
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
                          id           serial primary key,
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
                           id           serial primary key,
                           nome         text,
                           data_criacao timestamp default now(),
                           ativo        boolean
);

drop table if exists perfil cascade;

create table perfil (
                        id           serial primary key,
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
                              id           serial primary key,
                              nome         text,
                              data_criacao timestamp default now(),
                              ativo        boolean
);

drop table if exists veiculo cascade;

create table veiculo (
                         id              serial primary key,
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
                           id           serial primary key,
                           id_usuario   bigint,
                           id_veiculo   bigint,
                           nota         numeric(2, 1),
                           data_criacao timestamp default now(),
                           ativo        boolean,
                           foreign key (id_usuario) references usuario (id),
                           foreign key (id_veiculo) references veiculo (id)
);

drop table if exists pedido cascade;

create table pedido
(
    id                  serial primary key,
    id_entregador       bigint,
    id_cliente          bigint NOT NULL,
    id_veiculo          bigint,
    data_entrega        timestamp,
    altura              DOUBLE PRECISION NOT NULL ,
    largura             DOUBLE PRECISION NOT NULL ,
    peso_maximo         DOUBLE PRECISION NOT NULL,
    seguro              boolean default false,
    valor               DOUBLE PRECISION,
    status              text,
    origem_latitude     DOUBLE PRECISION,
    origem_longitude    DOUBLE PRECISION,
    origem_endereco     text,
    destino_latitude    DOUBLE PRECISION,
    destino_longitude   DOUBLE PRECISION,
    destino_endereco    text,
    distancia_metros    integer,
    duracao_segundos    integer,
    foreign key (id_entregador) references usuario (id),
    foreign key (id_cliente) references usuario (id),
    foreign key (id_veiculo) references veiculo (id)
);


drop table if exists solicitacao cascade;

create table solicitacao
(
    id                  serial primary key,
    id_pedido           bigint not null,
    id_entregador       bigint not null,
    status              text not null,
    foreign key (id_pedido) references pedido (id),
    foreign key (id_entregador) references usuario (id)
);