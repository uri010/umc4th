package com.example.umc4_heron_template_jpa.member;

import com.example.umc4_heron_template_jpa.config.BaseException;
import com.example.umc4_heron_template_jpa.utils.AES128;
import com.example.umc4_heron_template_jpa.utils.JwtService;
import com.example.umc4_heron_template_jpa.utils.Secret;
import com.example.umc4_heron_template_jpa.config.BaseResponseStatus;
import com.example.umc4_heron_template_jpa.member.dto.GetMemberRes;
import com.example.umc4_heron_template_jpa.member.dto.PatchMemberReq;
import com.example.umc4_heron_template_jpa.member.dto.PostLoginReq;
import com.example.umc4_heron_template_jpa.member.dto.PostLoginRes;
import com.example.umc4_heron_template_jpa.member.dto.PostMemberReq;
import com.example.umc4_heron_template_jpa.member.dto.PostMemberRes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    /**
     * 유저 생성 후 DB에 저장
     */
    public PostMemberRes createMember(PostMemberReq postMemberReq) throws BaseException {
        if(memberRepository.findByEmailCount(postMemberReq.getEmail()) >= 1) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }
        String pwd;
        try{
            // 암호화: postUserReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장합니다.
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postMemberReq.getPassword()); // 암호화코드
        }
        catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            Member member = new Member();
            member.createMember(postMemberReq.getEmail(), postMemberReq.getNickName(), pwd);
            memberRepository.save(member);
            return new PostMemberRes(member.getId(), member.getNickName());
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 유저 로그인
     */
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
        Member member = memberRepository.findMemberByEmail(postLoginReq.getEmail());
        String password;
        try{
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(member.getPassword()); // 암호화
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }
        if(postLoginReq.getPassword().equals(password)){
            String jwt = jwtService.createJwt(member.getId());
            return new PostLoginRes(member.getId(),jwt);
        }
        else{
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }
    }

    /**
     * 모든 회원 조회
     */
    public List<GetMemberRes> getMembers() throws BaseException {
        try{
            List<Member> members = memberRepository.findMembers(); //Member List로 받아 GetMemberRes로 바꿔줌
            List<GetMemberRes> GetMemberRes = members.stream()
                    .map(member -> new GetMemberRes(member.getId(), member.getNickName(), member.getEmail(), member.getPassword()))
                    .collect(Collectors.toList());
            return GetMemberRes;
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 특정 닉네임 조회
     */
    public List<GetMemberRes> getMembersByNickname(String nickname) throws BaseException {
        try{
            List<Member> members = memberRepository.findMemberByNickName(nickname);
            List<GetMemberRes> GetMemberRes = members.stream()
                    .map(member -> new GetMemberRes(member.getId(), member.getNickName(), member.getEmail(), member.getPassword()))
                    .collect(Collectors.toList());
            return GetMemberRes;
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 닉네임 변경
     */
    @Transactional
    public void modifyUserName(PatchMemberReq patchMemberReq) {
        Member member = memberRepository.getReferenceById(patchMemberReq.getMemberId());
        member.updateNickName(patchMemberReq.getNickname());
    }

    /**
     * 이미지 변경
     */
    @Transactional
    public void modifyImageUrl( Long id, String imageUrl) {
        Member member = memberRepository.getReferenceById(id);
        member.updateImage(imageUrl);
    }

    @Transactional
    public void modifyNickname(Long id, String nickname) {
        Member member = memberRepository.getReferenceById(id);
        member.updateNickName(nickname);
    }
}
