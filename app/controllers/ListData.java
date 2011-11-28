package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.logic.AuthorizationUtils;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import models.Category;
import models.NdgGroup;
import models.NdgResult;
import models.NdgUser;
import models.Survey;
import models.UserRole;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class ListData extends Controller {

    public static void categories(int surveyId) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
            String query = "survey_id = " + String.valueOf(surveyId) + " order by categoryIndex";
            List<Category> categories = Category.find(query).fetch();
            JSONSerializer categoryListSerializer = new JSONSerializer();
            categoryListSerializer.include("id", "categoryIndex", "questionCollection", "label").rootName("categories");
            renderJSON(categoryListSerializer.serialize(categories));
    }

    public static void results(int surveyId, int startIndex, int endIndex, boolean isAscending, String orderBy, String searchField, String searchText) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
            StringBuilder searchFilter = null;

            if (searchField != null && searchText != null) {
                searchFilter = new StringBuilder();
                searchFilter.append(searchField).append(" like '").append(searchText).append("%' ");
            }
            StringBuilder query = new StringBuilder();
            if (searchFilter != null) {
                query.append(searchFilter);
                query.append(" and ");
            }
            query.append("survey_id = ").append(String.valueOf(surveyId));
            query.append(" order by ").append(orderBy).append(isAscending ? " asc" : " desc");

            List<NdgResult> results = NdgResult.find(query.toString()).from(startIndex).fetch(endIndex - startIndex);
            JSONSerializer surveyListSerializer = new JSONSerializer();
            surveyListSerializer.include("id", "resultId", "title", "startTime", "ndgUser.username", "latitude").exclude("*").rootName("items");

            renderJSON(addRangeToJson(surveyListSerializer.serialize(results), startIndex, NdgResult.count()));
    }

    public static void surveys(int startIndex, int endIndex, boolean isAscending, String orderBy, String searchField, String searchText) {
        if (!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        List<Survey> surveys = null;
        StringBuilder searchFilter = null;
        long totalItems = 0;
        if (searchField != null && searchText != null) {
            searchFilter = new StringBuilder();
            searchFilter.append(searchField).append(" like '").append(searchText).append("%' ");
            totalItems = Survey.count(searchFilter.toString());
        } else {
            totalItems = Survey.count();
        }
        if (orderBy != null && orderBy.equals("resultCollection")) {
            if (searchFilter != null) {
                surveys = Survey.find(searchFilter.toString()).fetch();
            } else {
                surveys = Survey.all().fetch();
            }
            Collections.sort(surveys, new SurveyNdgResultCollectionComapator());
            if (!isAscending) {
                Collections.reverse(surveys);
            }

            int subListEndIndex = surveys.size() <= endIndex
                    ? surveys.size() : endIndex;
            surveys = surveys.subList(startIndex, subListEndIndex);
        } else {
            StringBuilder query = new StringBuilder();
            if (searchFilter != null) {
                query.append(searchFilter);
            }
            query.append("order by ").append(orderBy).append(isAscending ? " asc" : " desc");
            surveys = Survey.find(query.toString()).from(startIndex).fetch(endIndex - startIndex);
        }
        serializeSurveys(surveys, startIndex, totalItems);
    }

    public static void users(int startIndex, int endIndex, boolean isAscending, String orderBy, String searchField, String groupName, String searchText) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        List<NdgUser> users = null;
        StringBuilder searchFilter = null;
        long totalItems = 0;

        String groupFilter = "ndg_group.groupName ='" + groupName + "'";

        if (searchField != null && searchText != null) {
            searchFilter = new StringBuilder();
            searchFilter.append(searchField).append(" like '").append(searchText).append("%' ");
            if (groupName != null) {
                searchFilter.append(" and ").append(groupFilter);
            }
            totalItems = NdgUser.count(searchFilter.toString());
        } else {
            if (groupName != null) {
                totalItems = NdgUser.count(groupFilter);
            } else {
                totalItems = NdgUser.count();
            }
        }

        if (orderBy != null && orderBy.equals("userRoleCollection")) {
            if (searchFilter != null) {
                users = NdgUser.find(searchFilter.toString()).fetch();
            } else {
                if (groupName != null && groupName.length() > 0) {
                    users = NdgUser.find(groupFilter).fetch();
                } else {
                    users = NdgUser.all().fetch();
                }
            }
            Collections.sort(users, new NdgUserUserRoleCollectionComapator(isAscending));
            int subListEndIndex = users.size() <= endIndex
                    ? users.size() : endIndex;
            users = users.subList(startIndex, subListEndIndex);
        } else {
            StringBuilder query = new StringBuilder();
            if (searchFilter != null) {
                query.append(searchFilter);
            }
            if (searchFilter != null && groupName != null) {
                query.append(" and ").append(groupFilter);
            } else if (groupName != null) {
                query.append(groupFilter);
            }
            query.append("order by ").append(orderBy).append(isAscending ? " asc" : " desc");
            users = NdgUser.find(query.toString()).from(startIndex).fetch(endIndex - startIndex);
        }
        serializeUsers(users, startIndex, totalItems);
    }

    public static void groups(int startIndex, int endIndex, boolean isAscending, String orderBy, String searchField, String searchText) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response, true)) {
            return;
        }
        List<NdgGroup> groups = null;
        StringBuilder searchFilter = null;
        long totalItems = 0;
        if (searchField != null && searchText != null) {
            searchFilter = new StringBuilder();
            searchFilter.append(searchField).append(" like '").append(searchText).append("%' ");
            totalItems = NdgGroup.count(searchFilter.toString());
        } else {
            totalItems = NdgGroup.count();
        }
        if (orderBy != null && orderBy.equals("resultCollection")) {
            if (searchFilter != null) {
                groups = NdgGroup.find(searchFilter.toString()).fetch();
            } else {
                groups = NdgGroup.all().fetch();
            }

            int subListEndIndex = groups.size() <= endIndex
                    ? groups.size() : endIndex;
            groups = groups.subList(startIndex, subListEndIndex);
        } else {
            StringBuilder query = new StringBuilder();
            if (searchFilter != null) {
                query.append(searchFilter);
            }
            query.append("order by ").append(orderBy).append(isAscending ? " asc" : " desc");
            groups = NdgGroup.find(query.toString()).from(startIndex).fetch(endIndex);
        }
        serializeGroups(groups, startIndex, totalItems);
    }

    private static void serializeUsers(List<NdgUser> users, int startIndex, long totalSize) {
        JSONSerializer userListSerializer = new JSONSerializer();
        userListSerializer.include("id", "username", "phoneNumber", "email", "userRoleCollection.ndgRole.roleName", "firstName", "lastName").exclude("*").rootName("items");
        renderJSON(addRangeToJson(userListSerializer.serialize(users), startIndex, totalSize));
    }

    private static void serializeSurveys(List<Survey> subList, int startIndex, long totalSize) {
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.transform(new NdgResultCollectionTransformer(), "resultCollection");
        surveyListSerializer.include("id", "title", "uploadDate", "idUser", "surveyId", "ndgUser.username", "resultCollection", "available").exclude("*").rootName("items");
        renderJSON(addRangeToJson(surveyListSerializer.serialize(subList), startIndex, totalSize));
    }

    private static void serializeGroups(List<NdgGroup> subList, int startIndex, long totalSize) {
        JSONSerializer surveyListSerializer = new JSONSerializer();
        surveyListSerializer.transform(new NdgUserCollectionTransformer(), "userCollection");
        surveyListSerializer.include("groupName", "userCollection").exclude("*").rootName("items");
        renderJSON(addRangeToJson(surveyListSerializer.serialize(subList), startIndex, totalSize));
    }

    private static String addRangeToJson(String jsonString, int startIndex, long totalSize) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            object.addProperty("startIndex", startIndex);
            object.addProperty("totalSize", totalSize);
        }
        return element.toString();
    }

    public static class SurveyNdgResultCollectionComapator implements Comparator<Survey> {

        public int compare(Survey o1, Survey o2) {
            return o1.resultCollection.size() < o2.resultCollection.size() ? 1 : -1;
        }
    }

    public static void sendSurveysUserList(String formID) {
        if(!AuthorizationUtils.checkWebAuthorization(session, response)) {
            return;
        }
        List<NdgUser> users = JPA.em().createNamedQuery("findUserSendingSurvey").setParameter("surveyId", formID).getResultList();

        JSONSerializer userListSerializer = new JSONSerializer();
        userListSerializer.include("id", "username", "phoneNumber").exclude("*").rootName("items");

        renderJSON(userListSerializer.serialize(users));
    }

    private static class NdgUserUserRoleCollectionComapator implements Comparator<NdgUser> {

        private boolean isAscending = true;

        NdgUserUserRoleCollectionComapator(boolean isAscending) {
            this.isAscending = isAscending;
        }

        public int compare(NdgUser o1, NdgUser o2) {
            int retval = 0;
            UserRole role1 = (UserRole) o1.userRoleCollection.toArray()[0];
            UserRole role2 = (UserRole) o2.userRoleCollection.toArray()[0];
            retval = role1.ndgRole.id.compareTo(role2.ndgRole.id);
            return isAscending ? retval : -retval;
        }
    }

    private static class NdgResultCollectionTransformer extends AbstractTransformer {

        public void transform(Object collection) {
            Collection ndgResultCollection = (Collection) collection;
            getContext().write(String.valueOf(ndgResultCollection.size()));
        }
    }

    private static class NdgUserCollectionTransformer extends AbstractTransformer {

        public void transform(Object collection) {
            Collection ndgUserCollection = (Collection) collection;
            getContext().write(String.valueOf(ndgUserCollection.size()));
        }
    }
}
