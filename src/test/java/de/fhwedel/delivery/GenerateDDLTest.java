package de.fhwedel.delivery;

import de.fhwedel.delivery.transaction.SessionManager;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

public class GenerateDDLTest {
    @Test
    public void exportDDL() throws Exception {
        SchemaExport schemaExport = new SchemaExport(SessionManager.cfg);
        schemaExport.setDelimiter(";");
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
        schemaExport.setOutputFile("./pizza.sql");
        schemaExport.execute(true, false, false, false);
    }
}
