package com.serena.dmtpi;

/**
 * It is possible to configure <code>DimensionsSourceControl</code> and
 * <code>DimensionsTask</code> with the name of a class which implements
 * this interface using the <code>unscrambler="package.class.name"</code>
 * attribute in XML.
 * <p>
 * If present and valid, then an instance of the class will be created using
 * the required public no-argument constructor.
 * <p>
 * The <code>setUserID</code> and <code>setPassword</code> methods on the
 * resultant <code>Unscrambler</code> object instance will then be passed
 * the <code>userID</code> and <code>password</code> attributes that were
 * set on <code>DimensionsSourceControl</code> or <code>DimensionsTask</code>.
 * <p>
 * The <code>Unscrambler</code> object instance should process/decode these
 * values when the <code>unscramble()</code> method is called.
 * <p>
 * The <code>DimensionsSourceControl</code> or <code>DimensionsTask</code>
 * calls the <code>getUserID</code> and <code>getPassword</code> methods
 * are then called and the resultant credentials are used to log in to the
 * Dimensions server.
 * <p>
 * A typical implementation might leave the <code>userID</code> as it is,
 * but unscramble the <code>password</code> using encryption based on a
 * well-known or derived key. Such an implementation would also need to
 * provide some means for the admin to create the scrambled password text
 * in the first place.
 * <p>
 * More exotic implementations could possibly call out to Windows native
 * protected storage, or other native key-ring implementations.
 */
public interface Unscrambler {
	/**
	 * @param userID
	 * 	    What was set as the userID on the SourceControl or Task.
	 */
	void setUserID(String userID);

	/**
	 * @param password
	 * 	    What was set as the password on the SourceControl or Task.
	 *      Implementations should clone this array so that the caller
	 *      cannot influence the internal state without calling this
	 *      method again.
	 */
	void setPassword(char[] password);
	
    /**
     * @param database
     *      What was set as the database on the SourceControl or Task.
     */
    void setDatabase(String database);
    
    /**
     * @param server
     *      What was set as the server on the SourceControl or Task.
     */
    void setServer(String server);
    
	/**
	 * Do the deobfuscation. This method will be called just once.
	 */
	void unscramble();

	/**
	 * @return The deobfuscated userID.
	 */
	String getUserID();

	/**
	 * Note that implementations should clone their internal char array
	 * each time this method is called, rather than returning their
	 * internal representation of the password directly. This means
	 * one caller cannot influence the value that other callers see
	 * by altering the contents of the char array returned.
	 * 
	 * @return A copy of the deobfuscated password.
	 */
	char[] getPassword();

    /**
     * @return The deobfuscated database string.
     */
    String getDatabase();
    
    /**
     * @return The deobfuscated server name.
     */
    String getServer();
    
}
