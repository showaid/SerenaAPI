/* ===========================================================================
 *  Copyright 2007-2008 Serena Software. All rights reserved.
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
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.serena.dmclient.api.DimensionsConnection;
import com.serena.dmclient.api.DimensionsNetworkException;
import com.serena.dmclient.api.DimensionsRuntimeException;

/**
 * A custom Ant Task that allows the author of a build file to perform
 * certain Dimensions CM functions.
 * <p>
 * The version of Dimensions CM supported is 10.1 only and the functions
 * supported are:
 * <table>
 * <tr><th>Task</th><th>Attributes</th><th>Equivalent Command</th></tr>
 * <tr><td>dimensions</td><td>userid, password, database, server, unscrambler</td><td>n/a</td></tr>
 * <tr><td>run</td><td>cmd</td><td>n/a</td></tr>
 * <tr><td>getcopy</td><td>destpath, expand, recursive, srcproject, srcitem, srcpath</td><td>FI</td></tr>
 * <tr><td>export</td><td>allrevisions, destproject, recursive, srcproject, srcpath</td><td>AIWS</td></tr>
 * </table>
 */
public final class DimensionsTask extends Task {
	private String userID;
	private String password;
	private String database;
    private String server;
    private String unscrambler;
    private DimensionsConnection connection;
	private List nestedElements;

	private static final String INSUFFICIENT_LOGIN_DETAILS = "Please ensure you provide the following log in details: "; //$NON-NLS-1$

    public DimensionsTask() {
        nestedElements = new ArrayList();
    }

    /**
     * Get the user ID.
     * @return the user ID.
     */
    public final String getUserID() {
        return this.userID;
    }

    /**
     * Sets the user ID to use when running Dimensions commands
     * @param userID the user ID to use when running Dimensions commands
     */
    public final void setUserID(final String userID) {
        this.userID = userID;
    }

    /**
     * Get the password.
     * @return the password.
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * Sets the password to use when running Dimensions commands.
     * @param password a valid password paired with the userid attribute
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Get the base database name.
     * @return the database as dbname@dbconn.
     */
    public final String getDatabase() {
        return this.database;
    }

    /**
     * Sets the base database connection string for the remote Dimensions server.
     * <p>
     * This must be of the form dbname@dbconn.
     * @param database the base database connection string for the remote Dimensions server.
     */
    public final void setDatabase(final String database) {
        this.database = database;
    }

    /**
     * Get the server hostname.
     * @return the server hostname.
     */
    public final String getServer() {
        return this.server;
    }

    /**
     * Sets the remote Dimensions hostname against which to perform tasks
     * @param server the remote Dimensions hostname against which to perform tasks
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
     * Creates a new Export nested item.
     * @return the new Export nested item.
     */
    public final Export createExport() {
        Export export = new Export(this);
        this.nestedElements.add(export);
        return export;
    }

    /**
     * Creates a new GetCopy nested item.
     * @return the new GetCopy nested item.
     */
    public final GetCopy createGetCopy() {
        GetCopy getCopy = new GetCopy(this);
        this.nestedElements.add(getCopy);
        return getCopy;
    }

    /**
     * Creates a new Run nested item.
     * @return the new Run nested item. 
     */
    public final Run createRun() {
        Run run = new Run(this);
        this.nestedElements.add(run);
        return run;
    }
    
    /**
     * Executes this task
     * @throws BuildException if there was a problem with the task execution
     */
    public final void execute() throws BuildException {
        validate();
        synchronized (this) {
            try {
                connection = Util.login(getUserID(), getPassword(),
                        getDatabase(), getServer(), getUnscrambler());
                doWork();
            } catch (ParseException pe) {
                throw new BuildException(pe);
            } catch (DimensionsNetworkException dne) {
                throw new BuildException(dne);
            } catch (DimensionsRuntimeException dre) {
                throw new BuildException(dre);
            } finally {
                Util.logout(connection);
                // try to remove refs to the connection:
                connection = null;
                nestedElements.clear();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * Returns connection.
     */
    DimensionsConnection getConnection() {
    	return this.connection;
    }

	/*
	 * Returns a string containing a comma separated list of
	 * those mandatory attributes for the <dimensions> task that are missing.
	 */
	private void validate() throws BuildException {
        boolean valid = true;
        StringBuffer msg = new StringBuffer(DimensionsTask.INSUFFICIENT_LOGIN_DETAILS);
		if (getUserID() == null) {
			if (!valid) {
                msg.append(", "); //$NON-NLS-1$
            }
            msg.append("userid"); //$NON-NLS-1$
            valid = false;
		}
		if (getPassword() == null) {
            if (!valid) {
                msg.append(", "); //$NON-NLS-1$
            }
            msg.append("password"); //$NON-NLS-1$
            valid = false;
		}
		if (getDatabase() == null) {
            if (!valid) {
                msg.append(", "); //$NON-NLS-1$
            }
            msg.append("database"); //$NON-NLS-1$
            valid = false;
		}
        if (getServer() == null) {
            if (!valid) {
                msg.append(", "); //$NON-NLS-1$
            }
            msg.append("server"); //$NON-NLS-1$
            valid = false;
        }
		if (!valid) {
            throw new BuildException(msg.toString());
        }
	}

	/*
	 * This method processes and executes the list of sub tasks that have been specified
	 * within the main <dimensions> task.
	 */
	private void doWork() throws BuildException {
		for (int i = 0; i < this.nestedElements.size(); i++) {
			Object nestedObject = this.nestedElements.get(i);
			if (nestedObject instanceof NestedElement) {
				NestedElement nestedElement = (NestedElement) nestedObject;
				nestedElement.execute();
			}
        }
    }
    
    /*
    public static void main(String args[]) {
        DimensionsTask task = new DimensionsTask();
        task.setUserID("test.userid");
        task.setPassword("test.password");
        task.setDatabase("test.database");
        task.setServer("test.server");
        GetCopy getCopy = task.createGetCopy();
        getCopy.setSrcProject("test.srcproject");
        getCopy.setSrcPath("test.srcpath/");
        getCopy.setRecursive(true);
        getCopy.setDestPath("test.destpath");
        task.execute();
    }
    */
}
