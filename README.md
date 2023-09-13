# JMeterSockJS-Plugin - Amostra JMeter para WebSocket com Spring STOMP

O `JMeterSockJS-Plugin` é um Java Sampler desenvolvido para ser usado com o Apache JMeter. Ele permite que você teste a conexão WebSocket usando o Spring WebSocket com o protocolo STOMP. Este Sampler é útil para testar a funcionalidade de WebSocket em seu aplicativo web.

As funcionalidades de foco desse teste são:

- Realizar uma conexão SockJS via Stomp em um endpoint Websocket;
- Enviar uma mensagem para um tópico;
- Desconectar do servidor;

Os relatórios de tempo de resposta do JMeter são compátiveis com os testes, pois o objetivo desse projeto é fazer teste de Carga em um Webchat Spring Boot e documentar os Resultados no meu TCC.

## Pré-requisitos

Certifique-se de que você tenha os seguintes pré-requisitos instalados antes de usar o `JMeterSockJS-Plugin`:

- [Apache JMeter](https://jmeter.apache.org/)
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- As bibliotecas e dependências listadas no arquivo `pom.xml`

## Configuração

Para configurar e executar o `JMeterSockJS-Plugin`, siga as etapas abaixo:

1. Clone ou faça o download deste repositório.

2. Abra o Apache JMeter.

3. Carregue o plano de teste existente ou crie um novo.

4. Adicione um elemento "Java Request" ao seu plano de teste.

5. Configure o elemento "Java Request" e especifique a classe `SockJsSampler` como a classe de amostrador Java a ser usada.

6. Defina os parâmetros necessários, incluindo `HOST`, `TOPIC`, `PATH`, `CONNECTION_HEADERS_AUTHORIZATION`, e `CONNECTION_HEADERS_ACCEPT_VERSION`.

7. Execute o plano de teste.

8. Analise os resultados nos resultados do JMeter para avaliar o desempenho da sua conexão WebSocket.

## Criando um Pacote .jar Usando o Maven sem Nenhuma IDE (Java) e Adicionando-o ao JMeter

Para usar o amostrador no seu programa JMeter, você precisará do binário compilado na pasta JMeter\lib\ext.

Para criar o binário compilado, vá até o diretório de origem do projeto clonado.

Depois disso, execute o seguinte comando no seu terminal:

```shell
mvn package shade:shade
```

Isso criará o pacote jmetersockoverstomp-0.0.1-SNAPSHOT.jar no diretório target.

Para instalar este plugin, coloque o arquivo .jar na pasta \JMeter\lib\ext.

## Parâmetros

- `HOST`: O URL do host do servidor WebSocket.
- `TOPIC`: O tópico ao qual você deseja se inscrever ou enviar mensagens.
- `PATH`: O caminho da URL WebSocket.
- `CONNECTION_HEADERS_AUTHORIZATION`: O cabeçalho de autorização usado para autenticar-se no servidor WebSocket.
- `CONNECTION_HEADERS_ACCEPT_VERSION`: A versão do protocolo WebSocket que o cliente aceita.

## Resultados

Os resultados da amostra incluem:

- Tempo de início e término da amostra.
- Status de sucesso ou falha.
- Mensagem de resposta do servidor WebSocket.
- Dados de resposta, incluindo quaisquer problemas encontrados durante a execução.

## Baseado em Projetos Anteriores

Este projeto foi inteiramente baseado na idéia apresentada pelo projeto [JmeterSockJsSampler](https://github.com/MovingImage24/JmeterSockJsSampler), que se trata de uma extensão do Apache JMeter que foi adaptada a partir de dois projetos anteriores:

1. [JMeter-WebSocket-StompSampler](https://github.com/Fyro-Ing/JMeter-WebSocket-StompSampler): Um projeto que serviu como ponto de partida para o desenvolvimento deste Sampler JMeter. Ele forneceu a estrutura inicial e a ideia por trás da amostra.

2. [spring-websocket-client](https://github.com/jaysridhar/spring-websocket-client): Um projeto que contribuiu com código relacionado a WebSocket e STOMP para esta amostra JMeter. Isso ajudou a melhorar a funcionalidade e a compatibilidade com os protocolos WebSocket e STOMP.

Agradecemos a esses projetos anteriores e à comunidade de código aberto por seu trabalho, que permitiu a criação deste Sampler JMeter aprimorado e personalizado.

Para saber mais sobre como usar esta extensão no Apache JMeter, consulte as instruções fornecidas acima.

---
