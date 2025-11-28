**Hobby Explorer – QA Portfolio Project**

A full end-to-end QA project showcasing manual testing, UI automation (Java + Selenium + TestNG), and product documentation for a real hobby discovery web application.


**📌 Project Overview**

Hobby Explorer is a web application that helps users discover new hobbies based on their interests, goals, and preferences. This project was created as part of my QA portfolio to demonstrate real-world testing skills, including manual testing, UI automation, defect reporting, and Agile documentation.

The project includes:

Fully written test cases and acceptance criteria

UI test automation using Java, Selenium WebDriver, and TestNG

Negative and positive login flows

Functional tests for genre selection, user interests, and navigation

Bug reports, user stories, and sprint-style documentation


**✅ Features & Test Coverage
🔍 Manual Testing**

Full test case suite (functional, UI, regression)

User story review + acceptance criteria

Bug reporting with clear reproduction steps

Exploratory testing of all major flows

Cross-browser smoke tests (Chrome + Safari)

🧪 Automated UI Testing (Selenium + Java + TestNG)

Automated tests included in this project:

Positive Login Test – verifies users can sign in with valid credentials

Negative Login Test – verifies login fails with incorrect password and displays a toast error

Genre Selection Test – iterates through all hobby genres and validates navigation

User Interest Selection Test – selects interests and validates recommended results

Multi-Interest Flow – tests selecting multiple interests, navigating between pages, and handling UI waits

📂 Test Architecture

Page Object Model (POM)

BasePage for reusable actions (click, type, wait)

BaseTest for WebDriver setup/teardown

Modular pages: HomePage, LoginPage, etc.

TestNG annotations and structured reports


**📄 Manual QA Documentation**

This project includes complete manual QA deliverables that mirror real-world Agile workflows:

📝 Test Cases

40+ detailed test cases covering login, home page, interests, genre selection, navigation, and recommendations

Organized in Qase.io (Test Management Tool)

Includes preconditions, steps, expected results, test data, and pass/fail results

👉 Link to test case suite: (add your Qase link here)
(If private, write “Available upon request.”)

🐛 Bug Reports

All defects include:

Steps to reproduce

Expected vs. actual result

Screenshots / videos

Severity & priority

Testing environment



📌 User Stories + Acceptance Criteria

Includes user stories you wrote for the project:

Login flow

Interest selection

Genre recommendation

Navigation

Bookmarking (future)

**
Project Structure**


HobbyExplorer/
│── src/
│   ├── main/java/com.hobbyexplorer.core/
│   │     ├── BasePage.java
│   │     ├── DriverFactory.java
│   │
│   └── main/java/com.hobbyexplorer.pages/
│         ├── HomePage.java
│         ├── LoginPage.java
│
│── src/test/java/com.hobbyexplorer.tests/
│         ├── BaseTest.java
│         ├── SignInTest.java
│         ├── GenreSelectionTest.java
│         ├── UserSelectInterestTest.java
│         ├── MultipleInterestSelectionTest.java
│
│── pom.xml
│── README.md



▶️ How to Run the Automated Tests

You can run the UI automation suite on any machine with Java and Maven installed.

🛠️ Prerequisites

Make sure you have:

Java 17+

Maven

Google Chrome (default browser used)

Git installed

Check versions:

java -version
mvn -version

📥 Clone the Repository
git clone https://github.com/YOUR-USERNAME/Hobby--Explorer.git
cd Hobby--Explorer


(Replace YOUR-USERNAME with your GitHub username.)

▶️ Run All Tests With Maven
mvn clean test


This will:

Launch WebDriver

Open your hobby explorer app

Run all TestNG tests

Show results in the terminal

🎯 Run a Single Test Class

You can run only one test if you want:

mvn -Dtest=SignInTest test


or

mvn -Dtest=GenreSelectionTest test

🧪 TestNG Reports

After running the suite, TestNG generates a full HTML report:

/target/surefire-reports/index.html


You can open it in a browser to see passed/failed tests.


🚀 Future Enhancements

This project is actively growing. Here are planned improvements to expand test coverage and technical depth:

🔧 Automation Enhancements

Add cross-browser testing (Firefox, Edge)

Add headless mode for CI/CD pipelines

Integrate with GitHub Actions for automated test runs

Add more UI flows (bookmarking, filtering, profile settings)

Expand negative test scenarios (invalid emails, empty fields, rate limits)

🧪 API Testing

Create full API test suite using Postman or RestAssured

Validate recommendations endpoint

Add contract testing + schema validation

Add mocked responses for edge cases

📱 Mobile Testing (Optional)

Create mobile tests using Appium (future roadmap)

Validate responsive UI behavior

📊 QA Documentation

Add test plan + test strategy

Add risk analysis + mitigation strategies

Add traceability matrix (user story ↔ test cases ↔ automation)

🎥 Optional Enhancements

Add screenshots or GIF demos of UI flows

Add badges (build passing, license, coverage, etc.)


