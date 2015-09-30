package examples;

import java.util.List;

import com.clt.serena.resources.PropertyReader;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

public class FilterProjects {
	public static void main(String[] args) {
		PropertyReader prop = PropertyReader.getInstance();
		DimensionsConnectionDetails details = new DimensionsConnectionDetails();
		details.setUsername(prop.getUsername());
		details.setPassword(prop.getPassword());
		details.setDbName(prop.getDbName());
		details.setDbConn(prop.getDbConn());
		details.setServer(prop.getServer());

		DimensionsConnection connection = DimensionsConnectionManager.getConnection(details);
		try {
			findProjectsByName(connection, "TERMINAL");
			System.out.println(findProjectByName(connection, "TERMINAL:HJIT").getName());
		} finally {
			connection.close();
		}
	}

	private final static List<Project> findAllProject(DimensionsConnection conn) {
		List<Project> projects = conn.getObjectFactory().getProjects(null);
		for (Project project: projects) {
			System.out.print(project.getName());
		}
		return projects;
	}
	
	private final static List<Project> findProjectsByName(DimensionsConnection conn, String projectName) {
		String criteria = projectName + "%";
		Filter filter = new Filter();
		filter.criteria().add(new Filter.Criterion(SystemAttributes.PRODUCT_NAME, criteria, Filter.Criterion.EQUALS));
		List<Project> projects = conn.getObjectFactory().getProjects(filter);
		
		return projects;
	}
	
	private final static Project findProjectByName(DimensionsConnection conn, String projectName) {
		return conn.getObjectFactory().getProject(projectName);
	}
}
