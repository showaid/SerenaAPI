package com.clt.serena.demo;

import java.util.ArrayList;
import java.util.List;

import com.clt.serena.helper.AttributeHelper;
import com.clt.serena.helper.BaselineHelper;
import com.clt.serena.helper.ConnectionHelper;
import com.clt.serena.helper.ProjectHelper;
import com.clt.serena.helper.RequestHelper;
import com.serena.dmclient.api.Baseline;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;

public class AttributeHelperDemo {
	public static void main(String[] args) {
		DimensionsConnection conn = ConnectionHelper.getConnection();
		String projectName = "TERMINAL:P3.0.0";
		Project proj = ProjectHelper.findProjectByName(conn, projectName);
		System.out.println("Project: " + proj.getName());
		List<Project> prjList = new ArrayList<Project>();
		prjList.add(proj);
		AttributeHelper.queryProjectAttributes(conn, prjList);
		for (Project prj: prjList) {
			System.out.println(prj.getAttribute(SystemAttributes.OBJECT_SPEC));
			
			List<Request> rqList = RequestHelper.findRequestsByProject(prj);
			AttributeHelper.queryRequestAttributes(conn, rqList);
//			for (Request rq : rqList) {
//				System.out.println(rq.getAttribute(SystemAttributes.TITLE));
//				List<ItemRevision> items = RequestHelper.findRequestRelatedItems(conn, rq);
//				AttributeHelper.queryItemRevisionAttributes(conn, items);
//				for (ItemRevision item : items) {
//					System.out.println(item.getAttribute(SystemAttributes.FULL_PATH_NAME));
//				}
//			}
			List<Baseline> blList = BaselineHelper.findBaselinesByProject(prj);
			for (Baseline bl: blList) {
				List<ItemRevision> items = BaselineHelper.findBaselineRelatedItems(conn,prj, bl);
//				BulkOperator bulk = conn.getObjectFactory().getBulkOperator(items);
//				bulk.queryAttribute(SystemAttributes.FULL_PATH_NAME);
				AttributeHelper.queryItemRevisionAttributes(conn, items);
				for (ItemRevision item : items) {
					System.out.print(item.getAttribute(SystemAttributes.FULL_PATH_NAME));
					System.out.println("@" + item.getAttribute(SystemAttributes.OBJECT_ID));
				}
			}
		}
		
	}
}
