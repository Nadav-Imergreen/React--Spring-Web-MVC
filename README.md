## Authors
* Name: Nadav Imergreen
  Email: nadavyi@edu.hac.ac.il
* Name: Moshe Namdar
  Email: moshena@edu.hac.ac.il

## General Description
The Users Management is a web application that provides functionality for users to register, login, and manage their user details. 
It also includes admin functionality to monitor and manage user activity on the website.

## Functionality Details

### User:
Registration:
Users can create an account by providing their email address and password. Upon successful registration, users gain access to additional features.

Login:
Registered users can log in using their email address and password. After logging in, users can access their personalized dashboard and perform various actions.

Dashboard:
Once logged in, the dashboard displays user-specific information, including user details and the user's last visit. Users also have the option to change their account password. To change the password, users need to provide their current password and enter a new password. The new password is securely stored in the system.

### Admin:
Login:
Admins can log in using their hard-coded email address and password. 

Dashboard:
Admins have the ability to manage user accounts. They can view a list of all registered users and their visits on the web. Admins can also delete user profiles, along with their associated visits.

### General Information
The program will create two tables: the User table and the Visit table. These tables are related to each other through a One-to-Many relationship.

The User table will store user information, including their email address, password, and other relevant details. Each user can have multiple visits associated with their account.

The Visit table will store visit information, such as the date and time of the visit. Each visit is linked to a specific user through a foreign key relationship.

### Installation
1. Clone the repository to your local machine.
2. If you are using a Windows system, remove the 'root' keyword from the 'spring.datasource.password' field in the application properties file.
3. Set up the 'ex5' database folder using MAMP or any other suitable database management tool.


### Useful Information
Admin details are hard-coded in the UserService file:
ADMIN_USERNAME = "admin";
ADMIN_EMAIL = "admin@gmail.com";
ADMIN_PASSWORD = "1234";
