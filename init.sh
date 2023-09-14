cd Commons/ && ./gradlew pTML -x test
cd ../Backend-Commands/ && ./gradlew build -x test
cd ../Backend-Queries/ && ./gradlew build -x test
cd ../ && docker compose up --build