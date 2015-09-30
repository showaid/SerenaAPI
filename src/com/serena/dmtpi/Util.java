/* ===========================================================================
 *  Copyright 2006-2008 Serena Software. All rights reserved.
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

package com.serena.dmtpi;

import java.text.ParseException;
import java.util.*;

import com.serena.dmclient.api.*;
import com.serena.dmclient.api.DimensionsDatabaseAdmin.CommandFailedException;
import com.serena.dmfile.OraDates;

/**
 * This is a utility class with static methods used by the custom
 * Ant task and CruiseControl sourcecontrol for Dimensions CM & Express.
 */
final class Util {
	private static final String MISSING_SOURCE_PATH = "The nested element needs a valid 'srcpath' attribute"; //$NON-NLS-1$
	private static final String BAD_BASE_DATABASE_SPEC = "The <dimensions> task needs a valid 'database' attribute, in the format 'dbname@dbconn'"; //$NON-NLS-1$
	private static final String NO_COMMAND_LINE = "The <run> nested element need a valid 'cmd' attribute"; //$NON-NLS-1$
	private static final String SRCITEM_SRCPATH_CONFLICT = "The <getcopy> nested element needs exactly one of the 'srcpath' or 'srcitem' attributes"; //$NON-NLS-1$

	// should be initialized by DimensionsSourceControl
	static TimeZone timeZone = TimeZone.getDefault();

	private Util() {
		/* Prevent instantiation. */
	}

	static void export(DimensionsConnection connection, String destProject,
			String srcProject, String srcPath, boolean allRevisions,
			boolean isRecursive) {
		boolean changedProject = false;
		String savedProject = getCurrentProject(connection).getName();
		if (savedProject == null || !savedProject.equals(srcProject)) {
			setCurrentProject(connection, srcProject);
			changedProject = true;
		}
		trace("exporting item " + srcProject + ", " + srcPath + " to " + destProject); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		Project srcProjectObj = connection.getObjectFactory().getProject(
				srcProject);
		List itemObjects = findItemsForExport(connection, srcProjectObj,
				srcPath, isRecursive, allRevisions);
		Project destProjectObj = connection.getObjectFactory().getProject(
				destProject);
		destProjectObj.importItems(itemObjects);
		if (changedProject && savedProject != null) {
			setCurrentProject(connection, savedProject);
		}
	}

	/**
	 * Sets the current project for the current user, which is deduced from the
	 * current thread.
	 * 
	 * @param connection
	 *            the connection for which to set the current project.
	 * @param projectName
	 *            the project to switch to, in the form PRODUCT NAME:PROJECT
	 *            NAME.
	 * @throws DimensionsRuntimeException
	 */
	private static void setCurrentProject(DimensionsConnection connection,
			String projectName) {
		trace("setting current project to " + projectName + " ..."); //$NON-NLS-1$ //$NON-NLS-2$
		connection.getObjectFactory().setCurrentProject(projectName, false, "",
				"", null, true);
	}

	private static Project getCurrentProject(DimensionsConnection connection) {
		return connection.getObjectFactory().getCurrentUser()
				.getCurrentProject();
	}

