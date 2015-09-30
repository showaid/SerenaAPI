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

package com.serena.dmtpi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Modification;
import net.sourceforge.cruisecontrol.SourceControl;

import com.clt.serena.resources.PropertyReader;
import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.Filter;
import com.serena.dmclient.api.ItemRevision;
import com.serena.dmclient.api.Project;
import com.serena.dmclient.api.SystemAttributes;

/**
 * An implementation of the SourceControl interface that fetches modifications
 * from a Dimensions CM database using the dmclient API.
 */
public final class DimensionsSourceControl implements SourceControl {

    private String userID;
    private String password;
    private String database;
    private String server;
    private String unscrambler;
	private String project;
	private String path;
    private String dateType;
    private boolean allRevisions;
    private Hashtable properties;
    private String property; // modification property name
    private String propertyOnDelete; // deletion property name

	private static final String MISSING_ATTRIBUTES = "One or more mandatory attributes for the [dimensions] source control are missing."; //$NON-NLS-1$

    public DimensionsSourceControl() {
        dateType = "created"; //$NON-NLS-1$
        properties = new Hashtable();
    }

    //////////////////////////////////////////////////////////////////////
    // JavaBean methods for CC attributes
    
    /**
     * Gets the user ID for the connection.
     * @return the user ID of the user as whom to connect
     */
    public final String getUserID() {
        return this.userID;
    }

    /**
     * Sets the user ID for the connection.
     * @param userID the user ID of the user as whom to connect
     */
    public final void setUserID(final String userID) {
        this.userID = userID;
    }

    /**
     * Gets the password for the connection.
     * @return the password of the user as whom to connect
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * Sets the password for the connection.
     * @param password the password of the user as whom to connect
     */
    public final void setPassword(final String password) {
        this.password = password;
    }
    
    /**
     * Gets the base database for the connection (as "NAME@CONNECTION").
     * @return the name of the base database to connect to
     */
    public final String getDatabase() {
        return this.database;
    }

    /**
     * Sets the base database for the connection (as "NAME@CONNECTION").
     * @param database the name of the base database to connect to
     */
    public final void setDatabase(final String database) {
        this.database = database;
    }

    /**
     * Gets the server for the connection.
     * @return the name of the server to connect to
     */
    public final String getServer() {
        return this.server;
    }

    /**
	 * Sets the server for the connection.
	 * @param server the name of the server to connect to
	 */
	public final void setServer(final String server) {
		this.server = server;
	}

    /**
     * Gets the class name of an <code>Unscrambler</code> implementation.
     * @return the name of the class
     */
    public final String getUnscrambler() {
    	return this.unscrambler;
    }

    /**
     * Sets the class name of an <code>Unscrambler</code> implementation.
     * @param unscrambler the name of the class
     */
    public final void setUnscrambler(final String unscrambler) {
    	this.unscrambler = unscrambler;
    }

    /**
     * Gets the project against which to check for modifications.
     * @return the name of a project in the base database
     */
    public final String getProject() {
        return this.project;
    }

	/**
	 * Sets the project against which to check for modifications.
	 * @param project the name of a project in the base database
	 */
	public final void setProject(final String project) {
		this.project = project;
	}

	/**
	 * Gets the project path from which to check for modifications.
	 * @return the name of the project path
	 */
	public final String getPath() {
		return this.path;
	}

    /**
     * Sets the project path from which to check for modifications.
     * @param path the name of the project path
     */
    public final void setPath(final String path) {
        this.path = path;
    }

    /**
     * Gets the type of date against which to check for modifications.
     * @return one of created, updated, revised or actioned
     */
    public final String getDateType() {
        return this.dateType;
    }

	/**
	 * Sets the type of date against which to check for modifications.
	 * @param dateType one of created, updated, revised or actioned
	 */
	public final void setDateType(final String dateType) {
		this.dateType = dateType;
	}

    public final boolean getAllRevisions() {
        return this.allRevisions;
    }

	public final void setAllRevisions(final boolean allRevisions) {
		this.allRevisions = allRevisions;
	}

    //////////////////////////////////////////////////////////////////////
    // SourceControl methods
    
