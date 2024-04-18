# Levo pra você - Api
> API RESTful para aplicação de entregas `levo pra você`

![image](https://github.com/Levo-pra-voce/mobile/assets/77634037/65c6684e-c90e-40f9-9f21-40a50aaf7dc1)

## Pré-requisitos
* [Docker](https://www.docker.com)
* A [api](https://github.com/Levo-pra-voce/backend) do levo pra você estar online

### Tecnologias
<div>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/187070862-03888f18-2e63-4332-95fb-3ba4f2708e59.png" alt="websocket" title="websocket"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117208740-bfb78400-adf5-11eb-97bb-09072b6bedfc.png" alt="PostgreSQL" title="PostgreSQL"/></code>
</div>

## Inicialização do app
- Estar com o [banco de dados](https://github.com/Levo-pra-voce/database) up
- Inicializar o aplicação spring através do comando `./mvnw spring-boot:run` ou através da sua IDE de preferência

## Configuração para desenvolvimento
- Ter o docker instalado para rodar o banco de dados

## Desenvolvedores
[Luiz Eduardo](https://github.com/luiz-eduardo14)
[Eliezer Silva](https://github.com/Eliezer-TEC)
[Gabriel Vitorino](https://github.com/Tr00vuada)

## Modelo de desenvolvimento 
1. Criar branch com o código da feature (`git checkout -b feature/featureCode`)
2. Commitar suas alterações (`git commit -am 'commit message'`)
3. Fazer push para branch remota (`git push origin feature/featureCode`)
4. Criar pull request da feature para branch `develop`
