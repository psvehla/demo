/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.redbarn.swipejobs.demo.model.worker.Worker;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;

/**
 * @author peter
 *
 */
@SpringBootTest
public class WorkerServiceTest {

	@Value(value = "${workers.url}")
	private String workersUrl;

	@Autowired
	private WorkerService workerService;

	@Test
	void getWorker() {
		try {
			Worker worker = workerService.getWorker(5);
			assertEquals(5, worker.getUserId());
		} catch (UnsupportedDistanceUnitException e) {
			fail();
		}
	}

	@Test
	void getInvalidWorker() {
		assertThrows(NoSuchElementException.class, () -> {
			Worker worker = workerService.getWorker(1000);
			assertEquals(null, worker);
		});
	}

	@Test
	void getInactiveWorker() {
		assertThrows(NoSuchElementException.class, () -> {
			Worker worker = workerService.getWorker(1);
			assertEquals(null, worker);
		});
	}
}
