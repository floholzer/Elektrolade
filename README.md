![logo_elektrolade](https://github.com/floholzer/Elektrolade/assets/13693792/271f2fd4-60b5-43cb-a4a2-6f3f77c1c8a1)


# Elektrolade - Charging Stations
Elektrolade is a distributed system designed to generate invoices for customers of electric charging
stations. The system integrates a SpringBoot REST-API, RabbitMQ message queue, and a JavaFX UI. In the UI, the user can
input a customer ID to generate an invoice, which is then downloaded as a PDF file to the local file system.

## Services
- **Customer Database**
	- Contains customer data (id, first name, last name)
	- URL: localhost:30001
- **Stations Database**
	- Contains station data (id, db_url, latitude, longitude)
	- URL: localhost:30002
- **Individual Station Databases**
	- Contains customer station data (id, kwh, customer_id)
	- URL Station 1: localhost:30011
	- URL Station 2: localhost:30012
	- URL Station 3: localhost:30013
- **Queue**
	- URL: localhost:30003
	- Web: localhost:30083

## Requirements
- [Docker](https://docs.docker.com/get-docker/)

## Start
Run this command directly from here:
```shell
docker-compose up
```
Use the predefined Run/Debug Configurations in your IDE. (Top right corner)\
**Start Services and API** - Starts all services and the API\
**StationUI** - To start the JavaFX UI

## RabbitMQ-Dashboard
- [RabbitMQ-Dashboard](http://localhost:30083)
- Username: guest
- Password: guest


## Documentations
- [RabbitMQ](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)
- [GitHub](https://github.com/floholzer/Elektrolade)

### Queue Names
- RedQueue = start_queue
- GreenQueue = stations_info_queue
- PurpleQueue = job_notify_queue
- BlueQueue = station_data_queue
- YellowQueue = pdf_data_queue

## General
- JavaFX: Used to create the user interface (UI).
- Java Spring Boot: Provides the REST-based API.
- RabbitMQ: Manages the message queue for communication between components.

## Workflow
- User Interaction:
  The user inputs a customer ID into the JavaFX UI and clicks the "Generate Invoice" button.
- API Request:
  The UI sends an HTTP request to the Spring Boot REST-based API to initiate the invoice generation process.
- Data Gathering:
  Upon receiving the request, the application starts a new data gathering job for the specified customer.
- Message Queue:
  The data gathering process is coordinated via RabbitMQ message queues.
- PDF Generation:
  Once the data is gathered, it is sent to a PDF generator service.
  The PDF generator creates the invoice and saves it to the file system.
- Polling for Completion:
  The UI periodically checks (polls) the file system to see if the invoice PDF has been generated and is available.

## Diagrams
![uml_elektrolade](https://github.com/floholzer/Elektrolade/assets/13693792/ff5b1e45-b449-4c4b-adf1-82fe8a6267a4)
*UML Diagram*
