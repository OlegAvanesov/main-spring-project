# Online-Book-Store

The Online Book Store application is a platform for buying and selling books in the Internet.

### **KEY TECHNOLOGIES:**
- Java 17
- Maven
- Spring Data
- Spring Boot Web
- Spring Boot Security
- Spring Boot Testing
- Hibernate
- MySQL
- Docker
- Swagger
- Lombok
- MapStruct
- Liquibase

### **GENERAL INFO**
**In this app we will have the following domain models (entities):**
- **User:** Contains information about the registered user including their authentication details and personal information.
- **Role:** Represents the role of a user in the system, for example, admin or user.
- **Book:** Represents a book available in the store.
- **Category:** Represents a category that a book can belong to.
- **ShoppingCart:** Represents a user's shopping cart.
- **CartItem:** Represents an item in a user's shopping cart.
- **Order:** Represents an order placed by a user.
- **OrderItem:** Represents an item in a user's order.

**People involved:**
- **Shopper (User):** Someone who looks at books, puts them in a basket (shopping cart), and buys them.
- **Manager (Admin):** Someone who arranges the books on the shelf and watches what gets bought.

**Things Shoppers Can Do:**
1. Join and sign in:
- Join the store.
- Sign in to look at books and buy them.
2. Look at and search for books:
- Look at all the books.
- Look closely at one book.
- Find a book by typing its name.
3. Look at bookshelf sections:
- See all bookshelf sections.
- See all books in one section.
4. Use the basket:
- Put a book in the basket.
- Look inside the basket.
- Take a book out of the basket.
5. Buying books:
- Buy all the books in the basket.
- Look at past receipts.
6. Look at receipts:
- See all books on one receipt.
- Look closely at one book on a receipt.

**Things Managers Can Do:**
1. Arrange books:
- Add a new book to the store.
- Change details of a book.
- Remove a book from the store.
2. Organize bookshelf sections:
- Make a new bookshelf section.
- Change details of a section.
- Remove a section.
3. Look at and change receipts:
- Change the status of a receipt, like "Shipped" or "Delivered".

### **LIST OF AVAILABLE ENDPOINTS:**
**All endpoints were documented using Swagger**
1. Available for non authenticated users:
- POST: /api/auth/register
- POST: /api/auth/login

2. Available for users with role USER
- GET: /api/books
- GET: /api/books/{id}
- GET: /api/categories
- GET: /api/categories/{id}
- GET: /api/categories/{id}/books
- GET: /api/cart
- POST: /api/cart
- PUT: /api/cart/cart-items/{cartItemId}
- DELETE: /api/cart/cart-items/{cartItemId}
- GET: /api/orders
- POST: /api/orders
- GET: /api/orders/{orderId}/items
- GET: /api/orders/{orderId}/items/{itemId}

3. Available for users with role ADMIN:
- POST: /api/books/
- PUT: /api/books/{id}
- DELETE: /api/books/{id}
- POST: /api/categories
- PUT: /api/categories/{id}
- DELETE: /api/categories/{id}
- PATCH: /api/orders/{id}

### PROJECT FEATURES
1. Language: Java 17. Build System: Maven.
2. The application has Controller - Service - Repository architecture.
3. To ensure security, Spring Boot Security and Data Transfer Objects (DTOs) are used.
4. All the endpoints were documented using Swagger.
5. Books can be filtered by author and title. For this purpose it is used Criteria Query.
6. Liquibase was used to create tables and add some data to them in the database.
- Only one user has role ADMIN (email:john@example.com, password:1234). All new users will have role USER by default.
7. CustomGlobalExceptionHandler is used to handle exceptions. It provides more descriptive exception messages.
8. Tests were written using Testcontainers for repository-level, Mockito for service-level and MockMvc for controller-level.
9. For application deployment Docker was used.
####     TO START LOCALLY THIS APPLICATION YOU MUST:
1. Fetch this project to you local IDE.
2. Open this project in your IDE.
3. Create in the root directory .env file. As an example you can use .env.template file.
4. Run Docker Desktop.
5. To run the application for the first time via docker, run the commands “docker-compose build”, “docker-compose up mysqldb” and “docker-compose up” in the IDE terminal. In the future, to run the application, run the “docker-compose up” command.
6. If you want to try application you can use this [link](http://localhost:8081/swagger-ui/index.html#/)

### You can  watch [video](https://www.loom.com/share/b27188d409e74d33ab035c6c76222b08?sid=dd764008-b2b7-4b2e-bee0-056107ba6717) to see how this application works.
