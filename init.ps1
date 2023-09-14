Set-Location -Path "Commons/" ; ./gradlew pTML -x test
Set-Location -Path "../Backend-Commands/" ; ./gradlew build -x test
Set-Location -Path "../Backend-Queries/" ; ./gradlew build -x test
Set-Location -Path "../" ; docker compose up --build