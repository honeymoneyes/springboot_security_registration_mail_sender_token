# The application implements authorization with a token with confirmation by mail

# Technologies Used:
+ Spring Boot
+ Spring Data JPA
+ Spring Security
+ PostgreSQL Database
+ Docker
+ Lombok
+ Mail Sender
  
# Configuration Steps:

### Clone the Application:

Clone the repository using the following command:

```
git clone https://github.com/honeymoneyes/registration_mail_sender_security
```

### Create a PostgreSQL Database:
```
Create a new PostgreSQL database named "registration".
```

### Configure PostgreSQL Username and Password:
Open the file server/src/main/resources/application.yml and update the following properties with your PostgreSQL installation details:

```
spring.datasource.username=YOUR_POSTGRESQL_USERNAME
spring.datasource.password=YOUR_POSTGRESQL_PASSWORD
```

### Run docker container with virtual email:
```
$ docker run -p 1080:1080 -p 1025:1025 maildev/maildev
```

### Open the link with the mail client in the browser:
```
http://localhost:1080/
```
### Run the Application Using Maven:
Open a terminal, navigate to the project root directory (registration), and run the following command:

```
mvn spring-boot:run
```

The application will start running at http://localhost:8080.

### Send a post request:
![ImageAlt](https://github.com/honeymoneyes/registration_mail_sender_security/blob/master/src/main/resources/post.png)

### Check your mailbox: 
Go to address: http://localhost:1080/

Click Activate now and you will get access to the system thanks to the verified token

![ImageAlt](https://github.com/honeymoneyes/registration_mail_sender_security/blob/master/src/main/resources/response.png)
