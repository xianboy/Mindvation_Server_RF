package com.mdvns.mdvn.department;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


public class MdvnDepartmentApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void translateDateToLong() {
		Date dat = new Date();
		System.out.print(dat.getTime());
	}
}
