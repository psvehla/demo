/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.job;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.redbarn.swipejobs.demo.model.job.Job;

/**
 * Gets jobs from upstream web services.
 *
 * @author peter
 *
 */
@SpringBootTest
public class JobServiceTest {

	@Autowired
	private JobService jobService;

	@Test
	void getJobs() {

		final int TEST_JOB_ID = 1;

		List<Job> jobs = jobService.getJobs();
		assertTrue(jobs.size() > TEST_JOB_ID);

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
	}
}
