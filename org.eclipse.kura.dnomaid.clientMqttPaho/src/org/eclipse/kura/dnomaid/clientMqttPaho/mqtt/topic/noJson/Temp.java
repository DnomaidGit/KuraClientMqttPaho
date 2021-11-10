package org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.topic.noJson;

import org.eclipse.kura.dnomaid.clientMqttPaho.mqtt.topic.ActionTopic;

public class Temp implements ActionTopic {
	private String name = "Temp";
	private String Temp;

	public String getTemp() {
		return Temp;
	}

	public void setTemp(String temp) {
		Temp = temp;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getValueTopic(TypeTopic typeTopic) {
		String str = "--.--";
		switch (typeTopic) {
			case Temperature:
				str = getTemp();
				break;
			default:
				str = "??¿¿";
		}
		return str;
	}

}
