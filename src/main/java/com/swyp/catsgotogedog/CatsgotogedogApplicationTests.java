package com.swyp.catsgotogedog;

import com.swyp.catsgotogedog.content.repository.ContentElasticRepository;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class CatsgotogedogApplicationTests {

    @MockitoBean(reset = MockReset.AFTER)
    ContentElasticRepository contentElasticRepository;

    @Test
    public void contextLoads() {
    }

}