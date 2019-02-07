/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

/**
 *
 * @author Lukas
 */
public class Team
{
  private int id;
  private String name;
  private String abbr;
  private int wins,losses,ot;
  private String url;
  private int did;
  private int season;
  private String apiURL;

  public Team(int id, String name, String abbr, int wins, int losses, int ot, String url, int did, int season, String apiURL) {
    this.id = id;
    this.name = name;
    this.abbr = abbr;
    this.wins = wins;
    this.losses = losses;
    this.ot = ot;
    this.url = url;
    this.did = did;
    this.season = season;
    this.apiURL = apiURL;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAbbr() {
    return abbr;
  }

  public int getWins() {
    return wins;
  }

  public int getLosses() {
    return losses;
  }

  public int getOtLosses() {
    return ot;
  }

  public String getUrl() {
    return url;
  }

  public int getDid() {
    return did;
  }

  public int getSeason() {
    return season;
  }

  public String getApiURL() {
    return apiURL;
  }
  
  public int getPoints(){
    return wins*2 + ot;
  }
  
  public int getGamesPlayed(){
    return getWins() + getLosses() + getOtLosses();
  }
  
  
  
  
}
