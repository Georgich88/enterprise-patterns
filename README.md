# Enterprise patterns
Enterprise patterns sample code

## Simple factory
Simple application that illustrates Simpe factory design pattern.   
![Simple factory](/simple-factory/src/main/resourses/simple-factory-uml.png)

## Domain Logic Patterns

**Table Module**
A single instance that handles the business logic for all rows in a database table or view.

**Transaction Script**
Organizes business logic by procedures; each procedure handles a single request.
![Using commands for Transaction Script](/transaction-script/src/main/resources/revenue-recognition-transaction-script-uml.PNG)

## Data Source Architectural Patterns

**Active Record**
A simple, even simplistic, example to show how the bones of Active Record work.
A basic Person class with fields: id:int, firstName:String, lastName:String, email:String

**Data mapper**
Data Mapper for a person class.   
Person class fields:
- id: int
- firstName: String
- lastName: String
- email: String    

Tables:
- Persons (Id: INT, FIRST_NAME: STR, LAST_NAME: STR, MAIL_ID: FK - EMAIL)
- EMAILS (ID: INT, MAIL STR)
