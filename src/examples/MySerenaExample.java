package examples;

import java.util.ArrayList;
import java.util.List;

import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsRelatedObject;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;

public class MySerenaExample {
	public static void main(String[] args) {
		DimensionsConnection connection = DimensionsConnector.getDimesionsConnection();
		Filter filter = new Filter();
		filter.criteria().add(new Filter.Criterion(SystemAttributes.PRODUCT_NAME, "TERMINAL%", Filter.Criterion.EQUALS));
		List<Project> projects = new ArrayList<Project>();
		projects = connection.getObjectFactory().getProjects(filter);
		
		for (Project project : projects) {
			System.out.println(project.getName());
			List<DimensionsRelatedObject> objects = project.getChildRequests(null);
			for (DimensionsRelatedObject object : objects) {
				Request request = (Request)object.getObject();
				System.out.println("\t" + request.getName());
			}
		}
		
	}
}
