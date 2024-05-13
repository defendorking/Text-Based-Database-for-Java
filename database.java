import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

// Version 1.0.2

public class database {

    // Methods

    // Check if string is an integer value
    private static boolean isInteger(String strNum) {
        database.log("Method: isInteger called");
        if (strNum == null) {
            database.log("Output: Input is null, returned false");
            return false;
        }
        try {
            int i = Integer.valueOf(strNum);
        } catch (NumberFormatException e) {
            database.log("Error: NumberFormatException (line 29)");
            return false;
        }
        database.log("Output: Returned true");
        return true;
    }

    // Check if string is a double value
    private static boolean isDouble(String strNum) {
        database.log("Method: isDouble called");
        if (strNum == null) {
            database.log("Output: Input is null, returned false");
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException e) {
            database.log("Error: NumberFormatException (line 46)");
            return false;
        }
        database.log("Output: Returned true");
        return true;
    }

    private static int isFilterIndex(String filter) {
        database.log("Method: isFilterIndex called");
        if (filter.contains("!=")) {
            return filter.indexOf("!=");
        } else if (filter.contains(">=")) {
            return filter.indexOf(">=");
        } else if (filter.contains("<=")) {
            return filter.indexOf("<=");
        } else if (filter.contains("=")) {
            return filter.indexOf("=");
        } else if (filter.contains(">")) {
            return filter.indexOf(">");
        } else if (filter.contains("<")) {
            return filter.indexOf("<");
        } else {
            database.log("Output: Filter index not found");
            return -1;
        }
    }

    private static boolean filter(String data, String filter) {
        database.log("Method: filter called");
        String temp;
        if (filter.contains("!=")) {
            temp = filter.split("!=")[1].trim();
            if (data.trim().equals(temp)) {
                return false;
            } else {
                return true;
            }
        } else if (filter.contains(">=")) {
            temp = filter.split(">=")[1].trim();
            int num1 = Integer.parseInt(data);
            int num2 = Integer.parseInt(temp);
            if (num1 >= num2) {
                return true;
            } else {
                return false;
            }
        } else if (filter.contains("<=")) {
            temp = filter.split("<=")[1].trim();
            int num1 = Integer.parseInt(data);
            int num2 = Integer.parseInt(temp);
            if (num1 <= num2) {
                return true;
            } else {
                return false;
            }
        } else if (filter.contains("=")) {
            temp = filter.split("=")[1].trim();
            if (data.trim().equals(temp)) {
                return true;
            } else {
                return false;
            }
        } else if (filter.contains(">")) {
            temp = filter.split(">")[1].trim();
            int num1 = Integer.parseInt(data);
            int num2 = Integer.parseInt(temp);
            if (num1 > num2) {
                return true;
            } else {
                return false;
            }
        } else if (filter.contains("<")) {
            temp = filter.split("<")[1].trim();
            int num1 = Integer.parseInt(data);
            int num2 = Integer.parseInt(temp);
            if (num1 < num2) {
                return true;
            } else {
                return false;
            }
        } else {
            database.log("Output: No filter found, return false");
            return false;
        }

    }

