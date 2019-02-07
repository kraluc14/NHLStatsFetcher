/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhlstatsfetcher;

import nhlstatsbot.NHLStatsBot;
import fetcher.Fetcher;
import fetcher.Player;
import fetcher.Team;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 *
 * @author Lukas
 */
public class NHLStatsFetcher
{

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws IOException, TelegramApiRequestException {

   Fetcher f = Fetcher.getInstance();
   
   f.fetchPlayers();
  }

}