	/**
	 * Gets the specified item, which can be either single item or a directory.
	 * 
	 * @param connection
	 *            the connection for which to fetch items
	 * @param srcProject
	 *            the project from which to get the item
	 * @param srcPath
	 *            a fully qualified pathname that is valid for the specified
	 *            project
	 * @param srcItem
	 *            the full path, including filename, in the project of the
	 *            required item
	 * @param destPath
	 *            the required final path of the item on the local file system
	 * @param expand
	 *            whether to expand substitution variables within source files
	 *            or not
	 * @param isRecursive
	 *            whether to descend recursively into subfolders for this
	 *            operation
	 */
	static void getCopy(DimensionsConnection connection, String srcProject,
			String srcPath, String srcItem, String destPath, boolean expand,
			boolean isRecursive) {
		/*-
		 * 1. if both srcPath and srcItem are specified OR neither are
		 *        specified then fail.
		 * 2. if current project is not the set current project to the
		 *        one specified (and remember the previous one).
		 * 3. query item objects
		 * 4. for each object, perform a fetch
		 * 5. if switchedProject then reset to original project
		 */
		String printableItemName = ""; //$NON-NLS-1$
		if ((srcPath != null && srcItem != null)
				|| (srcPath == null && srcItem == null)) {
			throw new IllegalArgumentException(SRCITEM_SRCPATH_CONFLICT);
		}
		if (srcItem == null) {
			printableItemName = srcPath;
		} else {
			printableItemName = srcItem;
		}
		boolean switchedProject = false;
		Project currentProject = connection.getObjectFactory().getCurrentUser()
				.getCurrentProject();
		if (srcProject != null & !currentProject.getName().equals(srcProject)) {
			connection.getObjectFactory().setCurrentProject(srcProject, false,
					"", "", null, true);
			switchedProject = true;
		}
		trace("getting item " + getCurrentProject(connection).getName() + ":" + printableItemName + " to " + destPath); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		List rels = new ArrayList();
		if (srcItem == null || srcItem.equals("")) { //$NON-NLS-1$
			Filter filter = new Filter();
			filter.criteria().add(
					new Filter.Criterion(SystemAttributes.ITEMFILE_DIR,
							(isRecursive ? srcPath + '%' : srcPath), 0));
			/*-
			filter.criteria().add(
			        new Filter.Criterion(SystemAttributes.IS_EXTRACTED,
			                "Y", Filter.Criterion.NOT)); //$NON-NLS-1$
			*/
			filter.criteria()
					.add(
							new Filter.Criterion(
									SystemAttributes.IS_LATEST_REV, "Y", 0)); //$NON-NLS-1$
			rels.addAll(connection.getObjectFactory().getCurrentUser()
					.getCurrentProject().getChildItems(filter));
			for (int i = 0; i < rels.size(); ++i) {
				DimensionsRelatedObject rel = (DimensionsRelatedObject) rels
						.get(i);
				ItemRevision item = (ItemRevision) rel.getObject();
				item.getCopyToFolder(destPath, expand, true, false);
			}
		} else {
			ItemRevision item = connection.getObjectFactory().getCurrentUser()
					.getCurrentProject().findItemRevisionByPath(srcItem);
			item.getCopyToFolder(destPath, expand, true, false);
		}
		if (switchedProject) {
			setCurrentProject(connection, currentProject.getName());
		}
	}

	static int[] getItemFileAttributes(boolean isDirectory, String dateType) {
		if (isDirectory) {
			final int[] attrs = { SystemAttributes.OBJECT_SPEC,
					SystemAttributes.PRODUCT_NAME, SystemAttributes.OBJECT_ID,
					SystemAttributes.VARIANT, SystemAttributes.TYPE_NAME,
					SystemAttributes.REVISION, SystemAttributes.FULL_PATH_NAME,
					SystemAttributes.ITEMFILE_FILENAME,
					SystemAttributes.LAST_UPDATED_USER,
					SystemAttributes.REVISION_COMMENT,
					Util.getDateTypeAttribute(dateType) };
			return attrs;
		}
		final int[] attrs = { SystemAttributes.PRODUCT_NAME,
				SystemAttributes.OBJECT_ID, SystemAttributes.VARIANT,
				SystemAttributes.TYPE_NAME, SystemAttributes.REVISION,
				SystemAttributes.ITEMFILE_FILENAME,
				SystemAttributes.LAST_UPDATED_USER,
				Util.getDateTypeAttribute(dateType) };
		return attrs;
	}

	private static String preProcessSrcPath(String srcPath) {
		String path = srcPath.equals("/") ? "" : srcPath; //$NON-NLS-1$ //$NON-NLS-2$
		if (!path.endsWith("/") & !path.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
			path += "/"; //$NON-NLS-1$
		}
		return path;
	}

