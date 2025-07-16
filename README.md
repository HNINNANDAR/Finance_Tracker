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
Business logic is covered using JUnit tests. Instructions for running tests are included.

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
- Add/edit/delete transactions (amount, category, type, description).
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
- **Security:** Password hashing, secure session handling.
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

![Use Case Diagram](https://app.eraser.io/workspace/FAWyah9i9rHfhEp39JXi?origin=share&elements=uJEaA3_HmeeHzCsCm2UEXQ)

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

1. **Launch the Application:**  
   Run the `MainFrame` class to start the Finance Tracker desktop app.

2. **Login or Register:**
- If you have an account, enter your credentials on the login screen.
- To create a new account, click the "Register" link and fill out the registration form.

3. **Navigate the Dashboard:**
- After login, use the sidebar to access **Dashboard**, **Add Transaction**, **View Transactions**, and **Categories**.
- The dashboard shows an overview of your finances for the selected month and year.

4. **Manage Transactions:**
- Click **Add Transaction** to record income or expenses with categories and amounts.
- Use **View Transactions** to browse, edit, or delete existing entries.

5. **Manage Categories:**
- Use the **Categories** panel to add, update, or remove expense/income categories for better organization.

6. **Filter by Month and Year:**
- Use the dropdown selectors at the bottom of the dashboard to filter financial data by specific months and years.

7. **Logout:**
- Click the **Logout** button at the top-right corner to safely sign out and return to the login screen.

**Sample Test Account:**
- Email: `test@example.com`
- Password: `password123`


---

## 13. Design Justification & Principles

- **Use of Interfaces and Composition:**  
  The project uses DAO interfaces (e.g., `UserDAO`, `CategoryDAO`, `TransactionDAO`) to separate data access logic from UI and business logic. This allows easy replacement of data sources or mocking during testing. Composition is applied in UI panels by injecting DAO dependencies and callbacks, promoting loose coupling and better maintainability.

- **Liskov Substitution Principle (LSP):**  
  UI components like `DashboardPanel`, `AddTransactionPanel`, `ViewTransactionPanel`, and `ManageCategoryPanel` follow consistent interfaces and behavior, allowing them to be swapped or extended without breaking the main frame’s navigation or flow. This design ensures that subclasses or panel replacements maintain expected behaviors.

- **Open-Closed Principle (OCP):**  
  The application design supports adding new features without modifying existing code. For example, new transaction types or UI panels can be added by extending existing classes and registering them in the card layout, without changing the core navigation or data models. The use of functional interfaces for callbacks (e.g., refreshing dashboard after adding transactions) also adheres to OCP by allowing extension through composition.

- **Design Patterns:**
    - **DAO Pattern:** Abstracts all database operations through interfaces and concrete implementations (`UserDAO`, `CategoryDAO`, `TransactionDAO`), enabling clean separation of concerns and easy database swapping or mocking.
    - **Observer Pattern / Callback Functions:** Panels notify others about data changes using callbacks (e.g., `AddTransactionPanel` triggers a dashboard refresh), enabling responsive UI updates without tight coupling.
    - **CardLayout for View Management:** Manages switching between multiple UI screens (login, dashboard, add transaction, etc.) cleanly, maintaining separation of concerns and simplifying navigation logic.

These design choices enhance modularity, ease of testing, and future extensibility, making the Finance Tracker robust and adaptable.


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