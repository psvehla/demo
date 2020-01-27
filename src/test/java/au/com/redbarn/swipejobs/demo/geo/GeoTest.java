/**
 *
 */
package au.com.redbarn.swipejobs.demo.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class is responsible for geographic calculations.
 *
 * @author peter
 *
 */
@SpringBootTest
public class GeoTest {

	@Test
	void geoDistance() {
		assertEquals(54, Geo.geoDistance(49.782281, 13.971284, 50.256714, 14.127058));
		assertEquals(78, Geo.geoDistance(50.049914, 14.093382, 50.083564, 15.190295));
		assertEquals(51, Geo.geoDistance(50.112069, 15.089318, 50.072277, 14.372779));
	}
}
