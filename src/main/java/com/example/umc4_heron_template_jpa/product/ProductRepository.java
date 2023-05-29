package com.example.umc4_heron_template_jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository  extends JpaRepository<Product, Long> {
	@Query("select p from Product p where p.id = :productId")
	Product findProductById(@Param("productId") Long productId);

	List<Product> findAll();
}
