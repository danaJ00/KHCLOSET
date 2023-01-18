package com.shop.repository;

import com.shop.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findByMemberId(Long memberId);

    Page<Qna> findAll(Pageable pageable);
}