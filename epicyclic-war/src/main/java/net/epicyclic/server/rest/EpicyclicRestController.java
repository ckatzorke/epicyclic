package net.epicyclic.server.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EpicyclicRestController {
	
	
	@RequestMapping("/test")
	public @ResponseBody String test() {
		return "test";
	}
}