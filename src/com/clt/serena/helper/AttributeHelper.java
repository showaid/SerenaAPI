package com.clt.serena.helper;

import java.util.List;

import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;

public class AttributeHelper {
	private static int[] projectAttrs = {
		SystemAttributes.BASELINE_FLAGS,
		SystemAttributes.CREATION_DATE,
		SystemAttributes.CREATION_USER,
		SystemAttributes.DEFAULT_BRANCH,
		SystemAttributes.DEFAULT_PART,
		SystemAttributes.DEFAULT_REQUEST,
		SystemAttributes.DESCRIPTION,
		SystemAttributes.IDE_DM_UID,
		SystemAttributes.IDE_INITIAL,
		SystemAttributes.IDE_PROJECT_NAME,
		SystemAttributes.IDE_TAG,
		SystemAttributes.LAST_UPDATED_DATE,
		SystemAttributes.LAST_UPDATED_USER,
		SystemAttributes.LIB_CACHE_AREA,
		SystemAttributes.OBJECT_ID,
		SystemAttributes.OBJECT_UID,
		SystemAttributes.OBJECT_SPEC,
		SystemAttributes.PRODUCT_NAME,
		SystemAttributes.PROJECT_CM_RULES_USAGE,
		SystemAttributes.VISIBLE
	};

	private static int[] requestAttrs = {
		SystemAttributes.CATEGORY,
		SystemAttributes.CM_PHASE,
		SystemAttributes.CREATION_DATE,
		SystemAttributes.CREATION_USER,
		SystemAttributes.DELEGATED_OWNER_SITE,
		SystemAttributes.DESCRIPTION,
		SystemAttributes.LAST_UPDATED_DATE,
		SystemAttributes.LAST_UPDATED_USER,
		SystemAttributes.NUMBER,
		SystemAttributes.OBJECT_ID,
		SystemAttributes.OWNER_SITE,
		SystemAttributes.PRODUCT_NAME,
		SystemAttributes.PROJECT,
		SystemAttributes.STAGE,
		SystemAttributes.STATUS,
		SystemAttributes.TITLE,
		SystemAttributes.TYPE_NAME
	};
	
	private static int[] itemRevisionAttrs = {
		SystemAttributes.CREATION_DATE,
		SystemAttributes.CREATION_USER,
		SystemAttributes.DESCRIPTION,
		SystemAttributes.FILE_VERSION,
		SystemAttributes.FULL_PATH_NAME,
		SystemAttributes.IDE_DM_UID,
		SystemAttributes.IS_EXTRACTED,
		SystemAttributes.IS_LOCKED,
		SystemAttributes.ITEM_FORMAT,
		SystemAttributes.ITEMFILE_DIR,
		SystemAttributes.ITEMFILE_FILENAME,
		SystemAttributes.LAST_UPDATED_DATE,
		SystemAttributes.LAST_UPDATED_USER,
		SystemAttributes.LIB_CHECKSUM,
		SystemAttributes.LIB_FETCHSIZE,
		SystemAttributes.LOCKED_DATE,
		SystemAttributes.LOCKED_USER,
		SystemAttributes.MIME_TYPE,
		SystemAttributes.OBJECT_ID,
		SystemAttributes.OBJECT_SPEC,
		SystemAttributes.OBJECT_UID,
		SystemAttributes.PRODUCT_NAME,
		SystemAttributes.REVISION,
		SystemAttributes.REVISION_COMMENT,
		SystemAttributes.STAGE,
		SystemAttributes.STATUS,
		SystemAttributes.TYPE_NAME,
		SystemAttributes.UTC_MODIFIED_DATE,
		SystemAttributes.VARIANT
	};
	
	public static int[] getProjectAttrs() {
		return projectAttrs;
	}

	public static void queryProjectAttributes(DimensionsConnection conn, List<Project> objects) {
		BulkOperator bulk = conn.getObjectFactory().getBulkOperator(objects);
		bulk.queryAttribute(projectAttrs);
	}
	
	public static void queryRequestAttributes(DimensionsConnection conn, List<Request> requests) {
		BulkOperator bulk = conn.getObjectFactory().getBulkOperator(requests);
		bulk.queryAttribute(requestAttrs);
	}
	
	public static void queryItemRevisionAttributes(DimensionsConnection conn, List<ItemRevision> items) {
		BulkOperator bulk = conn.getObjectFactory().getBulkOperator(items);
		bulk.queryAttribute(itemRevisionAttrs);
	}
}
