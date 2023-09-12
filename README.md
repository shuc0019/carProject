How I covered the learning goals
ssss
- The idea with, and reasons for why to use, a ORM-mapper

  Speeds up development, makes it easier to change database

- The meaning of the terms JPA, Hibernate and Spring Data JPA and how they are connected


- How to create simple Java entities and map them to a database via the Spring Data API

   See how I implemented the Car and Member Entities
- How to control the mapping between individual fields in an Entity class and their matching columns in the database

   See my use of the @Column annotation in the Car entity class
  
- How to auto generate IDs, and how to ensure we are using  a specific database's preferred way of doing it (Auto Increment in our case for  MySQL)

   @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

- How to use and define repositories and relevant query methods using Spring Data JPAs repository pattern
   See the two repositories in the repository package
  
- How to write simple "integration" tests, using H2 as a mock-database instead of MySQL
  
   I did not have time for that
- How to add (dev) connection details for you local MySQL database

  I added the values in a configuratioin setup (values for this)

spring.datasource.url=${JDBC_DATABASE_URL}

spring.datasource.username=${JDBC_USERNAME}

spring.datasource.password=${JDBC_PASSWORD}
  
   How you did that
