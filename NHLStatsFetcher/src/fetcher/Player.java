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
public class Player
{
  private int id;
  private String name;
  private int teamid;
  private int goals, assists;
  private int posid;
  private int season;
  private int points;
  private int plusminus;
  private int jerseynumber;

  public Player(int id, String name, int teamid, int goals, int assists, int posid, int season, int points, int plusminus, int jerseynumber) {
    this.id = id;
    this.name = name;
    this.teamid = teamid;
    this.goals = goals;
    this.assists = assists;
    this.posid = posid;
    this.season = season;
    this.points = points;
    this.plusminus = plusminus;
    this.jerseynumber = jerseynumber;
  }

  public int getPlusminus() {
    return plusminus;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getTeamid() {
    return teamid;
  }

  public int getGoals() {
    return goals;
  }

  public int getAssists() {
    return assists;
  }

  public int getPosid() {
    return posid;
  }

  public int getSeason() {
    return season;
  }
  
  public int getPoints(){
    return points;
  }

  public int getJerseynumber() {
    return jerseynumber;
  }
  
  
  
}
