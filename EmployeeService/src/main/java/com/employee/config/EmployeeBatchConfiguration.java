package com.employee.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.employee.model.Employee;

@Configuration
@EnableBatchProcessing
public class EmployeeBatchConfiguration {

	@Autowired
	private JobBuilderFactory jbf;
	 
	@Autowired
	private StepBuilderFactory sbf;
	
	@Autowired
	private DataSource datasource;
	
	private static final Logger logger=LoggerFactory.getLogger(EmployeeBatchConfiguration.class);
	
	@Bean
	public LineMapper<Employee> lineMapper(){
		logger.debug("Enter lineMapper() method");
		DefaultLineMapper<Employee> linemapper=new DefaultLineMapper<>();
		DelimitedLineTokenizer linetokenizer=new DelimitedLineTokenizer();
		BeanWrapperFieldSetMapper<Employee> bfsMapper=new BeanWrapperFieldSetMapper<>();
		linetokenizer.setNames(new String[] {"eid","ename","email","phoneNumber","designation","experience"});
		linetokenizer.setIncludedFields(new int[] {0,1,2,3,4,5});
		bfsMapper.setTargetType(Employee.class);
		linemapper.setLineTokenizer(linetokenizer);//set lineTokenizer----
		linemapper.setFieldSetMapper(bfsMapper); //set bfsMapper--- to the linemapper
		logger.debug("Exit lineMapper() method");
		return linemapper;
	}
	
	@Bean
	public FlatFileItemReader<Employee> reader(){
		FlatFileItemReader<Employee> itemReader=new FlatFileItemReader<>();
		itemReader.setResource(new ClassPathResource("employlist.csv"));//employlist.csv
		//itemReader.setResource(new url);
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}
	
	@Bean
	public JdbcBatchItemWriter<Employee> writer(){
		logger.debug("Enter JdbcBatchItemWriter method");
		JdbcBatchItemWriter<Employee> itemWriter=new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(datasource);
		itemWriter.setSql("insert into employee (eid,ename,email,phonenumber,designation,experience) values(:eid,:ename,:email,:phonenumber,:designation,:experience)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		logger.debug("Exit JdbcBatchItemWriter method");
		return itemWriter;
	}
	
	@Bean
	public Step step1() {
		return sbf.get("step1").<Employee,Employee>chunk(10)
				.reader(reader())
				//.processor(function)--for logs enable then work
				.writer(writer()).build();
	}
	
	@Bean
	public Job readvendorcsvfile() {
		return jbf.get("readempBulkcsvfile").incrementer(new RunIdIncrementer()).start(step1()).build();
	}
	
	
}
