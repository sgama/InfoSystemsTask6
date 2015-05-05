package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.School;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.service.SchoolService;
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
@RequestMapping("/schools")
public class SchoolController extends TemplateController<String, School> {

    private static final Log LOG = LogFactory.getLog(SchoolController.class);

    @Autowired
    SchoolService schoolService;

    @Autowired
    PublicationService publicationService;

    @Override
    protected String singleEntityView() {
        return "school";
    }

    @Override
    protected String allEntitiesView() {
        return "school-list";
    }

    @Override
    protected String entitySingularName() {
        return "school";
    }

    @Override
    protected BaseService<String, School> service() {
        return schoolService;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, School entity) {
        String schoolId = entity.getId();
        tm.beginTransaction();
        List<Publication> publications = publicationService.findBySchoolOrderedByYear(schoolId);
        List<DTO<Publication>> publicationDtoList = DTOs.create(publications, PublicationDto.class);
        model.addAttribute("publications", publicationDtoList);
        tm.commitTransaction();
        LOG.info("Showing " + publicationDtoList.size() + " publications for " + entity.getName());
    }

    @Override
    protected Object wrapToDTO(School entity) {
        return DTOs.one(entity, SimpleSchoolDto.class);
    }

    @ResponseBody
    @RequestMapping(value = { "/ajax" }, method = RequestMethod.GET)
    public PageResponseDto<DTO<School>> getSchoolByAjaxJSON(HttpSession session, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "start", defaultValue = "0") Integer start, @RequestParam(value = "size", defaultValue = "20") Integer size, @RequestParam(value = "sortProperty", required = false) String sortField, @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<School> entities = null;
        long count;
        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<School>) schoolService.find(start, size);
                count = schoolService.count();
            } else {
                entities = (List<School>) schoolService.findByName(search, null, start, size);
                count = schoolService.countByName(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<School>) schoolService.find(orderFilterList, start, size);
                count = schoolService.count();
            } else {
                entities = (List<School>) schoolService.findByName(search, orderFilterList, start, size);
                count = schoolService.countByName(search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<School>> pageResponse = new PageResponseDto<>(DTOs.create(entities, SimpleSchoolDto.class), count);
        return pageResponse;
    }
}
