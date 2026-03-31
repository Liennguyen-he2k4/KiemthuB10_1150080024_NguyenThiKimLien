# Selenium Framework - Lab 11

Project kiểm thử tự động bằng Selenium WebDriver + TestNG + Maven.

## Chạy local
mvn -f kimlien/SeleniumFramework/pom.xml clean test "-Dbrowser=chrome" "-Denv=dev" "-DsuiteXmlFile=src/test/resources/testng-smoke.xml"

## GitHub Actions
- Workflow chạy tự động khi push lên main
- Có thể chạy thủ công bằng workflow_dispatch
- Chrome chạy headless trên CI