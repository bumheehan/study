package xyz.bumbing.main;

import java.util.Date;

import org.jboss.jandex.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "board")
public class H2Controller {

	@Autowired
	H2Repository repo;

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("entity", repo.findAll());
		return "list";
	}

	@RequestMapping(value = "/post")
	public String post(Model model) {
		model.addAttribute("entity", new H2Entity());
		return "post";
	}

	@RequestMapping(value = "/c", method = RequestMethod.POST)
	public String create(@ModelAttribute H2Entity h2) {
		h2.setDate(new Date());
		logger.info("H2Entity" + h2.toString());
		repo.save(h2);
		return "redirect:/board/list";
	}

	@RequestMapping(value = "/r")
	public String read(Model model, @ModelAttribute H2Entity h2) {
		logger.info("read number : " + h2.getNo());
		model.addAttribute("entity", repo.findById(h2.getNo()).get());
		return "update";
	}

	@RequestMapping(value = "/u")
	public String update(H2Entity h2) {
		System.out.println(h2.toString());
		repo.save(h2);
		return "redirect:/board/list";
	}

	@RequestMapping(value = "/d")
	public String delete(H2Entity h2) {
		System.out.println(h2.toString());
		repo.delete(h2);
		return "redirect:/board/list";
	}
}
