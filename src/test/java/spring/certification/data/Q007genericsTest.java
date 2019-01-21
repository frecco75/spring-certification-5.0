package spring.certification.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import spring.certification.data.helper.Gender;

import java.util.List;
import java.util.Map;

/**
 * Verifies {@link JdbcTemplate} functionality for executing generic queries.
 */
public class Q007genericsTest {

    private static final String GENERIC_SQL_QUERY = "SELECT %s FROM employees WHERE first_name = ? AND last_name = ?";
    private static final String LIMIT_SQL_QUERY = "SELECT %s FROM employees LIMIT %s";
    private static final String EMPLOYEE_FIRST_NAME = "Bezalel";
    private static final String EMPLOYEE_LAST_NAME = "Simmel";
    private static final String ID_COLUMN = "emp_no";

    private AnnotationConfigApplicationContext context;
    private JdbcTemplate jdbcTemplate;

    /**
     * Test fixture...
     */
    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(AutoDetectionMarker.class);
        context.registerShutdownHook();
        jdbcTemplate = context.getBean(JdbcTemplate.class);
    }

    /**
     * Verifies functionality for retrieving different types of objects using generic {@link JdbcTemplate} methods.
     */
    @Test
    public void shouldExecuteGenericQuery() {
        // Retrieve employee's id
        String formattedQuery = String.format(GENERIC_SQL_QUERY, ID_COLUMN);
        Integer employeeId = jdbcTemplate.queryForObject(formattedQuery, Integer.class, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME);
        Assert.assertTrue(employeeId instanceof Integer); // Dumb verification actually

        // Retrieve employee's gender
        formattedQuery = String.format(GENERIC_SQL_QUERY, "gender");
        Gender gender = jdbcTemplate.queryForObject(formattedQuery, Gender.class, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME);
        Assert.assertTrue(gender instanceof Gender); // Dumb verification actually
    }

    /**
     * Verifies that {@link JdbcTemplate} functionality allows to query for lists of objects.
     */
    @Test
    public void shouldQueryForList() {
        String formattedQuery = String.format(LIMIT_SQL_QUERY, ID_COLUMN, 10);
        List<Integer> ids = jdbcTemplate.queryForList(formattedQuery, Integer.class);
        Assert.assertEquals(10, ids.size());
    }

    /**
     * Verifies that {@link JdbcTemplate} functionality allow to query for map.
     */
    @Test
    public void shouldQueryForMap() {
        String formattedQuery = String.format(GENERIC_SQL_QUERY, "*");
        Map<String, Object> columnToValueMap = jdbcTemplate.queryForMap(formattedQuery, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME);
        Assert.assertTrue(columnToValueMap instanceof Map);
    }
}