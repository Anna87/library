This is Spring Boot application with micro service architecture.   As a classic micro service architecture, my application has a service registry(eureka) and gateway(zuul). Application is event driver(ActiveMQ) plus I used Spring Feigth client for server-to-server requests.

I've invested a lot into security part. Project has security model with 2 roles: «Admin» and «User». For autorization maintance I've used confirmation autorization via emals with tokens and expiry date.

Main project "library" is based on Spring MVC Framework, that allows to use CRUD operations for working with books withing library. Main models are book, holder and borrow. I've used MongoDB for store the data and Lombok for objects creations/edits. There are also user notifications via email for registration and business notifications.
