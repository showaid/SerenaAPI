/* 
 * @author Hyunwoo Jo
 * @email showaid@cyberlogitec.com
 */

package com.clt.serena.helper;

import java.util.ArrayList;
import java.util.List;

import com.serena.dmclient.api.Baseline;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsRelatedObject;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

public class BaselineHelper {
	public static List<Baseline> findBaselinesByProject(Project project) {
		List<DimensionsRelatedObject> baselineObjects = project.getChildBaselines(null);
		List<Baseline> baselines = new ArrayList<Baseline>();
		
		for (DimensionsRelatedObject baseline: baselineObjects) {
			baselines.add((Baseline)baseline.getObject());
		}
		
		return baselines;
	}
	
	static public List<ItemRevision> findBaselineRelatedItems(final DimensionsConnection connection, 
		final Project project, final Baseline baseline) {
		baseline.flushRelatedObjects(ItemRevision.class, true);
		List<DimensionsRelatedObject> revItems = baseline.getChildItems(null, project);

		List<ItemRevision> revObjs = new ArrayList<ItemRevision>(revItems.size());
		for (DimensionsRelatedObject relObj: revItems) {
			ItemRevision revObj = (ItemRevision) relObj.getObject();
			revObjs.add(revObj);
		}
		
		return revObjs;
	}
}
