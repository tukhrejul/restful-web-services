package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<User>();
    private static int userCount = 0;
    static
    {
        users.add(new User(++userCount,"Shamim", LocalDate.now().minusYears(37)));
        users.add(new User(++userCount,"Curzon", LocalDate.now().minusYears(41)));
        users.add(new User(++userCount,"Poran", LocalDate.now().minusYears(36)));
        users.add(new User(++userCount,"Rashed", LocalDate.now().minusYears(37)));
        users.add(new User(++userCount,"Kamal", LocalDate.now().minusYears(38)));
        users.add(new User(++userCount,"Fahim", LocalDate.now().minusYears(40)));
    }

    public List<User> getAllUsers()
    {
        return users;
    }

    public User getUser(Integer id)
    {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
       // return users.get(Id-1);
    }

    public  void deleteUserById(Integer id)
    {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }

    public User saveUser(User user)
    {
        user.setId(++userCount);
        users.add(user);

        return user;
    }
}
