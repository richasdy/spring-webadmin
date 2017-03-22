package com.richasdy.springwebadmin;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// @RunWith(SpringJUnit4ClassRunner.class)
// @SpringBootApplication
public abstract class AbstractTests {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

}
