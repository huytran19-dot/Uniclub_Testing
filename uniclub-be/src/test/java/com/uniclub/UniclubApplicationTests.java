package com.uniclub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.datasource.url=jdbc:h2:mem:uniclub;DB_CLOSE_DELAY=-1",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
	// use create to avoid noisy drop statements against an empty in-memory DB
	"spring.jpa.hibernate.ddl-auto=create"
})
class UniclubApplicationTests {

	@Test
	void contextLoads() {
	}

}
