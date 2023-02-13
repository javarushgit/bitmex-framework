package org.example.framework;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;

import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebSocket extends Endpoint {
  private String url;
  private Session session;
  private StringBuffer output;

  public WebSocket(String url, StringBuffer output) {
    super();
    this.url = url;
    this.output = output;
  }

  public void connect() throws URISyntaxException, IOException, DeploymentException {
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();

    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .configurator(new ClientEndpointConfig.Configurator())
        .build();
    container.connectToServer(this, config, new URI(url));

  }

  public void send_message(String message) {
    session.getAsyncRemote().sendText(message);
  }


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    this.session = session;
    session.addMessageHandler(new MessageHandler.Whole<String>() {
     @Override
      public void onMessage(String message) {
        output.setLength(0);
        output.append(message);
      }
    });
  }

  public void onError(Session session, Throwable throwable) {
    super.onError(session, throwable);
  }


  public boolean isOpen() {
    return session.isOpen();
  }

}
