# Bus Pass Application  

Welcome to the **Bus Pass Application** repository! This project demonstrates a practical implementation of **Jetpack Compose**, focusing on building a declarative, modern Android application for managing bus passes.  


## Table of Contents  
1. [Project Overview](#project-overview)  
2. [What is Jetpack Compose?](#what-is-jetpack-compose)  
3. [Features](#features)  
4. [Technologies Used](#technologies-used)  
5. [Setup Instructions](#setup-instructions)  


---


## Video  

[Video](https://hollandcollege-my.sharepoint.com/personal/aodu_hollandcollege_com/_layouts/15/stream.aspx?id=%2Fpersonal%2Faodu%5Fhollandcollege%5Fcom%2FDocuments%2FRecordings%2FMeeting%20with%20Anthony%20Odu%2D20250113%5F151345%2DMeeting%20Recording%2Emp4&referrer=StreamWebApp%2EWeb&referrerScenario=AddressBarCopied%2Eview%2E1008e6c0%2D1108%2D46cf%2D853f%2Da3c93292fa08)

## Question

For any question, just incase the video wasnt clear to you i will be able to expalin better.

## Project Overview  

The Bus Pass Application provides a streamlined way to manage bus passes through an intuitive interface. It allows users to:  
- View all bus passes in a dynamic list.  
- Add new bus passes with real-time form validation.  
- Edit existing bus passes.  
- Delete bus passes with immediate updates to the UI.  

This project is designed to explore **Jetpack Compose**'s capabilities for modern Android development.  

---

## What is Jetpack Compose?  

**Jetpack Compose** is Google’s modern, fully declarative UI toolkit for building native Android applications. It simplifies and accelerates UI development by:  
- Using **Kotlin-based composable functions** to define UI components.  
- Supporting **reactive state management**, which ensures the UI updates automatically when state changes.  
- Offering built-in support for **Material Design** principles, making it easier to create beautiful and consistent UIs.  
- Eliminating the need for XML layouts, enabling a single codebase for UI definitions.  

By using Jetpack Compose in this project, we embrace a cleaner, more intuitive way to design and manage Android user interfaces.  

---

## Features  

- **Jetpack Compose UI**: Fully implemented with Jetpack Compose for a declarative and reactive user interface.  
- **Dynamic Navigation**: Uses `NavHost` to enable seamless screen transitions.  
- **Reusable Components**: Modular and reusable composables like `DatePickerField` and `BusPassCard`.  
- **State Management**: Leverages `mutableStateOf` and `remember` for reactive state updates.  
- **Form Validation**: Ensures robust user input validation for all fields, including dates and numeric inputs.  

---

## Technologies Used  

- **Jetpack Compose**  
- **Kotlin**  
- **Material Design 3** for UI consistency  
- **DatePickerDialog** for date selection  

---

## Setup Instructions  

1. **Clone the Repository**  
   ```bash  
   git clone <repository-url>  
   cd bus-pass  
   ```  

2. **Open in Android Studio**  
   - Open the project in Android Studio Arctic Fox or later.  

3. **Build the Project**  
   - Ensure your environment uses **Gradle 8.7** or higher.  
   - Sync dependencies and build the project.  

4. **Run the Application**  
   - Connect an Android device or start an emulator.  
   - Run the app from Android Studio.  

