package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.Required;
import play.db.jpa.Model;

 
@Entity
@Table(name = "jobs")
public class Jobs extends Model {
    
    @Required
    @Column(nullable = false )
    public String surveyId;

    @Required
    @Column(nullable = false )
    public String dateTo;

    @Required
    @Column(nullable = false )
    public String dateFrom;

    @Required
    @Column(nullable = false )
    public String email;


    public boolean complete;

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
    
    public Jobs(String surveyId, String dateTo, String dateFrom, String email, Boolean complete) {
        this.surveyId = surveyId;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
        this.email = email;
        this.complete = complete;
    }

 
}