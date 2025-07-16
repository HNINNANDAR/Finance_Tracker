# Personal Finance Tracker

**Course:** Modern Programming Practices  
**Block:** July 2025  
**Instructor:** Dr. Bright Gee Varghese R

**Team Member:**
- Hnin Nandar Zaw (ID: 619564)

**Date of Submission:** July 16, 2025

---

## 🚀 Project Requirements

### ✅ Java Stream API
Utilized for efficient collection operations—filtering, mapping, reducing, and aggregating data.

### ✅ Unit Testing
Business logic is covered using JUnit tests. 

Run the following cmd to execute the test.

mvn test

### ✅ Singleton Pattern
Applied where only one instance is required (e.g., `DatabaseConnector`).

---

## 🧩 1. Problem Description

Managing personal finances can be difficult without a structured system. This project provides a simple finance tracker to record, view, and categorize transactions. It helps users gain financial awareness and develop better money habits.

---

## 👤 2. User Stories

- As a user, I want to register and log in to securely manage my data.
- As a user, I want to add income and expenses to track my financial activities.
- As a user, I want to view and categorize my transactions.
- As a user, I want to filter data by month and year for easy tracking.
- As a user, I want to securely log out after use.

---

## ⚙️ 3. Functional Requirements

### 1. Authentication
- Register with email and password.
- Login and logout functionality.

### 2. Dashboard
- Personalized greeting.
- Monthly and yearly summary view.

### 3. Transaction Management
- Add transactions (amount, category, type, description).
- View transaction history.

### 4. Category Management
- Create/edit/delete user-specific categories.

### 5. Date Filtering
- Filter transactions by selected month and year.

### 6. Navigation
- Sidebar with links: Dashboard, Add Transaction, View Transactions, Categories.
- Sidebar visible only after login.

### 7. Data Persistence
- All data stored in a relational database via JDBC.

---

## ⚙️ 4. Non-Functional Requirements

- **Usability:** Simple UI with clear navigation.
- **Security:** Password handling, secure session handling.
- **Reliability:** Handles invalid input gracefully.
- **Performance:** Fast UI and optimized DB queries.
- **Scalability:** Modular design for future expansion.
- **Maintainability:** Follows clean architecture and coding standards.
- **Portability:** Platform-independent (Java-based).

---

## 🏗 5. Architecture

Follows a modular **MVC** pattern with layered design.

### 5.1 Layer Overview

- **Presentation Layer:** Java Swing UI components (panels, forms, dashboards).
- **Service Layer (Controller):** Handles logic and validation.
- **DAO Layer:** Encapsulates database operations using JDBC.
- **Database:** PostgreSQL stores persistent data (users, transactions, categories).

### 5.2 Architecture Diagram

![Architecture Diagram](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/architectureDiagram.png)

### 5.3 Technologies Used

- Java 24
- JDBC
- PostgreSQL
- Maven
- JUnit
- IntelliJ IDEA

---

## 📌 6. Use Case Diagram

![Use Case Diagram](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/usecaseDiagram.png)

---

## 📋 7. Use Case Descriptions

### User Login
- **Actor:** Registered User
- **Steps:** Enter credentials → Authenticate → Show Dashboard

### User Registration
- **Actor:** New User
- **Steps:** Fill registration form → Save → Redirect to login

### Add Transaction
- **Actor:** Logged-in User
- **Steps:** Fill form → Save to DB → Update dashboard

### View Transactions
- **Actor:** Logged-in User
- **Steps:** Open view screen → Load transactions

### Manage Categories
- **Actor:** Logged-in User
- **Steps:** Open categories → Add/Edit/Delete

---

## 📦 8. Class Diagram

![Class Diagram](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/classDiagram.png)

---

## 📈 9. Sequence Diagrams

![Sequence Diagram](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/sequenceDiagram.png)

---

## 🖼 10. Screenshots

![Register](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/register.png "Register")

![Login](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/login.png)

![Dashboard](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/dashboard.png)

![Add Transaction](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/newTransaction.png)

![View Transactions](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/transactions.png)

