package com.swyp.catsgotogedog.content.service;

import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentElasticRepository contentElasticRepository;

}
