package stepDefinitions;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps {

	private static String EmpIDKey;
	private  int Response;
	private  String ResponseBody;


	@When("^Admin wants to add a new employee by providing employee details using dataTable$")
	public void admin_wants_to_add_a_new_employee_by_providing_employee_details_using_dataTable(DataTable arg1)
			throws Throwable {
		List<List<String>> data = arg1.raw();

		String id = data.get(0).get(1);
		String employeename = data.get(1).get(1);
		String firstName = data.get(2).get(1);
		String lastName = data.get(3).get(1);
		String email = data.get(4).get(1);
		String password = data.get(5).get(1);
		String phone = data.get(6).get(1);
		String employeeStatus = data.get(7).get(1);

		System.out.println("id " + id);

		String key = EmpIDKey;

		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2/employee";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json").header("X-Api-Key", key);

		JSONObject requestParams = new JSONObject();
		requestParams.put("employeename", employeename);
		requestParams.put("firstName", firstName);
		requestParams.put("lastName", lastName);
		requestParams.put("email", email);
		requestParams.put("password", password);
		requestParams.put("phone", phone);
		requestParams.put("employeeStatus", employeeStatus);

		request.body(requestParams.toString());
		Response response = request.post();

		System.out.println("Create Employee =" + response.asString());

		Response = response.getStatusCode();

	}

	@Given("^if Admin already exists in the employee portal$")
	public void if_Admin_already_exists_in_the_employee_portal(DataTable arg1) throws Throwable {
		List<List<String>> data = arg1.raw();
		String username = data.get(0).get(1);
		String password = data.get(1).get(1);
		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.get("/employee/login?employeename="+username+"&password="+password+"");
		Assert.assertEquals(response.getStatusCode(), 200);
		EmpIDKey = response.asString();

	}

	
	public void if_Admin_already_exists_in_the_employee_portal() throws Throwable {
		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.get("/employee/login?employeename=employee1&password=welcome");
		Assert.assertEquals(response.getStatusCode(), 200);
		EmpIDKey = response.asString();

	}

	
	
	@Then("^Verify the Status code \"([^\"]*)\"$")
	public void verify_the_Status_code(String expectedstatuscode) throws Throwable {

		System.out.println("Status Code =" + Integer.parseInt(expectedstatuscode));
		int response = Response;
		 Assert.assertEquals(response, Integer.parseInt(expectedstatuscode));

	}
	

	    
	

	@When("^Admin wants to add a Multiple employees by providing employee details using dataTable$")
	public void admin_wants_to_add_a_Multiple_employees_by_providing_employee_details_using_dataTable(DataTable arg1)
			throws Throwable {

		List<List<String>> data = arg1.raw();

		String id = data.get(0).get(1);
		String employeename = data.get(1).get(1);
		String firstName = data.get(2).get(1);
		String lastName = data.get(3).get(1);
		String email = data.get(4).get(1);
		String password = data.get(5).get(1);
		String phone = data.get(6).get(1);
		String employeeStatus = data.get(7).get(1);

		System.out.println("id " + id);

		String key = EmpIDKey;

		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2/employee";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json").header("X-Api-Key", key);

		JSONObject requestParams = new JSONObject();
		requestParams.put("employeename", employeename);
		requestParams.put("firstName", firstName);
		requestParams.put("lastName", lastName);
		requestParams.put("email", email);
		requestParams.put("password", password);
		requestParams.put("phone", phone);
		requestParams.put("employeeStatus", employeeStatus);

		request.body(requestParams.toString());
		Response response = request.post();

		System.out.println("Create Employee =" + response.asString());

		Response = response.getStatusCode();

	}

	/*
	 * public String getEmpIDKey() { return EmpIDKey; }
	 * 
	 * public void setEmpIDKey(String empIDKey) { EmpIDKey = empIDKey; }
	 * 
	 * 
	 * public int getResponse() { return Response; }
	 * 
	 * public void setResponse(int response) { Response = response; }
	 * 
	 * private int Response;
	 */

	@When("^if Admin wants to fetch a new employee details$")
	public void if_Admin_wants_to_fetch_a_new_employee_details(DataTable arg1) throws Throwable {
		List<List<String>> data = arg1.raw();

		String employeename = data.get(0).get(1);
		String key = EmpIDKey;

		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json").header("X-Api-Key", key);
		Response response = request.get("/employee/employee1?employeename=" + employeename + "");
		
		ResponseBody =response.asString();

	}

	@Then("^Employee details should be populated$")
	public void employee_details_should_be_populated() throws Throwable {
		System.out.println("Employee details fetch  " + ResponseBody);
		
		

	}

	@When("^Employee updates the values$")
	public void employee_updates_the_values(DataTable arg1) throws Throwable {
		List<List<String>> data = arg1.raw();

		String employeename = data.get(0).get(1);
		String email = data.get(1).get(1);
		String phone = data.get(2).get(1);

		String key = EmpIDKey;
		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2/employee";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json").header("X-Api-Key", key);

		JSONObject requestParams = new JSONObject();
		requestParams.put("employeename", employeename);
		requestParams.put("email", email);
		requestParams.put("phone", phone);

		request.body(requestParams.toString());
		Response response = request.put();

		System.out.println("Create Employee =" + response.asString());

		//Response = response.getStatusCode();
		ResponseBody =response.asString();

		

	}
	
	@Then("^Employee details should be deleted$")
	public void employee_details_should_be_deleted(DataTable arg1) throws Throwable {
		List<List<String>> data = arg1.raw();
		String employeename = data.get(0).get(1);
		String key = EmpIDKey;
		RestAssured.baseURI = "http://localhost:8081/employeeservice/v2/employee";
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json").header("X-Api-Key", key);

		JSONObject requestParams = new JSONObject();
		requestParams.put("employeename", employeename);

		request.body(requestParams.toString());
		Response response = request.delete();

}}
