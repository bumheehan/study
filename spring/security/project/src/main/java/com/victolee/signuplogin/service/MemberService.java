package com.victolee.signuplogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victolee.signuplogin.domain.Role;
import com.victolee.signuplogin.domain.entity.MemberEntity;
import com.victolee.signuplogin.domain.repository.MemberRepository;
import com.victolee.signuplogin.dto.MemberDto;

@Service
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Long joinUser(MemberDto memberDto) {
	// 비밀번호 암호화
	memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
	return memberRepository.save(memberDto.toEntity()).getId();
    }

    // 실제 db에서 가져온느 함수
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
	Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
	MemberEntity userEntity = userEntityWrapper.get();

	List<GrantedAuthority> authorities = new ArrayList<>();

	if (("admin@example.com").equals(userEmail)) {
	    authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
	} else {
	    authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
	}

	return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }
}