![Manage Categories](https://github.com/HNINNANDAR/Finance_Tracker/blob/main/src/main/java/finance/tracker/docs/categories.png)
---

## 11. Installation & Deployment

### 1. Clone the Repository

```bash
git clone https://github.com/HNINNANDAR/Finance_Tracker.git
cd Finance_Tracker
```

### 2. Environment Setup

Ensure the following are installed:

- Java 21+
- PostgreSQL (latest version)
- IntelliJ IDEA / Eclipse (recommended)
- JDBC PostgreSQL Driver (included in project libraries)

### 3. Database Setup

1. Open your PostgreSQL client (e.g., pgAdmin or psql).
2. Create a new database:

```sql
CREATE DATABASE finance_tracker;
```

3. Run the schema script located in:

```
/resources/schema.sql
```

> Make sure to update your DB credentials in `DatabaseConnector.java`:

```java
DriverManager.getConnection("jdbc:postgresql://localhost:5432/finance_tracker", "your_username", "your_password");
```

### 4. Run the Application

Open the project in your IDE and run the `MainFrame.java` file under:

```
src/finance/tracker/ui/MainFrame.java
```

This will launch the desktop GUI.

### ✅ Notes

- No Docker or web server setup required.
- Make sure PostgreSQL is running and accessible at `localhost:5432`.


---

## 12. How to Use

1. **Launch App:**  
   Run `MainFrame.java` to start the Finance Tracker.

2. **Login or Register:**
  - Existing users can log in with credentials.
  - New users can register using the "Register" link.

3. **Navigate:**  
   Use the sidebar to access Dashboard, Add/View Transactions, and Categories.

4. **Manage Finances:**
  - Add transactions with amount, category, and type.
  - View, update, or delete past transactions.

5. **Customize Categories:**  
   Create and manage custom income/expense categories.

6. **Filter by Date:**  
   Use dropdowns to view transactions for a specific month/year.

7. **Logout:**  
   Click "Logout" to securely exit the application.

**Test Account:**  
Email: `test@example.com`  
Password: `password123`

---

## 13. Design Justification & Principles

- **Interfaces & Composition:**  
  DAO interfaces separate UI and database logic, supporting testability and flexibility.

- **SOLID Principles:**
  - *Liskov Substitution:* UI panels follow consistent behavior for easy replacement.
  - *Open-Closed:* New features can be added without altering existing code.

- **Design Patterns:**
  - *DAO Pattern:* Encapsulates all database access logic.
  - *Observer Pattern:* Panels update views via callback functions.
  - *CardLayout:* Simplifies view navigation and screen transitions.

These principles ensure clean architecture, scalability, and easy maintenance.


---

## 14. Team Members

\[Hnin Nandar Zaw]

---

## 15. References

- GitHub and open-source projects related to finance tracking and Java Swing applications
- AI assistance by OpenAI’s ChatGPT for code review, design guidance, and documentation support
---
## Grading Rubric (Total: 10 Points)

| Criteria                                     | Points | Description / Expectations                                                                                   |
| -------------------------------------------- | :----: | ------------------------------------------------------------------------------------------------------------ |
| **Problem Description & User Stories**       |    1   | Clearly states the problem and provides meaningful, relevant user stories.                                   |
| **Functional & Non-Functional Requirements** |    1   | Functional and non-functional requirements are complete, clear, and relevant.                                |
| **Architecture & Design**                    |    1   | Well-structured layered architecture, clear diagrams (class, sequence, use case), thoughtful design.         |
| **Use of Stream API**                        |    1   | Appropriately uses Java Stream API wherever possible; usage is clear and well-documented.                    |
| **Singleton Pattern (when applicable)**      |    1   | Applies the Singleton pattern where necessary; justification provided in documentation.                      |
| **Unit Testing**                             |    1   | Implements unit tests for key business logic; tests are meaningful and cover main cases.                     |
| **Implementation Quality**                   |    1   | Code quality: modularity, clean structure, meaningful naming, adherence to SOLID principles, error handling. |
| **Deployment, Installation & Usability**     |    1   | Clear setup instructions, successful deployment, working UI/CLI, and usability.                              |
| **Documentation & Reporting**                |    1   | Detailed README: all sections complete (screenshots, diagrams, instructions, principles, references, etc).   |
| **Presentation & Teamwork**                  |    1   | Professionalism in presentation (repo, submission, screenshots), teamwork (if applicable), and originality.  |
| **Total**                                    | **10** |                                                                                                              |