# transfer-rest
A RESTful API (including data model and the backing implementation) for money  transfers between internal users/accounts.

**HOWTO:**

A simple test can be look like this.

`cd transfer-rest-api`

`mvn clean package`

`cd target/`

`java -jar transfer-rest-api-1.0-SNAPSHOT-jar-with-dependencies.jar`
 
 Wait for line _INFO: Starting ProtocolHandler ["http-bio-8080"]_
 
 Now server is up and running. 
 
 So far so good. Let's go further and create first user.
 ```
 POST http://localhost:8080/transfer/v1/rest/users
 {"name":"Pavel Durov"}
 ```
 In response header _Location_ we can find an ID for newly created user:
 `Location:http://localhost:8080/transfer/v1/rest/users/5`
 
 Let's create his first account:
 `POST http://localhost:8080/transfer/v1/rest/users/5/accounts`
 
 And again, an ID of this account is in _Location_ header:
 `Location:http://localhost:8080/transfer/v1/rest/users/5/accounts/5`
 
 Ok. Now we have one user. Let's create a buddy for him with an account:
 ```
POST http://localhost:8080/transfer/v1/rest/users
{"name":"Mark Zuckerberg"}

POST http://localhost:8080/transfer/v1/rest/users/6/accounts
 ```
 
 Here we go. We've got two users with accounts. Now Mark can thank Pavel for beer:
 ```
 POST http://localhost:8080/transfer/v1/rest/users/6/accounts/6/transactions
 {"toAccountId":"5","amount":100,"comment":"Thanks for beer!"}
 ```
 
 And then both can see this transaction in their lists:
 
 ```
 http://localhost:8080/transfer/v1/rest/users/5/accounts/5/transactions
 [
     {
         "id": 1,
         "fromAccountId": 6,
         "toAccountId": 5,
         "amount": 100,
         "comment": "Thanks for beer!"
     }
 ]
 
 http://localhost:8080/transfer/v1/rest/users/6/accounts/6/transactions
 [
      {
          "id": 1,
          "fromAccountId": 6,
          "toAccountId": 5,
          "amount": 100,
          "comment": "Thanks for beer!"
      }
  ]
 ```
 
 That's it!