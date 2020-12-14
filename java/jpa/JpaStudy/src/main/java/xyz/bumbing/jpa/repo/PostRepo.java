package xyz.bumbing.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.bumbing.jpa.entity.Post;

public interface PostRepo extends JpaRepository<Post, Long> {

}
