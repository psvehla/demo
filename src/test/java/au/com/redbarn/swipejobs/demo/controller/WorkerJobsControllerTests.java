/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author peter
 *
 */
@SpringBootTest
public class WorkerJobsControllerTests {

	private final WorkerJobsController workerJobsController = new WorkerJobsController();

	@Test
	void getJobsForWorker() {
		assertEquals("0", workerJobsController.getJobsForWorker("0"));
	}

	@Test
	void getJobsForInvalidWorker() {
		Exception exception = assertThrows(NoSuchElementException.class, () -> workerJobsController.getJobsForWorker("1000"));
		assertEquals("No value present", exception.getMessage());
	}

	@Test
	void getJobsForInactiveWorker() {
		Exception exception = assertThrows(NoSuchElementException.class, () -> workerJobsController.getJobsForWorker("1"));
		assertEquals("No value present", exception.getMessage());
	}
}
