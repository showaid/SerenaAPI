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

import java.util.List;

import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;
import com.serena.dmclient.api.DimensionsObjectFactory;
import com.serena.dmclient.api.DimensionsRelatedObject;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.FilterOptions;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;
import com.serena.dmclient.objects.DimensionsObject;

/**
 * Example to show how to get derived and built item revisions in Dimensions.
 */
public final class GetDerivedAndBuiltItems {
	public static void main(final String[] args) {
		if (args.length != 5) {
			usage();
		}
		// connect using the command-line arguments.
		DimensionsConnectionDetails details = new DimensionsConnectionDetails();
		details.setUsername(args[0]);
		details.setPassword(args[1]);
		details.setDbName(args[2]);
		details.setDbConn(args[3]);
		details.setServer(args[4]);
		DimensionsConnection connection = DimensionsConnectionManager
				.getConnection(details);
		try {
			ItemRevision item = findItem(connection);
			showDerivedItems(item);
			showBuiltItems(item);
		} finally {
			// disconnect.
			connection.close();
		}
	}

	/**
	 * find and show built (aka parent) items
	 */
	private static void showBuiltItems(ItemRevision item) {
		Filter filter = new Filter();
        filter.criteria().add(
        		new Filter.Criterion(FilterOptions.BUILT_RELATIONSHIP, Boolean.TRUE, Filter.Criterion.EQUALS));
        List parents = item.getChildItems(filter);

        if (parents != null && parents.size() > 0) {
        	for (int i=0; i<parents.size(); i++) {
        		Object parent = parents.get(i);
        		DimensionsObject obj = ((DimensionsRelatedObject)parent).getObject();
        		ItemRevision ir = (ItemRevision)obj;
        		printItem(ir, "built", i);
        	}
        } else {
        	System.out.println("Cannot find buit items");
        }
	}

	/**
	 * find and show derived (aka child) items
	 */
	static void showDerivedItems(ItemRevision item) {
		Filter filter = new Filter();
        filter.criteria().add(
        		new Filter.Criterion(FilterOptions.DERIVED_RELATIONSHIP, Boolean.TRUE, Filter.Criterion.EQUALS));
        List childs = item.getChildItems(filter);

        if (childs != null && childs.size() > 0) {
        	for (int i=0; i<childs.size(); i++) {
        		Object child = childs.get(i);
        		DimensionsObject obj = ((DimensionsRelatedObject)child).getObject();
        		ItemRevision ir = (ItemRevision)obj;
        		printItem(ir, "derived", i);
        	}
        } else {
        	System.out.println("Cannot find derived items");
        }
	}

	static void printItem(ItemRevision ir, String relationshipType, int index) {
		final int[] itemAttrNums = new int[] {SystemAttributes.ITEMFILE_FILENAME, SystemAttributes.ITEMFILE_DIR, SystemAttributes.ITEM_FORMAT, SystemAttributes.STATUS, SystemAttributes.REVISION};
	    final String[] itemAttrNames = new String[] {"File Name", "Directory", "Format", "Status", "Revision"};


		//populate the item attributes to the item revision object.
		ir.queryAttribute(itemAttrNums);

		System.out.println("============================");
		System.out.println(relationshipType + " " + index);
		System.out.println("Item name: " + ir.getName());
		for (int k=0; k<itemAttrNums.length; k++) {
    		System.out.println(itemAttrNames[k] + ": " + ir.getAttribute(itemAttrNums[k]));
		}
		System.out.println("============================");
	}

	/**
	 * Find a item revision which has derived and/or built relationships with other item revisions
	 */
	static ItemRevision findItem(final DimensionsConnection connection) {
		final String projectSpec = "SIC:BUILD_ZOS_DEMO";
		final String itemPath = "/OBJECTD/PROG100.OBJECTD";

		//1. get dimensions object factory
		DimensionsObjectFactory factory = connection.getObjectFactory();

		//2. get project which contains the item revision
		Filter filter = new Filter();
	    filter.criteria().add(
	    		new Filter.Criterion(SystemAttributes.OBJECT_SPEC,projectSpec,Filter.Criterion.EQUALS));
	    List projects = factory.getProjects(filter);
	 	Project project1 = (Project)projects.get(0);
	 	factory.setCurrentProject(project1.getName(),false,".",null,null,true);

	 	//3. get item revision by path
	 	ItemRevision item = project1.findItemRevisionByPath(itemPath);
		return item;
	}

	static void usage() {
			System.err.println("java " + GetDerivedAndBuiltItems.class.getName() + " \\");
			System.err.println("  {userID} {password} {dbName} {dbConn} {server}");
			System.exit(1);
	}
}