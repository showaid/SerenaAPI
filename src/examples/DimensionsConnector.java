package examples;

import com.clt.serena.resources.PropertyReader;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;

public class DimensionsConnector {
	public static DimensionsConnection getDimesionsConnection() {
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
