# BarberShop Appointment App

A simple and user-friendly Java application for booking barber appointments. This application helps users schedule haircuts, manage their bookings, and ensures they never miss an appointment.

## Features

- **User Authentication**: Secure Login and Sign Up functionality.
- **Book Appointments**:
  - Select your preferred date (up to 1 month in advance).
  - Choose your favorite Barber.
  - Pick a Hairstyle.
  - Calculate costs, including optional tips.
- **Manage Bookings**: View current appointments and delete them if needed.
- **Profile Management**: View and edit user profile details.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher.
- NetBeans IDE (recommended) or any Java IDE.
- SQLite (included/configured via JDBC).

### Installation & Run

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/vuyopapiyana/BarbershopApp.git
    cd BarbershopApp
    ```

2.  **Open in NetBeans**:
    - Open the project folder in NetBeans.
    - Resolve any missing library references if prompted (requires `sqlite-jdbc` and `jcalendar` libraries).

3.  **Run the Application**:
    - Build and Run `src/MainFrame.java` as the entry point.

## Usage Guide

1.  **Sign Up / Log In**: Create an account or log in with existing credentials.
2.  **Make a Booking**:
    - Navigate to the "Booking" section.
    - Pick a valid date.
    - Select a Barber and Haircut.
    - Enter payment amount and confirm.
3.  **Review Bookings**: Check the "Review Booking" tab to see your scheduled haircuts.

## Technologies Used

- **Java (Swing)**: GUI Framework.
- **SQLite**: Database for storing user and booking data.
- **JCalendar**: Date picker component.

## Contact

**Developer**: Notch’s Son
**Email**: 19vuypap@redhill.co.za
