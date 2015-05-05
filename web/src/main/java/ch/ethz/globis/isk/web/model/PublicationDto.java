package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.*;
import ch.ethz.globis.isk.web.utils.EncodingUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PublicationDto<T extends Publication> extends DTO<T> {

    private String id;

    private List<DTO<Person>> authors;

    private List<DTO<Person>> editors;

    private String title;

    private String url;

    private Integer year;

    private Integer month;

    private String note;

    private Integer number;

    private String pages;

    private String ee;

    private String volume;

    private String isbn;

    private DTO<Journal> journal;

    private DTO<JournalEdition> journalEdition;

    private DTO<ConferenceEdition> conferenceEdition;

    private DTO<Publication> book;

    private DTO<Publication> proceedings;

    private DTO<Publisher> publisher;

    private DTO<Series> series;

    private DTO<School> school;

    private List<DTO<Publication>> children;

    public PublicationDto() {
    }

    public PublicationDto(Article publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setPages(publication.getPages());
        setEe(publication.getElectronicEdition());
        setJournalEdition(DTOs.one(publication.getJournalEdition(), JournalEditionDto.class));
    }

    public PublicationDto(Book publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setMonth(publication.getMonth());
        setEe(publication.getElectronicEdition());
        setVolume(publication.getVolume());
        setIsbn(publication.getIsbn());
        setPublisher(DTOs.one(publication.getPublisher(), SimplePublisherDto.class));
        setSeries(DTOs.one(publication.getSeries(), SimpleSeriesDto.class));
        Set<Publication> children = new HashSet<Publication>(publication.getPublications());
        setChildren(DTOs.create(children, PublicationDto.class));
    }

    public PublicationDto(InCollection publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setNote(publication.getNote());
        setPages(publication.getPages());
        setEe(publication.getElectronicEdition());
        setBook(DTOs.one(publication.getParentPublication(), ParentPublicationDto.class));
    }

    public PublicationDto(InProceedings publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setNote(publication.getNote());
        setPages(publication.getPages());
        setEe(publication.getElectronicEdition());
        setProceedings(DTOs.one(publication.getProceedings(), ParentPublicationDto.class));
        if (publication.getProceedings() != null) {
            Proceedings proceedings = publication.getProceedings();
            setConferenceEdition(DTOs.one(proceedings.getConferenceEdition(), ConferenceEditionDto.class));
        }
    }

    public PublicationDto(MasterThesis publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setSchool(DTOs.one(publication.getSchool(), SimpleSchoolDto.class));
    }

    public PublicationDto(PhdThesis publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setMonth(publication.getMonth());
        setNote(publication.getNote());
        setNumber(publication.getNumber());
        setEe(publication.getElectronicEdition());
        setIsbn(publication.getIsbn());
        setPublisher(DTOs.one(publication.getPublisher(), SimplePublisherDto.class));
        setSchool(DTOs.one(publication.getSchool(), SimpleSchoolDto.class));
    }

    public PublicationDto(Proceedings publication) {
        String id = EncodingUtils.encode(publication.getId());
        setId(id);
        setAuthors(DTOs.create(publication.getAuthors(), SimplePersonDto.class));
        setEditors(DTOs.create(publication.getEditors(), SimplePersonDto.class));
        setTitle(publication.getTitle());
        setYear(publication.getYear());
        setNote(publication.getNote());
        setNumber(publication.getNumber());
        setEe(publication.getElectronicEdition());
        setVolume(publication.getVolume());
        setIsbn(publication.getIsbn());
        setPublisher(DTOs.one(publication.getPublisher(), SimplePublisherDto.class));
        setSeries(DTOs.one(publication.getSeries(), SimpleSeriesDto.class));
        setConferenceEdition(DTOs.one(publication.getConferenceEdition(), ConferenceEditionDto.class));
        Set<Publication> children = new HashSet<Publication>(publication.getPublications());
        setChildren(DTOs.create(children, PublicationDto.class));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DTO<Person>> getAuthors() {
        return authors;
    }

    public void setAuthors(List<DTO<Person>> authors) {
        this.authors = authors;
    }

    public List<DTO<Person>> getEditors() {
        return editors;
    }

    public void setEditors(List<DTO<Person>> editors) {
        this.editors = editors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public DTO<Journal> getJournal() {
        return journal;
    }

    public void setJournal(DTO<Journal> journal) {
        this.journal = journal;
    }

    public DTO<JournalEdition> getJournalEdition() {
        return journalEdition;
    }

    public void setJournalEdition(DTO<JournalEdition> journalEdition) {
        this.journalEdition = journalEdition;
    }

    public DTO<ConferenceEdition> getConferenceEdition() {
        return conferenceEdition;
    }

    public void setConferenceEdition(DTO<ConferenceEdition> conferenceEdition) {
        this.conferenceEdition = conferenceEdition;
    }

    public DTO<Publication> getBook() {
        return book;
    }

    public void setBook(DTO<Publication> book) {
        this.book = book;
    }

    public DTO<Publication> getProceedings() {
        return proceedings;
    }

    public void setProceedings(DTO<Publication> proceedings) {
        this.proceedings = proceedings;
    }

    public DTO<Publisher> getPublisher() {
        return publisher;
    }

    public void setPublisher(DTO<Publisher> publisher) {
        this.publisher = publisher;
    }

    public DTO<Series> getSeries() {
        return series;
    }

    public void setSeries(DTO<Series> series) {
        this.series = series;
    }

    public DTO<School> getSchool() {
        return school;
    }

    public void setSchool(DTO<School> school) {
        this.school = school;
    }

    public List<DTO<Publication>> getChildren() {
        return children;
    }

    public void setChildren(List<DTO<Publication>> children) {
        this.children = children;
    }

    @Override
    public DTO<T> convert(T entity) {
        if (entity instanceof Article) {
            return new PublicationDto((Article) entity);
        } else if (entity instanceof Book) {
            return new PublicationDto((Book) entity);
        } else if (entity instanceof InCollection) {
            return new PublicationDto((InCollection) entity);
        } else if (entity instanceof InProceedings) {
            return new PublicationDto((InProceedings) entity);
        } else if (entity instanceof MasterThesis) {
            return new PublicationDto((MasterThesis) entity);
        } else if (entity instanceof PhdThesis) {
            return new PublicationDto((PhdThesis) entity);
        } else if (entity instanceof Proceedings) {
            return new PublicationDto((Proceedings) entity);
        } else {
            return null;
        }
    }
}
