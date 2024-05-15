# Text-Based Database Simulation for Java

## Version 1.1.0

This project simulates a basic database system in Java using text-based commands. Keep in mind that this is a simplified version compared to an actual database, and the SQL query handling is sensitive to formatting. Do not use next lines too, all query should be on the same line.

Problems:
```
-Primary Keys and Foreign Keys have limited functions
-Primary Key only function to ensure input values to be unique
-Queries are very case and space sensitive
```

## Method: sql
    - The only method available is sql.
    - To use it, call database.sql(Query).
## Supported Queries
### Creating a New Table
``` 
    CREATE TABLE table_name (
    column_name1 data_type1 {optional arguments},
    column_name2 data_type2 {optional arguments},
    ...
); 
```
- Replace table_name with your desired table name.
- Specify column names, data types, and any optional arguments (e.g., NOT NULL, PRIMARY KEY, FOREIGN KEY).

### Adding Data into a Table
```
    INSERT INTO table_name (column_name1, column_name2, ...) VALUES
    (value1, value2, ...),
    (value3, value4, ...),
    ...
;
```
- Replace table_name with the target table.
- List the column names and corresponding values for each row.
  
### Retrieving Data from a Table
```
    SELECT column_name1, column_name2, ...
    FROM table_name
    WHERE column_name2 = value AND/OR column_name2 = value
    ORDER BY column_name ASC/DSC;
```
- Replace table_name with the desired table.
- Specify the columns you want to retrieve. (Currently limited to 1 table)
- Optionally, use WHERE to filter results based on a condition. (Filters: =, >, <, !=, >=, <=)
- Use ORDER BY to sort the results (ascending ASC or descending DSC).

### Updating Data in a Table
```
    UPDATE table_name
    SET column_name1 = value, column_names2 = value ...
    WHERE column_name3 = value AND/OR column_name4 = value ...
```
- Replace table_name with the desired table.
- Specify the columns you want to update.
- Specify conditions for the columns you want to update. (Conditions: =, >, <, !=, >=, <=)

### Delete Data in a Table
```
    DELETE FROM table_name
    WHERE column_name1 = value AND/OR column_name2 = value
```
- Replace table_name with the desired table.
- Specify conditions for the columns you want to delete. (Conditions: =, >, <, !=, >=, <=)
