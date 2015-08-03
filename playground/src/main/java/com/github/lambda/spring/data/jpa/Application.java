package com.github.lambda.spring.data.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

// ref: https://spring.io/guides/gs/accessing-data-jpa/
@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Customer("Jack", "NY"));
        repository.save(new Customer("Chloe", "CA"));
        repository.save(new Customer("Jack", "CA"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer by ID
        Customer customer = repository.findOne(1L);
        System.out.println("Customer found with findOne(1L):");
        System.out.println("--------------------------------");
        System.out.println(customer);
        System.out.println();

        // fetch customers by name
        System.out.println("Customer found with findByName('Jack'):");
        System.out.println("--------------------------------------------");
        for (Customer jack : repository.findByName("Jack")) {
            System.out.println(jack);
        }

        // fetch customers by address
        System.out.println("Customer found with findByName('CA'):");
        System.out.println("--------------------------------------------");
        for (Customer c : repository.findByAddress("CA")) {
            System.out.println(c);
        }
    }
}
