package com.ljs.shop.service;

import com.ljs.shop.dto.MemberFormDto;
import com.ljs.shop.entity.Member;
import com.ljs.shop.exception.DuplicateMemberException;
import com.ljs.shop.exception.PasswordMismatchException;
import com.ljs.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 생성
     *
     * @param memberFormDto 회원 정보를 담은 DTO
     * @throws IllegalArgumentException 비밀번호 불일치 또는 중복된 이메일로 가입 시 예외 발생
     */
    public void createMember(MemberFormDto memberFormDto) {
        // 비밀번호 체크
        checkPassword(memberFormDto.getPassword(), memberFormDto.getConfirmPassword());

        // 중복 회원 검사
        validateDuplicateMember(memberFormDto.getEmail());

        Member member = memberFormDto.toEntity();

        // 비밀번호 암호화
        member.encodePassword(passwordEncoder);

        // 회원 저장
        memberRepository.save(member);
    }

    /**
     * 비밀번호 확인
     *
     * @param password        비밀번호
     * @param confirmPassword 확인 비밀번호
     * @throws PasswordMismatchException 비밀번호 불일치 시 예외 발생
     */
    private void checkPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
    }

    /**
     * 중복 회원 검사
     *
     * @param email 회원 이메일
     * @throws DuplicateMemberException 이미 가입된 이메일인 경우 예외 발생
     */
    private void validateDuplicateMember(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(existingMember -> {
                    throw new DuplicateMemberException();
                });
    }

    /**
     * 이메일에 해당하는 회원 정보를 데이터베이스에서 조회
     *
     * @param email 사용자 이메일
     * @return UserDetails 인터페이스를 구현하고 있는 User 클래스
     * @throws UsernameNotFoundException 이메일에 해당하는 회원이 데이터베이스에 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 데이터베이스에서 이메일로 회원 정보를 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하는 회원이 없습니다."));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
