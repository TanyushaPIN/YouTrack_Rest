package dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {
    private String id;
    private String idReadable;
    private String summary;
    private String description;
    private IssueRequest.ProjectReference project;

    public String getId() {
        return id;
    }

    public String getIdReadable() {
        return idReadable;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public IssueRequest.ProjectReference getProject() {
        return project;
    }
}
