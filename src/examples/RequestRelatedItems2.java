/* ===========================================================================
 *  Copyright 2009 Serena Software. All rights reserved.
 *
 *  Use of the Sample Code provided by Serena is governed by the following
 *  terms and conditions. By using the Sample Code, you agree to be bound by
 *  the terms contained herein. If you do not agree to the terms herein, do
 *  not install, copy, or use the Sample Code.
 *
 *  1.  GRANT OF LICENSE.  Subject to the terms and conditions herein, you
 *  shall have the nonexclusive, nontransferable right to use the Sample Code
 *  for the sole purpose of developing applications for use solely with the
 *  Serena software product(s) that you have licensed separately from Serena.
 *  Such applications shall be for your internal use only.  You further agree
 *  that you will not: (a) sell, market, or distribute any copies of the
 *  Sample Code or any derivatives or components thereof; (b) use the Sample
 *  Code or any derivatives thereof for any commercial purpose; or (c) assign
 *  or transfer rights to the Sample Code or any derivatives thereof.
 *
 *  2.  DISCLAIMER OF WARRANTIES.  TO THE MAXIMUM EXTENT PERMITTED BY
 *  APPLICABLE LAW, SERENA PROVIDES THE SAMPLE CODE AS IS AND WITH ALL
 *  FAULTS, AND HEREBY DISCLAIMS ALL WARRANTIES AND CONDITIONS, EITHER
 *  EXPRESSED, IMPLIED OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY
 *  IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A
 *  PARTICULAR PURPOSE, OF LACK OF VIRUSES, OF RESULTS, AND OF LACK OF
 *  NEGLIGENCE OR LACK OF WORKMANLIKE EFFORT, CONDITION OF TITLE, QUIET
 *  ENJOYMENT, OR NON-INFRINGEMENT.  THE ENTIRE RISK AS TO THE QUALITY OF
 *  OR ARISING OUT OF USE OR PERFORMANCE OF THE SAMPLE CODE, IF ANY,
 *  REMAINS WITH YOU.
 *
 *  3.  EXCLUSION OF DAMAGES.  TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE
 *  LAW, YOU AGREE THAT IN CONSIDERATION FOR RECEIVING THE SAMPLE CODE AT NO
 *  CHARGE TO YOU, SERENA SHALL NOT BE LIABLE FOR ANY DAMAGES WHATSOEVER,
 *  INCLUDING BUT NOT LIMITED TO DIRECT, SPECIAL, INCIDENTAL, INDIRECT, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, DAMAGES FOR LOSS OF
 *  PROFITS OR CONFIDENTIAL OR OTHER INFORMATION, FOR BUSINESS INTERRUPTION,
 *  FOR PERSONAL INJURY, FOR LOSS OF PRIVACY, FOR NEGLIGENCE, AND FOR ANY
 *  OTHER LOSS WHATSOEVER) ARISING OUT OF OR IN ANY WAY RELATED TO THE USE
 *  OF OR INABILITY TO USE THE SAMPLE CODE, EVEN IN THE EVENT OF THE FAULT,
 *  TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY, OR BREACH OF CONTRACT,
 *  EVEN IF SERENA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  THE
 *  FOREGOING LIMITATIONS, EXCLUSIONS AND DISCLAIMERS SHALL APPLY TO THE
 *  MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW.  NOTWITHSTANDING THE ABOVE,
 *  IN NO EVENT SHALL SERENA'S LIABILITY UNDER THIS AGREEMENT OR WITH RESPECT
 *  TO YOUR USE OF THE SAMPLE CODE AND DERIVATIVES THEREOF EXCEED US$10.00.
 *
 *  4.  INDEMNIFICATION. You hereby agree to defend, indemnify and hold
 *  harmless Serena from and against any and all liability, loss or claim
 *  arising from this agreement or from (i) your license of, use of or
 *  reliance upon the Sample Code or any related documentation or materials,
 *  or (ii) your development, use or reliance upon any application or
 *  derivative work created from the Sample Code.
 *
 *  5.  TERMINATION OF THE LICENSE.  This agreement and the underlying
 *  license granted hereby shall terminate if and when your license to the
 *  applicable Serena software product terminates or if you breach any terms
 *  and conditions of this agreement.
 *
 *  6.  CONFIDENTIALITY.  The Sample Code and all information relating to the
 *  Sample Code (collectively "Confidential Information") are the
 *  confidential information of Serena.  You agree to maintain the
 *  Confidential Information in strict confidence for Serena.  You agree not
 *  to disclose or duplicate, nor allow to be disclosed or duplicated, any
 *  Confidential Information, in whole or in part, except as permitted in
 *  this Agreement.  You shall take all reasonable steps necessary to ensure
 *  that the Confidential Information is not made available or disclosed by
 *  you or by your employees to any other person, firm, or corporation.  You
 *  agree that all authorized persons having access to the Confidential
 *  Information shall observe and perform under this nondisclosure covenant.
 *  You agree to immediately notify Serena of any unauthorized access to or
 *  possession of the Confidential Information.
 *
 *  7.  AFFILIATES.  Serena as used herein shall refer to Serena Software,
 *  Inc. and its affiliates.  An entity shall be considered to be an
 *  affiliate of Serena if it is an entity that controls, is controlled by,
 *  or is under common control with Serena.
 *
 *  8.  GENERAL.  Title and full ownership rights to the Sample Code,
 *  including any derivative works shall remain with Serena.  If a court of
 *  competent jurisdiction holds any provision of this agreement illegal or
 *  otherwise unenforceable, that provision shall be severed and the
 *  remainder of the agreement shall remain in full force and effect.
 * ===========================================================================
 */

