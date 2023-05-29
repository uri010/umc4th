package com.example.umc4_heron_template_jpa.member;

import com.example.umc4_heron_template_jpa.config.BaseException;
import com.example.umc4_heron_template_jpa.config.BaseResponse;
import com.example.umc4_heron_template_jpa.member.dto.PatchMemberReq;
import com.example.umc4_heron_template_jpa.member.dto.PostLoginRes;
import com.example.umc4_heron_template_jpa.utils.JwtService;
import com.example.umc4_heron_template_jpa.config.BaseResponseStatus;
import com.example.umc4_heron_template_jpa.member.dto.GetMemberRes;
import com.example.umc4_heron_template_jpa.member.dto.PostLoginReq;
import com.example.umc4_heron_template_jpa.member.dto.PostMemberReq;
import com.example.umc4_heron_template_jpa.member.dto.PostMemberRes;
import com.example.umc4_heron_template_jpa.utils.ValidationRegex;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	/**
	 * 회원 가입
	 */
	@PostMapping("/create")
	public BaseResponse<PostMemberRes> createMember(@RequestBody PostMemberReq postMemberReq) {
		if (!ValidationRegex.isRegexEmail(postMemberReq.getEmail()))
			return new BaseResponse<>(
				BaseResponseStatus.POST_USERS_INVALID_EMAIL);
		try {
			return new BaseResponse<>(memberService.createMember(postMemberReq));
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 로그인
	 */
	@PostMapping("/log-in")
	public BaseResponse<PostLoginRes> loginMember(@RequestBody PostLoginReq postLoginReq) {
		try {
			if (!ValidationRegex.isRegexEmail(postLoginReq.getEmail()))
				return new BaseResponse<>(
					BaseResponseStatus.POST_USERS_INVALID_EMAIL);
			return new BaseResponse<>(memberService.login(postLoginReq));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 회원 조회
	 * nickname이 파라미터에 없을 경우 모두 조회
	 */
	@GetMapping("Read")
	public BaseResponse<List<GetMemberRes>> getMembers(@RequestParam(required = false) String nickName) {
		//  @RequestParam은, 1개의 HTTP Request 파라미터를 받을 수 있는 어노테이션(?뒤의 값). default로 RequestParam은 반드시 값이 존재해야 하도록 설정되어 있지만, (전송 안되면 400 Error 유발)
		//  지금 예시와 같이 required 설정으로 필수 값에서 제외 시킬 수 있음
		//  defaultValue를 통해, 기본값(파라미터가 없는 경우, 해당 파라미터의 기본값 설정)을 지정할 수 있음
		try {
			if (nickName == null) { // query string인 nickname이 없을 경우, 그냥 전체 유저정보를 불러온다.
				return new BaseResponse<>(memberService.getMembers());
			}
			// query string인 nickname이 있을 경우, 조건을 만족하는 유저정보들을 불러온다.
			return new BaseResponse<>(memberService.getMembersByNickname(nickName));
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 유저 닉네임 변경
	 */
	@PatchMapping("/update")
	public BaseResponse<String> modifyUserName(@RequestParam String email, @RequestParam String nickname) {
		try {
			Member member = memberRepository.findMemberByEmail(email);
			//jwt에서 idx 추출.
			Long userIdxByJwt = jwtService.getUserIdx();
			//userIdx와 접근한 유저가 같은지 확인
			if (member.getId() != userIdxByJwt) {
				return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
			}
			//같다면 유저네임 변경
			PatchMemberReq patchMemberReq = new PatchMemberReq(member.getId(), nickname);
			memberService.modifyUserName(patchMemberReq);
			String result = "회원정보가 수정되었습니다.";
			return new BaseResponse<>(result);
		} catch (BaseException exception) {
			return new BaseResponse<>((exception.getStatus()));
		}
	}

	/**
	 * 유저 닉네임 변경 (ver.yulee)
	 */
	@PatchMapping("/nickname")
	public BaseResponse<String> modifyNickname(@RequestParam Long id, @RequestParam String nickname) {
		memberService.modifyNickname(id, nickname);
		String result = "유저의 닉네임이 변경되었습니다.";
		return new BaseResponse<>(result);
	}
	/**
	 * 유저 이미지 변경
	 */
	@PatchMapping("/image")
	public BaseResponse<String> modifyImageUrl(@RequestParam Long id, @RequestParam String imageUrl) {
		memberService.modifyImageUrl(id, imageUrl);
		String result = "유저의 이미지가 변경되었습니다.";
		return new BaseResponse<>(result);
	}

}
