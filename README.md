# Users Service
Demo project that calls the API at https://bpdts-test-app.herokuapp.com/ and returns people who 
are listed as either living in London, or whose current coordinates are within 50 miles of London.

The swagger document describing the API for this demo project is contained in the root folder (swagger.yaml).

# Main Dependencies
- Maven
- Java JRE 1.8+
- SpringBoot 2.3.2.RELEASE
- GeographicLib-Java 1.50
- Docker (if you run `mvn package`)

# Notes
The location of London was retrieved from https://www.latlong.net/place/london-the-uk-14153.html which
defines its location as 
`Latitude	51.509865
Longitude	-0.118092`

The calculation to determine the distance between any two points uses a geodesic library built by 
Geographiclib (https://geographiclib.sourceforge.io/)

The service has been written to accept any city value as a path parameter but currently only includes
proximity functionality for London. Other cities will return Users if they match on the User's city of residence.

Where no Users are found, an empty collection and an Http status 200 are returned, mimicking the backend service's
behaviour. 

The `application.properties` file holds a property, `proximity.distance.miles` that may be amended to change the 
value of how close 'proximity' is determined to be.

# Sorting
Matching Users are sorted by id.

# Run tests and build application
`$ mvn clean package` 

This will create 
- a jar file `target/user-service.jar` 
- a docker image named `uk.co.hawdon/user-service` with tag `0.0.1-SNAPSHOT` in your local repository.

# Run as jar application
`java -jar target/user-service.jar`

# Run as Docker container
`docker run -p 8099:8099 uk.co.hawdon/user-service:0.0.1-SNAPSHOT`

# Example Query
`$ curl http://localhost:8099/v1/users/London -H "accept: application/json"`

# Logging
Each request/response is logged at INFO level with a unique correlationId
(examples)
- `{type: request, path: /v1/users/London, correlationId: eb3637fd-dae0-4419-86c0-f281e336a96b}`
- `{type: response, path: /v1/users/London, correlationId: eb3637fd-dae0-4419-86c0-f281e336a96b}`

# Health
The application can be queried via `http://localhost:8099/actuator/health`, returning `{"status":"UP"}` when healthy.