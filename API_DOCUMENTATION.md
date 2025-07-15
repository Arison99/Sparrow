# Sparrow MoMo API Documentation

## Overview

Sparrow is a comprehensive Mobile Money (MoMo) simulation system that provides REST and XML APIs for testing and learning mobile money services. This documentation covers all available endpoints and how to use them to build your own applications.

## Base URL

```
http://localhost:8080
```

## Quick Start

### 1. Start the Application

```bash
cd /path/to/sparrow
mvn spring-boot:run
```

### 2. Access the Admin Panel

```
http://localhost:8080/admin
```

### 3. Test API Endpoints

```bash
# Add a user
curl -X POST http://localhost:8080/admin/addUser \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&pin=1234&balance=100.00"

# Get all users
curl -X GET http://localhost:8080/api/users

# Send money
curl -X POST http://localhost:8080/admin/sendMoney \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "fromPhone=+1234567890&toPhone=+0987654321&amount=50.00"
```

## API Endpoints

### User Management

#### Get All Users (JSON)
- **URL:** `/api/users`
- **Method:** `GET`
- **Description:** Retrieve all users in JSON format
- **Response:**
  ```json
  [
    {"phone": "+1234567890"},
    {"phone": "+0987654321"}
  ]
  ```

#### Get All Users (XML)
- **URL:** `/api/users/xml`
- **Method:** `GET`
- **Description:** Retrieve all users in XML format
- **Response:**
  ```xml
  <users>
    <user><phone>+1234567890</phone></user>
    <user><phone>+0987654321</phone></user>
  </users>
  ```

#### Get User Balance
- **URL:** `/api/users/balance`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
- **Response:**
  ```json
  {
    "phone": "+1234567890",
    "balance": 100.50
  }
  ```
- **Error Response:**
  ```json
  {
    "error": "User not found"
  }
  ```

#### Get User Transactions
- **URL:** `/api/users/transactions`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
- **Response:**
  ```json
  {
    "phone": "+1234567890",
    "transactions": [
      {
        "id": "T1642512345678",
        "description": "Sent to +0987654321",
        "amount": -50.00,
        "date": "2025-01-15 10:30:00"
      }
    ]
  }
  ```

### Service Operations

#### Add User
- **URL:** `/admin/addUser`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `pin` (string, required) - 4-digit PIN
  - `balance` (number, required) - Initial balance
- **Response:** HTML page with success/error message

#### Send Money
- **URL:** `/admin/sendMoney`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `fromPhone` (string, required) - Sender's phone number
  - `toPhone` (string, required) - Recipient's phone number
  - `amount` (number, required) - Amount to transfer
- **Response:** HTML page with transaction result

#### Buy Airtime
- **URL:** `/admin/buyAirtime`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `amount` (number, required) - Airtime amount
- **Response:** HTML page with transaction result

#### Pay with MoMo
- **URL:** `/admin/payWithMoMo`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `merchant` (string, required) - Merchant name
  - `amount` (number, required) - Payment amount
- **Response:** HTML page with transaction result

#### Pay Bill
- **URL:** `/admin/payBill`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `biller` (string, required) - Biller name
  - `amount` (number, required) - Payment amount
- **Response:** HTML page with transaction result

#### Savings & Loans
- **URL:** `/admin/savingsLoans`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `action` (string, required) - "save" or "loan"
  - `amount` (number, required) - Amount
- **Response:** HTML page with transaction result

#### Financial Services
- **URL:** `/admin/financialServices`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `service` (string, required) - "insurance" or "investment"
  - `amount` (number, required) - Amount
- **Response:** HTML page with transaction result

#### Invest & Insure
- **URL:** `/admin/investInsure`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `action` (string, required) - "invest" or "insure"
  - `amount` (number, required) - Amount
- **Response:** HTML page with transaction result

#### Add Transaction
- **URL:** `/admin/addTransaction`
- **Method:** `POST`
- **Content-Type:** `application/x-www-form-urlencoded`
- **Parameters:**
  - `phone` (string, required) - User's phone number
  - `description` (string, required) - Transaction description
  - `amount` (number, required) - Transaction amount (positive for credit, negative for debit)
- **Response:** HTML page with transaction result

## Usage Examples

### JavaScript/Fetch API

```javascript
// Add a user
fetch('http://localhost:8080/admin/addUser', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  body: 'phone=+1234567890&pin=1234&balance=100.00'
})
.then(response => response.text())
.then(data => console.log(data));

// Get all users
fetch('http://localhost:8080/api/users')
.then(response => response.json())
.then(users => console.log(users));

// Get user balance
fetch('http://localhost:8080/api/users/balance', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  body: 'phone=+1234567890'
})
.then(response => response.json())
.then(data => console.log(data));

// Send money
fetch('http://localhost:8080/admin/sendMoney', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  body: 'fromPhone=+1234567890&toPhone=+0987654321&amount=50.00'
})
.then(response => response.text())
.then(data => console.log(data));
```

### Python Requests

