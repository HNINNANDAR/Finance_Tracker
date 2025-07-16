# Course Project Report

**\[Personal Finance Tracker]**

**Course:** Modern Programming Practices

**Block:** July 2025

**Instructor:** Dr. Bright Gee Varghese R

**Team Members:**

\[Hnin Nandar Zaw]  \[619564]


**Date of Submission:** \[07/16/2025]

---

# Important Project Requirements

**Stream API:**

You must make use of the Java Stream API wherever it is applicable, especially for collection processing, filtering, mapping, aggregation, and other data operations. Clearly highlight these usages in your code and documentation.

**Unit Testing:**

Implement unit tests for your business logic using JUnit or a similar testing framework. Include instructions for running your test suite and ensure your tests cover major functionalities and edge cases.

**Singleton Pattern:**

Where a class should have only one instance (for example, for managing resources, database connections, configuration, etc.), apply the Singleton design pattern.

---

## 1. Problem Description

Managing personal finances can be difficult without a proper system. Many people lose track of their expenses, leading to poor budgeting and financial stress. This project solves that problem by providing a simple finance tracker where users can record, view, and categorize their transactions. It helps users become more aware of their spending and build better money habits.

---

## 2. User Stories

Describe the system from the user's perspective using user stories:

* As a user, I want to register an account so that I can securely access my personal dashboard.

* As a user, I want to log in to my account so that I can manage my financial records.

* As a user, I want to add income and expense transactions so that I can track my spending.

* As a user, I want to view my past transactions so that I can review and analyze my expenses.

* As a user, I want to categorize my transactions so that I can organize my financial data.

* As a user, I want to select a specific month and year so that I can filter my transaction data.

* As a user, I want to log out when I’m done so that my data remains secure.
* ...

---

## 3. Functional Requirements

### 1. User Registration and Authentication
- Users can register with a unique email and password.
- The system authenticates users during login.
- Users can log out to end their session.

### 2. Dashboard
- Displays a personalized welcome message.
- Shows summary data filtered by selected month and year.

### 3. Transaction Management
- Users can add new transactions with details:
  - Amount
  - Category
  - Type (Income or Expense)
  - Description
- Users can view a list of transactions.
- Transactions can be updated or deleted.

### 4. Category Management
- Users can view, create, edit, and delete custom categories.
- Categories are user-specific and not shared between users.

### 5. Date Filtering
- Users can select a month and year from dropdowns.
- Dashboard and transaction views update accordingly.

### 6. Navigation
- Sidebar contains navigation buttons:
  - 🏠 Dashboard
  - ➕ Add Transaction
  - 📋 View Transactions
  - 🗂 Categories
- Sidebar is only visible after login.
- Sidebar is hidden on Login and Registration screens.

### 7. Data Persistence
- All data (users, categories, transactions) is saved in a relational database.
- The application reads from and writes to the database as needed.

---

## 4. Non-Functional Requirements

### 1. Usability
- The application provides a simple and intuitive user interface.
- Clear navigation through labeled buttons and icons.
- Minimal user input required for common actions (e.g., dropdown selectors for dates).

### 2. Security
- Passwords are securely stored using hashing.
- User authentication is required to access financial features.
- Session control ensures secure logout behavior.

### 3. Reliability
- The application handles invalid input and database failures gracefully.
- Error dialogs provide informative feedback to users.

### 4. Performance
- UI updates (e.g., dashboard refresh) are near-instantaneous.
- Efficient database queries support quick loading of transactions and categories.

### 5. Scalability
- Modular design allows for easy extension of features (e.g., reporting, budget goals).
- Supports multiple users with isolated data through user-specific queries.

### 6. Maintainability
- Codebase follows separation of concerns (UI, DAO, Model).
- Panels and services are modular and easy to test or extend.

### 7. Portability
- Runs on any platform with Java installed.
- No platform-specific dependencies or configurations.

---

## 5. Architecture of Project

The Finance Tracker follows a modular **MVC (Model-View-Controller)** architecture:

### 🧱 1. Model
- Contains plain Java classes (POJOs) representing core entities like `User`, `Transaction`, and `Category`.
- Handles data structures used throughout the app.

### 🖥 2. View (UI Layer)
- Built using Java Swing.
- Panels such as `LoginPanel`, `DashboardPanel`, `AddTransactionPanel`, etc., are responsible for rendering the user interface.
- UI components interact with the controller via listeners and callbacks.

### 🧠 3. Controller / Logic
- Business logic and interaction control are handled inside panel classes or passed as lambdas (e.g., onLoginSuccess).
- Handles user actions, input validation, and triggers data updates.

