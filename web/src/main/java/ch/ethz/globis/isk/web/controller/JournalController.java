package ch.ethz.globis.isk.web.controller;

import ch.ethz.globis.isk.domain.Article;
import ch.ethz.globis.isk.domain.Journal;
import ch.ethz.globis.isk.domain.JournalEdition;
import ch.ethz.globis.isk.service.ArticleService;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.JournalEditionService;
import ch.ethz.globis.isk.service.JournalService;
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
@RequestMapping("/journals")
public class JournalController extends TemplateController<String, Journal> {

    private static final Log LOG = LogFactory.getLog(JournalController.class);

    @Autowired
    JournalService journalService;

    @Autowired
    JournalEditionService journalEditionService;

    @Autowired
    ArticleService articleService;

    @Override
    protected String singleEntityView() {
        return "journal";
    }

    @Override
    protected String allEntitiesView() {
        return "journal-list";
    }

    @Override
    protected String entitySingularName() {
        return "journal";
    }

    @Override
    protected Object wrapToDTO(Journal entity) {
        return DTOs.one(entity, JournalDto.class);
    }

    @Override
    protected BaseService<String, Journal> service() {
        return journalService;
    }

    @RequestMapping(value = {"/editions/{editionId:.+}/journal/{journalId:.+}"}, method = RequestMethod.GET)
    public String showByJournalEdition(HttpSession session,
                                       @PathVariable String editionId,
                                       @PathVariable String journalId,
                                       Model model) {
        //the ids are encoded in the url
        editionId = EncodingUtils.decode(editionId);
        journalId = EncodingUtils.decode(journalId);

        tm.beginTransaction();
        //get the journal edition
        JournalEdition journalEdition = journalService.findByEdition(journalId, editionId);
        if (journalEdition!= null) {
            String journalEditionId = journalEdition.getId();
            List<Article> publications = articleService.findByJournalEditionOrderedByYear(journalEditionId);

            List<DTO<Article>> publicationDtos = DTOs.create(publications, PublicationDto.class);

            //add information to the model
            model.addAttribute("journalEdition", journalEdition);
            model.addAttribute("publications", publicationDtos);
        }

        tm.commitTransaction();
        return "journal-edition-home";
    }

    @ResponseBody
    @RequestMapping(value = {"/ajax"}, method = RequestMethod.GET)
    public PageResponseDto<DTO<Journal>> getSeriesByAjaxJSON(
            HttpSession session,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sortProperty", required = false) String sortField,
            @RequestParam(value = "sortDirection", required = false) String sortDirection) {
        List<Journal> entities = null;
        long count;

        if (sortField == null) {
            sortField = "name";
            sortDirection = "asc";
        }

        tm.beginTransaction();
        if (sortField == null || sortDirection == null) {
            if (search == null) {
                entities = (List<Journal>) journalService.find(start, size);
                count = journalService.count();
            } else {
                entities = (List<Journal>) journalService.findByName( search, null, start, size);
                count = journalService.countByName( search);
            }
        } else {
            List<OrderFilter> orderFilterList = new ArrayList<>();
            Order direction = Order.valueOf(sortDirection.toUpperCase());
            orderFilterList.add(new OrderFilter(sortField, direction));
            if (search == null) {
                entities = (List<Journal>) journalService.find(orderFilterList, start, size);
                count = journalService.count();
            } else {
                entities = (List<Journal>) journalService.findByName( search, orderFilterList, start, size);
                count = journalService.countByName( search);
            }
        }
        tm.commitTransaction();
        PageResponseDto<DTO<Journal>> pageResponse =
                new PageResponseDto<>(DTOs.create(entities, SimpleJournalDto.class), count);
        return pageResponse;
    }

    @Override
    protected void addAdditionalDataAboutEntity(Model model, Journal entity) {
        tm.beginTransaction();
        List<JournalEdition> editions = journalEditionService.findByJournalOrdered(entity.getId());
        List<DTO<JournalEdition>> editionDtos = DTOs.create(editions, JournalEditionDto.class);
        tm.commitTransaction();
        model.addAttribute("editions", editionDtos);
    }
}