package uz.pdp.linkedin.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.linkedin.Repo.AttachmentRepository;
import uz.pdp.linkedin.Repo.RoleRepository;
import uz.pdp.linkedin.Repo.UserRepository;


import java.util.List;
@Component
public class Run implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String isHad;

    @Override
    public void run(String... args) throws Exception {

    }

}
