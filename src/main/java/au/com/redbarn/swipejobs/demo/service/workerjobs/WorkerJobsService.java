/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.workerjobs;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import au.com.redbarn.swipejobs.demo.geo.Geo;
import au.com.redbarn.swipejobs.demo.model.job.Job;
import au.com.redbarn.swipejobs.demo.model.worker.Worker;
import au.com.redbarn.swipejobs.demo.service.job.JobService;
import au.com.redbarn.swipejobs.demo.service.worker.WorkerService;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;
import lombok.extern.slf4j.Slf4j;

/**
 * Finds jobs for workers.
 *
 * @author peter
 *
 */
@Service
@Slf4j
public class WorkerJobsService {

	@Autowired
	private WorkerService workerService;

	@Autowired
	private JobService jobService;

	@Value(value = "${number.of.jobs.returned}")
	private int numberOfJobsReturned;

	/**
	 * Gets up to three appropriate jobs for a given worker.
	 *
	 * @param workerId The worker to find appropriate jobs for.
	 * @return Up to three appropriate jobs for the given worker.
	 * @throws UnsupportedDistanceUnitException Only km are supported.
	 */
	public List<Job> getJobsForWorker(int workerId) throws UnsupportedDistanceUnitException {

		Worker worker = workerService.getWorker(workerId);
		List<Job> jobs = jobService.getJobs();

		Comparator<Job> jobComparator = (j1, j2) -> {

			double comparison = 0;

			try {
				comparison = (Double) NumberFormat.getCurrencyInstance().parse(j2.getBillRate()) - (Double) NumberFormat.getCurrencyInstance().parse(j1.getBillRate());
			} catch (ParseException e) {
				log.error("One of jobs " + j1.getJobId() + " or " + j2.getJobId() + " has an invalid bill rate.", e);
			}

			return (int) comparison;
		};

		return jobs.stream()
				.filter(j -> worker.isHasDriversLicense() || !j.isDriverLicenseRequired())
				.filter(j -> {

					var hasCertificates = true;

					for (String requiredCert : j.getRequiredCertificates()) {
						if (!worker.getCertificates().contains(requiredCert)) {
							hasCertificates = false;
						}
					}

					return hasCertificates;
				})
				.filter(j -> worker.getJobSearchAddress().getMaxJobDistance() >= Geo.geoDistance(Double.parseDouble(worker.getJobSearchAddress().getLatitude()),
						                                                                         Double.parseDouble(worker.getJobSearchAddress().getLongitude()),
						                                                                         Double.parseDouble(j.getLocation().getLatitude()),
						                                                                         Double.parseDouble(j.getLocation().getLongitude())))
				.sorted(jobComparator)
				.limit(numberOfJobsReturned)
				.collect(Collectors.toList());
	}
}
