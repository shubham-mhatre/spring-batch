package com.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.entity.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		// no logic in processor rn, just returning same incoming object.
		//but here we can write filtering, validation and etc internal processing logic
		return item;
	}

}
