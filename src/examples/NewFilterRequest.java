package examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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

/**
 * Log in and search for a request with a filter.
 */
public final class NewFilterRequest {
	String result = "";
	static InputStream inputStream;
    
	public static void main(final String[] args) throws IOException {
//		if (args.length != 1) {
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
			filterRequestSimple(connection, "TERMINAL%","%","%");
//			listRequestRelatedItems(connection);
//			getSource(prop);
//			runCommand(prop);
		} finally {
			// disconnect.
			connection.close();
		}
	}

//  create a very simple filter.
//  String objectId = objId; //"OPUS_BR_502%";
//  String stage = stage; //"DEV";
//  String status = status; //"CREATED";	
    static void filterRequestSimple(final DimensionsConnection connection, String objectId, String stage, String status) {

        Filter filter = new Filter();
        filter.criteria().add(
        		new Filter.Criterion(SystemAttributes.OBJECT_ID, objectId,
                        Filter.Criterion.EQUALS));
        //filter.criteria().add(Filter.Criterion.START_AND);
        filter.criteria().add(
                new Filter.Criterion(SystemAttributes.STAGE, stage,
                        Filter.Criterion.EQUALS));
        filter.criteria().add(
                new Filter.Criterion(SystemAttributes.STATUS, status,
                        Filter.Criterion.EQUALS));
        //filter.criteria().add(Filter.Criterion.END_AND);

        // search for requests matching the filter.
        List list = connection.getObjectFactory().getBaseDatabase()
                .getAllRequests(filter);
        System.out.println("Found " + list.size()
                + " request(s) with title like \'" + objectId + "\'.");

        // query the title attribute on all the list at once.
//        BulkOperator op = connection.getObjectFactory().getBulkOperator(list);
//        op.queryAttribute(SystemAttributes.TITLE);
        
        BulkOperator bulk = connection.getObjectFactory().getBulkOperator(list);
        int[] attrs = {
        		SystemAttributes.FULL_PATH_NAME,
        		SystemAttributes.ITEMFILE_FILENAME,
        		SystemAttributes.REVISION,
        		SystemAttributes.CREATION_DATE
        };
        
        bulk.queryAttribute(attrs);
        // now display them.
        for (int i = 0; i < list.size(); ++i) {
            Request req = (Request) list.get(i);
            System.out.println((i + 1) + ". " + req.getName() + " - "
                    + req.getAttribute(SystemAttributes.REVISION));
        }
    }

    private static void usage() {
		System.err.println("java " + FilterRequest.class.getName() + " \\");
		System.err.println("  {userID} {password} {dbName} {dbConn} {server}");
		System.exit(1);
	}
	
	private static void runCommand(final Properties prop) {
		 try {
	            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dmcli",
	            		"-user", prop.getProperty("Username"),
	            		"-pass", prop.getProperty("Password"),
	            		"-host", prop.getProperty("Server"),
	            		"-dbname", prop.getProperty("DbName"),
	            		"-dsn", prop.getProperty("DbConn"),
	            		"-file", "C:/14.Dimension/cmdfile.txt");
	            pb.redirectError();
	            Process p = pb.start();
	            InputStreamConsumer isc = new InputStreamConsumer(p.getInputStream());
	            isc.start();
	            int exitCode = p.waitFor();

	            isc.join();
	            System.out.println("Process terminated with " + exitCode);
	        } catch (IOException | InterruptedException exp) {
	            exp.printStackTrace();
	        }
	}
	
	private static void getSource(final Properties prop) {
		 try {
	            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dm", "get",
	            		"--stream", "BULK:BULKTEST", // prop.getProperty("Username"),
	            		"--directory", "C:/14.Dimension/Test", // prop.getProperty("Username"),
	            		"--requestId", "BULK_CR_1", // prop.getProperty("Username"),
	            		"--user", prop.getProperty("Username"),
	            		"--password", prop.getProperty("Password"),
	            		"--server", prop.getProperty("Server"),
	            		"--database", prop.getProperty("DbName")+"@"+prop.getProperty("DbConn"));
	            pb.redirectError();
	            Process p = pb.start();
	            InputStreamConsumer isc = new InputStreamConsumer(p.getInputStream());
	            isc.start();
	            int exitCode = p.waitFor();

	            isc.join();
	            System.out.println("Process terminated with " + exitCode);
	        } catch (IOException | InterruptedException exp) {
	            exp.printStackTrace();
	        }
	}

	static void listRequestRelatedItems(final DimensionsConnection connection) {
		// find the request QLARIUS_CR_1 - note that normally you would have
		// obtained a Request instance through some other means than this.
		Request requestObj = connection.getObjectFactory().findRequest(
				"OPUSCNTR_NYK_CR_9660");

		// all items live in the "global" project, however because filenames
		// of items can differ from project to project, the filename from the
		// global project may not be the same as it is in the project you are
		// interested in.
		Project globalProjectObj = connection.getObjectFactory()
				.getGlobalProject();

		// getChildItems from the global project will return all items that
		// may be in any project. However, the resulting ItemRevision instances
		// are scoped to the global project, not the current project.
		// the flushRelatedObjects calls may or may not be necessary depending
		// how up-to-date your version of the API JAR files is (it is safer
		// to do it).
		requestObj.flushRelatedObjects(ItemRevision.class, true);
		List relObjs = requestObj.getChildItems(null, globalProjectObj);
		requestObj.flushRelatedObjects(ItemRevision.class, true);

		// note that some items may appear more than once (for the
		// Affected revisions and the In Response To revision).
		List revObjs = new ArrayList(relObjs.size());
		for (int i = 0; i < relObjs.size(); ++i) {
			DimensionsRelatedObject relObj = (DimensionsRelatedObject) relObjs
					.get(i);
			ItemRevision revObj = (ItemRevision) relObj.getObject();
			revObjs.add(revObj);
		}

		// Because the ItemRevision objects are in the scope of the global
		// project, if we query filename then we'll get the filename in the
		// global project.
		BulkOperator bulk = connection.getObjectFactory().getBulkOperator(
				revObjs);
		bulk.queryAttribute(SystemAttributes.OBJECT_SPEC);

		// now display them.
		for (int i = 0; i < revObjs.size(); ++i) {
			ItemRevision revObj = (ItemRevision) revObjs.get(i);
			String itemSpec = (String) revObj
					.getAttribute(SystemAttributes.FULL_PATH_NAME);
			System.out.println((i + 1) + ". " + itemSpec);
		}
	}
	
	public static class InputStreamConsumer extends Thread {

        private InputStream is;

        public InputStreamConsumer(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {

            try {
                int value = -1;
                while ((value = is.read()) != -1) {
                    System.out.print((char)value);
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }

        }

    }
}