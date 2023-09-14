# Introduction
Catering Ordering is a web application that allow us, as the name suggests, to order caterings.

However, contrary to what one might think, the goal was not to implement fully functional and visually appealing web application but to address some problems related to developing distributed systems as well as present some good practises in developing them. 

Before I move on to the next part I would like to underline this: A (μ)SOA is NOT a cure-all. It introduces a number of problems and the biggest one is the communication. 
According to L Peter Deutsch and others at Sun Microsystems the fallacies of distributed systems are:
1. The network is reliable;
2. Latency is zero;
3. Bandwidth is infinite;
4. The network is secure;
5. Topology doesn't change;
6. There is one administrator;
7. Transport cost is zero;
8. The network is homogeneous.
These are false assumptions that cause many issues in developing distributed systems.

Moving on to the project, these topics were addressed in it:

# 1. Asynchronous communication.
Popular synchronous communication is widely popularized but it's not so great when it comes to distributed systems.
Why's that?
Let's imagine the scenario when service A wants to talk to service B but service B is down.
Wouldn't it be nice for service A to be able to send a message to service B, don't wait for the reply and simply let the service B to process the message in its time?
Also, what if service A will have sudden peek of information processing which cannot be handled by service B at the same rate?
The answer for these problems can be an asynchronous communication.
Of course you can simulate asynchronous communication with HTTP protocol but it'll never be "fire-and-forget" as with, for example, message brokers like RabbitMQ which was used in this project.

Message queuing enables cooperation between processes that don't necessarily have to operate at the same time. This means that one of the communicating processes may be unavailable for a certain period. Messages sent between such processes, whether on the same or different computers, are transmitted using queues. In simple terms, if the process the message is intended for is, for some reason, unavailable, it can wait for it at the queue broker. This mechanism enhances the reliability of built systems by ensuring information delivery and can improve its performance by naturally introducing an asynchronous processing model.

OK but what if message broker goes down? You can create persistent queues which are a little bit slower but can survive message broker server restart.

RabbitMQ is an implementation of such message broker. It's written in Erlang, released by Ericsson, designed for building distributed systems utilizing a large number of threads and known for its high reliability. It's available for the most popular operating systems: Windows, Linux, and OS X. It boasts a rich list of supported languages and frameworks, including Java and Spring, .NET, Ruby, Python, PHP, Objective-C, Swift, C, C++, and many others.

# 2. CQRS and Event Sourcing
Before we delve into the main topic which is CQRS, it's worth familiarizing ourselves with the concept from which it directly originates. This is known as CQS, or Command Query Separation, introduced by Bertrand Meyer in 1986. Thus, it's evident that contrary to common belief, this is not something new. So, what is CQS? It's a principle stating that each method in the system should be classified into one of two groups:

Command - these are methods that alter the application's state and return nothing.
Query - these are methods that return something but do not alter the application's state.

There is a perfect phrase that encapsulates this idea well:

*"Question should not change the answer."*

Nearly 20 years after the "birth" of CQS, two notable figures, Greg Young and Udi Dahan, introduced its successor to the world - CQRS, Command Query Responsibility Segregation. The idea was quite simple. Why limit ourselves to dividing methods into those that retrieve data and those that change the state of our application? After all, we can design our system so that these tasks are handled by separate classes. This is the main distinction between the two approaches.

*"When speaking of CQS, we're thinking about methods. When speaking of CQRS, we're thinking about objects."*

Keep in mind that CQRS is only a design pattern, not an architectural style. It was even confirmed by Greg Young himself:

*"CQRS and Event Sourcing are not architectural styles. Service Oriented Architecture, Event Driven Architecture are examples of architectural styles.”*

When it comes to Event Sourcing it's role is to reconstruct the current state of the application (referencing domain objects) based on events stored in a data repository known as the Event Store. Keep in mind that this project is a simplified version and it simply uses write DB instead of event store.

Why all of this?
Classic N-Layer applications seem much simpler in comparison and don't complicate the system to this extent. Here's a few reasons that might convince you of this concept:

1. Asymmetric Scalability - By using two data sources, we can scale our application towards either reading or writing. Moreover, this approach allows us to design databases and choose technologies in a way that reading/writing operations are performed as quickly as possible.
One way of scaling our application further towards reading can be increasing the number of queries service and read databases. Keep in mind that much more of the users' actions will be reads than writes. Another way is to denormalize our database, create NoSQL one for reads, for example in redis.

2. Team Work Division - As you've probably noticed, the part responsible for reading data is much simpler than the one for writing. Additionally, it doesn't implement any business logic. This gives us the opportunity to divide tasks within the team so that less experienced developers can work on the reading part without worrying about changing the behavior of our application.

