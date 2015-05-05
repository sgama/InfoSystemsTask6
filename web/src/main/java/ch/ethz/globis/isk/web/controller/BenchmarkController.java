package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.benchmark.BenchmarkSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/benchmarks")
public class BenchmarkController {

    @Autowired
    BenchmarkSuite suite;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String showFrontPage(Model model, HttpSession session) {
        return "benchmark-home";
    }

    @RequestMapping(value = "/run-suite", method = RequestMethod.GET)
    public String findPublicationById(Model model, HttpSession session) {
        Map<String, String> results = suite.runBenchmarks();
        model.addAttribute("results", results);
        return "benchmark-results";
    }
}