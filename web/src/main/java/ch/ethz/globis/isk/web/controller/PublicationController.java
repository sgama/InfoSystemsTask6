package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import ch.ethz.globis.isk.web.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/publications")
public class PublicationController extends TemplateController<String, Publication> {

    private static final Logger LOG = LoggerFactory.getLogger(PublicationController.class);

    @Autowired
    PublicationService publicationService;

    @Override
    protected String singleEntityView() {
        return "publication";
    }

    @Override
    protected String allEntitiesView() {
        return "publication-list";
    }

    @Override
    protected String entitySingularName() {
        return "publication";
    }

    @Override
    protected Object wrapToDTO(Publication entity) {
        return DTOs.one(entity, PublicationDto.class);
    }

    @Override
    protected BaseService<String, Publication> service() {
        return publicationService;
    }

    @ResponseBody
    @RequestMapping(value = { "/ajax" }, method = RequestMethod.GET)
    public PageResponseDto<DTO<Publication>> getPersonByAjaxJSON(HttpSession session, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "start", defaultValue = "0") Integer start, @RequestParam(value = "size", defaultValue = "20") Integer size, @RequestParam(value = "sortProperty", required = false) String sortField, @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Publication> entities = null;
        long count;
        if (sortField == null) {
            sortField = "title";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Publication>) publicationService.find(start, size);
                count = publicationService.count();
            } else {
                entities = (List<Publication>) publicationService.findByTitle(search, null, start, size);
                count = publicationService.countByTitle(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Publication>) publicationService.find(orderFilterList, start, size);
                count = publicationService.count();
            } else {
                entities = (List<Publication>) publicationService.findByTitle(search, orderFilterList, start, size);
                count = publicationService.countByTitle(search);
            }
        }
        tm.commitTransaction();
        List<DTO<Publication>> publicationSimpleDtos = DTOs.create(entities, PublicationSimpleDto.class);
        PageResponseDto<DTO<Publication>> pageResponse = new PageResponseDto<>(publicationSimpleDtos, count);
        return pageResponse;
    }

    private String setupAddPublicationForm(Model model, PublicationDto publicationDto) {
        if (publicationDto == null) {
            publicationDto = new PublicationDto();
        }
        model.addAttribute("publicationDto", publicationDto);
        return "publication-add";
    }
}