	/**
	 * @see net.sourceforge.cruisecontrol.SourceControl#getModifications(Date, Date)
	 */
	public final List getModifications(final Date lastBuild, final Date now) {
		List modifications = null;
        DimensionsConnection connection = null;
		try {
            connection = Util.login(getUserID(), getPassword(),
                    getDatabase(), getServer(), getUnscrambler());
            int[] attrs = Util.getItemFileAttributes(true, dateType);
            String dateAfter = Util.formatDatabaseDate(lastBuild, Util.timeZone);
            String dateBefore = Util.formatDatabaseDate(now, Util.timeZone);
            Util.trace("looking for dates between " + lastBuild + " and " + now + " against " + getServer() + "...");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
            Filter filter = new Filter();
            filter.criteria().add(new Filter.Criterion(Util.getDateTypeAttribute(dateType), dateAfter, Filter.Criterion.GREATER_EQUAL));
            filter.criteria().add(new Filter.Criterion(Util.getDateTypeAttribute(dateType), dateBefore, Filter.Criterion.LESS_EQUAL));
            filter.criteria().add(new Filter.Criterion(SystemAttributes.IS_EXTRACTED, "Y", Filter.Criterion.NOT)); //$NON-NLS-1$
            Project projectObj = connection.getObjectFactory().getProject(getProject());
            List items = Util.queryItems(connection, projectObj, getPath(), filter, attrs, true, !allRevisions);
            Util.trace("found " + items.size() + (items.size() == 1 ? " item" : " items"));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
            modifications = new ArrayList(items.size());
            for (int i = 0; i < items.size(); i ++) {
                ItemRevision file = (ItemRevision) items.get(i);
                String fileName = (String) file.getAttribute(SystemAttributes.ITEMFILE_FILENAME);
                String comment = (String) file.getAttribute(SystemAttributes.REVISION_COMMENT);
                String folderName = (String) file.getAttribute(SystemAttributes.FULL_PATH_NAME);
                String userName = (String) file.getAttribute(SystemAttributes.LAST_UPDATED_USER);
                String modTime = (String) file.getAttribute(Util.getDateTypeAttribute(dateType));
                //strip filename from pathname
                if (folderName.endsWith(fileName)) {
                    folderName = folderName.substring(0, folderName.length() - fileName.length());
                }
                //add revision to filename
                if (fileName == null || fileName.equals("")) { //$NON-NLS-1$
                    fileName = ""; //$NON-NLS-1$
                } else {
                    fileName += ";"; //$NON-NLS-1$
                }
                fileName += (String) file.getAttribute(SystemAttributes.REVISION);
                
                Modification mod = new Modification();
                mod.comment = comment;
                mod.userName = userName;
                mod.type = dateType;
                //mod.action = dateType;
                mod.modifiedTime = Util.parseDatabaseDate(modTime, Util.timeZone);
                Modification.ModifiedFile modFile = mod.createModifiedFile(fileName, folderName);
                modFile.action = dateType;
                modifications.add(mod);
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    Util.logout(connection);
            connection = null;
        }
        if (modifications != null && modifications.size() > 0) {
            if (property != null) {
                properties.put(property, "true");                
            }
            // we never see deletions so always set propertyOnDelete.
            if (propertyOnDelete != null) {
                properties.put(propertyOnDelete, "true");
            }                
        }
		return modifications;
	}

    /**
     * Note this returns a <code>java.util.Map</code> with CC 2.5, and
     * a <code>java.util.Hashtable</code> with CC 2.4.x and earlier.
     * Hence it is not possible for one class to support both versions.
     * @see net.sourceforge.cruisecontrol.SourceControl#getProperties()
     */
    public final Map getProperties() {
        return this.properties;
    }

    /**
     * Optional method <code>setProperty(java.lang.String)</code>
     * described in CruiseControl documentation.
     * @see net.sourceforge.cruisecontrol.SourceControl
     */
    public final void setProperty(final String property) {
        this.property = property;
    }

    /**
     * Optional method <code>setPropertyOnDelete(java.lang.String)</code>
     * described in CruiseControl documentation.
     * @see net.sourceforge.cruisecontrol.SourceControl
     */
    public final void setPropertyOnDelete(final String propertyOnDelete) {
        this.propertyOnDelete = propertyOnDelete;
    }

    /**
     * @see net.sourceforge.cruisecontrol.SourceControl#validate()
     */
    public final void validate() throws CruiseControlException {
        if (getUserID() == null || getPassword() == null
                || getDatabase() == null || getServer() == null
                || getProject() == null || getPath() == null) {
            throw new CruiseControlException(MISSING_ATTRIBUTES);
        }
    }

	public final TimeZone getTimeZone() {
		return Util.timeZone;
	}

	public final void setTimeZone(final String timeZone) {
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		Util.timeZone = tz;
	}
	
    
    public static void main(String args[]) {
    	PropertyReader prop = PropertyReader.getInstance();
        DimensionsSourceControl sc = new DimensionsSourceControl();
        sc.setUserID(prop.getUsername());
        sc.setPassword(prop.getPassword());
        sc.setDatabase(prop.getDbName() + "@" + prop.getDbConn());
        sc.setServer(prop.getServer());
        sc.setProject("TERMINAL:P3.0.0");
        sc.setPath("/");
        sc.setDateType("updated");
        sc.setTimeZone("Asia/Seoul");
        Date from = formatDateString("2015-09-01");
        Date to = new Date();
        List mods = sc.getModifications(from, to);
        System.out.println(mods);
    }
    
    private static Date formatDateString(String dateString) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date parsed = null;
    	try {
    		parsed = format.parse(dateString);
    	} catch (ParseException e) {
    		System.out.println("ERROR: Cannot parse \"" + dateString + "\"");
    	}
    	
    	return parsed;
    }
}
