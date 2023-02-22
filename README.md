# webapp


CSYE6225 : Assignment 04 Cloud Native Application with Basic Auth


# ORM
Hibernate

# Installation
Clone the repository to your local machine.
Install maven on your local machine.

# Setting up AWS
Configure your AWS credentials in github repository to automatically build your AMI from github actions

# Build AMI automatically with your java application
Merge the code to your organization repo and AMI build will automatically start

# Run the cloud formation template 
Run the cloud formation template in the infrastructure repository with new amiid created.

# API endpoints
 Open a terminal and run "curl -v http://localhost:8080/healthz" to get a status code 200.

# PostMan
POST Method
http://localhost:8080/v1/account 
and JSON format

    {
    "firstName": "",
    "lastName": "",
    "email": "",
    "password": ""
}

GET METHOD:
http://localhost:8080/v1/account/{id}
with Basic Authentication - Give your UserName and Password

PUT METHOD:
http://localhost:8080/v1/account/{id}
with BBasic Authentication - Give your UserName and Password
and data in JSON format
    {
        "firstName": "",
        "lastName": "",
        "email": "",
        "password": ""

    }


 


