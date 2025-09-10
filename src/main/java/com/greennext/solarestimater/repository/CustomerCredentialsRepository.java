package com.greennext.solarestimater.repository;

import com.greennext.solarestimater.model.CustomerCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerCredentialsRepository extends JpaRepository<CustomerCredentials, Long> {

    Optional<CustomerCredentials> findByUsername(String username);

    Optional<CustomerCredentials> findByCustomerUserId(String userId);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE CustomerCredentials c SET c.lastLoginDate = :loginDate WHERE c.username = :username")
    void updateLastLoginDate(@Param("username") String username, @Param("loginDate") LocalDateTime loginDate);

    @Modifying
    @Query("UPDATE CustomerCredentials c SET c.isActive = :status WHERE c.username = :username")
    void updateActiveStatus(@Param("username") String username, @Param("status") boolean status);
}
