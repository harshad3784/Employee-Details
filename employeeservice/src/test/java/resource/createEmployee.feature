

#In this Test case we are creating a new employee record and then we fetch the same record and then we make changes in the 
# record and than finally we delete the record.



Feature: Employee Details


@createemployee
Scenario: 
	# Create Employee:new employee record is created
	
Given if Admin already exists in the employee portal
|username|employee1|
|password|welcome  |


	When Admin wants to add a new employee by providing employee details using dataTable
		| id 							| 0 									|
		| employeename 		| harshad3784						|
		| firstName 			| Test1 							| 
		| lastName 				| Ltest1 							|
		| email 					| Harshadtest@123.com |
		| password 				| test1 							|
		| phone 					| 0612345678 					|
		| employeeStatus 	| 1 									|
		
		
		
		
		
	#Get Employee by Employee Name : Employee details are fetched based on employeename
	
Then Verify the Status code "200"
When if Admin wants to fetch a new employee details
		| employeename 		| harshadtest3784				|
		
		
		
		
	
	# Update Employee Details: Employee details are updated
	
Then Employee details should be populated 
When Employee updates the values 
 		| employeename 		| harshadtest3784 	 	|
		| email 	    		| jhon@gmail.com		| 
		| phone 					| 123456789 		|	
Then Employee details should be populated 






	#Delete Employee : employee details are removed based on employeename
	
Then Employee details should be deleted 
		| employeename 		|  harshadtest3784	|
And  Verify the Status code "200"



	
	
		
	
		


	
	
	
	
