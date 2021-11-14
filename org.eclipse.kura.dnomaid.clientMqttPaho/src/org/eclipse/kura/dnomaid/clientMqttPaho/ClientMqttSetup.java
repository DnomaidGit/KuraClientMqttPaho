package org.eclipse.kura.dnomaid.clientMqttPaho;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.ConnectionConstants;
import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.ConnectionDefaults;
import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.global.Constants;


public class ClientMqttSetup implements Constants {
	    
    private final Map<String, Object> properties;

    ClientMqttSetup (final Map<String, Object> properties) {
        requireNonNull(properties);
        this.properties = properties;
        Updata();
    }
    
    Boolean isEnable() {
    	Boolean mValue = false;
	    Object propertie = this.properties.get(ENABLE_PROP_NAME);
	    if (nonNull(propertie) && propertie instanceof Boolean) {
	    	mValue = (Boolean) propertie;
	    }
	    return mValue;
    }   
    
    private void Updata(){    	
    	ConnectionConstants.getInst().setServer(getPropertie(SERVER_PROP_NAME, ConnectionDefaults.SERVER));
    	ConnectionConstants.getInst().setServer(getPropertie(SERVER_PROP_NAME, ConnectionDefaults.SERVER));
    	ConnectionConstants.getInst().setPort(getPropertie(PORT_PROP_NAME, ConnectionDefaults.PORT));
    	ConnectionConstants.getInst().setClientId(getPropertie(CLIENTID_PROP_NAME, ConnectionDefaults.CLIENT_ID));    	
    	ConnectionConstants.getInst().setCleanSession(isPropertie(CLEANSESSION_PROP_NAME, ConnectionDefaults.CLEAN_SESSION));
        ConnectionConstants.getInst().setUsername(getPropertie(USERNAME_PROP_NAME, ConnectionDefaults.USERNAME));
    	ConnectionConstants.getInst().setPassword(getPropertie(PASSWORD_PROP_NAME, ConnectionDefaults.PASSWORD));
    	ConnectionConstants.getInst().setPublishTopic(getPropertie(PUBLISHTOPIC_PROP_NAME, ConnectionDefaults.PUBLISH_TOPIC));
      	ConnectionConstants.getInst().setPublishMessage(getPropertie(PUBLISHMESSAGE_PROP_NAME, ConnectionDefaults.PUBLISH_MESSAGE));
    	ConnectionConstants.getInst().setSubscribeTopic(getPropertie(SUBSCRIBETOPIC_PROP_NAME, ConnectionDefaults.SUBSCRIBE_TOPIC));        
    }
    
    private String getPropertie(String propertieName, String propertieDefault) {
		String mValue = propertieDefault;
	    Object propertie = this.properties.get(propertieName);
	    if (nonNull(propertie) && propertie instanceof String) {
	    	mValue = (String) propertie;
	    }
	    return mValue;
    }
    private Integer getPropertie(String propertieName, Integer propertieDefault) {
    	Integer mValue = propertieDefault;
	    Object propertie = this.properties.get(propertieName);
	    if (nonNull(propertie) && propertie instanceof Integer) {
	    	mValue = (Integer) propertie;
	    }
	    return mValue;
    }   
    private Boolean isPropertie(String propertieName, Boolean propertieDefault) {
    	Boolean mValue = propertieDefault;
	    Object propertie = this.properties.get(propertieName);
	    if (nonNull(propertie) && propertie instanceof Boolean) {
	    	mValue = (Boolean) propertie;
	    }
	    return mValue;
    }   
}
