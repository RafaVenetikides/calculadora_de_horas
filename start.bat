@echo off
REM Start Spring Boot backend
start cmd /c "cd back\calc_backend && mvn spring-boot:run"

REM Start Flutter frontend
start cmd /c "cd front\calc_frontend && flutter run -d chrome --web-port=5000"