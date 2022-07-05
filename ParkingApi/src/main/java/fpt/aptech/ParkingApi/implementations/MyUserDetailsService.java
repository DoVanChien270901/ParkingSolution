package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.entities.Account;
import fpt.aptech.ParkingApi.interfaces.IAccount;
import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.*;
import fpt.aptech.ParkingApi.repositorys.AccountRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;

/**
 *
 * @author CHIEN
 */
@Service
public class MyUserDetailsService implements UserDetailsService, IAccount {

    @Autowired
    private AccountRepo _accountRepository;
    @Autowired
    private ProfileRepo _profileRepository;
    @Autowired
    private ModelMapperUtil _mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = _accountRepository.getByUserName(username);
        if (acc != null) {
            List<GrantedAuthority> authoritys = new ArrayList<>();
            authoritys.add(new SimpleGrantedAuthority(acc.getRole()));
            return (UserDetails) new User(acc.getUsername(), acc.getPassword(), authoritys);
        }
        throw new UsernameNotFoundException(username);
    }

    @Override
    public boolean create(RegisterReq registerReq) {
        Account account = _mapper.map(registerReq, Account.class);
        Account acc = _accountRepository.getByUserName(account.getUsername());
        if (acc == null) {
            account.setPassword(encryPassword(account.getPassword()));
            account.setRole("user");
            _accountRepository.save(account);
            return true;
        } else {
            return false;
        }
    }

    public static String encryPassword(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pass);
    }

    @Override
    public List<Account> getAccount() {
        return _accountRepository.findAll();
    }

    @Override
    public Account getAccountById(String id) {
        Account a = _accountRepository.getByUserName(id);
        return a;
    }

    @Override
    public List<Account> findAll() {
        return _accountRepository.findAll();
    }
}
