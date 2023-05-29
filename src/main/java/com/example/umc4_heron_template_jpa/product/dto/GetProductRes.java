package com.example.umc4_heron_template_jpa.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProductRes {
	private String title;
	private String content;
	private int price;
	private int status;
	private Long viewCount;
}
