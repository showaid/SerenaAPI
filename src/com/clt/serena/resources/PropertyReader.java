package com.clt.serena.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	
	private static PropertyReader propReader = new PropertyReader();
	
	private InputStream inputStream;
	private String server;
	private String dbName;
	private String dbConn;
	private String username;
	private String password;
	private String prdId;
	
	private PropertyReader() {
		readProp();
	}
	
	public static PropertyReader getInstance() {
		return propReader; 
	}
	
	private void readProp() {
		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			this.server = prop.getProperty("server");
			this.dbName = prop.getProperty("dbName");
			this.username = prop.getProperty("user");
			this.password = prop.getProperty("password");
			this.prdId = prop.getProperty("prdId");
			this.dbConn = prop.getProperty("dbConn");
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Exception: " + e);
			}
		}
	}

	public String getServer() {
		return server;
	}

	public String getDbName() {
		return dbName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPrdId() {
		return prdId;
	} 
	
	public String getDbConn() {
		return dbConn;
	}
}
