package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.TransactionManager;
import ch.ethz.globis.isk.web.model.DTOs;
import ch.ethz.globis.isk.web.model.SimplePersonDto;
import ch.ethz.globis.isk.web.model.PublicationSimpleDto;
import ch.ethz.globis.isk.web.model.SearchResponse;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.PublicationServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PublicationServiceImpl publicationService;

    @Autowired
    private TransactionManager tm;

    private static final Log LOG = LogFactory.getLog(SearchController.class);

    @RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
    public String searchPersonsAndPublications(@RequestParam("searchString") String searchString, Model model) {
        tm.beginTransaction();
        List<Person> persons = (List<Person>) personService.findByName(searchString, null, 0, 50);
        List<Publication> publications = (List<Publication>) publicationService.findByTitle(searchString, null, 0, 50);
        SearchResponse searchResponse = new SearchResponse(DTOs.create(persons, SimplePersonDto.class), DTOs.create(publications, PublicationSimpleDto.class), searchString);
        tm.commitTransaction();
        LOG.info("Found " + persons.size() + " persons matching " + searchString);
        LOG.info("Found " + publications.size() + " publications matching " + searchString);
        model.addAttribute("searchResponse", searchResponse);
        return "search-results";
    }
}
