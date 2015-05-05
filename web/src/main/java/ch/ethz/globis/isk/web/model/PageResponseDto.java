package ch.ethz.globis.isk.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PageResponseDto<T> {

    @JsonProperty("data")
    List<T> data;

    @JsonProperty("total")
    long total;

    public PageResponseDto() { }

    public PageResponseDto(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
