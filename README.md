# MedintelRx

MedintelRx is a medication management platform focused on safe intake tracking. Users can register/login, save medications, and record intakes while system checks drug-drug interaction rules against normalized [RxNorm](https://lhncbc.nlm.nih.gov/RxNav/APIs/RxNormAPIs.html) data via [RxNav In-a-box](https://lhncbc.nlm.nih.gov/RxNav/applications/RxNav-in-a-Box.html). The Spring Boot backend uses JWT authentication and persists data in Postgres.

## Table of Contents
-[Installation](#installation)
-[File Tree](#tree)
-[Usage](#usage)
-[License](#license)

# Installation

**Prereqs**
- JDK 21
- Docker (postgres)
- RxNav In-a-box 

```bash
    git clone https://github.com/aaronhuan/MedintelRx.git
```

**Steps**
1. Start Postgres & RxNav In-a-box
```bash 
    docker compose up -d
```
2. Set environment variables: JWT_SECRET=your_32_character_jwt_secret_here_1234567890

3. Run the app.: `./mvnw spring-boot:run`


### Notes
- If you use RxNav locally, set `rxnav.base-url` in `application.properties` or env vars.
- No npm install needed for the backend.


# Tree
```text
📦MedintelRx
 ┣ 📂.mvn
 ┃ ┗ 📂wrapper
 ┃ ┃ ┗ 📜maven-wrapper.properties
 ┣ 📂src
 ┃ ┣ 📂main
 ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┗ 📂aaronhuang
 ┃ ┃ ┃ ┃ ┃ ┗ 📂medintel
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RestClientConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AvoidanceWindowController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IntakeEventController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InteractionController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MedicationController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserProfileController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂interaction
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DetectedConflict.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IntakeEvaluationRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InteractionEngine.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InteractionEngineImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InteractionResult.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜InteractionRule.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AvoidType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReminderCadence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Severity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AvoidanceWindow.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommonFood.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IntakeEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Medication.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedication.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationReminder.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserProfile.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RegisterRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AvoidanceWindowRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommonFoodRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IntakeEventRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MedicationRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationReminderRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserProfileRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂rxnav-in-a-box
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂rxnav-in-a-box-20260105
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AuthService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AvoidanceWindowService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IntakeEventService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InteractionService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RuleModels.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RuleService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RxNavClient.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RxNavModels.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationReminderService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserMedicationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserProfileService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MedintelApplication.java
 ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┣ 📜application.properties
 ┃ ┃ ┃ ┗ 📜rules.json
 ┣ 📜.env.example
 ┣ 📜.gitattributes
 ┣ 📜.gitignore
 ┣ 📜docker-compose.yml
 ┣ 📜mvnw
 ┣ 📜mvnw.cmd
 ┣ 📜pom.xml
 ┗ 📜README.md
 ```

# Usage

# License