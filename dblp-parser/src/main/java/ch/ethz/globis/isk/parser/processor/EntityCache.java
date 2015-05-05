package ch.ethz.globis.isk.parser.processor;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A cache of all the currently processed entities.
 */
public class EntityCache {

    private static final Logger LOG = LoggerFactory.getLogger(EntityCache.class);

    /** A Map containing all of the processed Domain Objects. */
    private Map<String, DomainObject> map = new HashMap<>();
    @Autowired private PublicationService publicationService;
    @Autowired private JournalService journalService;
    @Autowired private JournalEditionService journalEditionService;
    @Autowired private ConferenceService conferenceService;
    @Autowired private ConferenceEditionService conferenceEditionService;
    @Autowired private PublisherService publisherService;
    @Autowired private SchoolService schoolService;
    @Autowired private SeriesService seriesService;
    @Autowired private PersonService personService;
    @Autowired private TransactionManager tm;

    public EntityCache() { }

    public Object lookup(String key) {
        return map.get(key);
    }

    public Object put(String key, DomainObject value) {
        Object added = map.put(key, value);
        return added;
    }

    public <T> T findAndCast(String key, Class<T> clazz) {
        Object cached = map.get(key);
        if (cached == null) {
            return null;
        }
        if (clazz.isInstance(cached)) {
            return clazz.cast(cached);
        } else {
            LOG.warn("Could not cast {} to {}", cached, clazz);
            return null;
        }
    }

    /*
     * Special prefixes needed because some entities might share the same name
     */
    public Object putSchool(String key, DomainObject value) {
        return put("school-" + key, value);
    }

    public Object putSeries(String key, DomainObject value) {
        return put("series-" + key, value);
    }

    public Object putPublisher(String key, DomainObject value) {
        return put("publisher-" + key, value);
    }

    public Object putJournal(String key, DomainObject value) {
        return put("journal-" + key, value);
    }

    public Object putConference(String key, DomainObject value) {
        return put("conf-" + key, value);
    }

    public Object putBook(String key, DomainObject value) {
        return put("book-" + key, value);
    }

    /*
     * Methods used to find violations of the data model.
     */
    public Book findBook(String key) {
        Object entry = map.get( "book-" + key);
        if (entry instanceof Book) {
            return (Book) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + key);
        }
        return null;
    }

    public Journal findJournal(String key) {
        Object entry = map.get("journal-" + key);
        if (entry instanceof Journal) {
            return (Journal) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + key);
        }
        return null;
    }

    public Conference findConference(String key) {
        Object entry = map.get("conf-" + key);
        if (entry instanceof Conference) {
            return (Conference) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + key);
        }
        return null;
    }

    public Series findSeries(String name) {
        Object entry = map.get("series-" + name);
        if (entry instanceof Series) {
            return (Series) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + name);
        }
        return null;
    }

    public Publisher findPublisher(String name) {
        Object entry = map.get("publisher-" + name);
        if (entry instanceof Publisher) {
            return (Publisher) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + name);
        }
        return null;
    }

    public School findSchool(String name) {
        Object entry = map.get("school-" + name);
        if (entry instanceof School) {
            return (School) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + name);
        }
        return null;
    }

    public Publication findPublication(String key) {
        Object entry = map.get(key);
        if (entry instanceof Publication) {
            return (Publication) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + key);
        }
        return null;
    }

    public Proceedings findProceedings(String key) {
        Object entry = map.get(key);
        if (entry instanceof Proceedings) {
            return (Proceedings) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + key);
        }
        return null;
    }

    public Person findPerson(String name) {
        Object entry = map.get(name);
        if (entry instanceof Person) {
            return (Person) entry;
        } else if (entry != null) {
            LOG.debug("Cache retrieval error for " + name);
        }
        return null;
    }

    public void flush() {
        flushSingleTransaction();
    }



    private void flushSingleTransaction() {
        tm.beginTransaction();

        LOG.info("Starting flush of object cache to disk.");
        Iterator<Map.Entry<String, DomainObject>> it = map.entrySet().iterator();

        /*
         * Persist all the of the publications first.
         */
        LOG.info("Cache holding " + map.size() + " objects");
        LOG.info("Persisting publications. ");
        while (it.hasNext()) {
            Map.Entry<String, DomainObject> entry = it.next();
            Object entity = entry.getValue();
            if (entity instanceof Publication) {
                if (!isValidPublication((Publication) entity)) {
                    it.remove();
                }
            }
        }
        it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DomainObject> entry = it.next();
            DomainObject entity = entry.getValue();
            if (entity instanceof Publication) {
                tryInsert(publicationService, entity);
            } else if (entity instanceof Journal) {
                journalEditionService.insert(((Journal) entity).getEditions());
                tryInsert(journalService, entity);
            } else if (entity instanceof Conference) {
                conferenceEditionService.insert(((Conference) entity).getEditions());
                tryInsert(conferenceService, entity);
            } else if (entity instanceof Series) {
                tryInsert(seriesService, entity);
            } else if (entity instanceof Publisher) {
                tryInsert(publisherService, entity);
            } else if (entity instanceof School) {
                tryInsert(schoolService, entity);
            } else if (entity instanceof Person) {
                tryInsert(personService, entity);
            }
        }
        tm.commitTransaction();
        map.clear();
        LOG.info("End flush of object cache to disk");
    }

    /**
     * Wrapper method of the insert method for a service, to ease
     * exception handling.
     * @param baseService           The service used to insert the object.
     * @param object                The object to be inserted into the database.
     */
    private void tryInsert(BaseService baseService, DomainObject object) {
        try {
            baseService.insert(object);
        } catch (Exception ex) {
            //ignore invalid objects
            LOG.warn("Problem encountered inserting object. {} {} {}", object.getClass(), object, ex.getMessage());
        }
    }

    /**
     * Check if a publication is valid. Publications without a title are Proceedings or Book
     * publications created when processing InProceedings and InCollection publications and should
     * not be inserted.
     *
     * Furthermore, inProceedings and inCollection objects pointing to these publications should instead
     * have a null parent publication.
     *
     * @param publication               The publication to be checked.
     * @return                          True if the publication is valid and can be inserted in the
     *                                  database, false otherwise.
     */
    private boolean isValidPublication(Publication publication) {
        //Do not insert publications without a title
        if (publication.getTitle() == null) {
            return false;
        }
        if (publication instanceof InProceedings) {
            InProceedings inProceedings = (InProceedings) publication;
            Proceedings parent = inProceedings.getProceedings();
            if (parent != null && parent.getTitle() == null) {
                inProceedings.setProceedings(null);
                if (parent.getConferenceEdition() != null) {
                    parent.getConferenceEdition().setProceedings(null);
                }
            }
        } else if (publication instanceof InCollection) {
            InCollection inCollection = (InCollection) publication;
            Book parent = inCollection.getParentPublication();
            if (parent != null && parent.getTitle() == null) {
                inCollection.setParentPublication(null);
                if (parent.getSeries() != null) {
                    parent.getSeries().getPublications().remove(parent);
                }
                if (parent.getPublisher() != null) {
                    parent.getPublisher().getPublications().remove(parent);
                }
            }
        }
        return true;
    }
}