package com.example.umc4_heron_template_jpa.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.umc4_heron_template_jpa.config.BaseException;
import com.example.umc4_heron_template_jpa.config.BaseResponse;
import com.example.umc4_heron_template_jpa.product.dto.DeleteProductRes;
import com.example.umc4_heron_template_jpa.product.dto.GetProductRes;
import com.example.umc4_heron_template_jpa.product.dto.PatchProductRes;
import com.example.umc4_heron_template_jpa.product.dto.PostProductReq;
import com.example.umc4_heron_template_jpa.product.dto.PostProductRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
	private final ProductService productService;

	/**
	 * 상품 등록
	 */
	@PostMapping("/create")
	public BaseResponse<PostProductRes> createProduct(@RequestBody @Valid PostProductReq postProductReq) {
		try {
			return new BaseResponse<>(productService.createProduct(postProductReq));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 특정 상품 조회
	 */
	@GetMapping("/{productId}")
	public BaseResponse<GetProductRes> getProductById(@PathVariable Long productId) {
		try {
			return new BaseResponse<>(productService.getProductById(productId));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 전체 상품 조회
	 */
	@GetMapping("/all")
	public BaseResponse<List<GetProductRes>> getAllProductList(){
		try {
			return new BaseResponse<>(productService.getAllProductList());
		}catch(BaseException exception){
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 상품 가격 수정
	 */
	@PatchMapping("/price")
	public BaseResponse<PatchProductRes> modifyPrice(@RequestParam Long productId, @RequestParam int price) {
		try {
			return new BaseResponse<>(productService.modifyPrice(productId, price));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}

	/**
	 * 상품 삭제
	 */
	@DeleteMapping("/delete")
	public BaseResponse<DeleteProductRes> deleteProduct(@RequestParam Long productId) {
		try {
			return new BaseResponse<>(productService.deleteProduct(productId));
		} catch (BaseException exception) {
			return new BaseResponse<>(exception.getStatus());
		}
	}
}
