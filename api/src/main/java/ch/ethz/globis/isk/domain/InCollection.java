package ch.ethz.globis.isk.domain;

/**
 * A standalone publication that is part of a book. It can represent an
 * article, if the book is a collection of articles. Ic can also represent a
 * book chapter.
 */
public interface InCollection extends Publication {

    public String getNote();

    public void setNote(String note);

    public String getPages();

    public void setPages(String pages);

    public Book getParentPublication();

    public void setParentPublication(Book book);
}