# BlogApp

A full-stack blog application built with Java, Spring Boot, and Thymeleaf. This project serves as a complete platform for creating, managing, and sharing blog posts. It includes user authentication, role-based access (Admin, Guest), and a rich set of features for both readers and content creators.

## ✨ Features

-   **Post Management:** Create, Read, Update, and Delete (CRUD) operations for blog posts.
-   **User Authentication:** Secure user registration and login system using Spring Security.
-   **Role-Based Access Control:**
    -   **Admin:** Full control over all posts, comments, and categories. Can approve/reject posts.
    -   **Guest/User:** Can write and manage their own posts.
-   **Commenting System:** Users can comment on posts.
-   **Categories:** Organize posts into different categories.
-   **Pagination:** Efficiently browse through a large number of posts and comments.
-   **Search Functionality:** Search for posts based on keywords.
-   **Responsive UI:** A clean and modern user interface built with Bootstrap and Thymeleaf, adaptable to all screen sizes.

## 🛠️ Technologies Used

-   **Backend:**
    -   Java
    -   Spring Boot
    -   Spring Security
    -   Spring Data JPA (Hibernate)
-   **Frontend:**
    -   Thymeleaf
    -   HTML, CSS, JavaScript
    -   Bootstrap
-   **Database:**
    -   MySQL
-   **Build Tool:**
    -   Maven

## 🚀 How to Run

1.  **Prerequisites:**
    -   JDK (Java Development Kit)
    -   Maven
    -   MySQL Server

2.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/BlogApp.git
    ```

3.  **Configure the database:**
    -   Create a new database in MySQL named `MyBlogApp`.
    -   Update the `src/main/resources/application.properties` file with your MySQL username and password.

4.  **Run the application:**
    -   You can run the application directly from your IDE (like IntelliJ IDEA) by running the `BlogAppApplication` class.
    -   Or, you can build a JAR file and run it from the command line:
      ```bash
      mvn clean package
      java -jar target/BlogApp-0.0.1-SNAPSHOT.jar
      ```

The application will be accessible at `http://localhost:8082`.