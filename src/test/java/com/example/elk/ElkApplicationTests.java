package com.example.elk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ElkApplicationTests {

	// @Autowired private RestHighLevelClient client;

	@Test
	void contextLoads() {
	}

  @Test
  public void test() {
		log.info("test");
		assertEquals(1, 1, "pass");
  }

}
