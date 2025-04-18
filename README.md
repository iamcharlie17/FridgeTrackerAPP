# 🧊 Fridge Tracker App

A console-based Java application that helps users manage fridge inventory, reduce food waste, and stay on top of nutrition. Built with Java 21 and Maven, featuring intuitive menu-based navigation powered by the JLine library.

---

## ✅ Features

- **Seasonal Health Tips**  
  Displays relevant health advice based on the current season.

- **Add & Track Ingredients**  
  Enter ingredient details with category, quantity, and expiration date.

- **Update Ingredient Quantity**  
  Update or remove existing ingredient quantities.

- **View All Ingredients**  
  Displays a full list with name, category, quantity, and expiry.

- **Filter by Category**  
  Organize and view items by categories like Dairy, Meat, Veg, etc.

- **Recipe Suggestions**  
  Suggests recipes based on current fridge items using the Spoonacular API.

- **Nutrition Calculator**  
  Calculates nutritional info via Edamam API based on user input.


- **Remove Expired Items**  
  Automatically removes expired ingredients and logs them to a waste tracker.


- **Waste Log Viewer**  
  See what expired and was discarded to promote waste reduction.

- **Clear Waste Log**  
  To clear all ingredients from waste log

---



---

## 💡 Future Enhancements

- [ ] Persistent storage via SQLite or JSON  
- [ ] GUI implementation using JavaFX or Swing  
- [ ] Barcode scanner integration  
- [ ] Notification alerts before expiration  
- [ ] Smarter recipe suggestions with dynamic APIs  

---

## 🛠️ Tech Stack

- Java 21  
- Maven  
- OkHttp + Gson for API calls  
- Edamam & Spoonacular APIs for nutrition and recipes  

---

## 🚀 Getting Started

1. Clone this repository:
   ```bash
   git clone https://github.com/iamcharlie17/fridgetrackerapp.git
   cd fridgetrackerapp/App

1. Run the project:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="org.example.FridgeTrackerApp"



