package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.Publisher;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.service.PublisherService;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import ch.ethz.globis.isk.web.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/publishers")
public class PublisherController extends TemplateController<String, Publisher> {

    private static final Log LOG = LogFactory.getLog(PublisherController.class);

    @Autowired
    PublisherService publisherService;

    @Autowired
    PublicationService publicationService;

    @Override
    protected String singleEntityView() {
        return "publisher";
    }

    @Override
    protected String allEntitiesView() {
        return "publisher-list";
    }

    @Override
    protected String entitySingularName() {
        return "publisher";
    }

    @Override
    protected BaseService<String, Publisher> service() {
        return publisherService;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, Publisher entity) {
        String publisherId = entity.getId();
        tm.beginTransaction();
        List<Publication> publications = publicationService.findByPublisherOrderedByYear(publisherId);
        List<DTO<Publication>> publicationDtoList = DTOs.create(publications, PublicationDto.class);
        tm.commitTransaction();
        model.addAttribute("publications", publicationDtoList);
        LOG.info("Showing " + publicationDtoList.size() + " publications for " + entity.getName());
    }

    @Override
    protected Object wrapToDTO(Publisher entity) {
        return DTOs.one(entity, SimplePublisherDto.class);
    }

    @ResponseBody
    @RequestMapping(value = { "/ajax" }, method = RequestMethod.GET)
    public PageResponseDto<DTO<Publisher>> getSeriesByAjaxJSON(HttpSession session, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "start", defaultValue = "0") Integer start, @RequestParam(value = "size", defaultValue = "20") Integer size, @RequestParam(value = "sortProperty", required = false) String sortField, @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Publisher> entities = null;
        long count;
        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Publisher>) publisherService.find(start, size);
                count = publisherService.count();
            } else {
                entities = (List<Publisher>) publisherService.findByName(search, null, start, size);
                count = publisherService.countByName(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Publisher>) publisherService.find(orderFilterList, start, size);
                count = publisherService.count();
            } else {
                entities = (List<Publisher>) publisherService.findByName(search, orderFilterList, start, size);
                count = publisherService.countByName(search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<Publisher>> pageResponse = new PageResponseDto<>(DTOs.create(entities, SimplePublisherDto.class), count);
        return pageResponse;
    }
}
