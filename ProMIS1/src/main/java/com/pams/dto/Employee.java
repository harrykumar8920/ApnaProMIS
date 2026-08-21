package com.pams.dto;

import lombok.Data;

@Data
public class Employee {
private int id;
private String name;
private String department;
private String city ;
private double salary;
private String gender;
private int yearOfJoining;
private int age;
public Employee(int id, String name, int age, String gender, String department, int yearOfJoining, double salary) {
	this.id = id;
	this.name = name;
	this.age = age;
	this.gender = gender;
	this.department = department;
	this.yearOfJoining = yearOfJoining;
	this.salary = salary;
}

public Employee() {
	// TODO Auto-generated constructor stub
}



}
