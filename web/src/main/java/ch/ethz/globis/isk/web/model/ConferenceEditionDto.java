package ch.ethz.globis.isk.web.model;

import ch.ethz.globis.isk.domain.Conference;
import ch.ethz.globis.isk.domain.ConferenceEdition;
import ch.ethz.globis.isk.web.utils.EncodingUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConferenceEditionDto extends DTO<ConferenceEdition> {

    private String id;
    private Integer year;
    private DTO<Conference> conference;

    public ConferenceEditionDto() { }

    public ConferenceEditionDto(ConferenceEdition conferenceEdition) {
        this.id = EncodingUtils.encode(conferenceEdition.getId());
        this.year = conferenceEdition.getYear();
        this.conference = DTOs.one(conferenceEdition.getConference(), SimpleConferenceDto.class);
    }

    public static List<ConferenceEditionDto> fromCollection(Collection<ConferenceEdition> editions) {
        if (editions == null) {
            return new ArrayList<>();
        }
        List<ConferenceEditionDto> conferenceEditionDtoList = new ArrayList<>(editions.size());
        for (ConferenceEdition edition : editions) {
            conferenceEditionDtoList.add(new ConferenceEditionDto(edition));
        }
        return conferenceEditionDtoList;
    }

    public DTO<Conference> getConference() {
        return conference;
    }

    public String getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public static ConferenceEditionDto fromConferenceEdition(ConferenceEdition conferenceEdition) {
        if (conferenceEdition == null) {
            return null;
        }
        return new ConferenceEditionDto(conferenceEdition);
    }

    @Override
    public DTO<ConferenceEdition> convert(ConferenceEdition entity) {
        return new ConferenceEditionDto(entity);
    }
}
