package com.uniclub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.datasource.url=jdbc:h2:mem:uniclub;MODE=MySQL;DB_CLOSE_DELAY=-1",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.datasource.username=sa",
	"spring.datasource.password=",
	"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
	"spring.jpa.hibernate.ddl-auto=none",
	"spring.jpa.generate-ddl=false",
	"spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
class UniclubApplicationTests {

	@Test
	void contextLoads() {
	}

}con ca cha bac
