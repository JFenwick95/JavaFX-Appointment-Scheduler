# C195 - Performance Assessment
## Appointment Scheduler
### Jeff Fenwick

The title of the application is Appointment Scheduler.

The purpose of the application is to schedule appointments for customers and maintain records of all customers and appointments.
This application is designed to be used in an administrative capacity with active alerts for upcoming appointments.

Author: Jeff Fenwick
email: jfenwi4@wgu.edu
Application used: IntelliJ IDEA 2022.1.4 (Community Edition)
Date: 01/10/2023

JDK Version: 17.0.4
JavaFX SDK Version Used: 19 and 17.0.5
*The path to JFX is specified in the path variable "PATH_TO_FX".*

The program can be run from an IDE (preferably IntelliJ).

The additional report I chose to include is a report that generates the total amount of appointments created by each user.
I figured that this would be helpful in determining the which users make the most appointments to provide bonuses to employees.

To build the .jar, when you add the artifact, also add all files in the jfx \bin folder.
Make sure to include the login activity .txt file in the same folder as the .jar file.

MySQL Workbench 8.0 CE
*Needed to simulate the virtual environment that evaluators will use.*
- Create user account
- Login name: sqlUser
- Caching-sha2-password: passw0rd!
- localhost
- Administrative role: DBA
- New database connection: C195DBClient
- Create database: client_schedule
- Run DDL script in folder "Establishing a Client-Side Database"
- Run DML script in same folder


MySQL Server 8.0
MySQL connector driver version: mysql-connector-j-8.0.31