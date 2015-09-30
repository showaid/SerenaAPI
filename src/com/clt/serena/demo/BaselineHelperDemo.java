package com.clt.serena.demo;

import java.util.List;

import com.clt.serena.helper.BaselineHelper;
import com.clt.serena.helper.ConnectionHelper;
import com.clt.serena.helper.ProjectHelper;
import com.serena.dmclient.api.Baseline;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsResult;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

public class BaselineHelperDemo {
	public static void main(String[] args) {
		DimensionsConnection conn = ConnectionHelper.getConnection();
		String projectName = "TERMINAL:P3.0.0";
		Project project = ProjectHelper.findProjectByName(conn, projectName);
		List<Baseline> baselines = BaselineHelper.findBaselinesByProject(project);

		for (Baseline baseline: baselines) {
			System.out.println(baseline.getName());

			List<ItemRevision> items = BaselineHelper.findBaselineRelatedItems(conn, project, baseline);
			BulkOperator bulk = conn.getObjectFactory().getBulkOperator(items);
//			bulk.queryAttribute(AttributeHelper.itemRevisionAttrs);
			DimensionsResult result = baseline.downloadExec("", "D:\\tmp\\serena\\", 
					"", "D:\\tmp\\log.txt", true, false, true, true, true);
			if (baseline == baselines.get(0)) {
				for (ItemRevision item: items) {
					String fullPath = (String) item.getAttribute(SystemAttributes.FULL_PATH_NAME);
					String revision = (String) item.getAttribute(SystemAttributes.REVISION);
					String objId = (String) item.getAttribute(SystemAttributes.OBJECT_ID);
					System.out.println("ITEM: " +  fullPath + " " + revision + " " + objId);
					
//					item.getCopyToFolder("D:\\tmp\\serena\\workspace", true, true, false);
				}
			}
		}

		conn.close();
	}
}