	// find items given a directory spec
	static List queryItems(DimensionsConnection connection, Project srcProject,
			String srcPath, Filter filter, int[] attrs, boolean isRecursive, boolean isLatest) {
		// check srcPath validity check srcPath trailing slash do query
		if (srcPath == null) {
			throw new IllegalArgumentException(MISSING_SOURCE_PATH);
		}
		String path = preProcessSrcPath(srcPath);
		if (!(isRecursive && path.equals(""))) { //$NON-NLS-1$
			filter.criteria().add(
					new Filter.Criterion(SystemAttributes.ITEMFILE_DIR,
							(isRecursive ? path + '%' : path), 0));
		}
		if (isLatest) {
			filter.criteria().add(
					new Filter.Criterion(SystemAttributes.IS_LATEST_REV,
							Boolean.TRUE, 0));
		}
		/*-
		filter.criteria().add(
		        new Filter.Criterion(SystemAttributes.ITEMFILE_DIR,
		                srcPath, 0));
		*/
		List rels = srcProject.getChildItems(filter);
		List items = new ArrayList(rels.size());
		for (int i = 0; i < rels.size(); ++i) {
			DimensionsRelatedObject rel = (DimensionsRelatedObject) rels.get(i);
			items.add(rel.getObject());
		}
		BulkOperator bo = connection.getObjectFactory().getBulkOperator(items);
		bo.queryAttribute(attrs);
		return items;
	}

	private static List findItemsForExport(
			DimensionsConnection connection, Project srcProject,
			String srcPath, boolean isRecursive, boolean allRevisions) {
		/*
		 * create filter [not_extracted, date_before, date_after, recursive,
		 * allRevisions] finditems(srcPath, filter)
		 */
		List itemObjects = new ArrayList();
		Filter filter = new Filter();
		filter.criteria().add(
				new Filter.Criterion(SystemAttributes.IS_EXTRACTED,
						"Y", Filter.Criterion.NOT)); //$NON-NLS-1$
		if (!allRevisions) {
			filter.criteria()
					.add(
							new Filter.Criterion(
									SystemAttributes.IS_LATEST_REV, "", 0)); //$NON-NLS-1$
		}
		int[] attrs = getItemFileAttributes(true, null);
		itemObjects = queryItems(connection, srcProject, srcPath, filter,
				attrs, isRecursive, false);

		return itemObjects;
	}

	/**
	 * Runs a Dimensions command.
	 * 
	 * @param connection
	 *            the connection for which to run the command
	 * @param cmd
	 *            the command line to run
	 * @throws Exception
	 *             if the command failed
	 * @throws IllegalArgumentException
	 *             if the command string was null or an emptry dtring
	 * 
	 */
	static void run(DimensionsConnection connection, String cmd)
			throws CommandFailedException {
		trace("running command: " + cmd); //$NON-NLS-1$
		trace("connection is: " + connection);
		if (cmd == null || cmd.equals("")) { //$NON-NLS-1$
			throw new IllegalArgumentException(NO_COMMAND_LINE);
		}
		connection.getObjectFactory().getBaseDatabaseAdmin()
				.runCommand(cmd);
	}

	/**
	 * Creates a Dimensions session using the supplied login credentials and
	 * server details
	 * 
	 * @param userID
	 *            Dimensions user ID
	 * @param password
	 *            Dimensions password
     * @param database
     *            base database name
     * @param server
     *            hostname of the remote dimensions server
	 * @param unscrambler
	 *            class name of an <code>Unscrambler</code> implementation class
	 *            that will be instantiated to pre-process the login credentials
	 * @return a DimensionsConnection object
	 * @throws DimensionsNetworkException
	 */
	static DimensionsConnection login(String userID, String password,
			String database, String server, String unscrambler)
			throws ParseException {
        // check if we need to pre-process the login details
        if (unscrambler != null && unscrambler.length() > 0) {
            try {
                Class uc = Class.forName(unscrambler);
                Object uo = uc.newInstance();
                if (uo instanceof Unscrambler) {
                    // use the Unscrambler implementation we just created
                    // to pre-process the userID and password:
                    Unscrambler us = (Unscrambler) uo;
                    us.setUserID(userID);
                    us.setPassword(password.toCharArray());
                    us.setDatabase(database);
                    us.setServer(server);
                    us.unscramble();
                    userID = us.getUserID();
                    password = new String(us.getPassword());
                    database = us.getDatabase();
                    server = us.getServer();
                }
            } catch (ClassNotFoundException cnfe) {
                // ignore the unscrambler class
            } catch (InstantiationException ie) {
                // ignore the unscrambler class
            } catch (IllegalAccessException iae) {
                // ignore the unscrambler class
            }
        }
		String[] dbCompts = Util.parseDatabaseString(database);
		String dbName = dbCompts[0];
		String dbConn = dbCompts[1];
		DimensionsConnectionDetails details = new DimensionsConnectionDetails();
		details.setUsername(userID);
		details.setPassword(password);
		details.setDbName(dbName);
		details.setDbConn(dbConn);
		details.setServer(server);
		return DimensionsConnectionManager.getConnection(details);
	}

