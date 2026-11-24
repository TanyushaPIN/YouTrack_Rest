package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class IssueRequest {
    private String summary;
    private String description;
    private ProjectReference project;

    public IssueRequest(String summary, String description, String projectId) {
        this.summary = summary;
        this.description = description;
        this.project = new ProjectReference(projectId);
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public ProjectReference getProject() {
        return project;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProjectReference{
        private String id;

        public ProjectReference() {}

        public String getId() {
            return id;
        }

        public ProjectReference(String id){
            this.id = id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
