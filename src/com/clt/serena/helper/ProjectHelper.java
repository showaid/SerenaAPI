/* 
 * @author Hyunwoo Jo
 * @email showaid@cyberlogitec.com
 */

package com.clt.serena.helper;

import java.util.List;

import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

public class ProjectHelper {
	public final static List<Project> findAllProject(DimensionsConnection conn) {
		List<Project> projects = conn.getObjectFactory().getProjects(null);
		
		return projects;
	}
	
	public final static List<Project> findProjectsByName(DimensionsConnection conn, String projectName) {
		String criteria = "%" + projectName + "%";
		Filter filter = new Filter();
		filter.criteria().add(new Filter.Criterion(SystemAttributes.OBJECT_SPEC, criteria, Filter.Criterion.EQUALS));
		List<Project> projects = conn.getObjectFactory().getProjects(filter);
		
		return projects;
	}
	
	public final static Project findProjectByName(DimensionsConnection conn, String projectName) {
		return conn.getObjectFactory().getProject(projectName);
	}
}
