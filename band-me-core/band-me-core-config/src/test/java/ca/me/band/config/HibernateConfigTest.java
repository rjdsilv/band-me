package ca.me.band.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class})
public class HibernateConfigTest {
	@Autowired
	private HikariDataSource dataSource;

	@Test
	public void testDataSource() throws SQLException {
		final ResultSet rs = dataSource.getConnection().prepareStatement("SELECT 1 as RESULT FROM INFORMATION_SCHEMA.SYSTEM_USERS").executeQuery();
		Assert.assertNotNull(rs);
		if (rs.next()) {
			Assert.assertEquals(1, rs.getInt("RESULT"));
		} else {
			Assert.fail("The ResultSet was not returned!");
		}
	}
}
