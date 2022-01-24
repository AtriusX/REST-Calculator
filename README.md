# REST Calculator

This application is a 2-day project to get to learn a bit about Arrow and Spring Boot. The project is primarily a math expression processor, which uses Arrow to manage errors rather than through exceptions.

The math expression processor is accessed via a REST API built via spring boot. Clients can send requests such as `/calc/2+2` or `/calc/(4 + 10) / 5 ^ 2` and it will respond back with `4` or `0.56` respectively.

## Docker support

The application is docker-ready. If you have it installed and prefer to run your applications through it, you can simply run the `runDocker` gradle task, and it will build the application, containerize it, and then deploy it to your docker runtime.

Accessing the application can be done via `localhost:8080`.