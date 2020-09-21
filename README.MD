Run docker

```
./mvnw package
docker build -t amortisation-backend .
docker run -d -p 8081:8081 amortisation-backend
```