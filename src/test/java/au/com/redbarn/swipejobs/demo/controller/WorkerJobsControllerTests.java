/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
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
		assertEquals(40, workerJobsController.getJobsForWorker("5").size());
	}

	@Test
	void getJobsForInvalidWorker() {
		assertEquals(new ArrayList<>(), workerJobsController.getJobsForWorker("1000"));
	}

	@Test
	void getJobsForInactiveWorker() {
		assertEquals(new ArrayList<>(), workerJobsController.getJobsForWorker("1"));
	}
}
