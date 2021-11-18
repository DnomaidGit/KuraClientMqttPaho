package org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.client;

import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.Status;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements MqttCallback {
  public MqttCallbackHandler() {
  }
  @Override
  public void connectionLost(Throwable cause) {
    if (cause != null) {
      Connection c = Connection.getInstance();
      Status.getInst().changeConnectionStatus(Status.ConnectionStatus.DISCONNECTED);
      Object[] args = new Object[2];
      args[0] = c.getClientId();
      args[1] = c.getServer();
      String message = "connectionLost: "+ args;
      c.addAction(message + " -> " + cause.toString());
    }
  }
  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    Connection c = Connection.getInstance();
    String messagePayload = new String(message.getPayload());
    String[] args = new String[2];
    args[0] = "::>Message recieved: " +messagePayload;
    args[1] = " topic:"+topic+";qos:"+message.getQos()+";retained:"+message.isRetained();
    c.addAction(args[0] + args[1]);
    Status.getInst().setLastMessageReceived(args[0]);
  }
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) { }
}
