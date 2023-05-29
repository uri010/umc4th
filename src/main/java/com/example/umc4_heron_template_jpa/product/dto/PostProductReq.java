package com.example.umc4_heron_template_jpa.product.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostProductReq {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@NotBlank
	private int price;
}
