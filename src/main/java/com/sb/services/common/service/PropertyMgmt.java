package com.sb.services.common.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyMgmt {

	private static Properties prop = new Properties();

	public static void loadProperties() {
		InputStream input = null;
		try {
			input = new FileInputStream("./config.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getVsomUid() {
		return prop.getProperty("VSOM_UID");
	}

	public static String getRegistrationUid() {
		return prop.getProperty("REGISTRATION_UID");
	}

	public static String getLocation() {
		return prop.getProperty("LOCATION");
	}

	public static String getVsomVersion() {
		return prop.getProperty("VSOM_VERSION");
	}

	public static String getTenantUid() {
		return prop.getProperty("TENANT_UID");
	}

	public static String getBaseUrl() {
		return prop.getProperty("BASE_URL");
	}

	public static String getTestTopic() {
		return prop.getProperty("TEST_TOPIC");
	}

	public static String getRequestTopic() {
		return prop.getProperty("REQUEST_TOPIC");
	}

	public static String getResponseTopic() {
		return prop.getProperty("RESPONSE_TOPIC");
	}

	public static String getBroker() {
		return prop.getProperty("BROKER");
	}

	public static String getRequestClientId() {
		return prop.getProperty("REQUEST_CLIENT_ID");
	}

	public static String getResponseClientId() {
		return prop.getProperty("RESPONSE_CLIENT_ID");
	}

	public static String getVsmxPasswordFile() {
		return prop.getProperty("VSMX_PASSWORD_FILE");
	}

	public static String getRmqHost() {
		return prop.getProperty("RMQ_HOST");
	}

	public static Integer getRmqPort() {
		return Integer.parseInt(prop.getProperty("RMQ_PORT"));
	}

	public static String getRmqVHost() {
		return prop.getProperty("RMQ_VHOST");
	}

	public static String getRmqUserName() {
		return prop.getProperty("RMQ_USERNAME");
	}

	public static String getRmqPassword() {
		return prop.getProperty("RMQ_PASSWORD"); 

	}

	public static String getRmqQueue() {
		String queueName = System.getenv("RMQ_QUEUE");
		if (queueName == null) {
			return prop.getProperty("RMQ_QUEUE");
		}
		return queueName;
	}

}
