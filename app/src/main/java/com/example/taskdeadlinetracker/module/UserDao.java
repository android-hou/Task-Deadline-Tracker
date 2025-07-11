package com.example.taskdeadlinetracker.module;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    long insertUser(Entity_User user);

    @Query("SELECT * FROM users WHERE username = :username OR email = :email LIMIT 1")
    Entity_User findByUsernameOrEmail(String username, String email);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    Entity_User findByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Entity_User findByEmail(String email);
}
