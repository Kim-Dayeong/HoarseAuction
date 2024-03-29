package com.hoarse.auction.web.controller.member;

import com.hoarse.auction.web.config.jwt.JwtConfig;

import com.hoarse.auction.web.config.security.SecurityUser;
import com.hoarse.auction.web.dto.member.updateResponseMemberDto;
import com.hoarse.auction.web.dto.member.MemberDto;
import com.hoarse.auction.web.dto.member.MemberRequestDto;

import com.hoarse.auction.web.entity.member.Member;
import com.hoarse.auction.web.service.auth.AuthService;
import com.hoarse.auction.web.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @GetMapping("/info")
    public String getMemberInfo(HttpServletRequest request){

        String token = jwtConfig.resolveToken(request);

        if(token != null && jwtConfig.validateToken(token)){
            // 토큰이 유효하면
            String username = jwtConfig.getAuthentication(token).getName();
            return username;
        }

        return "토큰이 유효하지 않습니다";

    }


    @PostMapping("/signup")
    public MemberDto createUser(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.createUser(memberRequestDto);
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletRequest request) {
        // 사용자 정보 확인 및 토큰 생성
        MemberDto member = memberService.findByEmailAndPassword(memberRequestDto.getUsername(), memberRequestDto.getPassword());
        String token = jwtConfig.createToken(member.getUsername(), Arrays.asList(member.getRole().getValue()));

        // 토큰을 응답으로 반환
        return ResponseEntity.ok(token);
    }



    @GetMapping("/admin")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public List<MemberDto> findAllUser() {
        return memberService.findAll();
    }

//    @GetMapping("/my")
//    public ResponseEntity<Member> findUser(@RequestHeader(AUTHORIZATION)String token) {
//        if (token == null) {
//            throw new BadCredentialsException("회원 정보를 찾을 수 없습니다.(로그인 안됨)");
//        }
//        return ResponseEntity.status(OK).body(
//                authService.getMemberFromToken(token));
//    }

    @GetMapping("/my")
    public String findUser(@RequestHeader(AUTHORIZATION)String token) {
        if (token == null) {
            throw new BadCredentialsException("회원 정보를 찾을 수 없습니다.(로그인 안됨)");
        }
        String member = authService.getMemberFromToken(token).getUsername();
        return member;
    }



    // 회원탈퇴
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable("memberId") Long memberId,@AuthenticationPrincipal SecurityUser principal){
       memberService.deleteMember(memberId, principal.getMember());

       return ResponseEntity.ok().build();
    }

    //회원 수정
//    @PutMapping("/update/{memberId}")
//    public updateResponseMemberDto updateMember(MemberRequestDto memberDto,
//            @PathVariable("memberId") Long memberId,@AuthenticationPrincipal SecurityUser principal){
//         MemberDto updatemamber = memberService.updateMember(memberId,memberDto, principal.getMember());
//        return new updateResponseMemberDto(updatemamber.getName(),updatemamber.getPhone(),updatemamber.getPassword());
//    }



}
