package com.pams.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.pams.dto.Employee;

public class TestController {
	static List<Employee> emplist =  new ArrayList<Employee>();
	
	
	 
	 
	public static void main(String args[]) {
		
		
	
	 char ch = findFirstNonRepeatableChar("GouthamiSuchitra");
		 createEmpList(emplist);
		 //How many male and female employees are there in the organization?
		// method1();
		 
		// departmentList();
		 
		// method3();
		// method4();
		 evenNumber();
	}

	  private static void evenNumber() {
		
		List<Integer> number = Arrays.asList(10,45,26,8,99);
		number.stream().filter(n->n%2==0).forEach(System.out::println);
		number.stream().filter(n->n%2==0).forEach(System.out::println);
		
	}

	public static Character findFirstNonRepeatableChar(String str) {
	        Map<Character,Integer> map = new LinkedHashMap();
	        for (Character ch : str.toCharArray()) {
	            map.put(ch, map.containsKey(ch) ? map.get(ch) + 1 : 1);
	        }
	   
	        System.out.println(map);
	        return map.entrySet().stream().filter(x -> x.getValue() == 1).findFirst().get().getKey();
	} 

	private static void method3() {
		// TODO Auto-generated method stub
		emplist.stream().filter(e->e.getGender()=="Male").forEach(System.out::println);
	//	Map<String,List<Employee>> emplist1 =  emplist.stream().filter(e->e.getGender()=="Male").collect(Collectors.groupingBy(Employee::getName));
	     
///		emplist1.forEach((key,value)->System.out.println("department "+key+" employee"+value));
		
		/*
		 * Set<Entry<String, List<Employee>>> entrySet = emplist1.entrySet();
		 * 
		 * for(Entry<String,List<Employee>> entry:entrySet) {
		 * System.out.println("--------------------------------------");
		 * 
		 * System.out.println("Employees In "+entry.getKey() + " : ");
		 * 
		 * System.out.println("--------------------------------------");
		 * 
		 * List<Employee> list = entry.getValue();
		 * 
		 * for (Employee e : list) { System.out.println(e.getName()); } }
		 */
		//	System.out.println(emplist1);
        
	
	}

	private static void departmentList() {
		emplist.stream().map(Employee::getDepartment).distinct().forEach(System.out::println);
		
		Map<String ,List<Employee>> departmentwiselist = emplist.stream().collect(Collectors.groupingBy(Employee::getDepartment));
		
		//departmentwiselist.forEach((key,value)->System.out.println("depatment "+key+" employee"+value));
		
		
		 Map<String, Long> employeeCountByDepartment =
				 emplist.stream()
		             .collect(Collectors.groupingBy(Employee::getDepartment, 
		                TreeMap::new, Collectors.counting()));
		      employeeCountByDepartment.forEach(
		         (department, count) -> System.out.printf(
		            "%s has %d employee(s)%n", department, count));
		
	
	}

	private static void method1() {
		Map<String,Long> noOfmaleFemaleEmployee = emplist.stream().collect(Collectors.groupingBy(Employee::getGender,TreeMap::new,Collectors.counting()));
		
		
		Map<String,List<Employee>> noofemployegenderBase = emplist.stream().collect(Collectors.groupingByConcurrent(Employee::getGender));
		noOfmaleFemaleEmployee.forEach(
			         (gender, count) -> System.out.printf(
			            "%s has %d employee(s)%n", gender, count));
		
		
		Set<Entry<String, List<Employee>>> entrySet = noofemployegenderBase.entrySet();
        
		for (Entry<String, List<Employee>> entry : entrySet) 
		{
		    System.out.println("--------------------------------------");
		             
		    System.out.println("Employees In "+entry.getKey() + " : ");
		             
		    System.out.println("--------------------------------------");
		             
		    List<Employee> list = entry.getValue();
		             
		    for (Employee e : list) 
		    {
		        System.out.println(e.getName());
		    }
		}
		System.out.println(noOfmaleFemaleEmployee);
	}

	

	private static void createEmpList(List<Employee> 
	employeeList) {
		
		
		employeeList.add(new Employee(111, "Jiya Brein", 32, "Female", "HR", 2011, 25000.0));
		employeeList.add(new Employee(122, "Paul Niksui", 25, "Male", "Sales And Marketing", 2015, 13500.0));
		employeeList.add(new Employee(133, "Martin Theron", 29, "Male", "Infrastructure", 2012, 18000.0));
		employeeList.add(new Employee(144, "Murali Gowda", 28, "Male", "Product Development", 2014, 32500.0));
		employeeList.add(new Employee(155, "Nima Roy", 27, "Female", "HR", 2013, 22700.0));
		employeeList.add(new Employee(166, "Iqbal Hussain", 43, "Male", "Security And Transport", 2016, 10500.0));
		employeeList.add(new Employee(177, "Manu Sharma", 35, "Male", "Account And Finance", 2010, 27000.0));
		employeeList.add(new Employee(188, "Wang Liu", 31, "Male", "Product Development", 2015, 34500.0));
		employeeList.add(new Employee(199, "Amelia Zoe", 24, "Female", "Sales And Marketing", 2016, 11500.0));
		employeeList.add(new Employee(200, "Jaden Dough", 38, "Male", "Security And Transport", 2015, 11000.5));
		employeeList.add(new Employee(211, "Jasna Kaur", 27, "Female", "Infrastructure", 2014, 15700.0));
		employeeList.add(new Employee(222, "Nitin Joshi", 25, "Male", "Product Development", 2016, 28200.0));
		employeeList.add(new Employee(233, "Jyothi Reddy", 27, "Female", "Account And Finance", 2013, 21300.0));
		employeeList.add(new Employee(244, "Nicolus Den", 24, "Male", "Sales And Marketing", 2017, 10700.5));
		employeeList.add(new Employee(255, "Ali Baig", 23, "Male", "Infrastructure", 2018, 12700.0));
		employeeList.add(new Employee(266, "Sanvi Pandey", 26, "Female", "Product Development", 2015, 28900.0));
		employeeList.add(new Employee(277, "Anuj Chettiar", 31, "Male", "Product Development", 2012, 35700.0));
		
	}
	
	
	
	
}
