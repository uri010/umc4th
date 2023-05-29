package com.example.umc4_heron_template_jpa.product;

import com.example.umc4_heron_template_jpa.member.Member;
import com.example.umc4_heron_template_jpa.utils.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String content;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int status;
	@Column(nullable = false)
	private Long viewCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private Member seller;

	public Product createProduct(String title, String content, int price) {
		this.title = title;
		this.content = content;
		this.price = price;
		this.status = 1;
		this.viewCount = 0L;
		return this;
	}

	public void modifyPrice(int price) {
		this.price = price;
	}
}