### 🗄 4. Data Access Layer (DAO)
- Interfaces with a relational database using JDBC.
- Classes like `UserDAO`, `TransactionDAO`, and `CategoryDAO` abstract raw SQL operations.
- Ensures separation of database logic from UI code.

### 🧩 5. Database
- A MySQL (or similar) database holds all persistent data: users, transactions, and categories.
- Accessed securely via the DAO layer.

### 🔄 Navigation and Flow
- Uses `CardLayout` to manage screen transitions between login, dashboard, and other panels.
- The `MainFrame` acts as the root controller and navigator of the entire app.


### 5.1 Overview

The Finance Tracker system follows a layered architecture to promote separation of concerns and maintainability. The major layers are:

#### 1. Presentation Layer (UI)
- Built with Java Swing.
- Handles all user interactions, input forms, buttons, and display panels.
- Uses `CardLayout` to switch between views (e.g., Login, Dashboard, Add Transaction).

#### 2. Application Logic Layer (Controller)
- Manages interaction between the UI and the data layer.
- Handles business rules such as login validation, transaction creation, and category management.
- Often implemented using listeners, callbacks, and panel methods.

#### 3. Data Access Layer (DAO)
- Provides CRUD operations for accessing and modifying database records.
- Contains DAO classes like `UserDAO`, `TransactionDAO`, and `CategoryDAO`.
- Encapsulates all database logic using JDBC.

#### 4. Database Layer
- A relational database stores persistent data such as users, transactions, and categories.
- Tables are mapped to Java classes (models) and accessed via the DAO layer.
- Ensures data consistency, integrity, and long-term storage.

Each layer communicates only with adjacent layers, allowing the system to remain modular, testable, and easier to maintain or extend.

### 5.2 Architecture Diagram

*(Insert a diagram here)*

https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.oreilly.com%2Flibrary%2Fview%2Fsoftware-architecture-patterns%2F9781491971437%2Fch01.html&psig=AOvVaw1MvFBR-hwiX6nYyg9wbaq7&ust=1752555946799000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqGAoTCMjf0OfJu44DFQAAAAAdAAAAABCEAQ<img width="1127" height="843" alt="image" src="https://github.com/user-attachments/assets/e2aa8498-ae27-4ab7-bba0-03e5223de976" />

### 5.3 Technologies Used

- **Java 24** — Core programming language for the application logic and UI.
- **Java Swing** — GUI framework used to build the desktop user interface.
- **JDBC** — Java Database Connectivity for interacting with the relational database.
- **PostgreSQL** (or your chosen RDBMS) — Relational database for persistent storage of users, transactions, and categories.
- **Maven** (optional) — Build and dependency management tools.
- **JUnit** (optional) — For unit testing the application.
- **IntelliJ IDEA** — Popular IDEs for Java development.


### 5.4 Layer Descriptions

* **Presentation Layer:**  
  Implements the user interface using Java Swing components, including dashboards, forms, and navigation. It handles user interactions such as login, transaction entry, category management, and viewing financial data.

* **Service Layer:**  
  Contains the business logic for managing users, transactions, and categories. It validates input, coordinates actions between the UI and data access layer, and manages application workflows like login/logout and data updates.

* **Data Access Layer:**  
  Uses DAO (Data Access Object) classes to perform database operations on users, transactions, and categories. This layer abstracts database queries and ensures secure, efficient CRUD operations.

* **Database:**  
  A relational database stores all persistent data such as user credentials, financial transaction records, and category information, enabling reliable data storage and retrieval across sessions.


---

## 6. Use Case Diagram(s)

Insert your use case diagram(s) here (as an image or diagram link).

---

## 7. Use Case Descriptions

Provide detailed descriptions for each use case:

* **Use Case Name:**
* **Primary Actor(s):**
* **Preconditions:**
* **Postconditions:**
* **Main Success Scenario:**

---

## 8. Class Diagram

Insert your UML class diagram image. Include key classes, their attributes, methods, relationships (associations, inheritance, interfaces, composition).

---

## 9. Sequence Diagrams

Provide sequence diagrams for important use cases.

---

## 10. Screenshots

Include relevant screenshots of your application's interface and features.

---

## 11. Installation & Deployment

Detailed, step-by-step instructions for:

* Cloning the repository
* Setting up dependencies and environment
* Database setup (with scripts if needed)
* Running the application (CLI/GUI/Web)
* (Optional) Docker setup instructions

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
