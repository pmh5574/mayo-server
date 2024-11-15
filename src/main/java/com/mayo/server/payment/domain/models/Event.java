package com.mayo.server.payment.domain.models;

import com.mayo.server.common.utility.CommonUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event")
@Entity
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aggregateId;
    private String aggregateType;
    private String eventType;
    private Long version;
    private String data;
    private String createdAt;

    public static String parsedStringEvent(Object data) {

        return CommonUtility.getJSONString(data);
    }

    public <T extends Record> T getMetadata(JSONParser jsonParser, Class<T> record) {
        try {
            return CommonUtility.getMappingJson((JSONObject) jsonParser.parse(this.data), record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}