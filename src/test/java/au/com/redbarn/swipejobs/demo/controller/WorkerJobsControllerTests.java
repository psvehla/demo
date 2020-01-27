/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.redbarn.swipejobs.demo.model.job.Job;

/**
 * @author peter
 *
 */
@SpringBootTest
public class WorkerJobsControllerTests {

	@Autowired
	private WorkerJobsController workerJobsController;

	private static final long NUMBER_OF_JOBS_RETURNED = 3;

	@Test
	void getJobsForWorker() {
		assertEquals(NUMBER_OF_JOBS_RETURNED, workerJobsController.getJobsForWorker("5").size());		// no driver's licence
		assertEquals(NUMBER_OF_JOBS_RETURNED, workerJobsController.getJobsForWorker("8").size());		// has driver's licence
	}

	@Test
	void getJobsForInvalidWorker() {
		assertEquals(new ArrayList<>(), workerJobsController.getJobsForWorker("1000"));
	}

	@Test
	void getJobsForInactiveWorker() {
		assertEquals(new ArrayList<>(), workerJobsController.getJobsForWorker("1"));
	}

	@Test
	void checkBillRateOrder() {
		List<Job> jobs = workerJobsController.getJobsForWorker("5");

		try {
			Double prevJobRate = (Double) NumberFormat.getCurrencyInstance().parse(jobs.get(0).getBillRate());

			for (var job : jobs) {
				if ((Double) NumberFormat.getCurrencyInstance().parse(job.getBillRate()) > prevJobRate) {
					fail();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
	}
}
