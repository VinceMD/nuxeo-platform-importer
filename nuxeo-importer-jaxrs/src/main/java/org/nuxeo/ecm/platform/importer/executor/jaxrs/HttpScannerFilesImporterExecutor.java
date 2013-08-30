package org.nuxeo.ecm.platform.importer.executor.jaxrs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.importer.service.DefaultImporterService;
import org.nuxeo.ecm.platform.scanimporter.processor.ScannedFileImporter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by IntelliJ IDEA.
 * User: vincent brouillet
 * Date: 30/08/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("scanner")
public class HttpScannerFilesImporterExecutor  extends AbstractJaxRSImporterExecutor {


    private static final Log log = LogFactory.getLog(HttpFileImporterExecutor.class);
    protected DefaultImporterService importerService;
    private static boolean ingestionInProgress = false;
    public static String START_EVENT = "ScanIngestionStart";

    @Override
    protected Log getJavaLogger() {
        return log;
    }

    @GET
    @Path("runWithConfigLoad")
    @Produces("text/plain; charset=UTF-8")
    public String runScannerWithMapping()  throws Exception    {


        if (ingestionInProgress) {
            log.info("Ingestion already in progress, waiting for next wake up");
            return "Ingestion already in progress, waiting for next wake up";
        } else {
            log.info("Start injection process");
        }

        String returnMessage="Unknown state or error occured";
        ingestionInProgress = true;
        try {
            ScannedFileImporter importer = new ScannedFileImporter();
            try {
                importer.doImport();
                returnMessage="Start injection process";
            } catch (Exception e) {
                log.error("Error during import ", e);
                throw new ClientException(e);
            }

        } finally {
            ingestionInProgress = false;
        }
        return returnMessage;
    }

}
