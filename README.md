# Spring Auth Application

A secure Spring Boot authentication application with comprehensive CI/CD pipeline and Grype security scanning.

## Features

### 🔐 Security Features
- **Spring Security** - Robust authentication and authorization
- **OAuth2 Integration** - Third-party authentication support
- **2FA/TOTP Support** - Two-factor authentication
- **Secure Sessions** - Protected session management

### 🚀 CI/CD Pipeline
- **GitHub Actions** - Automated build, test, and deployment
- **Multi-architecture Builds** - Support for amd64 and arm64
- **Automated Testing** - Unit and integration tests
- **Docker Containerization** - Consistent deployment environments

### 🔍 DevSecOps
- **Grype Vulnerability Scanning** - Comprehensive dependency and container scanning
- **SBOM Generation** - Software Bill of Materials with Syft
- **Security Gates** - Automatic blocking on critical vulnerabilities
- **GitHub Advanced Security** - Integrated security reporting

## Quick Start

### Prerequisites
- Java 17 or later
- Docker (optional, for containerized deployment)
- Git

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd spring-auth-app
   ```

2. **Build the application**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the application**
   - Open http://localhost:8080 in your browser
   - Login with default credentials: `admin` / `password`

### Docker Deployment

1. **Build Docker image**
   ```bash
   docker build -t spring-auth-app:latest .
   ```

2. **Run the container**
   ```bash
   docker run -p 8080:8080 spring-auth-app:latest
   ```

## Project Structure

```
spring-auth-app/
├── src/main/java/com/example/auth/    # Java source code
│   ├── AuthApplication.java           # Main application class
│   ├── HomeController.java            # Web controllers
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/                # Configuration and templates
│   ├── application.properties          # Application configuration
│   └── templates/                     # Thymeleaf templates
├── .github/workflows/                 # GitHub Actions workflows
│   ├── ci.yml                        # CI pipeline
│   ├── build-and-scan.yml            # Build and security scan
│   └── ...
├── .grype.yml                         # Grype configuration
├── build.gradle                      # Gradle build script
├── Dockerfile                         # Docker configuration
└── README.md                          # This file
```

## Security Scanning

### Grype Configuration
The project includes comprehensive Grype configuration (`.grype.yml`) that:
- Scans both source dependencies and container images
- Fails builds on critical vulnerabilities
- Generates SARIF reports for GitHub Advanced Security
- Supports custom vulnerability database configurations

### Security Policy
Our security policy includes:
- **Critical vulnerabilities**: Block deployment immediately
- **High vulnerabilities**: Warn with 7-day SLA for remediation
- **Medium vulnerabilities**: Log only with 30-day SLA
- **Low vulnerabilities**: Address in next maintenance cycle

## CI/CD Pipeline

### Workflows

#### 1. CI Pipeline (`.github/workflows/ci.yml`)
- **Triggers**: Push and pull requests
- **Builds**: Java 17 compilation and testing
- **Quality**: SpotBugs static code analysis
- **Security**: Grype dependency scanning

#### 2. Build & Security Scan (`.github/workflows/build-and-scan.yml`)
- **Triggers**: Pushes and daily schedule
- **Build**: Multi-architecture Docker builds
- **Scan**: Grype container image scanning
- **Report**: Security vulnerability reports

### Deployment
The CI/CD pipeline supports:
- **Staging**: Automatic deployment on main branch
- **Production**: Manual approval required
- **Security Gates**: Automatic blocking on critical vulnerabilities

## Configuration

### Application Properties
Key configuration options in `src/main/resources/application.properties`:
```properties
# Server
server.port=8080

# Database (H2 for development)
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update

# Security
spring.security.user.name=admin
spring.security.user.password=password
```

### Environment Variables
- `JAVA_OPTS` - JVM options for the application
- `SERVER_PORT` - Server port (default: 8080)

## Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport
```

### Security Testing
The project includes automated security testing:
- **OWASP Dependency Check**: Via Grype scanning
- **Static Code Analysis**: Via SpotBugs
- **Container Security**: Via Grype image scanning

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Security Best Practices
- All dependencies are scanned for vulnerabilities
- Code is reviewed for security issues
- Containers are built from trusted base images
- Secrets are never committed to the repository

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Check the [Issues](../../issues) page
- Review the [Security Policy](SECURITY_POLICY.md)
- Consult the [GitHub Actions Setup Guide](GITHUB_ACTIONS_SETUP.md)

---

**Built with ❤️ using Spring Boot, GitHub Actions, and Grype**