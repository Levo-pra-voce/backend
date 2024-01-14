drop table if exists usuario cascade;

create table usuario (
                         id            bigserial primary key,
                         email         text unique,
                         senha         text,
                         cpf           text,
                         primeiro_nome text,
                         sobrenome     text,
                         contato       text,
                         tipo          text,
                         data_criacao  timestamp default now(),
                         ativo         boolean,
                         status        text
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

drop table if exists grupo cascade;

create table grupo (
                       id           bigserial primary key,
                       nome         text,
                       data_criacao timestamp default now(),
                       ativo        boolean
);

drop table if exists usuario_grupo;

create table usuario_grupo (
                               id_usuario   bigint,
                               id_grupo     bigint,
                               data_criacao timestamp default now(),
                               ativo        boolean,
                               primary key (id_usuario, id_grupo),
                               foreign key (id_usuario) references usuario (id),
                               foreign key (id_grupo) references grupo (id)
);


drop table if exists mensagem;

create table mensagem (
                          id                   bigserial primary key,
                          id_usuario           bigint not null,
                          id_grupo             bigint not null,
                          id_mensagem_resposta bigint,
                          mensagem             bytea  not null,
                          tipo_mensagem        text,
                          data_criacao         timestamp default now(),
                          ativo                boolean,
                          foreign key (id_usuario) references usuario (id),
                          foreign key (id_grupo) references grupo (id)
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
                         cor             text,
                         renavam         text,
                         data_criacao    timestamp default now(),
                         ativo           boolean,
                         cnh             text,
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
                           id_usuario   bigint,
                           id_veiculo   bigint,
                           id_grupo     bigint,
                           valor        numeric(10, 2),
                           data_criacao timestamp default now(),
                           ativo        boolean,
                           status       text,
                           foreign key (id_usuario) references usuario (id),
                           foreign key (id_veiculo) references veiculo (id),
                           foreign key (id_grupo) references grupo (id)
);

drop table if exists carga cascade;

create table carga
(
    id           bigserial primary key,
    id_usuario   bigint,
    id_veiculo   bigint,
    id_pagamento bigint,
    descricao    text,
    peso         numeric(10, 2),
    largura      numeric(10, 2),
    altura       numeric(10, 2),
    data_criacao timestamp default now(),
    ativo        boolean,
    foreign key (id_usuario) references usuario (id),
    foreign key (id_veiculo) references veiculo (id),
    foreign key (id_pagamento) references pagamento (id)
);

drop table if exists pedido cascade;

create table pedido
(
    id                  bigserial primary key,
    id_usuario          bigint,
    id_veiculo          bigint,
    id_pagamento        bigint,
    id_endereco_origem  bigint,
    id_endereco_destino bigint,
    data_criacao        timestamp default now(),
    ativo               boolean,
    status              text,
    foreign key (id_usuario) references usuario (id),
    foreign key (id_veiculo) references veiculo (id),
    foreign key (id_pagamento) references pagamento (id),
    foreign key (id_endereco_origem) references endereco (id),
    foreign key (id_endereco_destino) references endereco (id)
);


drop table if exists pedido_carga;
create table pedido_carga
(
    id_pedido    bigint,
    id_carga     bigint,
    data_criacao timestamp default now(),
    ativo        boolean,
    primary key (id_pedido, id_carga),
    foreign key (id_pedido) references pedido (id),
    foreign key (id_carga) references carga (id)
);