package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.spring.batch.entity.Employee;
import com.spring.batch.processor.EmployeeProcessor;
import com.spring.batch.repository.EmployeeRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;//to created object of job
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;//to create object of step
	
	@Autowired
	private EmployeeRepository empRepo;//repo for itemWritter.
	
	//step required itemReader, itemProcessor, itemWritter.
	
	//itemReader to read from file
	@Bean
	FlatFileItemReader<Employee> employeeFileReader(){
		FlatFileItemReader<Employee> itemReader= new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/employee.csv"));//provide file to reader
		itemReader.setName("employeeReader");//give any name
		itemReader.setLinesToSkip(1);//skip first row, as first row is header & don't want to store it in db.
		itemReader.setLineMapper(lineMapper());//how to read & map data from file to object is defined in lineMapper
		return itemReader;
	}

	//how to read & map data from file to object is defined in lineMapper
	private LineMapper<Employee> lineMapper() {
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer delimiter = new DelimitedLineTokenizer();//how to read
		delimiter.setDelimiter(",");
		delimiter.setStrict(false);
		delimiter.setNames("id","firstname","lastname","email","age","city","gender");
		
		BeanWrapperFieldSetMapper<Employee> beanMapper = new BeanWrapperFieldSetMapper<>();//how to map csv data to object
		beanMapper.setTargetType(Employee.class);
		
		//provide both lineTokenizer & FieldSetMapper to lineMapper.
		lineMapper.setLineTokenizer(delimiter);
		lineMapper.setFieldSetMapper(beanMapper);
		
		return lineMapper;
	}
	
	//itemProcessor
	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Employee> employeeWritter(){
		RepositoryItemWriter<Employee> empWritter = new RepositoryItemWriter<>();
		empWritter.setRepository(empRepo);
		empWritter.setMethodName("save");
		return empWritter;
	}
	
	//create object of step & provide itemReader, itemProcessor & itemWritter to step
	@Bean
	public Step empStep() {
		return stepBuilderFactory.get("empStep").<Employee,Employee>//any name
				chunk(10)
				.reader(employeeFileReader())
				.processor(employeeProcessor())
				.writer(employeeWritter())
				.build();
	}
	
	//create object of job & provide steps to it. job can have multiple steps
	@Bean
	public Job employeeCsvtoDbJob() {
		return jobBuilderFactory.get("employeeCsvtoDbJob")
				.flow(empStep()).end().build();
	}
	
}
