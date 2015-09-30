package com.clt.serena.demo;

import java.util.List;

import com.clt.serena.helper.AttributeHelper;
import com.clt.serena.helper.ConnectionHelper;
import com.clt.serena.helper.ProjectHelper;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

public class ProjectHelperDemo {
	public static void main(String[] args) {
		DimensionsConnection conn = ConnectionHelper.getConnection();

		System.out.println("Find all projects in the system: ");
		List<Project> projects = ProjectHelper.findAllProject(conn);
		AttributeHelper.queryProjectAttributes(conn, projects);
		for (Project project: projects) {
			System.out.println("\t" + project.getName());
			System.out.println("BASELINE_FLAGS: " + project.getAttribute(SystemAttributes.BASELINE_FLAGS));
			System.out.println("CREATION DATE: " + project.getAttribute(SystemAttributes.CREATION_DATE));
			System.out.println("CREATION USER: " + project.getAttribute(SystemAttributes.CREATION_USER));
			System.out.println("DEFAULT BRANCH: " + project.getAttribute(SystemAttributes.DEFAULT_BRANCH));
			System.out.println("DEFAULT PART: " + project.getAttribute(SystemAttributes.DEFAULT_PART));
			System.out.println("DEFAULT REQUEST: " + project.getAttribute(SystemAttributes.DEFAULT_REQUEST));
			System.out.println("DESCRIPTION: " + project.getAttribute(SystemAttributes.DESCRIPTION));
			System.out.println("IDE_DM_UID" + project.getAttribute(SystemAttributes.IDE_DM_UID));
			System.out.println("IDE_INITIAL" + project.getAttribute(SystemAttributes.IDE_INITIAL));
			System.out.println("IDE PROJECT NAME: " + project.getAttribute(SystemAttributes.IDE_PROJECT_NAME));
			System.out.println("IDE TAG: " + project.getAttribute(SystemAttributes.IDE_TAG));
			System.out.println("LAST UPDATED DATE: " + project.getAttribute(SystemAttributes.LAST_UPDATED_DATE));
			System.out.println("LAST UPDATED USER: " + project.getAttribute(SystemAttributes.LAST_UPDATED_USER));
			System.out.println("LIB CACHE AREA: " + project.getAttribute(SystemAttributes.LIB_CACHE_AREA));
			System.out.println("OBJECT ID: " + project.getAttribute(SystemAttributes.OBJECT_ID));
			System.out.println("OBJECT SPEC: " + project.getAttribute(SystemAttributes.OBJECT_SPEC));
			System.out.println("OBJECT UID: " + project.getAttribute(SystemAttributes.OBJECT_UID));
			System.out.println("PRODUCT NAME: " + project.getAttribute(SystemAttributes.PRODUCT_NAME));
			System.out.println("PROJECT CM RULES USAGE: " + project.getAttribute(SystemAttributes.PROJECT_CM_RULES_USAGE));
			System.out.println("VISIBLE: " + project.getAttribute(SystemAttributes.VISIBLE));

			System.out.println();
		}
		
		String projectName = "TERMINAL:P3.0.0";
		System.out.println("\nFind a specific project in the system:");
		Project project = ProjectHelper.findProjectByName(conn, projectName);
		System.out.println("\t" + project.getName());
		
		projectName = "TERMINAL:P3.0.0";
		System.out.println("\nFind projects by name:");
		projects = ProjectHelper.findProjectsByName(conn, projectName);
		for (Project prj: projects) {
			System.out.println("\t" + prj.getName());
		}
				
		conn.close();
	}
}
