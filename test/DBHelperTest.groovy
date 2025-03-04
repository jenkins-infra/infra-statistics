@GrabConfig(systemClassLoader=true)
@Grab('org.xerial:sqlite-jdbc:3.16.1')
import org.sqlite.*
import java.sql.*
import groovy.sql.Sql

class DBHelperTest {
    /**
     * Test the error handling in doImport method
     */
    static void testDoImport() {
        println "Running doImport tests..."
        
        // Setup test database
        def testDb = Sql.newInstance("jdbc:sqlite::memory:", "org.sqlite.JDBC")
        testDb.execute("create table importedfile(name text)")
        
        // Test 1: Null database
        try {
            DBHelper.doImport(null, "test.json")
            println "ERROR: Should have thrown exception for null database"
        } catch (IllegalArgumentException e) {
            println "SUCCESS: Caught null database exception: ${e.message}"
        }

        // Test 2: Null filename
        try {
            DBHelper.doImport(testDb, null)
            println "ERROR: Should have thrown exception for null filename"
        } catch (IllegalArgumentException e) {
            println "SUCCESS: Caught null filename exception: ${e.message}"
        }

        // Test 3: File not imported yet
        try {
            def result = DBHelper.doImport(testDb, "test.json")
            if (result) {
                println "SUCCESS: Correctly identified new file"
            } else {
                println "ERROR: Failed to identify new file"
            }
        } catch (Exception e) {
            println "ERROR: Unexpected exception: ${e.message}"
        }

        // Test 4: Already imported file
        testDb.execute("insert into importedfile(name) values(?)", ["test.json"])
        try {
            def result = DBHelper.doImport(testDb, "test.json")
            if (!result) {
                println "SUCCESS: Correctly identified already imported file"
            } else {
                println "ERROR: Failed to identify already imported file"
            }
        } catch (Exception e) {
            println "ERROR: Unexpected exception: ${e.message}"
        }

        testDb.close()
        println "Tests completed."
    }

    static void main(args) {
        testDoImport()
    }
}