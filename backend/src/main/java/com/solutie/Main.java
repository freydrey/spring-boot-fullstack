package com.solutie;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.solutie.customer.Customer;
import com.solutie.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {

            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstname = name.firstName();
            String lastname = name.lastName();
            Customer customer = new Customer(
                    firstname + ' ' + lastname,
                    firstname + '.' + lastname + "@amigoscode.com",
                    random.nextInt(19,99)
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