    // Log
    private static void log(String log) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            File f = new File("databaseLog.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            Writer output = new FileWriter("databaseLog.txt", true);
            output.append(String.format("%s %s\n", dtf.format(now), log));
            output.close();
        } catch (IOException e) {
            System.out.println("An unexpected error occured.");
            e.printStackTrace();
        }
    }

    // Database cmd

    // Create table in database
    private static void createTable(String name, String cmd) {
        database.log("Method: createTable called");
        try {
            String colName = "";
            String dataType = "";
            String notNull = "";
            String key = "";
            // Get inividual column names
            String[] columns = cmd.split(", ");
            for (int i = 0; i < columns.length; ++i) {
                String[] temp = columns[i].split(" ");
                if (colName.contains(temp[0])) {
                    database.log("Input Error: dupliate column name (line 166)");
                    System.out.println(String.format("The column name %s has been used multiple times.", temp[0]));
                    return;
                } else {
                    colName = colName + temp[0] + ",";
                }
                // Check for valid datatypes for each column
                String[] dataTypes = { "int", "str", "double", "bit", "date" };
                if (Arrays.asList(dataTypes).contains(temp[1])) {
                    dataType = dataType + temp[1] + ",";
                } else {
                    database.log("Input Error: Invalid datatype (line 177)");
                    System.out.println(String.format("%s is not a valid datatype.", temp[1]));
                    return;
                }

                // Check for availability of null value in columns
                if (columns[i].contains("NOT NULL")) {
                    notNull = notNull + "YES,";
                } else {
                    notNull = notNull + "NO,";
                }
                // Check for PK and FK
                if (columns[i].contains("PRIMARY KEY")) {
                    key = key + "PK,";
                } else if (columns[i].contains("FOREIGN KEY references")) {
                    String FKName = columns[i].substring(columns[i].indexOf("(\"") + 2, columns[i].indexOf("\")"));
                    File f = new File(FKName);
                    if (f.exists() && !f.isDirectory()) {
                        key = key + "FK(" + FKName + "),";
                    } else {
                        database.log("Input Error: Invalid FK (line 197)");
                        System.out.println(String.format("Invalid Foreign Key. The table %s does not exist.", FKName));
                        return;
                    }
                } else {
                    key = key + "NO,";
                }
            }
            // Creating new files for the table
            name = name + ".txt";
            File createFile = new File(name);
            if (createFile.createNewFile()) {
                System.out.println("Table created: " + createFile.getName());
            } else {
                database.log("Input Error: Table already exist (line 211)");
                System.out.println("The table already exists.");
            }

            FileWriter writer = new FileWriter(name);
            writer.write(String.format("%s\n%s\n%s\n%s\n", colName, dataType, notNull, key));
            writer.close();

        } catch (IOException e) {
            database.log("Error: IOException (line 220)");
            System.out.println("An unexpected error occured.");
            e.printStackTrace();
        }
    }

    // Input values into tables
    private static void insertValue(String name, String cmd) {
        database.log("Method: insertValue called");
        name = name + ".txt";
        String[] columns = cmd.substring(cmd.indexOf("(") + 1, cmd.indexOf(")")).split(",");
        String[] values;
        if (cmd.contains("values")) {
            values = cmd.substring(cmd.indexOf("values") + 7, cmd.length() - 1).split("\\),\\(");
        } else {
            database.log("Input Error: Invalid command (line 235)");
            System.out.println("Invalid command.");
            return;
        }
        ArrayList<String> inputData = new ArrayList<String>();
        ArrayList<String> tableData = new ArrayList<String>();
        try {
            File table = new File(name);
            Scanner tableRawData = new Scanner(table);
            for (int i = 0; i < 4; ++i) {
                tableData.add(tableRawData.nextLine());
            }
            tableRawData.close();
        } catch (FileNotFoundException e) {
            database.log("Input Error: Table does not exist (line 249)");
            System.out.println("The table does not exist");
            e.printStackTrace();
            return;
        }
        String[] colName = tableData.get(0).split(",");
        String[] dataType = tableData.get(1).split(",");
        String[] notNull = tableData.get(2).split(",");
        String[] key = tableData.get(3).split(",");
        String[] arrangedValue = new String[colName.length];
        ArrayList<String> colNameArrList = new ArrayList<String>();
        for (int i = 0; i < colName.length; ++i) {
            colNameArrList.add(colName[i]);
        }
        for (int i = 0; i < values.length; ++i) {
            String[] readValue = values[i].split(",");
            for (int k = 0; k < columns.length; ++k) {
                int posValue = colNameArrList.indexOf(columns[k]);
                if (posValue == -1) {
                    database.log("Input Error: Invalid column name (line 268)");
                    System.out.println("Invalid column name");
                    return;
                }
                if (dataType[posValue].equals("str") && Character.toString(readValue[k].charAt(0)).equals("'")
                        && Character.toString(readValue[k].charAt(readValue[k].length() - 1)).equals("'")) {
                    arrangedValue[posValue] = readValue[k].substring(1, readValue[k].length() - 1);
                } else if (dataType[posValue].equals("int") && database.isInteger(readValue[k])) {
                    arrangedValue[posValue] = readValue[k];
                } else if (dataType[posValue].equals("double") && database.isDouble(readValue[k])) {
                    arrangedValue[posValue] = readValue[k];
                } else if (dataType[posValue].equals("bit") && (readValue[k].equals("0") || readValue[k].equals("1"))) {
                    arrangedValue[posValue] = readValue[k];
                } else {
                    database.log("Input Error: Invalid datatype (line 282)");
                    System.out.println("Invalid datatype");
                    return;
                }
            }
            for (int k = 0; k < arrangedValue.length; ++k) {
                if (arrangedValue[k] == null && notNull[k].equals("Yes")) {
                    database.log("Input Error: Null value in non null column (line 289)");
                    System.out.println("Null value in non null column");
                    return;
                }
            }
            String newValue = "";
            for (int k = 0; k < arrangedValue.length; ++k) {
                if (arrangedValue[k] != null) {
                    newValue = newValue + arrangedValue[k] + ",";
                } else {
                    newValue = newValue + "Null,";
                }
            }
            inputData.add(newValue);
        }
        try {
            Writer output = new BufferedWriter(new FileWriter(name, true));
            for (int i = 0; i < inputData.size(); ++i) {
                output.append(inputData.get(i) + "\n");
            }
            output.close();
        } catch (IOException e) {
            database.log("Error: IOException (line 311)");
            System.out.println("An unexpected error occured.");
            e.printStackTrace();
        }

    }

    // Get all values within a table in an ArrayList in ArrayList format
    // Returns: 0th column names, 1st+ table data(rows)
    private static ArrayList<ArrayList<String>> tableValue(String name) {
        database.log("Method tableValue called");
        ArrayList<ArrayList<String>> returnData = new ArrayList<ArrayList<String>>();
        name = name + ".txt";
        ArrayList<String> tableData = new ArrayList<String>();
        try {
            File table = new File(name);
            Scanner tableRawData = new Scanner(table);
            while (tableRawData.hasNextLine()) {
                tableData.add(tableRawData.nextLine());
            }
            tableRawData.close();
        } catch (FileNotFoundException e) {
            database.log("Error: FileNotFoundException (line 333)");
            System.out.println("The table does not exist");
            e.printStackTrace();
            return null;
        }
        ArrayList<String> colName = new ArrayList<String>(Arrays.asList(tableData.get(0).split(",")));
        returnData.add(colName);
        for (int i = 4; i < tableData.size(); ++i) {
            returnData.add(new ArrayList<String>(Arrays.asList((tableData.get(i).split(",")))));
        }
        return returnData;
    }

    private static ArrayList<ArrayList<String>> tableParam(String name) {
        database.log("Method tableParam called");
        ArrayList<ArrayList<String>> returnData = new ArrayList<ArrayList<String>>();
        name = name + ".txt";
        ArrayList<String> tableData = new ArrayList<String>();
        try {
            File table = new File(name);
            Scanner tableRawData = new Scanner(table);
            while (tableRawData.hasNextLine()) {
                tableData.add(tableRawData.nextLine());
            }
            tableRawData.close();
        } catch (FileNotFoundException e) {
            database.log("Error: FileNotFoundException (line 359)");
            System.out.println("The table does not exist");
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < 4; ++i) {
            returnData.add(new ArrayList<String>(Arrays.asList((tableData.get(i).split(",")))));
        }
        return returnData;
    }

    private static ArrayList<ArrayList<String>> selectValue(String cmd) {
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<ArrayList<String>> returnArray = new ArrayList<ArrayList<String>>();

        // Get all info from the command
        int position = cmd.indexOf("FROM") + 3;
        int codePos = 0;
        values.add(cmd.substring(0, position - 4));
        if (cmd.contains("WHERE")) {
            values.add(cmd.substring(position + 2, cmd.indexOf("WHERE") - 1));
            position = cmd.indexOf("WHERE") + 4;
        }
        if (cmd.contains("ORDER BY")) {
            values.add(cmd.substring(position + 2, cmd.indexOf("ORDER BY") - 1));
            position = cmd.indexOf("ORDER BY") + 7;
        }
        values.add(cmd.substring(position + 2));
        // Get column names
        String[] column;
        if (values.get(codePos).equals("*")) {
            String[] temp = { "*" };
            column = temp;
        } else {
            column = values.get(codePos).split(",");
        }
        ++codePos;

        // Get table names
        String table = values.get(codePos);
        ++codePos;

        // Get table data
        ArrayList<ArrayList<String>> tableData = database.tableValue(table);

        // Set column names
        String columns[];
        if (column[0].equals("*")) {
            int colLength = tableData.get(0).size();
            String[] allCols = new String[colLength];
            for (int i = 0; i < tableData.get(0).size(); ++i) {
                allCols[i] = tableData.get(0).get(i);
            }
            columns = allCols;
        } else {
            columns = column;
        }
        ArrayList<String> columnConvert = new ArrayList<String>();
        for (int i = 0; i < columns.length; ++i) {
            columns[i].trim();
            columnConvert.add(columns[i]);
        }
        returnArray.add(columnConvert);

        // Handles filters
        int filterType; // AND: 0, OR: 1, SINGLE: 2
        String[] filter;
        if (cmd.contains("WHERE")) {
            // Split up the filters
            if (cmd.contains("AND") && !cmd.contains("OR")) {
                filter = values.get(codePos).split("AND");
                for (int i = 0; i < filter.length; ++i) {
                    filter[i].trim();
                }
                filterType = 0;
            } else if (cmd.contains("OR") && !cmd.contains("AND")) {
                filter = values.get(codePos).split("OR");
                for (int i = 0; i < filter.length; ++i) {
                    filter[i].trim();
                }
                filterType = 1;
            } else {
                String[] temp = { "temp" };
                temp[0] = values.get(codePos).trim();
                filter = temp;
                filterType = 2;
            }
            ++codePos;
            // Check if valid filter
            for (int i = 0; i < filter.length; ++i) {
                if (database.isFilterIndex(filter[i]) == -1) {
                    database.log("Input Error: Invalid command (line 450)");
                    System.out.println("Invalid command");
                    return null;
                }
            }

            // Check if column names are valid for filter
            for (int i = 0; i < filter.length; ++i) {
                if (!Arrays.asList(columns)
                        .contains(filter[i].substring(0, database.isFilterIndex(filter[i])).trim())) {
                    database.log("Input Error: Invalid command (line 460)");
                    System.out.println("Invalid command");
                    return null;
                }
            }

            // Filtering process
            ArrayList<Integer> filteredRows = new ArrayList<Integer>();
            // Check which rows matches the filter
            for (int i = 0; i < filter.length; ++i) {
                String colName = filter[i].substring(0, database.isFilterIndex(filter[i])).trim();
                int colFilterIndex = tableData.get(0).indexOf(colName);
                for (int k = 1; k < tableData.size(); ++k) {
                    if (database.filter(tableData.get(k).get(colFilterIndex), filter[i])) {
                        filteredRows.add(k);
                    }
                }
            }
            // Count the number of times a row appears, for AND, filter by how many
            // instances of filters, for OR, more than one occurence
            ArrayList<ArrayList<String>> newTable = new ArrayList<ArrayList<String>>();
            newTable.add(columnConvert);
            for (int i = 1; i < tableData.size(); ++i) {
                int count = Collections.frequency(filteredRows, i);
                if (filterType == 0 && count == filter.length) {
                    newTable.add(tableData.get(i));
                } else if (filterType == 1 && count >= 1) {
                    newTable.add(tableData.get(i));
                } else if (filterType == 2 && count == 1) {
                    newTable.add(tableData.get(i));
                }
            }
            tableData = newTable;
        }

        // Handle row order

        if (cmd.contains("ORDER BY")) {
            String colName; // Column Name for Ordering
            int orderByType; // ASC: 0; DSC: 1
            String[] temp = values.get(codePos).trim().split(" ");
            colName = temp[0];
            try {
                if (temp[1].equals("ASC")) {
                    orderByType = 0;
                } else if (temp[1].equals("DSC")) {
                    orderByType = 1;
                } else {
                    database.log("Input Error: Invalid command (line 508)");
                    System.out.println("Invalid command");
                    return null;
                }
            } catch (Exception e) {
                database.log("Input Error: Invalid command, missing ASC/DSC (line 513)");
                System.out.println("Invalid command");
                return null;
            }

            // Sort by order
            int colOrderIndex = tableData.get(0).indexOf(colName);
            ArrayList<String> arrangingValues = new ArrayList<String>();
            arrangingValues.add("Empty");
            for (int i = 1; i < tableData.size(); ++i) {
                String addData = String.format("%s,%s", tableData.get(i).get(colOrderIndex), String.valueOf(i));
                arrangingValues.add(addData);
            }
            arrangingValues.remove("Empty");
            Collections.sort(arrangingValues);
            ArrayList<ArrayList<String>> newTable = new ArrayList<ArrayList<String>>();
            newTable.add(columnConvert);
            for (int i = 0; i < arrangingValues.size(); ++i) {
                int originalPos = Integer.parseInt(arrangingValues.get(i).split(",")[1]);
                newTable.add(tableData.get(originalPos));
            }
            if (orderByType == 0) {
                tableData = newTable;
            } else if (orderByType == 1) {
                ArrayList<ArrayList<String>> anotherNewTable = new ArrayList<ArrayList<String>>();
                anotherNewTable.add(columnConvert);
                for (int i = newTable.size() - 1; i > 0; --i) {
                    anotherNewTable.add(newTable.get(i));
                }
                tableData = anotherNewTable;
            }
        }

        // temp array for converting row format to column format
        ArrayList<ArrayList<String>> tempData = new ArrayList<ArrayList<String>>();
        tempData.add(columnConvert);
        for (int i = 0; i < columns.length; ++i) {
            int colIndex = tableData.get(0).indexOf(columns[i]);
            ArrayList<String> temp = new ArrayList<String>();
            for (int k = 1; k < tableData.size(); ++k) {
                temp.add(tableData.get(k).get(colIndex));
            }
            tempData.add(temp);
        }

        returnArray = tempData;

        // The return
        return returnArray;
    }

    private static void updateValue(String cmd) {
        if (!cmd.contains("SET") || !cmd.contains("WHERE")) {
            database.log("Input Error: Invalid command (line 566)");
            System.out.println("Invalid command");
            return;
        }

        String tableName = cmd.substring(cmd.indexOf("UPDATE") + 7, cmd.indexOf("SET")).trim();
        if (tableName.contains(" ")) {
            database.log("Input Error: Invalid command (line 573)");
            System.out.println("Invalid command");
            return;
        }
        String[] replacement = cmd.substring(cmd.indexOf("SET") + 4, cmd.indexOf("WHERE")).split(",");
        int filterType; // AND 0; OR 1; SINGLE 2
        String[] condition;
        if (cmd.substring(cmd.indexOf("WHERE")).contains("AND")) {
            filterType = 0;
            condition = cmd.substring(cmd.indexOf("WHERE") + 6).split("AND");
        } else if (cmd.substring(cmd.indexOf("WHERE")).contains("OR")) {
            filterType = 1;
            condition = cmd.substring(cmd.indexOf("WHERE") + 6).split("OR");
        } else {
            filterType = 2;
            String[] temp = { "temp " };
            condition = temp;
            condition[0] = cmd.substring(cmd.indexOf("WHERE") + 6);
        }
        ArrayList<String> filterCols = new ArrayList<String>();
        for (int i = 0; i < condition.length; ++i) {
            condition[i] = condition[i].trim();
            int filterIndex = database.isFilterIndex(condition[i]);
            if (filterIndex == -1) {
                database.log("Input Error: Invalid command (line 597)");
                System.out.println("Invalid command");
                return;
            }
            filterCols.add(condition[i].substring(0, filterIndex).trim());
        }
        ArrayList<ArrayList<String>> replacements = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < replacement.length; ++i) {
            replacement[i] = replacement[i].trim();
            if (!replacement[i].contains("=")) {
                database.log("Input Error: Invalid command (line 607)");
                System.out.println("Invalid command");
                return;
            }
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(replacement[i].substring(0, replacement[i].indexOf("=")).trim());
            temp.add(replacement[i].substring(replacement[i].indexOf("=") + 2).trim());
            replacements.add(temp);
        }
        ArrayList<ArrayList<String>> tableData = tableValue(tableName);
        for (int i = 1; i < tableData.size(); ++i) {
            int conditionCount = 0;
            for (int k = 0; k < condition.length; ++k) {
                if (database.filter(tableData.get(i).get(tableData.get(0).indexOf(filterCols.get(k))), condition[k])) {
                    ++conditionCount;
                }
            }
            if (filterType == 0 && conditionCount == condition.length) {

            } else if (filterType == 1 && conditionCount >= 1) {

            } else if (filterType == 2 && conditionCount == 1) {

            } else {
                continue;
            }
            for (int k = 0; k < replacements.size(); ++k) {
                tableData.get(i).set((tableData.get(0).indexOf(replacements.get(k).get(0))),
                        replacements.get(k).get(1));
            }
        }
        ArrayList<ArrayList<String>> tableInfo = database.tableParam(tableName);
        String replaceValue = "";
        for (int i = 0; i < tableInfo.size(); ++i) {
            for (int k = 0; k < tableInfo.get(i).size(); ++k) {
                replaceValue = String.format("%s%s,", replaceValue, tableInfo.get(i).get(k));
            }
            replaceValue = String.format("%s%s", replaceValue, "\n");
        }
        for (int i = 1; i < tableData.size(); ++i) {
            for (int k = 0; k < tableData.get(i).size(); ++k) {
                replaceValue = String.format("%s%s,", replaceValue, tableData.get(i).get(k));
            }
            replaceValue = String.format("%s%s", replaceValue, "\n");
        }
        try {
            String name = tableName + ".txt";
            FileWriter writer = new FileWriter(name);
            writer.write(replaceValue);
            writer.close();
        } catch (IOException e) {
            database.log("Error: IOException (line 658)");
            System.out.println("An unexpected error occred");
            e.printStackTrace();
        }
    }

    private static void deleteValue(String cmd) {
        if (!cmd.contains("WHERE")) {
            database.log("Input Error: Invalid commandn (line 666)");
            System.out.println("Invalid command");
        }

        String tableName = cmd.substring(cmd.indexOf("FROM") + 5, cmd.indexOf("WHERE")).trim();
        if (tableName.contains(" ")) {
            database.log("Input Error: Invalid command (line 672)");
            System.out.println("Invalid command");
            return;
        }
        String[] conditions;
        int conditionType; // AND: 0, OR: 1, SINGLE: 2
        if (cmd.contains("AND")) {
            conditionType = 0;
            conditions = cmd.substring(cmd.indexOf("WHERE") + 6).split("AND");
        } else if (cmd.contains("OR")) {
            conditionType = 1;
            conditions = cmd.substring(cmd.indexOf("WHERE") + 6).split("OR");
        } else {
            conditionType = 2;
            String[] temp = { "temp" };
            temp[0] = cmd.substring(cmd.indexOf("WHERE") + 6);
            conditions = temp;
        }
        ArrayList<String> filterCols = new ArrayList<String>();
        for (int i = 0; i < conditions.length; ++i) {
            conditions[i] = conditions[i].trim();
            int filterIndex = database.isFilterIndex(conditions[i]);
            if (filterIndex == -1) {
                database.log("Input Error: Invalid command (line 695)");
                System.out.println("Invalid command");
                return;
            }
            filterCols.add(conditions[i].substring(0, filterIndex).trim());
        }
        ArrayList<ArrayList<String>> tableData = tableValue(tableName);
        ArrayList<ArrayList<String>> endTableData = new ArrayList<ArrayList<String>>();
        for (int i = 1; i < tableData.size(); ++i) {
            int conditionCount = 0;
            for (int k = 0; k < conditions.length; ++k) {
                if (database.filter(tableData.get(i).get(tableData.get(0).indexOf(filterCols.get(k))), conditions[k])) {
                    ++conditionCount;
                }
            }
            if (conditionType == 0 && conditionCount == conditions.length) {

            } else if (conditionType == 1 && conditionCount >= 1) {

            } else if (conditionType == 2 && conditionCount == 1) {

            } else {
                endTableData.add(tableData.get(i));
                continue;
            }
        }
        ArrayList<ArrayList<String>> tableInfo = database.tableParam(tableName);
        String replaceValue = "";
        for (int i = 0; i < tableInfo.size(); ++i) {
            for (int k = 0; k < tableInfo.get(i).size(); ++k) {
                replaceValue = String.format("%s%s,", replaceValue, tableInfo.get(i).get(k));
            }
            replaceValue = String.format("%s%s", replaceValue, "\n");
        }
        for (int i = 0; i < endTableData.size(); ++i) {
            for (int k = 0; k < endTableData.get(i).size(); ++k) {
                replaceValue = String.format("%s%s,", replaceValue, endTableData.get(i).get(k));
            }
            replaceValue = String.format("%s%s", replaceValue, "\n");
        }
        try {
            String name = tableName + ".txt";
            FileWriter writer = new FileWriter(name);
            writer.write(replaceValue);
            writer.close();
        } catch (IOException e) {
            database.log("Error: IOException (line 741)");
            System.out.println("An unexpected error occred");
            e.printStackTrace();
        }
    }

    // SQL command activation
    public static ArrayList<ArrayList<String>> sql(String cmd) {

        String[] words = cmd.split(" ");

        if (words[0].equals("CREATE") && words[1].equals("TABLE")) {
            String command = cmd.substring(cmd.indexOf("(") + 1, cmd.length() - 2);
            database.createTable(words[2].substring(0, words[2].indexOf("(")), command);
            return null;
        } else if (words[0].equals("INSERT") && words[1].equals("INTO")) {
            String command = cmd.substring(cmd.indexOf(words[2]) + words[2].length() + 1, cmd.length());
            database.insertValue(words[2], command);
            return null;
        } else if (words[0].equals("SELECT")) {
            String command = cmd.substring(7);
            ArrayList<ArrayList<String>> value = database.selectValue(command);
            return value;
        } else if (words[0].equals("UPDATE")) {
            database.updateValue(cmd);
            return null;
        } else if (words[0].equals("DELETE") && words[1].equals("FROM")) {
            database.deleteValue(cmd);
            return null;
        } else {
            database.log("Input Error: Invalid command (line 771)");
            System.out.println("Invalid command.");
            return null;
        }
    }
}
