package ch.ethz.globis.isk.web.controller;


import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.service.BaseService;
import ch.ethz.globis.isk.service.TransactionManager;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * An abstract web controlled containing the the shared behaviour for a
 * controller over web pages that interact with the same type of domain object.
 * @param <K>                   The type of the id of the domain object.
 * @param <T>                   The type of the domain object.
 */
public abstract class TemplateController<K extends Serializable, T extends DomainObject> {

    /** The name of the template for viewing a single object of type T */
    protected abstract String singleEntityView();

    /** The name of the template for viewing all objects of type T */
    protected abstract String allEntitiesView();

    /** The singular name corresponding to objects of type T. Used by DTOs. */
    protected abstract String entitySingularName();

    protected void addAdditionalDataAboutEntity(Model model, T entity) { }

    /** Wrap the DomainObject to its corresponding DTO. */
    protected abstract Object wrapToDTO(T entity);

    /** The BaseService object corresponding to objects of type T. */
    protected abstract BaseService<K, T> service();

    @Autowired
    protected TransactionManager tm;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String showAll() {
        return allEntitiesView();
    }

    @RequestMapping(value = "/{entityId:.+}", method = RequestMethod.GET)

    public String showEntity(HttpSession session,
                             @PathVariable K entityId, Model model) {
        //the id is encoded in the URL
        entityId = (K) EncodingUtils.decode((String) entityId);

        tm.beginTransaction();
        T entity = service().findOne(entityId);
        tm.commitTransaction();
        model.addAttribute(entitySingularName(), wrapToDTO(entity));
        addAdditionalDataAboutEntity(model, entity);

        return singleEntityView();
    }
}