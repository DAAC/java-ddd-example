package tv.codely.apps.mooc.backend.controller.courses;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tv.codely.mooc.courses.application.CourseResponse;
import tv.codely.mooc.courses.application.CoursesResponse;
import tv.codely.mooc.courses.application.find.FindCourseQuery;
import tv.codely.mooc.courses.application.find.FindCoursesQuery;
import tv.codely.mooc.courses.domain.CourseNotExist;
import tv.codely.shared.domain.DomainError;
import tv.codely.shared.domain.bus.command.CommandBus;
import tv.codely.shared.domain.bus.query.QueryBus;
import tv.codely.shared.domain.bus.query.QueryHandlerExecutionError;
import tv.codely.shared.infrastructure.spring.ApiController;

@RestController
public final class CourseGetController extends ApiController {

	public CourseGetController(QueryBus queryBus, CommandBus commandBus) {
		super(queryBus, commandBus);
	}

	@GetMapping("/courses/{id}")
	public ResponseEntity<HashMap<String, Serializable>> index(@PathVariable String id)
		throws QueryHandlerExecutionError {
		CourseResponse course = ask(new FindCourseQuery(id));
		HashMap<String, Serializable> response = new HashMap<>();
		response.put("id", course.id());
		response.put("name", course.name());
		response.put("duration", course.duration());
		return ResponseEntity
			.ok()
			.body(response);
	}

	@GetMapping("/courses")
	public ResponseEntity<HashMap<String, String>> courses()
		throws QueryHandlerExecutionError {
		CoursesResponse courses = ask(new FindCoursesQuery());
		HashMap<String, String> response = new HashMap<>();
		courses.courses().forEach(course -> {
			response.put("id", course.id());
			response.put("name", course.name());
			response.put("duration", course.duration());
		});
		return ResponseEntity
			.ok()
			.body(response);
	}

	@Override
	public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
		 HashMap<Class<? extends DomainError>, HttpStatus> errorMap = new HashMap<>();
		 errorMap.put(CourseNotExist.class, HttpStatus.NOT_FOUND);
		 return errorMap;

	}
}
