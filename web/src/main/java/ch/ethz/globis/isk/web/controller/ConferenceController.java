package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.*;
import ch.ethz.globis.isk.util.Order;
import ch.ethz.globis.isk.util.OrderFilter;
import ch.ethz.globis.isk.web.model.*;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
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
@RequestMapping("/conferences")
public class ConferenceController extends TemplateController<String, Conference> {

    private static final Log LOG = LogFactory.getLog(ConferenceController.class);

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ConferenceEditionService conferenceEditionService;

    @Autowired
    private InProceedingsService inProceedingsService;

    @Override
    protected String singleEntityView() {
        return "conference";
    }

    @Override
    protected String allEntitiesView() {
        return "conference-list";
    }

    @Override
    protected String entitySingularName() {
        return "conference";
    }

    @Override
    protected Object wrapToDTO(Conference entity) {
        return DTOs.one(entity, ConferenceDto.class);
    }

    @Override
    protected BaseService<String, Conference> service() {
        return conferenceService;
    }

    @RequestMapping(value = {"/editions/{editionId:.+}/conference/{conferenceId:.+}"},
                    method = RequestMethod.GET)
    public String showByConferenceEdition(HttpSession session,
                                          @PathVariable String editionId,
                                          @PathVariable String conferenceId,
                                          Model model) {
        //the ids in the URL are encoded, decode them before searching the database
        editionId = EncodingUtils.decode(editionId);
        conferenceId = EncodingUtils.decode(conferenceId);

        tm.beginTransaction();
        //get the conference edition
        ConferenceEdition edition = conferenceService.findByEdition(conferenceId, editionId);
        if (edition != null && edition.getProceedings() != null) {
            String proceedingsId = edition.getProceedings().getId();
            List<InProceedings> publications = inProceedingsService.findByProceedingsIdOrderByYear(proceedingsId);

            List<DTO<InProceedings>> publicationDtos = DTOs.create(publications, PublicationDto.class);

            //add information to the model
            model.addAttribute("conferenceEdition", edition);
            model.addAttribute("publications", publicationDtos);
            model.addAttribute("proceedings", DTOs.one(edition.getProceedings(), PublicationDto.class));
        }
        tm.commitTransaction();
        return "conference-edition-home";
    }

    @ResponseBody
    @RequestMapping(value = {"/ajax"}, method = RequestMethod.GET)
    public PageResponseDto<DTO<Conference>> getSeriesByAjaxJSON(
            HttpSession session,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sortProperty", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Conference> entities = null;
        long count;

        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }
        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Conference>) conferenceService.find(start, size);
                count = conferenceService.count();
            } else {
                entities = (List<Conference>) conferenceService.findByName(search, null, start, size);
                count = conferenceService.countByName(search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Conference>) conferenceService.find(orderFilterList, start, size);
                count = conferenceService.count();
            } else {
                entities = (List<Conference>) conferenceService.findByName(search, orderFilterList, start, size);
                count = conferenceService.countByName(search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<Conference>> pageResponse = new PageResponseDto<>(DTOs.create(entities, SimpleConferenceDto.class), count);
        return pageResponse;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, Conference entity) {
        tm.beginTransaction();
        List<ConferenceEdition> editions = conferenceEditionService.findByConferenceOrderedByYear(entity.getId());
        List<DTO<ConferenceEdition>> editionDtos = DTOs.create(editions, ConferenceEditionDto.class);
        tm.commitTransaction();
        model.addAttribute("editions", editionDtos);
    }
}
