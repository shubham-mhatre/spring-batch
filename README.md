# spring-batch
process csv file to mysql database using spring batch.
For CSV to DB.
1.added csv file in src/main/resources.
2.also added mysql database script in src/main/resources
3.to process csv and save data in db, call GET : "/csvToDb". you use implemented swagger api also. access swagger "http://localhost:8080/swagger-ui/#/" after launching the springboot application.
4.all the spring config resides in package: com.spring.batch.config , class : SpringBatchConfig.java

![image](https://user-images.githubusercontent.com/55918816/224928899-0bda1c86-d122-4d8a-9380-7c3396e60ab2.png)
![image](https://user-images.githubusercontent.com/55918816/224929189-661947cb-6b48-4fe0-ab6f-bb6109a562f9.png)

check your local database after the process completed. you should see csv data inserted in employee table
