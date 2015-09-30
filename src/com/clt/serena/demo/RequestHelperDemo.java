package com.clt.serena.demo;

import java.util.List;

import com.clt.serena.helper.AttributeHelper;
import com.clt.serena.helper.ConnectionHelper;
import com.clt.serena.helper.ProjectHelper;
import com.clt.serena.helper.RequestHelper;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;

public class RequestHelperDemo {
	public static void main(String[] args) {
		DimensionsConnection conn = ConnectionHelper.getConnection();
		String projectName = "TERMINAL:P3.0.0";
		Project project = ProjectHelper.findProjectByName(conn, projectName);
		List<Request> requests = RequestHelper.findRequestsByProject(project);
//		BulkOperator bulk = conn.getObjectFactory().getBulkOperator(requests);
//		bulk.queryAttribute(SystemAttributes.STATUS);
		AttributeHelper.queryRequestAttributes(conn, requests);
		for (Request request: requests) {
			System.out.println(request.getName() + " " + request.getAttribute(SystemAttributes.STATUS));
			List<ItemRevision> items = RequestHelper.findRequestRelatedItems(conn, request);
			for (ItemRevision item: items) {
				System.out.println("Item: " + item.getAttribute(SystemAttributes.FULL_PATH_NAME));
			}
		}
		
		
		conn.close();
	}
}
