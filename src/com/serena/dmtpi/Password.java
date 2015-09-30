package com.serena.dmtpi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Password utility class.
 * <p>
 * Passwords that begin with OBF: will be deobfuscated if you set the class
 * name of this class as the unscrambler attribute.
 * <p>
 * Passwords can be obfuscated by running this class as a main class.
 * Obfuscated passwords are required if a system needs to recover the
 * full password so that it may be passed to another system (like a
 * network server).
 * <p>
 * Obfuscation helps to prevent casual observation of passwords.
 */
public final class Password implements Unscrambler {
	private static final String CLASSNAME = Password.class.getName();
    private String userID;
    private char[] password;
    private String database;
    private String server;
    
    public Password() {
    }
    
    public void setUserID(final String userID) {
    	this.userID = userID;
    }

    public void setPassword(final char[] password) {
        if (this.password != null) {
            // wipe out the old password
            Arrays.fill(this.password, ' ');
        }
    	this.password = (char[]) password.clone();
    }

    public void setDatabase(final String database) {
        this.database = database;
    }

    public void setServer(final String server) {
        this.server = server;
    }

    public void unscramble() {
        String pw = new String(this.password);
        Arrays.fill(this.password, ' ');
        pw = internalDeobfuscate(pw);
        // wipe out the old password
        this.password = pw.toCharArray();
        pw = null;
    }
    
    public String getUserID() {
    	return this.userID;
    }
    
    public char[] getPassword() {
        return (char[]) this.password.clone();
    }

    public String getDatabase() {
        return this.database;
    }
    
    public String getServer() {
        return this.server;
    }
    
    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Use one of the following to obfuscate passwords.");
            System.err.println();
            System.err.println("    java -cp dmtpi.jar " + CLASSNAME + " --console");
            System.err.println("    java -cp dmtpi.jar " + CLASSNAME + " --gui");
            System.err.println("    java -cp dmtpi.jar " + CLASSNAME + " <password>");
            System.err.println();
            System.err.println("Enter --console or --gui to be prompted for the password.");
            System.exit(1);
        }
        
        // make it less obvious to users who run 'strings' on the
        // class files that the '--deobfuscate' option exists.
        final StringBuffer d1 = new StringBuffer();
        d1.append('-');
        d1.append('-');
        d1.append((char) ('c'+1));
        d1.append((char) ('f'-1));
        d1.append((char) ('m'+2));
        d1.append((char) ('a'+1));
        d1.append((char) ('g'-1));
        d1.append((char) ('w'-2));
        d1.append((char) ('q'+2));
        d1.append((char) ('a'+2));
        d1.append((char) ('d'-3));
        d1.append((char) ('q'+3));
        d1.append((char) ('i'-4));
        final String d2 = d1.toString(); 

        String p = args[0];
        if (p != null) {
        	if (p.equals("--gui")) {
            	AWTCredentialsDialog gui = new AWTCredentialsDialog(null, 200, 100, false);
            	gui.setUserID("<Only a password is needed>");
            	gui.showModal();
            	p = new String(gui.getPassword());
                if (p.startsWith("OBF:") || p.startsWith("OBF\\:")) {
                    System.err.println("The password you entered may already been obfuscated.");
                }
                p = internalObfuscate(p);
        	} else if (p.equals("--console")) {
            	ConsoleReader console = new ConsoleReader(System.in, System.out);
            	System.out.print("Password: ");
            	System.out.flush();
            	p = new String(console.readPassword()); 
                if (p.startsWith("OBF:") || p.startsWith("OBF\\:")) {
                    System.err.println("The password you entered may already been obfuscated.");
                }
                p = internalObfuscate(p);
            } else if (p.equals(d2)) {
                // the availability of the '--deobfuscate' option is made
                // less obvious by the bit of silly string manipulation above.
                ConsoleReader console = new ConsoleReader(System.in, System.out);
                System.out.print("Enter the obfuscated password: ");
                System.out.flush();
                p = console.readLine(); 
                if (!p.startsWith("OBF:") && !p.startsWith("OBF\\:")) {
                    System.err.println("This does not look like an obfuscated password.");
                }
                p = internalDeobfuscate(p);
        	} else {
                if (p.startsWith("OBF:") || p.startsWith("OBF\\:")) {
                    System.err.println("The password you entered may already been obfuscated.");
                }
        	    p = internalObfuscate(p);
        	}
        }
        System.out.println();
        System.out.println(p);
        System.out.println();
        System.out.flush();
    }
    
    private static String internalObfuscate(final String s) {
	    StringBuffer buf = new StringBuffer();
	    byte[] b;
	    try {
	    	b = s.getBytes("UTF-8");
	    } catch (UnsupportedEncodingException uee) {
	    	b = s.getBytes();
	    }
        buf.append("OBF:");
        for (int i = 0; i < b.length; ++i) {
            byte b1 = b[i];
            byte b2 = b[s.length()-1-i];
            int i1 = 127 + b1 + b2;
            int i2 = 127 + b1 - b2;
            int i0 = i1 * 256 + i2;
            String r = Integer.toString(i0, 36);
            switch(r.length()) {
            case 1:
            	buf.append('0');
            	// fall through
            case 2:
            	buf.append('0');
            	// fall through
            case 3:
            	buf.append('0');
            	// fall through
            default:
            	buf.append(r);
            	break;
            }
        }
        return buf.toString();
    }

    private static String internalDeobfuscate(final String s) {
    	String t;
    	if (s.startsWith("OBF:")) {
	        t = s.substring(4);
    	} else if (s.startsWith("OBF\\:")) {
    		// allow for literal 'OBF\:' which is how the prefix
    		// looks when stored to a properties file.
    		t = s.substring(5);
    	} else {
    		// is not obfuscated
    		return s;
    	}
        byte[] b = new byte[t.length() / 2];
        int l = 0;
	    for (int i = 0; i < t.length(); i+=4) {
            String x = t.substring(i, i+4);
            int i0 = Integer.parseInt(x, 36);
            int i1 = i0 / 256;
            int i2 = i0 % 256;
            b[l++] = (byte) ((i1 + i2 - 254) / 2);
        }
	    try {
	    	t = new String(b, 0, l, "UTF-8");
	    } catch (UnsupportedEncodingException uee) {
	    	t = new String(b, 0, l);
	    }
	    return t;
    }
}