```python
import requests

# Add a user
response = requests.post('http://localhost:8080/admin/addUser', data={
    'phone': '+1234567890',
    'pin': '1234',
    'balance': 100.00
})
print(response.text)

# Get all users
response = requests.get('http://localhost:8080/api/users')
print(response.json())

# Get user balance
response = requests.post('http://localhost:8080/api/users/balance', data={
    'phone': '+1234567890'
})
print(response.json())

# Send money
response = requests.post('http://localhost:8080/admin/sendMoney', data={
    'fromPhone': '+1234567890',
    'toPhone': '+0987654321',
    'amount': 50.00
})
print(response.text)
```

### cURL Commands

```bash
# Add a user
curl -X POST http://localhost:8080/admin/addUser \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&pin=1234&balance=100.00"

# Get all users (JSON)
curl -X GET http://localhost:8080/api/users

# Get all users (XML)
curl -X GET http://localhost:8080/api/users/xml

# Get user balance
curl -X POST http://localhost:8080/api/users/balance \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"

# Get user transactions
curl -X POST http://localhost:8080/api/users/transactions \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"

# Send money
curl -X POST http://localhost:8080/admin/sendMoney \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "fromPhone=+1234567890&toPhone=+0987654321&amount=50.00"

# Buy airtime
curl -X POST http://localhost:8080/admin/buyAirtime \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&amount=20.00"

# Pay with MoMo
curl -X POST http://localhost:8080/admin/payWithMoMo \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&merchant=SuperMarket&amount=75.00"

# Pay bill
curl -X POST http://localhost:8080/admin/payBill \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&biller=ElectricityCompany&amount=120.00"

# Savings (save money)
curl -X POST http://localhost:8080/admin/savingsLoans \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&action=save&amount=200.00"

# Request loan
curl -X POST http://localhost:8080/admin/savingsLoans \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&action=loan&amount=500.00"

# Buy insurance
curl -X POST http://localhost:8080/admin/financialServices \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&service=insurance&amount=150.00"

# Make investment
curl -X POST http://localhost:8080/admin/investInsure \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&action=invest&amount=1000.00"
```

## Error Handling

### Common Error Responses

```json
{
  "error": "User not found"
}

{
  "error": "Insufficient funds"
}

{
  "error": "Invalid amount"
}
```

### HTTP Status Codes

- `200` - Success
- `400` - Bad Request (invalid parameters)
- `404` - Resource not found
- `500` - Internal server error

## Admin Panel Routes

### Web Interface Routes

- `/admin` - Main admin dashboard
- `/admin/users` - User management page
- `/admin/services` - Service operations page
- `/admin/transactions` - Transaction management page
- `/admin/api-docs` - API documentation page

## Learning Mobile Money Services

### Typical Mobile Money Flow

1. **User Registration**: Add users with initial balance
2. **Money Transfer**: Send money between users
3. **Airtime Purchase**: Buy airtime for phone numbers
4. **Bill Payments**: Pay utilities and services
5. **Merchant Payments**: Pay at stores and businesses
6. **Financial Services**: Access insurance and investment services
7. **Savings & Loans**: Save money or request loans

### Example Learning Scenario

```bash
# 1. Create two users
curl -X POST http://localhost:8080/admin/addUser \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&pin=1234&balance=1000.00"

curl -X POST http://localhost:8080/admin/addUser \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+0987654321&pin=5678&balance=500.00"

# 2. Check initial balances
curl -X POST http://localhost:8080/api/users/balance \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"

# 3. Send money from user 1 to user 2
curl -X POST http://localhost:8080/admin/sendMoney \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "fromPhone=+1234567890&toPhone=+0987654321&amount=100.00"

# 4. Check balances after transfer
curl -X POST http://localhost:8080/api/users/balance \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"

curl -X POST http://localhost:8080/api/users/balance \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+0987654321"

# 5. View transaction history
curl -X POST http://localhost:8080/api/users/transactions \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"
```

## Building Your Own Application

### Mobile App Integration

You can use these APIs to build a mobile money application:

1. **User Interface**: Create forms for user registration and service operations
2. **API Integration**: Use the REST endpoints to communicate with the backend
3. **Transaction History**: Display user transactions and balances
4. **Service Menu**: Implement USSD-style menus for different services

### Web Dashboard

Build a web dashboard for monitoring:

1. **Real-time Updates**: Poll the API for user and transaction data
2. **Analytics**: Create charts showing transaction patterns
3. **Admin Controls**: Manage users and transactions
4. **Reporting**: Generate reports on system usage

### Testing Framework

Use Sparrow as a testing backend:

1. **Automated Tests**: Write tests that use the API endpoints
2. **Load Testing**: Test system performance with multiple users
3. **Integration Testing**: Test your mobile money app against Sparrow
4. **Mock Services**: Use for development when real services aren't available

## Project Structure

```
sparrow/
├── src/main/java/
│   ├── adapters/          # Service adapters for different operations
│   ├── core/              # Core business logic
│   ├── handlers/          # HTTP request handlers
│   └── ui/                # Spring MVC controllers and UI
├── src/main/resources/
│   └── templates/         # Thymeleaf templates
├── pom.xml               # Maven configuration
└── README.md             # Project documentation
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions or issues:
- Open an issue on GitHub
- Check the API documentation at `/admin/api-docs`
- Review the source code for implementation details
