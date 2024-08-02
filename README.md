**University Student Database**

Practise using SQL and Pgadmin to simulate a university database of students where you can display student information, add students, update student information, or delete a student
 
**How to set up database:**

    -run pgadmin 4
    -make new database
    - press query tool and open table.sql and run it
    - press query tool and open initialize.sql and run it, this will get the database going
    in app.java file change lines 18-20 to resemble your database details:
    private static final String url = "jdbc:postgresql://<your localhost name>:<your port>/<your database name>";
    private static final String user = "your username here";
    private static final String password = "your password here";
    (these can be retrieved in pgadmin by left clicking on PostgresSQL 16 -Properties -Connection)

**How to run:**

    make sure postgresql-42.7.0.jar is in the javaCode folder (if not download it from https://jdbc.postgresql.org/download/)
    go into folder directory
    run command: java -cp postgresql-42.7.0.jar app.java

