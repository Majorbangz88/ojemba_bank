package com.big_joe.ojemba_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Ojemba Bank Plc Application",
                description = "Backend REST API's for Ojemba Bank",
                version = "v1.0",
                contact = @Contact(
                        name = "Chukwu Joel Chimaobi",
                        email = "joelchimaobichukwu@gmail.com",
                        url = "https://github.com/Majorbangz88/ojemba_bank.git"
                ),
                license = @License(
                        name = "Ojemba Bank Plc",
                        url = "https://github.com/Majorbangz88/ojemba_bank.git"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Ojemba Bank Plc Application Documentation",
                url = "https://github.com/Majorbangz88/ojemba_bank.git"
        )
)
public class OjembaBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjembaBankApplication.class, args);
    }

}
