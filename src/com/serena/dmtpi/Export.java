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

import org.apache.tools.ant.BuildException;

/**
 * Represents the export nested element.
 * Should be just a simple JavaBean.
 */
public final class Export implements NestedElement {
	private final DimensionsTask task;
    private boolean allRevisions;
    private String destProject;
    private boolean recursive;
    private String srcPath;
    private String srcProject;

	/**
	 * Creates a new Export nested element.
	 */
	Export(final DimensionsTask task) {
		this.task = task;
	}

	public final void execute() {
		try {
			Util.export(
					task.getConnection(), this.getDestProject(),
                    this.getSrcProject(), this.getSrcPath(),
					this.isAllRevisions(), this.isRecursive());
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

    /**
     * Gets the allRevisions attribute for this element.
     * @return true if all revisions must be exported, false
     *          if only latest revisions.
     */
    public final boolean isAllRevisions() {
        return this.allRevisions;
    }

    /**
     * Sets the allRevisions attribute for this element.
     * @param allRevisions true if the element is for all revisions,
     *          false if only for latest revisions.
     */
    public final void setAllRevisions(final boolean allRevisions) {
        this.allRevisions = allRevisions;
    }

    /**
     * Gets the destProject attribute for this element.
     * @return the name of the project.
     */
    public final String getDestProject() {
        return this.destProject;
    }

    /**
     * Sets the destProject attribute for this element.
     * @param destProject the name of the project to
     *      which items will be exported.
     */
    public final void setDestProject(final String destProject) {
        this.destProject = destProject;
    }

    /**
     * Gets the recursive attribute for this element.
     * @return true if the element is recursive, false if not.
     */
    public final boolean isRecursive() {
        return this.recursive;
    }

	/**
	 * Sets the recursive attribute for this element.
	 * @param recursive true if the element is recursive, false if not.
	 */
	public final void setRecursive(final boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * Gets the srcPath atribute for this element.
	 * @return the srcPath atribute for this element.
	 */
	public final String getSrcPath() {
		return this.srcPath;
	}

    /**
     * Sets the srcPath atribute for this element.
     * @param srcPath the project path name from
     *      which items will be exported.
     */
    public final void setSrcPath(final String srcPath) {
        this.srcPath = srcPath;
    }

    /**
     * Gets the srcProject atribute for this element.
     * @return the srcProject atribute for this element.
     */
    public final String getSrcProject() {
        return this.srcProject;
    }

    /**
     * Sets the srcProject atribute for this element.
     * @param srcProject the project from
     *      which items will be exported.
     */
    public final void setSrcProject(final String srcProject) {
        this.srcProject = srcProject;
    }
}