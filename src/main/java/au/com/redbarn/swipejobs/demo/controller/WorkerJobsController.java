/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import au.com.redbarn.swipejobs.demo.model.job.Job;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;
import au.com.redbarn.swipejobs.demo.service.workerjobs.WorkerJobsService;
import lombok.extern.slf4j.Slf4j;

/**
 * Gets jobs for workers.
 *
 * @author peter
 *
 */
@RestController
@RequestMapping("/get-jobs-for-worker")
@Slf4j
public class WorkerJobsController {

	@Autowired
	private WorkerJobsService workerJobsService;

	/**
	 * Gets up to three appropriate jobs for a given worker.
	 *
	 * @param workerId The worker to find appropriate jobs for.
	 * @return Up to three appropriate jobs for the given worker.
	 */
	@GetMapping("/{workerId}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Job> getJobsForWorker(@PathVariable("workerId") String workerId) {
		try {
			return workerJobsService.getJobsForWorker(Integer.parseInt(workerId));
		}
		catch (NoSuchElementException e) {
			log.error("Worker " + workerId + " not found.", e);
			return new ArrayList<>();
		}
		catch (UnsupportedDistanceUnitException e) {
			log.error("Worker " + workerId + " has an unsupported record format.", e);
			return new ArrayList<>();
		}
	}
}
