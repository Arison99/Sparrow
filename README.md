# Sparrow Mobile Money Platform

![GitHub stars](https://img.shields.io/github/stars/arison99/Sparrow?style=social)
![GitHub forks](https://img.shields.io/github/forks/arison99/Sparrow?style=social)
![GitHub issues](https://img.shields.io/github/issues/arison99/Sparrow)
![GitHub license](https://img.shields.io/github/license/arison99/Sparrow)
![Java](https://img.shields.io/badge/Java-11%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.6-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-orange)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20MacOS-lightgrey)

## 🚀 Overview

**Sparrow** is a comprehensive mobile money simulation platform built with Spring Boot and modern web technologies. It provides a complete ecosystem for testing, developing, and learning mobile money services with both USSD simulation and RESTful API endpoints. The platform features a professional web-based admin dashboard, comprehensive service management, and extensive API documentation.

### Key Highlights
- **Enterprise-grade Architecture**: Built with Spring Boot 3.2.6 and Maven for scalability and maintainability
- **Dual Interface Support**: USSD simulation frontend and modern web-based admin interface
- **RESTful API**: Complete REST and XML endpoints for all mobile money operations
- **Professional UI**: Modern responsive design with Tailwind CSS
- **Comprehensive Documentation**: Extensive API documentation with examples and integration guides

## ✨ Features

### 💼 Core Services
- **💸 Send Money**: Secure peer-to-peer money transfers
- **📱 Buy Airtime**: Mobile airtime top-up services
- **🏪 Pay with MoMo**: Merchant payment processing
- **🧾 Bill Payments**: Utility and service bill payments
- **💰 Savings & Loans**: Financial savings and loan services
- **📈 Investment & Insurance**: Investment portfolios and insurance plans

### 🎯 Technical Features
- **🔌 RESTful API**: Complete REST and XML endpoints for all services
- **🌐 Web Dashboard**: Professional admin interface with real-time monitoring
- **📊 Transaction Management**: Comprehensive transaction tracking and history
- **👥 User Management**: Complete user registration and profile management
- **🔐 Security**: PIN-based authentication and secure transaction processing
- **📱 USSD Simulation**: Traditional USSD interface for mobile money operations

### 🛠️ Development Tools
- **📚 API Documentation**: Comprehensive documentation with cURL examples
- **🧪 Testing Framework**: Built-in testing capabilities for service validation
- **🔧 Service Management**: Individual service configuration and monitoring
- **📈 Analytics Dashboard**: Real-time service statistics and user metrics

### 🏗️ Architecture
- **🎯 Modular Design**: Clean separation of concerns with adapter pattern
- **⚡ Spring Boot**: Enterprise-grade framework with auto-configuration
- **🎨 Modern UI**: Responsive design with Tailwind CSS
- **🗄️ Database Ready**: H2 in-memory database with JPA support
- **🔄 Maven Build**: Standardized build process and dependency management

## 🛠️ Quick Start

### Prerequisites
- **Java 11+** - Oracle JDK or OpenJDK
- **Maven 3.6+** - For dependency management and build
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/arison99/Sparrow.git
   cd Sparrow
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the platforms**
   - **Web Admin Dashboard**: [http://localhost:8080/admin](http://localhost:8080/admin)
   - **API Documentation**: [http://localhost:8080/admin/api-docs](http://localhost:8080/admin/api-docs)
   - **USSD Simulator**: Run `MoMoUSDDApplication.java` for traditional USSD interface

### Docker Support (Optional)
```bash
# Build Docker image
docker build -t sparrow-momo .

# Run container
docker run -p 8080:8080 sparrow-momo
```

## 🚀 Usage

### Web Admin Dashboard
1. **Access the Dashboard**: Navigate to `http://localhost:8080/admin`
2. **User Management**: Add users, view balances, and manage accounts
3. **Service Operations**: Process transactions across all mobile money services
4. **Transaction Monitoring**: View real-time transaction history and analytics
5. **API Testing**: Use built-in tools to test REST and XML endpoints

### Individual Service Management
- **Send Money**: `/admin/service/send-money`
- **Buy Airtime**: `/admin/service/buy-airtime`
- **Pay with MoMo**: `/admin/service/pay-with-momo`
- **Pay Bills**: `/admin/service/pay-bill`
- **Savings & Loans**: `/admin/service/savings-loans`
- **Invest & Insure**: `/admin/service/invest-insure`

### API Integration
```bash
# Add a new user
curl -X POST http://localhost:8080/admin/addUser \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890&pin=1234&balance=1000.00"

# Send money
curl -X POST http://localhost:8080/admin/sendMoney \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "fromPhone=+1234567890&toPhone=+0987654321&amount=100.00"

# Get user balance
curl -X POST http://localhost:8080/api/users/balance \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "phone=+1234567890"
```

### USSD Simulation
1. Run `MoMoUSDDApplication.java` for traditional USSD interface
2. Navigate through menu options using number selection
3. Perform operations like balance inquiry, money transfer, and PIN management

## 📂 Project Architecture

```
Sparrow/
├── src/main/java/
│   ├── adapters/              # Service adapters for different operations
│   │   ├── BackendServiceSendMoneyAdapter.java
│   │   ├── BackendServiceAirtimeAdapter.java
│   │   ├── BackendServicePayWithMoMoAdapter.java
│   │   ├── BackendServicePaymentsAdapter.java
│   │   ├── BackendServiceSavingsLoansAdapter.java
│   │   ├── BackendServiceInvestInsureAdapter.java
│   │   ├── BackendServiceFinancialServicesAdapter.java
│   │   ├── BackendServiceJsonAdapter.java
│   │   └── BackendServiceXmlAdapter.java
│   ├── core/                  # Core business logic
│   │   ├── BackendService.java
│   │   ├── Menu.java
│   │   ├── MenuSystem.java
│   │   └── MenuSystemImpl.java
│   ├── handlers/              # HTTP request handlers
│   │   ├── Rest*Handler.java  # REST endpoint handlers
│   │   └── Xml*Handler.java   # XML endpoint handlers
│   └── ui/                    # User interface controllers
│       ├── AdminController.java
│       ├── BackendServer.java
│       └── MoMoUSDDApplication.java
├── src/main/resources/
│   └── templates/             # Thymeleaf templates
│       ├── admin.html         # Main dashboard
│       ├── users.html         # User management
│       ├── services.html      # Service overview
│       ├── transactions.html  # Transaction management
│       ├── api-docs.html      # API documentation
│       └── [service-name].html # Individual service pages
├── pom.xml                    # Maven configuration
├── API_DOCUMENTATION.md       # Comprehensive API guide
└── README.md                  # This file
```

### Design Patterns
- **Adapter Pattern**: Service adapters for different operation types
- **MVC Pattern**: Spring Boot controllers with Thymeleaf templates
- **Repository Pattern**: Data access abstraction
- **Factory Pattern**: Service creation and management

## 🎯 Use Cases

### 🏦 Financial Institution Testing
- **Service Validation**: Test mobile money services before production deployment
- **Load Testing**: Simulate high transaction volumes
- **Integration Testing**: Validate third-party integrations
- **Compliance Testing**: Ensure regulatory compliance

### 🎓 Educational Platform
- **Computer Science**: Learn about distributed systems and API design
- **Fintech Training**: Understand mobile money ecosystem
- **Software Engineering**: Study clean architecture and design patterns
- **API Development**: Practice RESTful service development

### 🔧 Development Framework
- **Prototype Development**: Rapid prototyping of mobile money features
- **API Testing**: Test client applications against realistic endpoints
- **Documentation**: Generate comprehensive API documentation
- **Mock Services**: Development environment for mobile money apps

### 📊 Research & Analytics
- **Transaction Pattern Analysis**: Study mobile money usage patterns
- **Service Performance**: Analyze service response times and reliability
- **User Behavior**: Understand mobile money user interactions
- **Market Research**: Simulate different market scenarios

## 🔧 Configuration

### Database Configuration
```properties
# H2 Database (default)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# MySQL Configuration (production)
spring.datasource.url=jdbc:mysql://localhost:3306/sparrow
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Application Properties
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Logging
logging.level.org.springframework=INFO
logging.level.ui=DEBUG

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
```

## 🔌 API Reference

### Authentication
All endpoints support form-based authentication. No API key required for development.

### Base URL
```
http://localhost:8080
```

### Core Endpoints

#### User Management
- `POST /admin/addUser` - Create new user
- `GET /api/users` - Get all users (JSON)
- `GET /api/users/xml` - Get all users (XML)
- `POST /api/users/balance` - Get user balance
- `POST /api/users/transactions` - Get user transactions

#### Service Operations
- `POST /admin/sendMoney` - Send money between users
- `POST /admin/buyAirtime` - Purchase airtime
- `POST /admin/payWithMoMo` - Pay merchant
- `POST /admin/payBill` - Pay utility bills
- `POST /admin/savingsLoans` - Savings and loan operations
- `POST /admin/investInsure` - Investment and insurance services

For detailed API documentation with examples, visit: `/admin/api-docs`

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### API Testing
Use the built-in API documentation page or tools like Postman:
- Import the API collection from `/admin/api-docs`
- Test all endpoints with sample data
- Validate JSON and XML responses

## 🚀 Deployment

### Production Deployment
1. **Build the JAR**
   ```bash
   mvn clean package
   ```

2. **Run the application**
   ```bash
   java -jar target/sparrow-1.0.0.jar
   ```

3. **Configure production database**
   ```bash
   java -jar target/sparrow-1.0.0.jar --spring.datasource.url=jdbc:mysql://localhost:3306/sparrow
   ```

### Cloud Deployment
- **AWS**: Deploy to Elastic Beanstalk or ECS
- **Google Cloud**: Use App Engine or Cloud Run
- **Azure**: Deploy to App Service or Container Instances
- **Heroku**: Direct deployment with Maven support

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Setup
1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Add tests** for new functionality
5. **Ensure all tests pass**
   ```bash
   mvn test
   ```
6. **Submit a pull request**

### Code Standards
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Write unit tests for new features
- Use consistent code formatting
- Update documentation for API changes

### Bug Reports
Please include:
- Java version and OS
- Steps to reproduce
- Expected vs actual behavior
- Stack traces if applicable

## 📈 Roadmap

### Version 2.0 (Upcoming)
- [ ] **Real-time Notifications**: WebSocket support for live updates
- [ ] **Advanced Analytics**: Transaction pattern analysis and reporting
- [ ] **Multi-currency Support**: Handle multiple currencies and exchange rates
- [ ] **Security Enhancements**: OAuth2 and JWT token authentication
- [ ] **Performance Monitoring**: Metrics and health check endpoints

### Version 2.1 (Future)
- [ ] **Microservices Architecture**: Break down into individual services
- [ ] **Message Queue Support**: RabbitMQ/Kafka for async processing
- [ ] **GraphQL API**: Alternative to REST endpoints
- [ ] **Mobile SDK**: Native mobile app development kit
- [ ] **Blockchain Integration**: Cryptocurrency transaction support



### Community Stats
- ⭐ **5+ GitHub Stars**
- 👥 **01 Contributor**
- 🐛 **95% Issue Resolution Rate**

## 📚 Resources

### Documentation
- [API Documentation](API_DOCUMENTATION.md)
- [Developer Guide](docs/DEVELOPER_GUIDE.md)
- [Deployment Guide](docs/DEPLOYMENT.md)
- [Security Guide](docs/SECURITY.md)



## 📜 License

This project is licensed under the [MIT License](LICENSE).

```
MIT License

Copyright (c) 2024 Sparrow Mobile Money Platform

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 🌟 Show Your Support

If you find Sparrow helpful, please consider:
- ⭐ **Star the repository** to show your support
- 🍴 **Fork the project** to contribute
- 🐛 **Report issues** to help improve the platform
- 📢 **Share with others** who might benefit from this project
- 💬 **Join our community** for discussions and support

## 📧 Contact & Support

### Maintainer
- **Name**: Harrison Arison
- **Email**: harrisondaviinci@gmail.com
- **GitHub**: [arison99](https://github.com/arison99)

### Support Channels
- **Issues**: [GitHub Issues](https://github.com/arison99/Sparrow/issues)
- **Discussions**: [GitHub Discussions](https://github.com/arison99/Sparrow/discussions)


### Business Inquiries
For commercial support, enterprise licensing, or custom development:
- **Email**: harrisondaviinci@gmail.com

---

<div align="center">
<p><strong>Built with ❤️ by the Sparrow Team</strong></p>
<p>Making mobile money accessible for everyone</p>

[⭐ Star on GitHub](https://github.com/arison99/Sparrow) | 
[📖 Documentation](API_DOCUMENTATION.md) | 
[🚀 Get Started](#-quick-start) | 
</div>