	static void logout(DimensionsConnection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (DimensionsNetworkException dne) {
				/* do nothing */
			} catch (DimensionsRuntimeException dne) {
				/* do nothing */
			}
		}
	}

	/**
	 * Parses a base database specification
	 * <p>
	 * Valid patterns are dbName/dbPassword@dbConn or dbName@dbConn. Anything
	 * else will cause a java.text.ParseException to be thrown. Returns an array
	 * of either [dbName, dbConn, dbPassword] or [dbName, dbConn].
	 * 
	 * @param database
	 *            a base database specification
	 * @return an array of base database specification components
	 * @throws ParseException
	 *             if the supplied String does not conform to the above rules
	 */
	private static String[] parseDatabaseString(String database)
			throws ParseException {
		String[] dbCompts;
		int endName = database.indexOf('/');
		int startConn = database.indexOf('@');
		if (startConn < 1 || startConn == database.length() - 1) {
			throw new ParseException(BAD_BASE_DATABASE_SPEC, startConn);
		}
		String dbName = null;
		String dbConn = null;
		String dbPassword = null;
		if (endName < 0 || startConn <= endName) {
			// no '/' or '@' is before '/':
			dbName = database.substring(0, startConn);
			dbConn = database.substring(startConn + 1);
			dbCompts = new String[2];
			dbCompts[0] = dbName;
			dbCompts[1] = dbConn;
		} else if (endName == 0 || startConn == endName + 1) {
			// '/' at start or '/' immediately followed by '@':
			throw new ParseException(BAD_BASE_DATABASE_SPEC, endName);
		} else {
			dbName = database.substring(0, endName);
			dbPassword = database.substring(endName + 1, startConn);
			dbConn = database.substring(startConn + 1);
			dbCompts = new String[3];
			dbCompts[0] = dbName;
			dbCompts[1] = dbConn;
			dbCompts[2] = dbPassword;
		}
		return dbCompts;
	}

	/**
	 * TODO - convert to use java.util.logging package
	 * 
	 * @param message
	 *            the message to be printed out
	 */
	static void trace(String message) {
		// use java.util.logging here
		System.out.println(message);
	}

	/**
	 * Convert the human-readable <code>dateType</code> into a DMClient
	 * attribute name.
	 * <p>
	 * Defaults to
	 * {@link com.serena.dmclient.api.SystemAttributes#CREATION_DATE} if it is
	 * not recognized.
	 * 
	 * @param dateType
	 *            created, updated, revised or actioned.
	 * @return the corresponding field value from
	 *         {@link com.serena.dmclient.api.SystemAttributes}
	 */
	static int getDateTypeAttribute(String dateType) {
		int ret = SystemAttributes.CREATION_DATE;
		if (dateType != null) {
			if (dateType.equalsIgnoreCase("updated")) { //$NON-NLS-1$
				ret = SystemAttributes.LAST_UPDATED_DATE;
			} else if (dateType.equalsIgnoreCase("actioned")) { //$NON-NLS-1$
				ret = SystemAttributes.LAST_ACTIONED_DATE;
			} else if (dateType.equalsIgnoreCase("revised")) { //$NON-NLS-1$
				ret = SystemAttributes.UTC_MODIFIED_DATE;
			} else if (dateType.equalsIgnoreCase("created")) { //$NON-NLS-1$
				ret = SystemAttributes.CREATION_DATE;
			}
		}
		return ret;
	}
	
	// database times are in Oracle format, in a specified timezone
	static String formatDatabaseDate(Date date, TimeZone timeZone) {
		return (timeZone == null) ? OraDates.format(date) : OraDates.format(date, timeZone);
	}

	// database times are in Oracle format, in a specified timezone
	static Date parseDatabaseDate(String date, TimeZone timeZone) {
		return (timeZone == null) ? OraDates.parse(date) : OraDates.parse(date, timeZone);
	}
}
