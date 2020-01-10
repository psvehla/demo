/**
 * 
 */
package au.com.redbarn.swipejobs.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author peter
 *
 */
@Controller
public class WorkerJobsController {
	
	@GetMapping("/get-jobs-for-worker/{workerId}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getJobsForWorker(@PathVariable("workerId") String workerId) {
		return workerId;
	}
}
