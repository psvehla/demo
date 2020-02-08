/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.workerjobs;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.redbarn.swipejobs.demo.model.job.Job;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;

/**
 * @author peter
 *
 */
@SpringBootTest
public class WorkerJobsServiceTest {

	@Autowired
	private WorkerJobsService workerJobsService;

	@Test
	void getJobsForWorker() {

		final int WORKER_ID_TO_TEST = 8;
		final int TEST_JOB_ID = 1;

		try {
			List<Job> jobs = workerJobsService.getJobsForWorker(WORKER_ID_TO_TEST);
			assertTrue(jobs.size() <= 3);

			Job testJob = jobs.get(TEST_JOB_ID);

			assertNotEquals(0, testJob.getJobId());
			assertNotNull(testJob.getGuid());
			assertNotNull(testJob.getCompany());
			assertNotNull(testJob.getJobTitle());
			assertNotNull(testJob.getAbout());
			assertNotNull(testJob.getStartDate());
			assertTrue(testJob.getWorkersRequired() > 0);
			assertNotNull(testJob.getBillRate());
			assertNotNull(testJob.getLocation().getLongitude());
			assertNotNull(testJob.getLocation().getLatitude());
			assertTrue(testJob.getRequiredCertificates().size() > 0);
			assertTrue(testJob.isDriverLicenseRequired());

		} catch (UnsupportedDistanceUnitException e) {
			fail();
		}
	}

	@Test
	void getJobsForInvalidWorker() {

		final int INVALID_WORKER_ID = 1000;

		assertThrows(NoSuchElementException.class, () -> {workerJobsService.getJobsForWorker(INVALID_WORKER_ID);});
	}

	@Test
	void getJobsForInactiveWorker() {

		final int INACTIVE_WORKER_ID = 1;

		assertThrows(NoSuchElementException.class, () -> {workerJobsService.getJobsForWorker(INACTIVE_WORKER_ID);});
	}

	@Test
	void checkBillRateOrder() {

		final int WORKER_ID_TO_TEST = 5;

		try {
			List<Job> jobs = workerJobsService.getJobsForWorker(WORKER_ID_TO_TEST);

			Double prevJobRate = (Double) NumberFormat.getCurrencyInstance().parse(jobs.get(0).getBillRate());

			for (var job : jobs) {
				if ((Double) NumberFormat.getCurrencyInstance().parse(job.getBillRate()) > prevJobRate) {
					fail();
				}
			}
		} catch (ParseException | UnsupportedDistanceUnitException e) {
			e.printStackTrace();
			fail();
		}
	}
}
