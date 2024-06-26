package com.solutie;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.solutie.customer.Customer;
import com.solutie.customer.CustomerRepository;
import com.solutie.customer.Gender;
import io.jsonwebtoken.security.Password;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository,
                             PasswordEncoder passwordEncoder){
        return args -> {

            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstname = name.firstName();
            String lastname = name.lastName();
            int age = random.nextInt(19, 99);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Customer customer = new Customer(
                    firstname + '.' + lastname + "@amigoscode.com",
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    firstname + ' ' + lastname,
                    age,
                    gender
            );
            customerRepository.save(customer);
        };

    }



    /*@GetMapping("/greet")
    public GreetResponse greet(@RequestParam(value = "name", required=false) String name){
        String greetMessage = name == null || name.isBlank() ? "Hello" : "Hello " + name;
        GreetResponse greetResponse = new GreetResponse(
                greetMessage,
                List.of("Java", "Javascript", "Golang"),
                new Person("guzman", 28,30_000)
        );
        return greetResponse;
    }

    record Person(String name, int age, double salary){

    }*/

   /* record GreetResponse(
            String greet,
            List<String> favProgrammingLanguages,
            Person person
                         ){}*/
}