package examples;

import java.util.ArrayList;
import java.util.List;

import com.clt.serena.resources.PropertyReader;
import com.serena.dmclient.api.BulkOperator;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;
import com.serena.dmclient.api.DimensionsRelatedObject;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.Request;
import com.serena.dmclient.api.SystemAttributes;
import com.serena.dmclient.api.SystemRelationship;
import com.serena.dmclient.objects.DimensionsObject;

/**
 * Example to show how to query related items to a request in Dimensions.
 */
public final class RequestRelatedItems2 {
    
    /**
     * Small bean-like class to pass related items around.
     */
    static final class RelatedItem {
        private String longFilename; 
        private String shortFilename; 
        private String revision; 
        private String createdDate; 
        private String relationshipType;
        String getLongFilename() {
            return longFilename;
        }
        void setLongFilename(String longFilename) {
            this.longFilename = longFilename;
        }
        String getShortFilename() {
            return shortFilename;
        }
        void setShortFilename(String shortFilename) {
            this.shortFilename = shortFilename;
        }
        String getRevision() {
            return revision;
        }
        void setRevision(String revision) {
            this.revision = revision;
        }
        String getCreatedDate() {
            return createdDate;
        }
        void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }
        String getRelationshipType() {
            return relationshipType;
        }
        void setRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
        }
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(longFilename);
            sb.append(";");
            sb.append(revision);
            sb.append(" (");
            sb.append(shortFilename);
            sb.append("), related as ");
            sb.append(relationshipType);
            sb.append(", created at ");
            sb.append(createdDate);
            return sb.toString();
        }
    }

	public static void main(final String[] args) {
//		if (args.length != 5) {
//			usage();
//		}
		
		PropertyReader prop = PropertyReader.getInstance();
		
		// connect using the command-line arguments.
		DimensionsConnectionDetails details = new DimensionsConnectionDetails();
		details.setUsername(prop.getUsername());
		details.setPassword(prop.getPassword());
		details.setDbName(prop.getDbName());
		details.setDbConn(prop.getDbConn());
		details.setServer(prop.getServer());
		DimensionsConnection connection = DimensionsConnectionManager
				.getConnection(details);
		try {
			listRequestRelatedItems(connection);
		} finally {
			// disconnect.
			connection.close();
		}
	}

	/**
	 * Assumes that the process model is set up in a compatible way. (i.e. with
	 * a QLARIUS product, CR request type, only title, detailed description, and
	 * severity mandatory on creation). You may need to change this code for
	 * other process models.
	 */
	static void listRequestRelatedItems(final DimensionsConnection connection) {
	    String requestSpec = "TERMINAL_P3_0_0_344";
	    String projectSpec = "TERMINAL:P3.0.0";
	    Request requestObj = connection.getObjectFactory().findRequest(requestSpec);
	    
	    Project projectObj = connection.getObjectFactory().getProject(projectSpec);
	    List relObjs = requestObj.getChildItems(null, projectObj);
	    Filter filter = new Filter();
	    connection.getObjectFactory().getProjects(null);
	    // Create a list of ItemRevision instances that we can use
	    // with BulkOperator.
	    List revObjs = new ArrayList(relObjs.size());
	    for (int i = 0; i < relObjs.size(); ++i) {
	        DimensionsRelatedObject relObj = (DimensionsRelatedObject) relObjs.get(i);
	        ItemRevision revObj = (ItemRevision) relObj.getObject();
	        revObjs.add(revObj);
	    }

	    // Bulk query the attributes for all of the ItemRevision
	    // instances at once.
	    BulkOperator bulk = connection.getObjectFactory().getBulkOperator(revObjs);
	    int[] attrs = {
	            SystemAttributes.FULL_PATH_NAME,
	            SystemAttributes.ITEMFILE_FILENAME,
	            SystemAttributes.REVISION,
	            SystemAttributes.CREATION_DATE };
	    bulk.queryAttribute(attrs);

	    // Copy the bulk-queried attributes from the ItemRevision
	    // instances into our custom 'RelatedItem' object instances.
        RelatedItem relItems[] = new RelatedItem[relObjs.size()];
	    for (int i = 0; i < relItems.length; ++i) {
            relItems[i] = new RelatedItem();
            // The relationship type is stored in the DimensionsRelatedObject
            // instance, and not in the ItemRevision instance itself
            DimensionsRelatedObject relObj = (DimensionsRelatedObject) relObjs.get(i);
            String relName;
            // DimensionsRelatedObject.getRelationship() returns an instance
            // of class SystemRelationship for request-item relationships
            // (since there are no custom request-item relationship names).
            // However, for other object classes, the return value could be
            // an instance of another class (e.g. RequestRelationshipType).
            DimensionsObject relType = relObj.getRelationship();
            if (SystemRelationship.AFFECTED.equals(relType)) {
                relName = "Affected";
            } else if (SystemRelationship.IN_RESPONSE.equals(relType)) {
                relName = "In Response To";
            } else {
                relName = "(unknown)";
            }
            relItems[i].setRelationshipType(relName);
            // The other attributes have been bulk-queried into the
            // ItemRevision instances.
	        ItemRevision revObj = (ItemRevision) revObjs.get(i);
	        relItems[i].setShortFilename((String) revObj.getAttribute(SystemAttributes.ITEMFILE_FILENAME));
	        relItems[i].setRevision((String) revObj.getAttribute(SystemAttributes.REVISION));
	        relItems[i].setCreatedDate((String) revObj.getAttribute(SystemAttributes.CREATION_DATE));
	        relItems[i].setLongFilename((String) revObj.getAttribute(SystemAttributes.FULL_PATH_NAME));
	    }
	    
	    // Print the RelatedItem instances.
	    for (int i = 0; i < relItems.length; ++i) {
	        System.out.println(relItems[i].toString());
	    }
	}

	private static void usage() {
		System.err.println("java " + RequestRelatedItems2.class.getName() + " \\");
		System.err.println("  {userID} {password} {dbName} {dbConn} {server}");
		System.exit(1);
	}
}