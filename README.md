**Hobby Explorer – QA Portfolio Project**

A complete end-to-end QA project demonstrating manual testing, UI automation (Java + Selenium + TestNG), API testing prep, bug reporting, and Agile-style documentation for a real hobby discovery application.


## 🧠 Why This Project Matters

Hobby Explorer was built to demonstrate real-world QA skills across the full testing lifecycle.  
This project shows that I can design, execute, and automate tests for a real application—not just follow tutorials.

### 🔍 Skills Demonstrated
- **Functional testing** (login, navigation, interest selection, recommendations)
- **Negative testing** (invalid login, invalid inputs)
- **Automated UI testing** with Selenium WebDriver (Java + TestNG + POM)
- **Page Object Model design** (clean, reusable test architecture)
- **Bug reporting** with clear repro steps, expected vs actual, and severity
- **Test case writing** in Qase.io (40+ manual test cases)
- **Agile-style documentation**: user stories, acceptance criteria, DoD
- **Version control**: Git + GitHub workflow



**📌 Project Overview**

Hobby Explorer is a web app that helps users discover hobbies based on their interests.
This project demonstrates full QA lifecycle coverage, including:

- 40+ manual test cases

- UI automation using Selenium WebDriver (Java + TestNG + POM)

- Negative + positive login tests

- User interest & genre recommendation flow testing

- Bug reports with steps to reproduce

- User stories with acceptance criteria

- Sprint-style documentation



**✅ Features & Test Coverage**


🔍 Manual Testing

- Full test case suite (functional, UI, regression)

- User story review + acceptance criteria

- Bug reporting with clear reproduction steps

- Exploratory testing of all major flows

- Cross-browser smoke tests (Chrome + Safari)



**🧪 Automated UI Testing (Selenium + Java + TestNG)**

- Automated tests included in this project:

- Positive Login Test – verifies valid login

- Negative Login Test – verifies invalid login shows error toast

- Genre Selection Test – loops through all genres + validates navigation

- User Interest Selection Test – selects interests + validates recommended results

- Multi-Interest Flow – selects multiple interests, handles waits, verifies navigation



**📂 Test Architecture**

- Page Object Model (POM)

- BasePage for reusable actions (click, type, wait)

- BaseTest for WebDriver setup/teardown

- Modular pages (HomePage, LoginPage, etc.)

- TestNG annotations + structured reporting



**📄 Manual QA Documentation**


📝 Test Cases

- 40+ detailed test cases covering login, home page, interests, genre selection, navigation, and recommendations

- Organized in Qase.io (Test Management Tool)

- Includes preconditions, steps, expected results, test data, pass/fail



**🐛 Bug Reports**

All defects include:

- Steps to reproduce

- Expected vs. actual results

- Screenshots / videos

- Severity & priority

- Testing environment



**📌 User Stories + Acceptance Criteria**

Includes user stories for:

- Login flow

- Interest selection

- Genre recommendation

- Navigation


## 📁 Project Structure (POM Architecture)


A clean Page Object Model structure with separated tests, pages, core utilities, and driver setup.


## 📸 Automation Screenshots

### 📁 Project Structure (POM)

<img width="372" height="300" alt="Screenshot 2025-11-30 at 1 51 56 PM" src="https://github.com/user-attachments/assets/7ef80b50-4156-494e-b9d7-8eb889875df2" />

<br><br>

### 🧪 Test Execution Results (SignIn Test)

<img width="372" height="400" alt="Screenshot 2025-11-30 at 1 54 23 PM" src="https://github.com/user-attachments/assets/bf033b88-3a89-42e9-acfa-f4faa151e729" />


<br><br>

### 🔐 SignIn Flow – Negative Login (Automated Browser Interaction)

<img width="372" height="400" alt="Screenshot 2025-11-30 at 2 31 11 PM" src="https://github.com/user-attachments/assets/26873fb9-11d2-4f5e-aa83-134c7b03c96b" />


<br><br>

### 🎯 Interest Selection Flow – Automated Browser Interaction

<img width="372" height="400" alt="Screenshot 2025-11-30 at 2 27 43 PM" src="https://github.com/user-attachments/assets/385645fd-30a7-4b5a-a954-4306fe65cce6" />


**▶️ How to Run the Automated Tests**

You can run the UI automation suite on any machine with Java and Maven installed.

🛠️ Prerequisites

Java 17+

Maven

Chrome

Git

**Check your versions:**

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



**🚀 Future Enhancements**


**🔧 Automation Enhancements**

- Add cross-browser testing (Firefox, Edge)

- Enable headless mode for CI/CD

- Add GitHub Actions pipeline

- Add more UI flows (bookmarking, filtering, profile settings)

- Expand negative scenarios (invalid email, empty fields)



**API Testing (Postman)**

- Created a Postman collection using the public JSONPlaceholder API.
- Implemented GET tests for valid and invalid resource IDs.
- Added a POST test (`/posts`) with automated checks for 201 status and JSON fields.
- Used Postman test scripts (`pm.expect`) to validate status codes and response structure.


**📱 Mobile Testing**

- Appium mobile flows

- Responsive UI tests



**📊 QA Documentation**

- Add test plan + test strategy

- Add risk analysis

- Add traceability matrix

**🎥 Optional Enhancements
**
- Add screenshots or GIFs

- Add build/test badges
