package com.example.umc4_heron_template_jpa.product;

import static com.example.umc4_heron_template_jpa.config.BaseResponseStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.umc4_heron_template_jpa.config.BaseException;
import com.example.umc4_heron_template_jpa.config.BaseResponseStatus;
import com.example.umc4_heron_template_jpa.product.dto.DeleteProductRes;
import com.example.umc4_heron_template_jpa.product.dto.GetProductRes;
import com.example.umc4_heron_template_jpa.product.dto.PatchProductRes;
import com.example.umc4_heron_template_jpa.product.dto.PostProductReq;
import com.example.umc4_heron_template_jpa.product.dto.PostProductRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public PostProductRes createProduct(PostProductReq postProductReq) throws BaseException {
		try {
			Product product = new Product();
			product.createProduct(postProductReq.getTitle(), postProductReq.getContent(), postProductReq.getPrice());
			productRepository.save(product);
			return new PostProductRes(product.getTitle());
		} catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public GetProductRes getProductById(Long productId) throws BaseException {
		try {
			Product product = productRepository.findProductById(productId);
			return new GetProductRes(product.getTitle(), product.getContent(), product.getPrice(), product.getStatus(),
				product.getViewCount());
		} catch (Exception exception) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public PatchProductRes modifyPrice(Long productId, int price) throws BaseException {
		try {
			Product product = productRepository.findProductById(productId);
			product.modifyPrice(price);
			return new PatchProductRes(product.getTitle(), product.getPrice());
		} catch (Exception exception) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public DeleteProductRes deleteProduct(Long productId) throws BaseException {
		try {
			Product product = productRepository.findProductById(productId);
			productRepository.deleteById(productId);
			return new DeleteProductRes(product.getTitle());
		} catch (Exception exception) {
			throw new BaseException(DATABASE_ERROR);
		}
	}

	public List<GetProductRes> getAllProductList() throws BaseException {
		try {
			List<Product> products = productRepository.findAll(); //Member List로 받아 GetMemberRes로 바꿔줌
			List<GetProductRes> GetProductRes = products.stream()
				.map(product -> new GetProductRes(product.getTitle(), product.getContent(), product.getPrice(),
					product.getStatus(), product.getViewCount()))
				.collect(Collectors.toList());
			return GetProductRes;
		} catch (Exception exception) {
			throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
		}
	}
}
