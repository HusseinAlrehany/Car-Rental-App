package com.coding.car_rental_app.repository;

import com.coding.car_rental_app.entity.User;
import com.coding.car_rental_app.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
}
