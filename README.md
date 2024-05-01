Text-Based Java Database SimulationThis project simulates a basic database system in Java using text-based commands. Keep in mind that this is a simplified version compared to an actual database, and the SQL query handling is sensitive to formatting. Do not use next lines too, all query should be on the same line.
Usage- Method: sql
    - The only method available is sql.
    - To use it, call database.sql(Query).
Supported QueriesCreating a New Table
CREATE TABLE table_name (
    column_name1 data_type1 {optional arguments},
    column_name2 data_type2 {optional arguments},
    ...
);

- Replace table_name with your desired table name.
- Specify column names, data types, and any optional arguments (e.g., NOT NULL, PRIMARY KEY, FOREIGN KEY).
Adding Data into a Table
INSERT INTO table_name (column_name1, column_name2, ...) VALUES
    (value1, value2, ...),
    (value3, value4, ...),
    ...
;

- Replace table_name with the target table.
- List the column names and corresponding values for each row.
Retrieving Data from a Table
SELECT column_name1, column_name2, ...
FROM table_name
WHERE column_name = value
ORDER BY column_name ASC/DSC;

- Replace table_name with the desired table.
- Specify the columns you want to retrieve.
- Optionally, use WHERE to filter results based on a condition.
- Use ORDER BY to sort the results (ascending or descending).

