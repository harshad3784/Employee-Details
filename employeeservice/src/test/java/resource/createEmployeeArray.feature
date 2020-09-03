

Feature: Employee Details

@CreateemployeeArray
Scenario Outline: Create Employees Using Array
	Given if Admin already exists in the employee portal
	When Admin wants to add a Multiple employees by providing employee details using dataTable


		| id 							| 0 						|
		| employeename 		| Test4 				|
		| firstName 			| Test4 				| 
		| lastName 				| Ltest4 				|
		| email 					| test4@123.com |
		| password 				| test4 				|
		| phone 					| 0612345678 		|
		| employeeStatus 	| 1 						|
		
		| id 							| 0 						|
		| employeename 		| Test5 				|
		| firstName 			| Test5 				| 
		| lastName 				| Ltest5 				|
		| email 					| test5@123.com |
		| password 				| test5 				|
		| phone 					| 0612345678 		|
		| employeeStatus 	| 1 						|
		
		
		| id 							| 0 						|
		| employeename 		| Test6					|
		| firstName 			| Test6 				| 
		| lastName 				| Ltest6 				|
		| email 					| test6@123.com |
		| password 				| test6 				|
		| phone 					| 0612345678 		|
		| employeeStatus 	| 1 						|
		
		
	And Verify the Status code "200"
	
	Examples:
	
	|id   |employeename  |firstName  |lastName  |email  				|password  |phone   		 |employeeStatus  |
	|0  	|data1  			 |data1  		 |ldata1  	|data1@123.com  |data1  	 |0612345678   |1  							|
	|1  	|data2  			 |data2  		 |ldata2  	|data2@123.com  |data1  	 |0612345678   |1  							|
	|2   	|data3  			 |data3    	 |ldata3  	|data3@123.com  |data1  	 |0612345678   |1  							|
