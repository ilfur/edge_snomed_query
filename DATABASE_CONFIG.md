# Database configuration

This file describes the database configuration of the *edge_find_patient_adapter_db*.

You may find this file within the `config` directory of the container image.

To configure the database, we use a file named
`microprofile-config-db.properties` inside the `config` subdirectory of the
directory, where the application is run from.

This file should contain values for at least the following properties:

| Property | Description | Example |
| ----- | ----- | ----- |
| javax.sql.DataSource.UploadMetaData.driverClassName | the JDBC driver class name | oracle.jdbc.driver.OracleDriver |
| javax.sql.DataSource.UploadMetaData.url | the JDBC-URL to connect to the database | jdbc:oracle:thin:@//12.34.56.78:1521/database-service |
| javax.sql.DataSource.UploadMetaData.user | the database username used to connect to the database | demouser |
| javax.sql.DataSource.UploadMetaData.password | the database users password | more-secret-than-this-one |

