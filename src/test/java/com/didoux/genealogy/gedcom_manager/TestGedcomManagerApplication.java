package com.didoux.genealogy.gedcom_manager;

import org.springframework.boot.SpringApplication;

public class TestGedcomManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(GedcomManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
