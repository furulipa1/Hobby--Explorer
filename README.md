**Hobby Explorer – QA Portfolio Project**

A complete end-to-end QA project demonstrating manual testing, UI automation (Java + Selenium + TestNG), API testing prep, bug reporting, and Agile-style documentation for a real hobby discovery application.


📌 Project Overview

Hobby Explorer is a web app that helps users discover hobbies based on their interests.
This project demonstrates full QA lifecycle coverage, including:

- 40+ manual test cases

- UI automation using Selenium WebDriver (Java + TestNG + POM)

- Negative + positive login tests

- User interest & genre recommendation flow testing

- Bug reports with steps to reproduce

- User stories with acceptance criteria

- Sprint-style documentation



✅ Features & Test Coverage


🔍 Manual Testing

- Full test case suite (functional, UI, regression)

- User story review + acceptance criteria

- Bug reporting with clear reproduction steps

- Exploratory testing of all major flows

- Cross-browser smoke tests (Chrome + Safari)



  🧪 Automated UI Testing (Selenium + Java + TestNG)

- Automated tests included in this project:

- Positive Login Test – verifies valid login

- Negative Login Test – verifies invalid login shows error toast

- Genre Selection Test – loops through all genres + validates navigation

- User Interest Selection Test – selects interests + validates recommended results

- Multi-Interest Flow – selects multiple interests, handles waits, verifies navigation



📂 Test Architecture

- Page Object Model (POM)

- BasePage for reusable actions (click, type, wait)

- BaseTest for WebDriver setup/teardown

- Modular pages (HomePage, LoginPage, etc.)

- TestNG annotations + structured reporting



📄 Manual QA Documentation


📝 Test Cases

- 40+ detailed test cases covering login, home page, interests, genre selection, navigation, and recommendations

- Organized in Qase.io (Test Management Tool)

- Includes preconditions, steps, expected results, test data, pass/fail



🐛 Bug Reports

All defects include:

- Steps to reproduce

- Expected vs. actual results

- Screenshots / videos

- Severity & priority

- Testing environment



📌 User Stories + Acceptance Criteria

Includes user stories for:

- Login flow

- Interest selection

- Genre recommendation

- Navigation


## 📁 Project Structure

```
HobbyExplorer/
│── src/
│   ├── main/java/com.hobbyexplorer.core/
│   │    ├── BasePage.java
│   │    ├── DriverFactory.java
│   ├── main/java/com.hobbyexplorer.pages/
│   │    ├── HomePage.java
│   │    ├── LoginPage.java
│
│── src/test/java/com.hobbyexplorer.tests/
│   ├── BaseTest.java
│   ├── SignInTest.java
│   ├── GenreSelectionTest.java
│   ├── UserSelectInterestTest.java
│   ├── MultipleInterestSelectionTest.java
│
│── pom.xml
│── README.md
```




▶️ How to Run the Automated Tests

You can run the UI automation suite on any machine with Java and Maven installed.

🛠️ Prerequisites

Java 17+

Maven

Chrome

Git

Check your versions:

java -version
mvn -version

📥 Clone the Repository
git clone https://github.com/YOUR-USERNAME/Hobby--Explorer.git
cd Hobby--Explorer

▶️ Run All Tests with Maven
mvn clean test

🎯 Run a Single Test Class
mvn -Dtest=SignInTest test


or

mvn -Dtest=GenreSelectionTest test

🧪 TestNG Reports

HTML report generated at:

/target/surefire-reports/index.html



🚀 Future Enhancements
🔧 Automation Enhancements

- Add cross-browser testing (Firefox, Edge)

- Enable headless mode for CI/CD

- Add GitHub Actions pipeline

- Add more UI flows (bookmarking, filtering, profile settings)

- Expand negative scenarios (invalid email, empty fields)



🧪 API Testing

- Build Postman/RestAssured API suite

- Validate recommendations endpoint

- Add contract testing / schema validation

- Mocked edge-case responses



📱 Mobile Testing

- Appium mobile flows

- Responsive UI tests



📊 QA Documentation

- Add test plan + test strategy

- Add risk analysis

- Add traceability matrix

🎥 Optional Enhancements

- Add screenshots or GIFs

- Add build/test badges
