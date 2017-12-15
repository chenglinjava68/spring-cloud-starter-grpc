package com.icekredit.examples.test.client.service.impl;

import com.icekredit.examples.test.client.Application;
import com.icekredit.examples.test.client.service.TestService;
import com.icekredit.phone.PhoneType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TestServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 12, 2017</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestServiceImplTest {
    @Autowired
    private TestService testService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: addPhoneToUser(int uid, PhoneType phoneType, String phoneNubmer)
     */
    @Test
    public void testAddPhoneToUser() throws Exception {
        Thread.sleep(60000);

        long startTime = System.currentTimeMillis();
        testService.addPhoneToUser(1, PhoneType.WORK, "1388888887");
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
