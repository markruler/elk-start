package com.example.elk.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RestDaoImpl implements RestDao {

  @Override
  public String getAggregation(String index, String query) {
    HttpURLConnection conn = null;
    String result = "";
    String mapping = index;
    log.info("mapping : {}", mapping);
    log.info("query : {}", query);

    try {
      // URL 설정
      URL url = new URL("http://127.0.0.1:9200" + mapping);

      conn = (HttpURLConnection) url.openConnection();
      // Request 형식 설정
      conn.setRequestMethod("POST");

      // request에 JSON data 준비
      conn.setRequestProperty("Content-Type", "application/json; utf-8");
      conn.setRequestProperty("Accept", "application/json");
      conn.setDoOutput(true);

      // request에 쓰기
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
      bw.write(query);
      bw.flush();
      bw.close();
      // try (OutputStream os = conn.getOutputStream()) {
      //   byte[] input = query.getBytes("utf-8");
      //   os.write(input, 0, input.length);
      // }
      // try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
      //   StringBuilder response = new StringBuilder();
      //   String responseLine = null;
      //   while ((responseLine = br.readLine()) != null) {
      //     response.append(responseLine.trim());
      //   }
      //   System.out.println(response.toString());
      // }

      // 보내고 결과값 받기
      int responseCode = conn.getResponseCode();
      if (responseCode == 400) {
        log.info("400:: 해당 명령을 실행할 수 없음");
      } else if (responseCode == 500) {
        log.info("500:: 서버 에러, 문의 필요");
      } else { // 성공 후 응답 JSON 데이터받기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
          sb.append(line);
        }

        result = sb.toString();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}