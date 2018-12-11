package com.education.xeal.modal;

/*
 * @author by Mohit Arora on 14/5/18.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "success",
        "msg"
})
public class GetNotificationModal {

    @JsonProperty("success")
    private String success;
    @JsonProperty("msg")
    private List<String> msg = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("success")
    public String getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(String success) {
        this.success = success;
    }

    @JsonProperty("msg")
    public List<String> getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(List<String> msg) {
        this.msg = msg;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
