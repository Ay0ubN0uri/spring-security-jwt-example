package com.a00n.springsecurityjwtexample;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.a00n.springsecurityjwtexample.entities.Role;
import com.a00n.springsecurityjwtexample.entities.Todo;
import com.a00n.springsecurityjwtexample.entities.User;
import com.a00n.springsecurityjwtexample.repositories.RoleRepository;
import com.a00n.springsecurityjwtexample.repositories.TodoRepository;
import com.a00n.springsecurityjwtexample.repositories.UserRepository;

@SpringBootApplication
public class SpringSecurityJwtExampleApplication {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			Role adminRole = Role.builder().name("ROLE_ADMIN").build();
			Role userRole = Role.builder().name("ROLE_USER").build();
			Role managerRole = Role.builder().name("ROLE_MANAGER").build();
			adminRole = roleRepository.save(adminRole);
			userRole = roleRepository.save(userRole);
			managerRole = roleRepository.save(managerRole);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			var admin = User.builder()
					.firstName("admin")
					.lastName("nouri")
					.username("admin").email("admin@gmail.com")
					.password(passwordEncoder.encode("admin"))
					.roles(Set.of(adminRole, userRole, managerRole))
					.build();
			var user = User.builder()
					.firstName("user")
					.lastName("user")
					.username("user").email("user@gmail.com")
					.password(passwordEncoder.encode("user"))
					.roles(Set.of(userRole))
					.build();
			var manager = User.builder()
					.firstName("manager")
					.lastName("manager")
					.username("manager").email("manager@gmail.com")
					.password(passwordEncoder.encode("manager"))
					.roles(Set.of(managerRole))
					.build();
			userRepository.save(admin);
			userRepository.save(user);
			userRepository.save(manager);

			todoRepository.save(Todo.builder().name("todo 1").user(admin).build());
			todoRepository.save(Todo.builder().name("todo 2").user(admin).build());
			todoRepository.save(Todo.builder().name("todo 3").user(user).build());

			System.out.println(userRepository.findAll());
		};
	}

}
