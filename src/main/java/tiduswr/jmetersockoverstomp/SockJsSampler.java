package tiduswr.jmetersockoverstomp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.tomcat.websocket.Constants;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
 
public class SockJsSampler extends AbstractJavaSamplerClient implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Jackson2SockJsMessageCodec CODEC = new Jackson2SockJsMessageCodec();
	
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String TRANSPORT = "transport";
	public static final String HOST = "host";
	public static final String PATH = "path";
	public static final String TOPIC = "topic";
 	public static final String HEART_BEAT_DEFAULT = "heart-beat:0,0";
	public static final String CONNECTION_HEADERS_AUTHORIZATION = "connectionHeadersAuthJwt";
	public static final String CONNECTION_HEADERS_ACCEPT_VERSION = "connectionHeadersAcceptVersion";
	
	
	@Override
    public void setupTest(JavaSamplerContext context){
		super.setupTest(context);
    }
	
    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(HOST, "https://[xxx].[xxx]");
        defaultParameters.addArgument(TOPIC, "/app/message");
		defaultParameters.addArgument(PATH, "/[xxx]/");
        defaultParameters.addArgument(CONNECTION_HEADERS_AUTHORIZATION, "Authorization:Bearer [xxx]");
        defaultParameters.addArgument(CONNECTION_HEADERS_ACCEPT_VERSION, "accept-version:1.1,1.0");
        return defaultParameters;
    }
 
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.sampleStart();
		ResponseMessage responseMessage = new ResponseMessage();
         
        try {

        	this.createWebsocketConnection(context, responseMessage);
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(true);
            sampleResult.setResponseMessage(responseMessage.getMessage());
            sampleResult.setResponseCodeOK();

        } catch (Exception e) {

        	sampleResult.sampleEnd();
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage(responseMessage.getMessage());

            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            sampleResult.setResponseData(stringWriter.toString(), null);
            sampleResult.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
            sampleResult.setResponseCode("500");

        }
 
        return sampleResult;
    }
    
    @Override
    public void teardownTest(JavaSamplerContext context) {
    	super.teardownTest(context);
    }

 	public void createWebsocketConnection(JavaSamplerContext context, ResponseMessage responseMessage) throws Exception {
		CountDownLatch latch = new CountDownLatch(1);

 		StandardWebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
		trustAll_ManagerConfig(simpleWebSocketClient); 				
 		configureStompClient(simpleWebSocketClient);
 		
		WebSocketStompClient stompClient = configureStompClient(simpleWebSocketClient);
 		URI stompUrlEndpoint = new URI(context.getParameter(HOST) + context.getParameter(PATH));
 		StompSessionHandler sessionHandler = new SockJsWebsocketStompSessionHandler(
			responseMessage, latch, context.getParameter(TOPIC)
		);
 		 		
 		WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
 		StompHeaders connectHeaders = new StompHeaders();
 		String connectionHeadersString = this.getConnectionsHeaders(context);
 		String[] splitHeaders = connectionHeadersString.split("\n");

		String m = "\n\n>> CONNECT";
 		for (int i = 0; i < splitHeaders.length; i++) {
 			int key = 0;
 			int value = 1;
			m = m + "\n" + splitHeaders[i];
 			String[] headerParameter = splitHeaders[i].split(":");
 			connectHeaders.add(headerParameter[key], headerParameter[value]);			
 		}
 		
 		String startMessage = "\n[Roteiro de Execução]"
 						    + "\n - Abertura de Conexão STOMP"
 							+ "\n - Envio de Mensagem para o tópico"
 							+ "\n - Desconexão do Servidor Websocket"
							+ m;
 		responseMessage.addMessage(startMessage);
		
 		StompSession session = stompClient.connect(stompUrlEndpoint.toString(), handshakeHeaders, connectHeaders, 
			sessionHandler, new Object[0]).get();

		latch.await();

 		stompClient.stop();
		session.disconnect();
 	
 		String messageProblems = "\n[Problems]"
								+ "\n" + responseMessage.getProblems();
 		
 		responseMessage.addMessage(messageProblems);
 	}
    
	private void trustAll_ManagerConfig(StandardWebSocketClient simpleWebSocketClient) throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { new BlindTrustManager() }, null);
		Map<String, Object> userProperties = new HashMap<>();
		userProperties.put(Constants.SSL_CONTEXT_PROPERTY, sslContext);
		simpleWebSocketClient.setUserProperties(userProperties);
	}

	private WebSocketStompClient configureStompClient(StandardWebSocketClient simpleWebSocketClient){
		List<Transport> transports = new ArrayList<>(1);
     	transports.add(new WebSocketTransport(simpleWebSocketClient));
		SockJsClient sockJsClient = new SockJsClient(transports);
 		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
 		MessageConverter messageConverter = new MappingJackson2MessageConverter();
		stompClient.setMessageConverter(messageConverter);
		return stompClient;
	}
    
    private String getConnectionsHeaders(JavaSamplerContext context) {
    	return String.format(
			"%s\n%s\n%s", 
			context.getParameter(CONNECTION_HEADERS_AUTHORIZATION),
			context.getParameter(CONNECTION_HEADERS_ACCEPT_VERSION),
			HEART_BEAT_DEFAULT			
		);
    }

}
