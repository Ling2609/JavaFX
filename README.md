# 🛒 Automated Purchase Order Management System (OWSB)

The **OWSB** (Order Workflow System for Businesses) is a desktop-based **Automated Purchase Order Management System** designed to simplify and digitalize inventory and procurement workflows. This JavaFX application is ideal for small-to-medium scale businesses managing daily sales, stock tracking, and purchasing.

---

## 🔧 System Functionalities

The system provides the following key modules:

- **Item Management**  
  Create, update, and organize inventory items.

- **Supplier Management**  
  Maintain supplier contact information and transaction records.

- **Daily Sales Record**  
  Log daily sales for accurate stock tracking and reporting.

- **Purchase Requisition Management**  
  Users can request restocking of items when inventory is low.

- **Purchase Order Management**  
  Generate official purchase orders for suppliers based on approved requisitions.

- **Financial and Stock Report Generation**  
  Produce summaries of financial activity and current inventory status.

- **Low Stock Alert**  
  Alert users when stock levels fall below pre-defined minimum thresholds.

---

## 👥 User Roles & Access Control

- **Admin**

- **Sales Manager**
  
- **Purchase Manager**
  
- **Finance Manager**
  
- **Inventory Manager**
  
---

## 🧰 Tech Stack

| Layer            | Technology                                |
|------------------|-------------------------------------------|
| **Architecture** | Object-Oriented Programming (OOP)         |
| **Build Tool**   | Apache Maven                              |
| **UI**           | JavaFX with custom CSS                    |
| **Data Storage** | Plain text files (`.txt`) for persistence |

---

## 📁 Project Structure (Simplified)

```
Final_OOP/
├── Data/                     # Plain text files for persistent data storage
│   ├── ItemsList.txt
│   ├── dailySales.txt
│   ├── Suppliers.txt
│   └── ...
├── Reports/                  # Generated PDF reports (financial, stock)
│   └── *.pdf
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       ├── Admin/               # Admin module (add users, control panel)
│       │       ├── financemanager/      # Finance Manager module
│       │       ├── inventorymanager/    # Inventory Manager module
│       │       ├── purchasemanager/     # Purchase Manager module
│       │       ├── salesmanager/        # Sales Manager module
│       │       └── groupfx/JavaFXApp/   # Core application logic & shared classes
│       ├── resources/
│       │   ├── css/                     # UI styling (JavaFX CSS)
│       │   ├── fxml/                    # FXML layouts per module
│       │   └── img/                     # UI image resources
├── pom.xml                 # Maven build configuration
└── README.md
```
