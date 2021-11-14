package org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global;

public class Status {
    private ConnectionStatus status;
    private TopicStatus topicStatus;
    private String lastMessageReceived;
    private Boolean newMessageReceived;
    public static final String SPACE = " ";
    public static final String EMPTY = new String();
    public enum ConnectionStatus {CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED, ERROR, NONE}
    public enum TopicStatus {SUBSCRIBED, UNSUBSCRIBED, PUBLISHED, ERROR, NONE}

    private static Status instance = null;
    private Status(){
    	status = ConnectionStatus.NONE;
    	topicStatus = TopicStatus.NONE;
    	lastMessageReceived = SPACE;
    }
    public  static synchronized Status getInst() {
        if (instance==null) {
            instance=new Status();
        }
        return instance;
    }
    public void addStatusChange(String status) {Notify.printf(status); }
    public void changeConnectionStatus(ConnectionStatus connectionStatus) { 
    	status = connectionStatus;
    	addStatusChange("--Status--: "+ status);
    }
    public String getConnectionStatus() { return status.toString(); }
    public void changeTopicStatus(TopicStatus topicStatus) { 
    	this.topicStatus = topicStatus; 
    	addStatusChange("--Status--: "+ this.topicStatus);    	
    }
    public String getTopicStatus() { return topicStatus.toString(); }
    public boolean isNoneTopicStatus() {
        return topicStatus == TopicStatus.NONE;
    }
    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }
    public boolean isSubscribed() { return (topicStatus==TopicStatus.SUBSCRIBED||topicStatus==TopicStatus.PUBLISHED);}
    public boolean isConnectedOrConnecting() {
        return (status == ConnectionStatus.CONNECTED) || (status == ConnectionStatus.CONNECTING);
    }
    public boolean noError() { return status != ConnectionStatus.ERROR; }
	public String getLastMessageReceived() {
		return lastMessageReceived;
	}
	public void setLastMessageReceived(String lastMessageReceived) {
		this.lastMessageReceived = lastMessageReceived;
	}
	public Boolean isNewMessageReceived(String lastMessageReceived) {
		newMessageReceived = false;
		if (!this.lastMessageReceived.equals(lastMessageReceived))newMessageReceived = true;
		return newMessageReceived;
	}
}