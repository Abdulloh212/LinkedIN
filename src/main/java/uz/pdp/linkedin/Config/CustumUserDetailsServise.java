package uz.pdp.linkedin.Config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.linkedin.Repo.UserRepository;

@Service
public class CustumUserDetailsServise implements UserDetailsService {

    private final UserRepository usersRepository;

    public CustumUserDetailsServise(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email).orElseThrow();
    }
}
