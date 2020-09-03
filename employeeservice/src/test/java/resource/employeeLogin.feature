
# In this Test case we Login with a particular employee and fetch API-KEY and then we perform Logout functionality.



@employeeLogin
Feature: Employee Details

Scenario: employee login

#Login : employee login

 Given if Admin already exists in the employee portal
|username|employee1|
|password|welcome  |
Then Verify the Status code "200"


#Logout: Employee Logout

  

  #Then Enter Username and Password
  
  #|username|employee1|
	#|password|welcome  |
  
  #When Username and Password match
  #Then Session is succcessfully deleted