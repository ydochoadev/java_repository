package com.danicode.eats_hub_catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EatsHubCatalogApplication implements CommandLineRunner {

    private Repo repo;

    public EatsHubCatalogApplication(Repo repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        this.repo.findAll()
                .doOnNext(System.out::println)
                .subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(EatsHubCatalogApplication.class, args);
    }

}
