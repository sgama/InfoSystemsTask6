package ch.ethz.globis.isk.domain;

import java.util.Set;

/**
 * Represents a journal. A journal usually has more editions, each issue containing
 * a list of published articles.
 */
public interface Journal extends DomainObject {

    public String getName();

    public void setName(String name);

    public Set<JournalEdition> getEditions();

    public void addEdition(JournalEdition edition);

    public void setEditions(Set<JournalEdition> editions);

}
