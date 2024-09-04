package com.example.shop2.repository.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemImgReposiotryTest {

    @Autowired
    private ItemImgRepository itemImgRepository;

    @BeforeEach
    public void setUp() {
        assertNotNull(itemImgRepository);
    }

    @Test
    public void hello() {

    }
}