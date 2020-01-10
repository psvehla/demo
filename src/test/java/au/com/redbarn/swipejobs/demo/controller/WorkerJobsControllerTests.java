/**
 * 
 */
package au.com.redbarn.swipejobs.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals("1", workerJobsController.getJobsForWorker("1"));
	}
}
