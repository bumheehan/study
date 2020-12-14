package xyz.bumbing.jpa.cont;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.bumbing.jpa.dto.MemberDTO.MemberAddDTO;
import xyz.bumbing.jpa.dto.PostDTO.PostAddDTO;
import xyz.bumbing.jpa.entity.Member;
import xyz.bumbing.jpa.entity.Post;
import xyz.bumbing.jpa.repo.MemberRepo;
import xyz.bumbing.jpa.repo.PostRepo;

@RestController
public class TestController {

    @Autowired
    MemberRepo memberRepo;
    @Autowired
    PostRepo postRepo;

    @PostMapping("/member")
    public ResponseEntity<String> addMember(@RequestBody MemberAddDTO memberAddDTO) {
	try {
	    memberRepo.save(Member.builder().name(memberAddDTO.getName()).build());
	    return ResponseEntity.ok().build();
	} catch (Exception e) {
	    return ResponseEntity.badRequest().build();
	}

    }

    @PostMapping("/post")
    public ResponseEntity<String> addPost(@RequestBody PostAddDTO postAddDTO) {

	try {
	    memberRepo.findById(postAddDTO.getMemberId()).ifPresent(s -> {
		postRepo.save(Post.builder().description(postAddDTO.getDescription()).title(postAddDTO.getTitle())
			.member(s).build());
	    });
	    return ResponseEntity.ok().build();
	} catch (Exception e) {
	    return ResponseEntity.badRequest().build();
	}

    }

    //json 순환 참조 테스트
    @GetMapping("/test")
    @Transactional
    public void test(Long m, Long p) {
	Member member = memberRepo.findById(m).get();
	Post post = postRepo.findById(p).get();

	ObjectMapper om = new ObjectMapper();

	try {
	    String memberJson = om.writeValueAsString(member);
	    String postJson = om.writeValueAsString(post);

	} catch (JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
