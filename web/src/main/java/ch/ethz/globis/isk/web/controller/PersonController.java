package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.PersonService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import ch.ethz.globis.isk.web.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController extends TemplateController<String, Person> {

    private static final Log LOG = LogFactory.getLog(PersonController.class);

    @Autowired
    PersonService personService;

    @Autowired
    PublicationService publicationService;

    @Override
    protected String singleEntityView() {
        return "person-home";
    }

    @Override
    protected String allEntitiesView() {
        return "person-list";
    }

    @Override
    protected String entitySingularName() {
        return "person";
    }

    @Override
    protected Object wrapToDTO(Person entity) {
        return DTOs.one(entity, SimplePersonDto.class);
    }

    @Override
    protected BaseService<String, Person> service() {
        return personService;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, Person entity) {
        String entityId = entity.getId();
        tm.beginTransaction();
        List<DTO<Publication>> authoredPublicationDtos = DTOs.create(publicationService.findByAuthorIdOrderedByYear(entityId), PublicationDto.class);
        List<DTO<Publication>> editedPublicationDtos = DTOs.create(publicationService.findByEditorIdOrderedByYear(entityId), PublicationDto.class);
        tm.commitTransaction();
        model.addAttribute("authoredPublications", authoredPublicationDtos);
        model.addAttribute("editedPublications", editedPublicationDtos);
    }

    @ResponseBody
    @RequestMapping(value = { "/ajax" }, method = RequestMethod.GET)
    public PageResponseDto<DTO<Person>> getPersonByAjaxJSON(HttpSession session, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "start", defaultValue = "0") Integer start, @RequestParam(value = "size", defaultValue = "20") Integer size, @RequestParam(value = "sortProperty", required = false) String sortField, @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Person> entities;
        long count;
        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Person>) personService.find(start, size);
                count = personService.count();
            } else {
                entities = (List<Person>) personService.findByName(search, null, start, size);
                count = personService.countByName(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Person>) personService.find(orderFilterList, start, size);
                count = personService.count();
            } else {
                entities = (List<Person>) personService.findByName(search, orderFilterList, start, size);
                count = personService.countByName(search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<Person>> pageResponse = new PageResponseDto<>(DTOs.create(entities, SimplePersonDto.class), count);
        return pageResponse;
    }
}
