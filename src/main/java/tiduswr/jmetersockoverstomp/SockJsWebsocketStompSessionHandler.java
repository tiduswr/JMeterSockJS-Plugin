package tiduswr.jmetersockoverstomp;

import java.util.concurrent.CountDownLatch;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class SockJsWebsocketStompSessionHandler extends StompSessionHandlerAdapter
{
	private CountDownLatch countDownLatch;
	private String messageStorage = "";
	private ResponseMessage responseMessage;
	private String topic;
		
	public SockJsWebsocketStompSessionHandler(ResponseMessage responseMessage, CountDownLatch countDownLatch, String topic) {
		this.countDownLatch = countDownLatch;
		this.responseMessage = responseMessage;
		this.topic = topic;
	}
	
	public String getMessageStorage() {
		return this.messageStorage;
	}
	
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		String connectionMessage = "\n - Conexão Realizada!"
								 + "\n - Session id: " + session.getSessionId();

		this.responseMessage.addMessage(connectionMessage);

		Payload message = new Payload();
		message.setMessage("JMETER BOT MESSAGE");
		message.setSender("user");
		message.setRead(false);
		message.setStatus("MESSAGE");
		session.send(topic, message);
		this.countDownLatch.countDown();
	}
	
	@Override
	public void handleException(
		StompSession session, 
		StompCommand command, 
		StompHeaders headers,
		byte[] payload, 
		Throwable exception
	) {
		String exceptionMessage = " - Received exception: " + exception.getMessage();
		
		this.responseMessage.addProblem(exceptionMessage);
	}
	
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		String handleFrameMessage = " - Received frame: " + payload.toString();

		this.responseMessage.addMessage(handleFrameMessage);
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		String exceptionMessage = " - Received exception: " + exception.getMessage();
		
		this.responseMessage.addProblem(exceptionMessage);
	}

}
