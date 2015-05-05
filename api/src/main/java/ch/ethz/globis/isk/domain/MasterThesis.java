package ch.ethz.globis.isk.domain;

/**
 * Represents a Master Thesis.
 */
public interface MasterThesis extends Publication {

    public School getSchool();

    public void setSchool(School school);

}