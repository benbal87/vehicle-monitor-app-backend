# Vehicle Monitoring Application Backend

## Prerequisites

Before starting the application, ensure you have the following prerequisites installed on your machine:

1. **Java SDK (version 22)**:
    - You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html).

2. **Apache Maven**:
    - Instructions for installation can be found [here](https://maven.apache.org/install.html).

3. **PostgreSQL**:
    - You can download and install PostgreSQL from [here](https://www.postgresql.org/download/).

4. **Python**:
    - You can download Python from [here](https://www.python.org/downloads/).
    - Ensure you have `pip` installed. You can check by running `pip --version`.

## Starting the Application

### 1. Start the Java Spring Boot Application

Navigate to the root directory of your Spring Boot project and execute the following command:

```sh
mvn spring-boot:run
```

This will start the main backend application.

### 2. Start the Python Mock Backend (Vehicle Simulator)

The vehicle simulator sends data to the database to mock vehicle data. Follow these steps to start the simulator:

1. Navigate to the `vehicle-simulator` directory:

   ```sh
   cd vehicle-simulator
   ```

2. Create and activate the virtual environment:

    - On **Linux**:

      ```sh
      . venv/bin/activate
      ```

    - On **Windows**:

      ```sh
      venv\Scripts\activate
      ```

3. Install the required dependencies:

   ```sh
   pip install -r requirements.txt
   ```

4. Run the `vehicles.py` script with the appropriate arguments. Use the following command template:

   ```sh
   python vehicles.py <ip> <port> <path> --num <number_of_vehicles> --lat <initial_latitude> --lon <initial_longitude>
   ```

   Example command:

   ```sh
   python vehicles.py 127.0.0.1 8080 "/api/v1/vehicle" --num 5 --lat 47.497913 --lon 19.040236
   ```

### 3. Start the Frontend Application

To visualize the data, you can use the Vehicle Monitor App Frontend. The frontend application can be found at the following repository:

[Vehicle Monitor App Frontend](https://github.com/benbal87/vehicle-monitor-app-frontend)

Follow the instructions in the frontend repository's README to set it up and start the application.

---

That's it! You should now have the full stack running with data being populated in your database by the vehicle simulator and can view the results in the frontend application.
