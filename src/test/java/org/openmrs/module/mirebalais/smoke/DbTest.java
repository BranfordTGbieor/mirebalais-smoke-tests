package org.openmrs.module.mirebalais.smoke;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.IOUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openmrs.module.mirebalais.smoke.dataModel.Patient;
import org.openmrs.module.mirebalais.smoke.helper.SmokeTestProperties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

public abstract class DbTest extends BasicMirebalaisSmokeTest {

	private static SmokeTestProperties properties = new SmokeTestProperties();

    private static DatabaseConnection connection;

    private IDataSet dataset;

	private String patientIdentifierValue;

    private static Object synch = new Object();

    @BeforeClass
	public static void setDatabaseConnection() throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(properties.getDatabaseUrl(), properties.getDatabaseUsername(), properties.getDatabasePassword());
        connection = new DatabaseConnection(jdbcConnection);

        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
        config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
    }

	@AfterClass
	public static void closeDatabaseConnection() throws Exception {
		connection.close();
	}

	@Before
	public void setUpPatientData() throws Exception {
        synchronized (synch) {
            try {
                testPatient = new Patient(getNextValidPatientIdentifier(), "Crash Test Dummy",
                        getNextAutoIncrementFor("person"), UUID.randomUUID().toString(), getPatientIdentifierId(),
                        getNextAutoIncrementFor("person_name"), getNextAutoIncrementFor("person_address"),
                        getNextAutoIncrementFor("patient_identifier"));

                lockPatientIdentifier();

                dataset = createDataset();

                DatabaseOperation.INSERT.execute(getConnection(), dataset);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new Exception("set up failed", e);
            }
        }
	}
public void setUp() throws Exception {

}
	@After
	public void tearDownPatientData() throws Exception {
		try {
			QueryDataSet createdData = new QueryDataSet(getConnection());
			createdData.addTable("obs",
			    "select * from obs where encounter_id in (select encounter_id from encounter where patient_id="
			            + testPatient.getId() + ") and obs_group_id is not null");
			DatabaseOperation.DELETE.execute(getConnection(), createdData);

			createdData = new QueryDataSet(getConnection());
			createdData.addTable("name_phonetics",
			    "select * from name_phonetics where person_name_id = " + testPatient.getPerson_name_id());
			createdData.addTable("person_attribute",
			    "select * from person_attribute where person_id = " + testPatient.getId());
			createdData.addTable("patient_identifier",
			    "select * from patient_identifier where patient_id=" + testPatient.getId());
			createdData.addTable("emr_paper_record_request", "select * from emr_paper_record_request where patient_id="
			        + testPatient.getId());
			createdData.addTable("visit", "select * from visit where patient_id=" + testPatient.getId());
			createdData.addTable("encounter", "select * from encounter where patient_id=" + testPatient.getId());
			createdData.addTable("orders", "select * from orders where patient_id=" + testPatient.getId());
			createdData.addTable(
			    "test_order",
			    "select * from test_order where order_id in (select order_id from orders where patient_id="
			            + testPatient.getId() + ")");
			createdData.addTable("emr_radiology_order",
                    "select * from emr_radiology_order where order_id in (select order_id from orders where patient_id="
                            + testPatient.getId() + ")");
			createdData.addTable("obs",
			    "select * from obs where encounter_id in (select encounter_id from encounter where patient_id="
			            + testPatient.getId() + ")");
			createdData.addTable("encounter_provider",
                    "select * from encounter_provider where encounter_id in (select encounter_id from encounter where patient_id="
                            + testPatient.getId() + ")");
			
			DatabaseOperation.DELETE.execute(getConnection(), createdData);
			
			if (dataset == null) {
				dataset = createDataset();
			}
			DatabaseOperation.DELETE.execute(getConnection(), dataset);
			
			unlockPatientIdentifier();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("tear down failed", e);
		}
	}
	
	protected IDatabaseConnection getConnection() throws Exception {
        return connection;
    }
	
	private IDataSet createDataset() throws IOException, DataSetException {
		Handlebars handlebars = new Handlebars();
		Template template = handlebars.compile("datasets/patients_dataset.xml");
		
		return new FlatXmlDataSetBuilder().build(new InputStreamReader(IOUtils.toInputStream(template.apply(testPatient))));
	}
	
	private String getNextValidPatientIdentifier() throws Exception {
		ITable patientIdentifier = getConnection().createQueryTable("idgen_pooled_identifier",
		    "select * from idgen_pooled_identifier where date_used is null limit 1");
		
		patientIdentifierValue = (String) patientIdentifier.getValue(0, "identifier");
		
		return patientIdentifierValue;
	}
	
	private void lockPatientIdentifier() throws Exception {
		setDateUsedOfPatientIdentifierTo("sysdate()");
	}
	
	private void unlockPatientIdentifier() throws Exception {
		setDateUsedOfPatientIdentifierTo("null");
	}
	
	private void setDateUsedOfPatientIdentifierTo(String date) throws Exception {
		Connection connection = getConnection().getConnection();
		connection.createStatement().executeUpdate(
		    "update idgen_pooled_identifier set date_used = " + date + " where identifier = '" + patientIdentifierValue
		            + "'");
	}
	
	private Integer getPatientIdentifierId() throws Exception {
		ITable patientIdentifierType = getConnection().createQueryTable("identifier_source",
		    "select * from patient_identifier_type where name = 'ZL EMR ID'");
		return (Integer) patientIdentifierType.getValue(0, "patient_identifier_type_id");
	}
	
	private BigInteger getNextAutoIncrementFor(String table_name) throws Exception {
		ITable autoIncrement = getConnection().createQueryTable(
		    table_name,
		    "select Auto_increment from information_schema.tables where table_schema = DATABASE() AND table_name = '"
		            + table_name + "'");
		return (BigInteger) autoIncrement.getValue(0, "Auto_increment");
	}
}
