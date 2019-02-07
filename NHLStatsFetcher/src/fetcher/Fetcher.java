/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Lukas
 */
public class Fetcher
{
  
  private final String baseUrl = "http://statsapi.web.nhl.com";
  private int season;
  
  private List<Team> teams;
  private List<Player> players;
  
  
  private Fetcher() {
    season = calculateSeason();
  }
  
  public static Fetcher getInstance() {
    return FetcherHolder.INSTANCE;
  }
  
  private static class FetcherHolder
  {

    private static final Fetcher INSTANCE = new Fetcher();
  }
  
  private int calculateSeason(){
    String year;
    if(!(LocalDate.now().getMonthValue() < 9)){ //9 since in the offseason stats dont change and a new season starts in october
      year = String.valueOf(LocalDate.now().getYear()) + String.valueOf(LocalDate.now().getYear() + 1);//old season
    }else{
      year = String.valueOf(LocalDate.now().getYear() - 1) + String.valueOf(LocalDate.now().getYear());
    }
    System.out.println(year);
    return Integer.parseInt(year);
  }
  
  public List<Team> fetchTeams() throws MalformedURLException, IOException{
    if (teams != null)
      return teams;
    
    List<Team> retTeams = new ArrayList<>();
    
    //conDB.dropTeams(season);
    System.out.println("Drop Teams: " + season);
    //conDB.dropDivision();
    String standingsUrl = baseUrl + "/api/v1/standings";
    InputStream input = new URL(standingsUrl).openStream();

    JsonReader reader = Json.createReader(input);
    JsonArray divisionarr = reader.readObject().getJsonArray("records");

    for (int i = 0; i < divisionarr.size(); i++) {
      int did = divisionarr.getJsonObject(i).getJsonObject("division").getInt("id");
      /*try{
        
      }catch(Exception e){
        conDB.insertDivision(did, divisionarr.getJsonObject(i).getJsonObject("division").getString("name"));
      }*/
      
      JsonArray teams = divisionarr.getJsonObject(i).getJsonArray("teamRecords");
      for (int j = 0; j < teams.size(); j++) {

        String teamname = teams.getJsonObject(j).getJsonObject("team").getString("name");

        int teamid = teams.getJsonObject(j).getJsonObject("team").getInt("id");

        int won = teams.getJsonObject(j).getJsonObject("leagueRecord").getInt("wins");

        int lost = teams.getJsonObject(j).getJsonObject("leagueRecord").getInt("losses");

        int ot = teams.getJsonObject(j).getJsonObject("leagueRecord").getInt("ot");

        String apiURL = teams.getJsonObject(j).getJsonObject("team").getString("link");

        int points = won * 2 + ot;

        String link = teams.getJsonObject(j).getJsonObject("team").getString("link");

        String teamUrl = baseUrl + link;
        InputStream teamInput = new URL(teamUrl).openStream();
        JsonReader teamReader = Json.createReader(teamInput);
        JsonObject team = teamReader.readObject();

        String abbr = team.getJsonArray("teams").getJsonObject(0).getString("abbreviation");
        String siteUrl = team.getJsonArray("teams").getJsonObject(0).getString("officialSiteUrl");

        //System.out.println("Insert Team");
        retTeams.add(new Team(teamid, teamname, abbr, won, lost, ot, siteUrl, did, season, apiURL));
        //conDB.insertTeam(new Team(teamid, teamname, abbr, won, lost, ot, siteUrl, did, season, apiURL));
      }
    }
    
    teams = retTeams;
    return retTeams;
  }
  
  public void insertTeamsDB(){
    
  }
  
  public void updateTeams(){
    //eg update then insert
  }
  
  public List<Player> fetchPlayers() throws MalformedURLException, IOException{
    if (players != null)
      return players;
    
    List<Player> retPlayers = new ArrayList<>();
    
    //fetchTeams();
    
    //conDB.dropPlayers(season);
    //TODO CHECK URL FOR RIGHT SEASON!!!!!
    //"http://www.nhl.com/stats/rest/skaters?isAggregate=true""&reportType=basic&isGame=false&reportName=skatersummary&""sort=[{%22property%22:%22points%22,%22direction%22:%22DESC%22}]""&cayenneExp=gameTypeId=2and%20seasonId%3E=20172018and%20seasonId%3C=20172018"
    String skatersURL = "http://www.nhl.com/stats/rest/skaters?isAggregate=true"
            + "&reportType=basic&isGame=false&reportName=skatersummary&"
            + "sort=[{%22property%22:%22points%22,%22direction%22:%22DESC%22}]"
            + "&cayenneExp=gameTypeId=2and%20seasonId%3E=" + season + "and%20seasonId%3C=" + season; //3e = <, 3c >: => seasonId<=20172018and seasonId>=20172018
    InputStream input = new URL(skatersURL).openStream();

    JsonReader reader = Json.createReader(input);
    JsonArray playerArray = reader.readObject().getJsonArray("data");

    for (int i = 0; i < playerArray.size(); i++) {
      if (playerArray.getJsonObject(i).getInt("playerIsActive") == 1) {

        int id = playerArray.getJsonObject(i).getInt("playerId");
        String playerUrl = "http://statsapi.web.nhl.com/api/v1/people/" + id + "/";
        String name = playerArray.getJsonObject(i).getString("playerName");

        InputStream inputPlayer = new URL(playerUrl).openStream();
        reader = Json.createReader(inputPlayer);
        JsonObject player = reader.readObject().getJsonArray("people").getJsonObject(0);
        
        int teamid = -1;
        
        try{
          teamid = player.getJsonObject("currentTeam").getInt("id");
        }catch(NullPointerException e){
          //no team
        }
        int goals = playerArray.getJsonObject(i).getInt("goals") + playerArray.getJsonObject(i).getInt("ppGoals") + playerArray.getJsonObject(i).getInt("shGoals");
        int assists = playerArray.getJsonObject(i).getInt("assists");
        String poscode = playerArray.getJsonObject(i).getString("playerPositionCode");
        int points = playerArray.getJsonObject(i).getInt("points");
        int plusminus = playerArray.getJsonObject(i).getInt("plusMinus");
        int jerseynumber = 0;
        try {
          jerseynumber = Integer.parseInt(player.getString("primaryNumber"));
        }
        catch (Exception e) {
          jerseynumber = -1;
        }

        int posid = 0;

        switch (poscode.charAt(0)) {
          case 'G':
            posid = 1;
            break;
          case 'D':
            posid = 2;
            break;
          case 'R':
            posid = 3;
            break;
          case 'L':
            posid = 4;
            break;
          case 'C':
            posid = 5;
            break;
          default:
            posid = -1;
        }
        
        System.out.println(i);
        
        retPlayers.add(new Player(id, name, teamid, goals, assists, posid, season, points, plusminus, jerseynumber));
        
      }
    }
    players = retPlayers;
    return retPlayers;
  }
}
