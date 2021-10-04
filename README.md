###Setup
Git clone this project and install maven if you haven’t already using
```
brew install mvn
```
cd into the project folder 
```
cd java-any-uknppc/account-manager
```

Run the following command so that maven can run the unit tests, pull down all the dependencies, and compile the java project.
```
mvn package
```

Run the server
```
mvn exec:java -Dexec.mainClass="com.ydalal.accounts.App" -e
```

In a new tab, run the following `curl` commands to hit the server. Due to time constraints I did not implement accepting query params or a JSON request body for POST requests. If you would like to test the server against different inputs, you can update the App.java file, and rerun the package using the `mvn package` and `mvn exec:java -Dexec.mainClass="com.ydalal.accounts.App" -e`.

Create a new account.
```
curl -v -X POST localhost:8000/account/create 
```
Create a new account.
```
curl -v -X POST localhost:8000/account/create 
```
Transfer money from one account to another
```
curl -v -X POST localhost:8000/account/transfer 
```
Get current account balance
```
curl -v -X GET localhost:8000/account/balance 
```
Get current account transactions
```
curl -v -X GET localhost:8000/account/transactions 
```

The customers.json is required. The accounts.json and transactions.json files can be updated to test different scenarios.

####Extras:
* Dependency injection using guice
* Lombok to reduce boilerplate code
* AssertJ for more english readable tests

####Future improvements:
* Rather than throwing an IOException, it would be good to give the user a descriptive message with the chance to correct their input.
* FindBugs
* Checkstyle
* Logging using Log4J2
* For simplicity, account id generator is sequential and only generated integers. In a real production environment it would be better to generate a random string as id.
-----

### Objective

Your assignment is to build an internal API for a fake financial institution using Java and any framework.

### Brief

While modern banks have evolved to serve a plethora of functions, at their core, banks must provide certain basic features. Today, your task is to build the basic HTTP API for one of those banks! Imagine you are designing a backend API for bank employees. It could ultimately be consumed by multiple frontends (web, iOS, Android etc).

### Tasks

- Implement assignment using:
  - Language: **Java**
  - Framework: **any framework**
- There should be API routes that allow them to:
  - Create a new bank account for a customer, with an initial deposit amount. A
    single customer may have multiple bank accounts.
  - Transfer amounts between any two accounts, including those owned by
    different customers.
  - Retrieve balances for a given account.
  - Retrieve transfer history for a given account.
- Write tests for your business logic

Feel free to pre-populate your customers with the following:

```json
[
  {
    "id": 1,
    "name": "Arisha Barron"
  },
  {
    "id": 2,
    "name": "Branden Gibson"
  },
  {
    "id": 3,
    "name": "Rhonda Church"
  },
  {
    "id": 4,
    "name": "Georgina Hazel"
  }
]
```

You are expected to design any other required models and routes for your API.

### Evaluation Criteria

- **Java** best practices
- Completeness: did you complete the features?
- Correctness: does the functionality act in sensible, thought-out ways?
- Maintainability: is it written in a clean, maintainable way?
- Testing: is the system adequately tested?
- Documentation: is the API well-documented?

### CodeSubmit

Please organize, design, test and document your code as if it were going into production - then push your changes to the master branch. After you have pushed your code, you may submit the assignment on the assignment page.

All the best and happy coding,

The Atlys Team