3. Microservices - Since the aggregates described earlier act as a binding element for a logical part of our domain, we can consider breaking down our monolithic application into smaller parts.

4. Reconstructing the Application State at Any Given Moment - As was mentioned before, ES allows us to reconstruct the current state of our application. However, there's nothing stopping us from applying events only up to a certain point, thereby obtaining the state of our domain from the past.

5. Natural Audit - By implementing Event Sourcing, we simultaneously ensure a very pleasant and detailed audit of our data.

One thing to add to the topis is that I went one step further than original CQRS assumption which was that CQRS is about objects and I've made two separate services out of it, so we have one service for commands and another one for queries.

# 3. Good practises in developing distributed systems
The worst thing that we can do when we encounter problems is not to return any response at all.
I've proposed a few good practises that can be used instead of lack of response. 
### 1. Fail Fast
It's much better to quickly signal an error than to try to handle it at a low level, for example, through repetitions.
    - For a database, sometimes a retry may make sense (e.g., in the case of deadlocks).
    - It's better to validate data before, rather than at the end of a transaction.
    - It's better to make sure we have all the necessary resources before starting time-consuming processing (e.g., calling external services).
To show this concept I've implemented fail fast in case of database problems.
### 2. Shed Load
Explicitly rejecting commands under high load (similarly to the TCP protocol).
### 3. Timeouts
Timeouts should be implemented everywhere that it's possible, so I approached this project in no other way. 
Speaking of timeouts, please keep in mind that increasing them in not a cure-all, contrary to what many developers think.
### 4. Handshaking
Instead of immediately asking the service for potentially large data (especially at the start or after a period of inactivity) we should first try with the simplest data or just a status.
### 5. Circuit breakers
Circuit breakers means that we consciously temporally refrain from making calls in case of some issues. 
In the project example: if a service detects an issue with the database like extended response time, it immediately reports the error and refrains from making calls to the database for some time period to allow the database time to recover.

# 4. Containerization

Containerization in one sentence is an answear to the common statement: "It works on my machine". The most popular containerization technology which I've used in this project is Docker.

Docker is an open-source software that automates the application deployment process. It shares a concept with virtual machines but is much lighter. A Docker container is a package that allows running individual processes. Unlike VMs, they run in the host operating system's kernel, minimizing additional overhead on execution time but also potentially reducing the security of the executed code. Due to their minimal disk space usage, they start up extremely quickly, allowing them to be used for executing short tasks, being created and removed multiple times (the microservices concept). They communicate with each other and the environment much like computers on a local network. Each of them has a unique IP address assigned to a virtual interface.

Containers work best for stateless applications, enabling easy horizontal scaling. Otherwise, the application's state should be stored outside the container, e.g., in a database or file system - although this is not recommended as it introduces additional dependencies. The statelessness of containers arises from the fact that they are re-created from an image with each startup.

Initially, only Linux containers existed; Microsoft began supporting this technology in Windows 10 (x64) and Server 2016. With the availability of Windows containers, so-called Nano Servers appeared within them, allowing the hosting of applications written in .NET, Java, JavaScript/Node.js, or Python/Django. The increasing popularity of Docker has led to their support by leading cloud computing providers.

Even though Docker is most popular containerization technology it has some drawbacks, especially when it comes to security. Podman addresses many of these issues like the necesity of root privileges or the requirement for a separate daemon to run containers. What's worth mentioning is that Docker recently added rootless mode to its daemon configuration and let's hope that they will continue such improvements.

I've created simple Dockerfiles for my services as well as Docker Compose which is a tool that helps you define and share multi-container applications.

# 5. How to launch this app?

In order to launch this app (or rather system if we consider that system=N*app + connections) please follow these steps:
1. Make sure you have JAVA_HOME properly set as an environment variable as well as added JAVA_HOME/bin to the path. For this project it should be Java 17.
2. Make sure you have docker daemon running.
3. Make .env file out of .env.sample file. Adjust mail credentials (MAIL_LOGIN and MAIL_PASSWORD).
4. Run ./init.ps1 if you're on Windows machine or ./init.sh if you're on Linux distro. It is likely that if you're on windows machine you'd first have to allow to execute powershell scripts. 

The script will first publish commonly used objects from Commons project to maven local and then build every spring app with them.

Keep in mind that it's not the best approach but it's the simplest one. 
It would be better to use some popular solutions for hosting, managing, and distributing binaries and artifacts like Artifactory or AWS CodeArtifact. 

With the proper approach we'd also be able to improve Dockerfiles by performing building there instead of copying already built artifacts, introducing multistage build or adding cache.




