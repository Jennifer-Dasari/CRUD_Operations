Feature: Database Connection and Operations
  As a user of the application
  I want to connect to the database, generate employee data, perform CRUD operations, and extract the data into an XML file
  So that I can manage employee information efficiently

  Scenario: Establishing a database connection
    Given the application configuration is loaded with JDBC URL, username, and password
    When the application attempts to connect to the database
    Then a successful connection is established
    And a log message "connection has been successfully established!" is recorded

  Scenario: Generating and inserting employee data into the database
    Given a successful database connection
    When the application generates employee XML data
    And inserts the data into the database
    Then the data should be successfully inserted into the database

  Scenario: Performing CRUD operations on employee data
    Given a successful database connection
    When the application displays the CRUD operations menu
    Then the user can perform create, read, update, and delete operations on employee data

  Scenario: Extracting employee data to an XML file
    Given a successful database connection
    When the application extracts employee data from the database
    And saves it to an XML file
    Then the data should be successfully saved to "extracted_employees.xml"

  Scenario: Handling a connection failure
    Given the application attempts to connect to the database
    When the connection fails
    Then an error message "connection disrupted! <exception message>" is logged
