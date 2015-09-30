/* ===========================================================================
 *  Copyright (c) 2007 Serena Software. All rights reserved.
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

import java.util.Iterator;

import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsConnectionDetails;
import com.serena.dmclient.api.DimensionsConnectionManager;
import com.serena.dmclient.api.DimensionsDatabaseAdmin;
import com.serena.dmclient.objects.Lifecycle;
import com.serena.dmclient.objects.LifecycleImageRevision;
import com.serena.dmclient.objects.LifecycleImageRevisionDetails;

/**
 * This example demonstrates lifecyle image related functionality. It also shows
 * how the JavaScript dmpmcli examples can be translated from JavaScript into
 * Java - see also lifecycleImagesDemo.js.
 * The liberal use of <code>catch (Exception)</code> like this is <em>not</em>
 * recommended in ordinary use of the Java API.
 */
public class LifecycleImagesDemo {

	static DimensionsDatabaseAdmin defaultDatabase;

	public static void main(final String[] args) {
		// preliminary connect code:
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
			defaultDatabase = connection.getObjectFactory().getBaseDatabaseAdmin();
			lifecycleImagesDemo();
		} finally {
			defaultDatabase = null;
			// close the connection.
			connection.close();
		}
	}
	
	/**
	 * Find a lifecycle in the base database
	 */
	static Lifecycle getLifecycle(final String name) {
		// obtain a reference to the DimensionsDatabaseAdmin instance
		DimensionsDatabaseAdmin db = defaultDatabase;
		String lcName = name;

		// find a lifecycle name if necessary
		if (lcName == null) {
			// loop over lifecycle names
			for (Iterator it = db.getLifecycles().iterator(); it.hasNext();) {
				// get next lifecycle name
				lcName = (String) it.next();
				break;
			}
		}

		Lifecycle lifecycleObj = db.getLifecycles().get(lcName);
		if (lifecycleObj != null) {
			// print the results
			System.out.println("\tFound lifecycle: " + lifecycleObj.getName()
					+ "; " + "description: " + lifecycleObj.getDescription());
		}
		return lifecycleObj;
	}

	/**
	 * List all image revisions for a lifecycle
	 */
	static void listImageRevisions(final Lifecycle lc) {
		if (lc == null) {
			System.out.println("lifecycle was not specified");
			return;
		}
		System.out
				.println("\n\n\n\nListing image revisions registered for lifecycle "
						+ lc.getName());
		System.out.println("Found " + lc.getImageRevisions().size()
				+ " image revisions");

		// loop over template revision identifiers
		for (Iterator it = lc.getImageRevisions().iterator(); it.hasNext();) {
			// get next revision
			String rev = (String) it.next();
			// fetch corresponding LifecycleImageRevision object
			LifecycleImageRevision img = lc.getImageRevisions().get(rev);
			// print the results
			System.out.println("Image revision: (" + img.getRevision()
					+ ") created by: (" + img.getCreatedBy()
					+ ") creation date: (" + img.getCreationDate()
					+ ") lastImportedBy: (" + img.getLastImportedBy()
					+ ") last import date: (" + img.getLastImportDate() + ")");
		}
		System.out.println("Done");
	}

	/**
	 * Create a new image revision
	 */
	static LifecycleImageRevision createImage(final Lifecycle lc, final String rev,
			final String filename) {
		System.out.println("Creating new image revision");
		if (lc == null) {
			System.out.println("lifecycle was not specified");
			return null;
		}

		// set up a LifecycleImageRevisionDetails object
		LifecycleImageRevisionDetails d = new LifecycleImageRevisionDetails();
		d.setRevision(rev);
		d.setFilename(filename);

		// create new image
		LifecycleImageRevision img = lc.getImageRevisions().add(d);

		// print results
		System.out.println("Image revision: (" + img.getRevision()
				+ ") created by: (" + img.getCreatedBy() + ") creation date: ("
				+ img.getCreationDate() + ") lastImportedBy: ("
				+ img.getLastImportedBy() + ") last import date: ("
				+ img.getLastImportDate() + ")");

		return img;
	}

	/**
	 * Delete the specified image revision
	 */
	static void deleteImage(final Lifecycle lc, final LifecycleImageRevision image) {
		if (lc == null) {
			System.out.println("lifecycle was not specified");
			return;
		}
		if (image == null) {
			System.out.println("image revision was not specified");
			return;
		}

		LifecycleImageRevisionDetails details = lc.getImageRevisions().remove(
				image);

		// reload collection
		lc.getImageRevisions().refresh();

		// try to get the same image object again
		LifecycleImageRevision image1 = lc.getImageRevisions().get(
				details.getRevision());
		if (image1 != null) {
			throw new RuntimeException("image revision "
					+ details.getRevision() + " still exists");
		}

		System.out.println("image " + details.getRevision() + " deleted");
	}

	/**
	 * This function show how to manipulate lifecycle images
	 */
	static void testLifecycle(final Lifecycle lc) {
		if (lc == null) {
			System.out.println("lifecycle was not specified");
			return;
		}
		// list registered lifecycle image revisions
		listImageRevisions(lc);

		LifecycleImageRevision dt = lc.getDefaultImageRevision();
		if (dt != null) {
			System.out.println("Default image revision: (" + dt.getRevision()
					+ ") created by: (" + dt.getCreatedBy()
					+ ") creation date: (" + dt.getCreationDate()
					+ ") lastImportedBy: (" + dt.getLastImportedBy()
					+ ") last import date: (" + dt.getLastImportDate() + ")");
		}

		LifecycleImageRevision t = null;
		try {
			t = createImage(lc, "10", "/tmp/image.gif");
			// export image contents
			t.exportFile("/tmp/imageExport.gif");
			// import new image contents
			t.importFile("/tmp/image.jpg");
			// assign new image to the lifecycle
			lc.setDefaultImageRevision(t);
			// deassign image from the lifecycle
			lc.setDefaultImageRevision(null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (t != null) {
				deleteImage(lc, t);
			}
			if (dt != null) {
				lc.setDefaultImageRevision(dt);
			}
		}
	}

	static void lifecycleImagesDemo() {
		try {
			// get a lifecycle
			Lifecycle lifecycle = getLifecycle("CR_LC");
			if (lifecycle != null) {
				testLifecycle(lifecycle);
			}
		} catch (Exception e) {
			// print exception message
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("java " + LifecycleImagesDemo.class.getName()
				+ " \\");
		System.err.println("  {userID} {password} {dbName} {dbConn} {server}");
		System.exit(1);
	}
}