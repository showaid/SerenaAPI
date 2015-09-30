/* 
 * @author Hyunwoo Jo
 * @email showaid@cyberlogitec.com
 */

package com.clt.serena.helper;

import java.util.ArrayList;
import java.util.List;

import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsRelatedObject;
import com.serena.dmclient.api.DimensionsRuntimeException;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;

public class RequestHelper {
	public final static List<Request> findRequestsByProject(Project project) {
		List<DimensionsRelatedObject> objects = project.getChildRequests(null);
		List<Request> requests = new ArrayList<Request>();
		
		for (DimensionsRelatedObject object: objects) {
			requests.add((Request)object.getObject());
		}
		return requests;
	}

	public final static List<Request> findRequestsById(DimensionsConnection conn, String requestId) {
		String criteria = "%" + requestId + "%";
		Filter filter = new Filter();
		filter.criteria().add(new Filter.Criterion(SystemAttributes.OBJECT_ID, criteria, Filter.Criterion.EQUALS));
		List<Request> requests = conn.getObjectFactory().getBaseDatabase().getAllRequests(filter);
		
		for (Request request: requests) {
			System.out.println(request.getName());
		}
		return requests;
	}

	public final static Request findRequestById(DimensionsConnection conn, String requestId) {
		Filter filter = new Filter();
		filter.criteria().add(new Filter.Criterion(SystemAttributes.OBJECT_ID, requestId, Filter.Criterion.EQUALS));
		List<Request> requests = conn.getObjectFactory().getBaseDatabase().getAllRequests(filter);
		if (requests.size() > 1) throw new DimensionsRuntimeException("Duplicated request found");
		return requests.get(0);
	}
	
	static public List<ItemRevision> findRequestRelatedItems(final DimensionsConnection connection, final Request request) {
//		Request requestObj = connection.getObjectFactory().findRequest(request.getName());
		Project globalProjectObj = connection.getObjectFactory().getGlobalProject();
//		requestObj.flushRelatedObjects(ItemRevision.class, true);

//		List<DimensionsRelatedObject> relObjs = requestObj.getChildItems(null, globalProjectObj);
		request.flushRelatedObjects(ItemRevision.class, true);
		List<DimensionsRelatedObject> relObjs = request.getChildItems(null, globalProjectObj);
		List<ItemRevision> revObjs = new ArrayList<ItemRevision>(relObjs.size());
		
		for (DimensionsRelatedObject relObj: relObjs) {
			ItemRevision revObj = (ItemRevision) relObj.getObject();
			revObjs.add(revObj);
		}

		AttributeHelper.queryItemRevisionAttributes(connection, revObjs);

		return revObjs;
	}
}
