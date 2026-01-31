package com.calebtl.blog.repositories;

import com.calebtl.blog.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
