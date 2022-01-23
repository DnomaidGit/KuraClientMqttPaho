package org.eclipse.kura.dnomaid.clientMqttPaho;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.Mqtt;
import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.ConnectionConstants;
import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.Status;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMqttService implements ConfigurableComponent{
	private Mqtt mqtt;
	//Logger /var/log/kura.log or /var/log/kura.log
	private static final Logger S_LOGGER = LoggerFactory.getLogger(ClientMqttService.class);
    private static final String ALIAS_APP_ID = "ClientMqttPaho";     
    private final ScheduledExecutorService worker;
    private ScheduledFuture<?> handle;
    private static boolean ENABLE;
    private static String LAST_SUBSCRIBE;
    private static String LAST_MESSAGE_RECEIVED;
    private ClientMqttSetup clientMqttSetup;
    
    public ClientMqttService() {
    	super();
    	this.worker = Executors.newSingleThreadScheduledExecutor();
    	this.mqtt = new Mqtt();
    	ENABLE = false;
    	LAST_SUBSCRIBE = " ";
    	LAST_MESSAGE_RECEIVED = " ";
    }
     
    // ----------------------------------------------------------------
    // Activation APIs
    // ----------------------------------------------------------------
    protected void activate(ComponentContext componentContext,Map<String, Object> properties) {
    	S_LOGGER.info("##Active component " + ALIAS_APP_ID); 
    	updated(properties);    	
    	S_LOGGER.info("##Bundle " + ALIAS_APP_ID + " has started!");
    }    
    protected void deactivate(ComponentContext componentContext) {
    	S_LOGGER.info("##Desactive component " + ALIAS_APP_ID);    	
    	if(Status.getInst().isConnected()) {
			mqtt.disconnection();
		}
    	S_LOGGER.info("Bundle " + ALIAS_APP_ID + " has stopped!");
        this.worker.shutdown();
    }    
    public void updated(Map<String, Object> properties) {
    	S_LOGGER.info("Updated properties..." + ALIAS_APP_ID);
    	dumpProperties("Update", properties);
    	this.clientMqttSetup = new ClientMqttSetup(properties);    	
        ENABLE = this.clientMqttSetup.isEnable();  
        // store the properties received
        doPublish();
        S_LOGGER.info("...Updated properties done."+ ALIAS_APP_ID);
    }
    
    // ----------------------------------------------------------------
    // Private methods
    // ----------------------------------------------------------------  
    private static void dumpProperties(final String action, final Map<String, Object> properties) {
        final Set<String> keys = new TreeSet<>(properties.keySet());
        for (final String key : keys) {
            S_LOGGER.info("{} - {}: {}", action, key, properties.get(key));
        }
    }	    
    private void doPublish() {
        // cancel a current worker handle if one if active
        if (handle != null) {
            handle.cancel(true);
        }       
        // schedule a new worker based on the properties of the service
        int pubrate = 5000;
        handle = worker.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	Thread.currentThread().setName(getClass().getSimpleName());
	            	if(ENABLE) {
	            		if(!Status.getInst().isConnectedOrConnecting()) {
	                		mqtt.connection();	                		
	                	}
	            		if(Status.getInst().isConnected()& !LAST_SUBSCRIBE.equals(ConnectionConstants.getInst().getSubscribeTopic())) {
	            			mqtt.subscribe();
	            			LAST_SUBSCRIBE = ConnectionConstants.getInst().getSubscribeTopic();
	            		}
	            		if(Status.getInst().isConnected()) {
		            		String publishTopic = ConnectionConstants.getInst().getPublishTopic();
		            		String publishMessage = ConnectionConstants.getInst().getPublishMessage();
		            		mqtt.publish(publishTopic, publishMessage);
		            		S_LOGGER.info("#Publish topic: {}  #Publish message: {}",publishTopic,publishMessage);
		            		if (Status.getInst().isNewMessageReceived(LAST_MESSAGE_RECEIVED)) {
		            			LAST_MESSAGE_RECEIVED = Status.getInst().getLastMessageReceived();
		            			S_LOGGER.info("#Last message received: {}",LAST_MESSAGE_RECEIVED);
		            		}
	            		}
	            		if(!Status.getInst().noError())S_LOGGER.error("##Error connetion!!");
	            	}else {
	            		if(Status.getInst().isConnected()) {
	            			mqtt.disconnection();
	            		}
	            	}
            	}
        }, 0, pubrate, TimeUnit.MILLISECONDS);
    }
}