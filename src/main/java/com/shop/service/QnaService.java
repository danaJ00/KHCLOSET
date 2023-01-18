package com.shop.service;

import com.shop.dto.QnaDto;
import com.shop.entity.*;
import com.shop.repository.MemberRepository;
import com.shop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QnaService {

    @Autowired
    private final QnaRepository qnaRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public QnaDto getQnaDtl(Long id) {
        Qna qna = qnaRepository.findById(id).get();
        QnaDto qnaDto = QnaDto.of(qna);

        return qnaDto;
    }

    public Long saveQna(QnaDto qnaDto, String email) {
        Member member = memberRepository.findByEmail(email);
        Qna qna = Qna.createQna(qnaDto, member);

        qnaRepository.save(qna);

        return qna.getId();
    }

    public long updateQna(QnaDto qnaDto) throws Exception{
        Qna qna = qnaRepository.findById(qnaDto.getId()).orElseThrow(EntityNotFoundException::new);
        qna.updateQna(qnaDto);

        return qna.getId();
    }

    public List<Qna> qnaList(String email){
        List<Qna> qnaList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        qnaList = qnaRepository.findByMemberId(member.getId());

        return qnaList;
    }

    public Page<Qna> getqnaMng(int page) {
        Pageable pageable = PageRequest.of(page, 8);

        return this.qnaRepository.findAll(pageable);
    }

    public void qnaDelete(Long id){
        qnaRepository.deleteById(id);
    }
}