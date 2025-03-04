import org.sqlite.*
import java.sql.*
import groovy.sql.Sql

class DBHelper {

    def static setupDB(workingDir){

        def dbFile = new File(workingDir, "stats.db")

        boolean dbExists = dbFile.exists()

        // in memory
        // db = groovy.sql.Sql.newInstance("jdbc:sqlite::memory:","org.sqlite.JDBC")

        // persistent
        def db = groovy.sql.Sql.newInstance("jdbc:sqlite:"+dbFile.absolutePath,"org.sqlite.JDBC")

        if(!dbExists){
            // define the tables
            db.execute("create table jenkins(instanceid, month, version, jvmvendor, jvmname, jvmversion)")
            db.execute("create table plugin(instanceid, month, name, version)")
            db.execute("create table job(instanceid, month, type, jobnumber)")
            db.execute("create table node(instanceid, month, osname, nodenumber)")
            db.execute("create table executor(instanceid, month, numberofexecutors)")
            db.execute("create table importedfile(name)")
            db.execute("CREATE INDEX plugin_name on plugin (name)")
            db.execute("CREATE INDEX jenkins_version on jenkins (version)")
            db.execute("CREATE INDEX plugin_month on plugin (month)")
            db.execute("CREATE INDEX plugin_namemonth on plugin (name,month)")
        }

        return db;
    }


    /**
     * Checks if a file with the given name has already been imported into the database
     * @param db Database connection
     * @param fileName Name of the file to check
     * @return true if the file should be imported (hasn't been imported yet), false otherwise
     * @throws SQLException if there is a database error
     */
    static boolean doImport(db, fileName) {
        if (!db) {
            throw new IllegalArgumentException("Database connection cannot be null")
        }
        if (!fileName) {
            throw new IllegalArgumentException("Filename cannot be null")
        }

        try {
            def filePrefix = fileName.substring(0, fileName.indexOf(".")) + "%"
            def rows = db.rows("select name from importedfile where name like ?", [filePrefix])
            return rows.size() == 0
        } catch (Exception e) {
            println "Error checking if file ${fileName} was already imported: ${e.message}"
            throw e // Re-throw to let caller handle it
        }
    }
}



