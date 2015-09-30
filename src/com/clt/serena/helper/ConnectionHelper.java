/* 
 * @author Hyunwoo Jo
 * @email showaid@cyberlogitec.com
 */

package com.clt.serena.helper;

import com.clt.serena.resources.PropertyReader;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;

public class ConnectionHelper {
	public static DimensionsConnection getConnection() {
		PropertyReader prop = PropertyReader.getInstance();
		DimensionsConnectionDetails details = new DimensionsConnectionDetails();
		details.setUsername(prop.getUsername());
		details.setPassword(prop.getPassword());
		details.setDbName(prop.getDbName());
		details.setDbConn(prop.getDbConn());
		details.setServer(prop.getServer());
		
		return DimensionsConnectionManager.getConnection(details);
	}
}
