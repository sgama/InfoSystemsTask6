package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.Series;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.PublicationService;
import ch.ethz.globis.isk.service.SeriesService;
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
@RequestMapping("/series")
public class SeriesController extends TemplateController<String, Series> {

    private static final Log LOG = LogFactory.getLog(SeriesController.class);

    @Autowired
    SeriesService seriesService;

    @Autowired
    PublicationService publicationService;

    @Override
    protected String singleEntityView() {
        return "series";
    }

    @Override
    protected String allEntitiesView() {
        return "series-list";
    }

    @Override
    protected String entitySingularName() {
        return "series";
    }

    @Override
    protected BaseService<String, Series> service() {
        return seriesService;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, Series entity) {
        String seriesId = entity.getId();
        tm.beginTransaction();
        List<Publication> publications = publicationService.findBySeriesOrderedByYear(seriesId);
        List<DTO<Publication>> publicationDtoList = DTOs.create(publications, PublicationDto.class);
        tm.commitTransaction();
        model.addAttribute("publications", publicationDtoList);
        LOG.info("Showing " + publicationDtoList.size() + " publications for " + entity.getName());
    }

    @Override
    protected Object wrapToDTO(Series entity) {
        return DTOs.one(entity, SimpleSeriesDto.class);
    }

    @ResponseBody
    @RequestMapping(value = { "/ajax" }, method = RequestMethod.GET)
    public PageResponseDto<DTO<Series>> getSeriesByAjaxJSON(HttpSession session, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "start", defaultValue = "0") Integer start, @RequestParam(value = "size", defaultValue = "20") Integer size, @RequestParam(value = "sortProperty", required = false) String sortField, @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Series> entities = null;
        long count;
        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Series>) seriesService.find(start, size);
                count = seriesService.count();
            } else {
                entities = (List<Series>) seriesService.findByName(search, null, start, size);
                count = seriesService.countByName(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Series>) seriesService.find(orderFilterList, start, size);
                count = seriesService.count();
            } else {
                entities = (List<Series>) seriesService.findByName(search, orderFilterList, start, size);
                count = seriesService.countByName(search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<Series>> pageResponse = new PageResponseDto<>(DTOs.create(entities, SimpleSeriesDto.class), count);
        return pageResponse;
    }
}
