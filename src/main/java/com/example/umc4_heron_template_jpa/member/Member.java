package com.example.umc4_heron_template_jpa.member;

import com.example.umc4_heron_template_jpa.member_image.MemberImage;
import com.example.umc4_heron_template_jpa.utils.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String nickName;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private Float manner_rate;

	@Column(nullable = false)
	private boolean activated;

	@OneToOne(fetch = FetchType.LAZY)
	private MemberImage memberImage;

	public Member createMember(String email, String nickName, String password) {
		this.email = email;
		this.nickName = nickName;
		this.password = password;
		return this;
	}

	public void updateNickName(String nickName) {
		this.nickName = nickName;
	}

	public void updateImage(String image_url) { memberImage.updateImage(image_url);}
}
