package com.mdvns.mdvn.mdvnfile.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class UpdateAttchRequest {

    /*AttchInfo表中附件的Id*/
    private Long attchId;

    /*附件主体的Id*/
    private String subjectid;

    private List<String> remarks;

    @Override
    public int hashCode() {
        int result = getAttchId() != null ? getAttchId().hashCode() : 0;
        result = 31 * result + (getSubjectid() != null ? getSubjectid().hashCode() : 0);
        return result;
    }
}
