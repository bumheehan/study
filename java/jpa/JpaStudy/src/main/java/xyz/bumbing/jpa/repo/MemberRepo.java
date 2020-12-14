package xyz.bumbing.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.bumbing.jpa.entity.Member;

public interface MemberRepo extends JpaRepository<Member, Long> {

}
