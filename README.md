# bomberz

A Java Maven project for building and running a JVM application.

## Setup

### Prerequisites
- Java 17+ (or the version required by pom.xml)
- Maven 3.9+

### Build

mvn clean package

### Run
For CLI or standard entry-point projects:

mvn exec:java

## Notes
- Keep secrets in environment variables rather than committed files.
- Sensitive files are intentionally excluded through .gitignore rules.
