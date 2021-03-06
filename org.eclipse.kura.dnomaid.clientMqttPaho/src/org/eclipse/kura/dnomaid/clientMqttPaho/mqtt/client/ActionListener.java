package org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.client;

import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.Status;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class ActionListener implements IMqttActionListener {
  public enum Action {CONNECT, DISCONNECT, SUBSCRIBE, UNSUBSCRIBE, PUBLISH}

  private Action action;
  private String[] addArgs;
  private Connection connection;

  public ActionListener(Action action, String... addArgs) {
    this.action = action;
    this.addArgs = addArgs;
  }
  
  private void addAction(String actionTaken){
	connection = Connection.getInstance();
	connection.addAction(actionTaken);
  }
  @Override
  public void onSuccess(IMqttToken asyncActionToken) {
	  addAction("##Action::>"+action.toString());
	  switch (action) {
      case CONNECT : connect();break;
      case DISCONNECT : disconnect();break;
      case SUBSCRIBE : subscribe();break;
      case UNSUBSCRIBE : unsubscribe();break;
      case PUBLISH : publish();break;
    }
  }
  private void publish() {
	addAction("Published "+ addArgs[0] + addArgs[1] + addArgs[2] + addArgs[3]);
    Status.getInst().changeTopicStatus(Status.TopicStatus.PUBLISHED);
  }
  private void subscribe() {
	addAction("Subscribed "+ addArgs[0] + addArgs[1] + addArgs[2]);
    Status.getInst().changeTopicStatus(Status.TopicStatus.SUBSCRIBED);
  }
  private void unsubscribe() {
	addAction("Unsubscribe "+ addArgs[0] + addArgs[1] + addArgs[2]);
    Status.getInst().changeTopicStatus(Status.TopicStatus.UNSUBSCRIBED);
  }
  private void disconnect() {
	addAction("Disconnected "+ addArgs[0] + addArgs[1]);
    Status.getInst().changeConnectionStatus(Status.ConnectionStatus.DISCONNECTED);
    Status.getInst().changeTopicStatus(Status.TopicStatus.NONE);
  }
  private void connect() {    
	addAction("Connected "+ addArgs[0] + addArgs[1]);
    Status.getInst().changeConnectionStatus(Status.ConnectionStatus.CONNECTED);
  }
  @Override
  public void onFailure(IMqttToken token, Throwable exception) {
	  addAction("##Action::>"+action.toString());
	  addAction("!!Action Exception::>"+" Msg: "+exception.getMessage());
	  switch (action) {
	      case CONNECT : connect(exception);break;
	      case DISCONNECT : disconnect(exception);break;
	      case SUBSCRIBE : subscribe(exception);break;
	      case UNSUBSCRIBE : unsubscribe(exception);break;
	      case PUBLISH : publish(exception);break;
	  }
  }
  private void publish(Throwable exception) {
	  addAction("Failed publish "+ addArgs[0] + addArgs[1] + addArgs[2] + addArgs[3]);
	  Status.getInst().changeTopicStatus(Status.TopicStatus.ERROR);
  }
  private void subscribe(Throwable exception) {
	  addAction("Failed subscribe "+ addArgs[0] + addArgs[1] + addArgs[2]);
	  Status.getInst().changeTopicStatus(Status.TopicStatus.ERROR);
  }
  private void unsubscribe(Throwable exception) {
	  addAction("Failed unsubscribe "+ addArgs[0] + addArgs[1] + addArgs[2]);
	  Status.getInst().changeTopicStatus(Status.TopicStatus.ERROR);
  }
  private void disconnect(Throwable exception) {
	  addAction("Failed disconnect "+ addArgs[0] + addArgs[1]);
	  Status.getInst().changeConnectionStatus(Status.ConnectionStatus.ERROR);
	  Status.getInst().changeTopicStatus(Status.TopicStatus.NONE);
  }
  private void connect(Throwable exception) {
	  addAction("Failed connect "+ addArgs[0] + addArgs[1]);
	  Status.getInst().changeConnectionStatus(Status.ConnectionStatus.ERROR);
  }
}
