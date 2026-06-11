# 🚗 Smart Car Parking System

A full-stack web application built with **Spring Boot + Thymeleaf + MySQL** for managing car parking operations with three user roles: User, Owner, and Admin.

---

## 📌 Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [How to Run](#how-to-run)
- [Default Login](#default-login)
- [API Endpoints](#api-endpoints)
- [Booking Flow](#booking-flow)
- [Common Interview Questions](#common-interview-questions)

---

## 📖 Project Overview

Smart Car Parking System connects **Parking Area Owners** who have parking slots with **Car Owners** who need parking space. An **Admin** manages the entire platform.

### Who uses the system?

| Role | What they do |
|---|---|
| 👤 **User (Car Owner)** | Search parking, book slots, pay, view history |
| 🏢 **Owner (Parking Owner)** | Add parking areas, manage bookings, track revenue |
| 👑 **Admin** | Approve listings, manage users, view analytics |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend Framework | Spring Boot 3.2 |
| Language | Java 17 |
| Frontend | Thymeleaf (HTML templates) |
| Security | Spring Security (BCrypt + Form Login) |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Map | OpenStreetMap + Leaflet.js (Free, no API key) |
| Build Tool | Maven |
| Server | Embedded Tomcat |

---

## ✅ Features

### User Module
- Register / Login
- Search parking by city
- View parking on interactive map
- Book parking slot
- View estimated cost before booking
- Cancel booking
- Pay online after exit
- View booking history
- Write reviews with star ratings

### Owner Module
- Register / Login
- Add parking area with map location picker
- Upload parking image (file upload or URL)
- Set price per hour and slot count
- Confirm or reject bookings
- Record vehicle entry and exit
- View bookings and earnings dashboard
- View customer reviews

### Admin Module
- Approve or suspend parking listings
- Manage all users and owners
- Enable or disable accounts
- View all bookings across the platform
- Analytics dashboard with revenue stats

### Extra Features
- 🗺️ Interactive map (OpenStreetMap / Leaflet) — free, no API key
- 🖼️ Parking images — upload file or paste URL
- ⭐ Review system — 1 to 5 star ratings with comments
- 📞 Owner contact info shown on parking detail page
- 💰 Auto bill calculation based on actual entry/exit time
- 📱 Responsive design

---

## 📁 Project Structure

```
smart-parking-complete/
│
├── pom.xml                          ← Maven dependencies
├── README.md                        ← This file
├── sample-data.sql                  ← Sample data for testing
│
└── src/main/
    ├── java/com/smartparking/
    │   │
    │   ├── SmartParkingApplication.java     ← Main class
    │   │
    │   ├── config/
    │   │   ├── DataInitializer.java         ← Seeds admin on startup
    │   │   ├── SecurityConfig.java          ← Login, roles, URL protection
    │   │   └── WebConfig.java               ← Serves uploaded images
    │   │
    │   ├── controller/
    │   │   ├── HomeController.java          ← Home, login, register, search
    │   │   ├── UserController.java          ← Booking, payment, review
    │   │   ├── OwnerController.java         ← Parking, bookings, dashboard
    │   │   └── AdminController.java         ← Users, parking approval
    │   │
    │   ├── entity/
    │   │   ├── User.java                    ← users table
    │   │   ├── ParkingArea.java             ← parking_areas table
    │   │   ├── Booking.java                 ← bookings table
    │   │   └── Review.java                  ← reviews table
    │   │
    │   ├── enums/
    │   │   ├── Role.java                    ← USER, OWNER, ADMIN
    │   │   ├── BookingStatus.java           ← PENDING, CONFIRMED, ACTIVE...
    │   │   ├── ParkingStatus.java           ← ACTIVE, INACTIVE, SUSPENDED...
    │   │   └── PaymentStatus.java           ← PENDING, PAID, REFUNDED
    │   │
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── ParkingAreaRepository.java
    │   │   ├── BookingRepository.java
    │   │   └── ReviewRepository.java
    │   │
    │   ├── security/
    │   │   └── CustomUserDetailsService.java  ← Loads user for Spring Security
    │   │
    │   └── service/
    │       ├── UserService.java
    │       ├── ParkingAreaService.java
    │       ├── BookingService.java
    │       ├── ReviewService.java
    │       └── ImageUploadService.java
    │
    └── resources/
        ├── application.properties           ← DB config, server port
        ├── static/
        │   ├── css/style.css                ← All styles
        │   └── images/default-parking.svg   ← Default parking image
        └── templates/
            ├── index.html                   ← Home page
            ├── auth/
            │   ├── login.html
            │   └── register.html
            ├── parking/
            │   ├── search.html              ← Search with map
            │   └── detail.html              ← Parking detail, map, reviews
            ├── user/
            │   ├── dashboard.html
            │   ├── bookings.html
            │   └── book.html                ← Booking form with cost calculator
            ├── owner/
            │   ├── dashboard.html
            │   ├── parking.html
            │   ├── add-parking.html         ← Map picker + image upload
            │   ├── bookings.html
            │   └── reviews.html
            └── admin/
                ├── dashboard.html
                ├── users.html
                ├── owners.html
                ├── parking.html
                └── bookings.html
```

---

## 🗄️ Database Design

### Tables and Relationships

```
users
├── id (PK)
├── full_name
├── email (unique)
├── password (BCrypt encrypted)
├── phone (unique)
├── role → USER / OWNER / ADMIN
├── enabled → 1 or 0
├── vehicle_number
├── vehicle_type → CAR / BIKE / TRUCK
└── created_at

parking_areas
├── id (PK)
├── name, address, city, state, pincode
├── latitude, longitude  ← for map
├── total_slots, available_slots
├── price_per_hour
├── supported_vehicle_types
├── description, facilities
├── image_file_name  ← uploaded file
├── image_url        ← external URL
├── status → ACTIVE / INACTIVE / SUSPENDED / PENDING_APPROVAL
├── owner_id (FK → users.id)
└── created_at

bookings
├── id (PK)
├── booking_code (unique) ← e.g. BK-20240115-ABC123
├── user_id (FK → users.id)
├── parking_area_id (FK → parking_areas.id)
├── planned_entry_time, planned_exit_time
├── actual_entry_time, actual_exit_time
├── vehicle_number, vehicle_type
├── slot_number
├── status → PENDING / CONFIRMED / REJECTED / ACTIVE / COMPLETED / CANCELLED
├── estimated_amount, final_amount
├── payment_status → PENDING / PAID / REFUNDED
├── payment_method, transaction_id
├── rejection_reason
└── created_at

reviews
├── id (PK)
├── user_id (FK → users.id)
├── parking_area_id (FK → parking_areas.id)
├── rating → 1 to 5
├── comment
└── created_at
```

---

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- MySQL 8.x
- Eclipse IDE (or any Java IDE)
- Maven (comes with Eclipse)

### Step 1 — Create MySQL Database
Open MySQL Workbench and run:
```sql
CREATE DATABASE smart_parking_db;
```

### Step 2 — Update Password
Open `src/main/resources/application.properties` and update:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3 — Import in Eclipse
```
File → Import → Maven → Existing Maven Projects
→ Browse to the extracted folder → Finish
```
Wait for dependencies to download (needs internet, 2-5 minutes).

### Step 4 — Run the App
```
Right-click SmartParkingApplication.java
→ Run As → Java Application
```

### Step 5 — Open Browser
```
http://localhost:8080
```

### Step 6 — Add Sample Data (Optional)
Open `sample-data.sql` in MySQL Workbench and click Execute (⚡).

---

## 🔐 Default Login

| Role | Email | Password |
|---|---|---|
| 👑 Admin | admin@smartparking.com | admin123 |
| 🏢 Owner | suresh@gmail.com | password123 |
| 👤 User | raj@gmail.com | password123 |

> **Note:** Sample users only available after running `sample-data.sql`

---

## 🌐 Pages and URLs

| Page | URL | Access |
|---|---|---|
| Home Page | http://localhost:8080 | Everyone |
| Login | http://localhost:8080/login | Everyone |
| Register | http://localhost:8080/register | Everyone |
| Search Parking | http://localhost:8080/parking/search | Everyone |
| Parking Detail | http://localhost:8080/parking/{id} | Everyone |
| User Dashboard | http://localhost:8080/user/dashboard | USER only |
| My Bookings | http://localhost:8080/user/bookings | USER only |
| Book Parking | http://localhost:8080/user/book/{id} | USER only |
| Owner Dashboard | http://localhost:8080/owner/dashboard | OWNER only |
| Add Parking | http://localhost:8080/owner/parking/add | OWNER only |
| Owner Bookings | http://localhost:8080/owner/bookings | OWNER only |
| Admin Dashboard | http://localhost:8080/admin/dashboard | ADMIN only |
| Admin Users | http://localhost:8080/admin/users | ADMIN only |
| Admin Parking | http://localhost:8080/admin/parking | ADMIN only |

---

## 📅 Booking Flow

```
User searches parking area
        ↓
User clicks "Book Now" → fills date/time
        ↓
Booking created → Status: PENDING
        ↓
Owner sees new booking → clicks Confirm
        ↓
Status: CONFIRMED
        ↓
Vehicle arrives → Owner clicks "Entry"
        ↓
Status: ACTIVE (timer starts)
        ↓
Vehicle leaves → Owner clicks "Exit"
        ↓
Status: COMPLETED
Final amount = actual hours × price per hour
        ↓
User clicks "Pay Now"
        ↓
Payment Status: PAID ✅
```

---

## 💡 Important Notes

### Why BCrypt for passwords?
Plain text passwords like `mypassword` are stored as BCrypt hash like `$2a$10$N.zmdr9k...`. Even if someone steals the database, they cannot reverse the hash to get the original password.

### Why EAGER fetch?
We changed `FetchType.LAZY` to `FetchType.EAGER` to avoid `LazyInitializationException` when Thymeleaf renders pages outside a transaction context.

### How image upload works
- Uploaded files are saved to the `uploads/` folder in the project root
- The filename is stored in the database
- Files are served at `/uploads/filename.jpg`
- Or paste an external image URL instead

### How the map works
- Uses OpenStreetMap tiles (free, no API key needed)
- Leaflet.js library for interactive map
- Owner clicks map to pick exact parking location
- Latitude and longitude saved in database
- All parking areas shown as markers on home and search pages

---

## 🔧 Configuration

### application.properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/smart_parking_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=true

# File Upload
spring.servlet.multipart.max-file-size=5MB
app.upload.dir=uploads
```

---

## 🎯 Future Improvements

- [ ] Add Razorpay / Stripe payment gateway integration
- [ ] Send email notifications on booking confirmation
- [ ] Add JWT-based REST API for mobile app
- [ ] Real-time slot availability using WebSockets
- [ ] QR code scanning for vehicle entry/exit
- [ ] Deploy on AWS / Heroku with CI/CD
- [ ] Add booking reminder notifications
- [ ] Monthly revenue reports with charts for owners

---

## 👩‍💻 Developer Notes

### Common Errors and Fixes

| Error | Cause | Fix |
|---|---|---|
| `Access denied for user 'root'` | Wrong MySQL password | Update password in application.properties |
| `LazyInitializationException` | LAZY fetch outside session | Use FetchType.EAGER |
| `Could not parse expression` | Missing single quotes in th:text | Use `th:text="'Your Text'"` |
| `Constructor threw exception` | Lombok not working in Eclipse | Replace @Autowired instead of @RequiredArgsConstructor |
| `Port 8080 already in use` | Another app running on 8080 | Stop other app or change server.port |

---

## 📚 Technologies Explained

**Spring Boot** — Makes Java web development easy with auto-configuration and embedded server.

**Spring Security** — Handles login, logout, password encryption, and role-based access control.

**Spring Data JPA** — Connects Java objects to database tables without writing SQL manually.

**Thymeleaf** — Server-side HTML template engine. Processes HTML on server and sends to browser.

**Hibernate** — ORM (Object Relational Mapping) tool that converts Java objects to database rows.

**BCrypt** — Password hashing algorithm. Stores passwords securely so they cannot be reversed.

**Leaflet.js** — JavaScript library for interactive maps using OpenStreetMap tiles.

**MySQL** — Relational database management system for storing all application data.

**Maven** — Build tool that manages project dependencies (downloads libraries automatically).

---

*Built with ❤️ using Spring Boot*
