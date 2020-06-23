package moviething;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class moviemain {
	public static final String SEARCH_URL = "http://www.omdbapi.com/?s=TITLE&apikey=APIKEY";
	public static final String SEARCH_BY_IMDB_URL = "http://www.omdbapi.com/?i=IMDB&apikey=APIKEY";
	public static String sendGetRequest(String requestURL) throws IOException {
	     StringBuffer response = new StringBuffer();
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = 
					(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("ACCEPT", "*/*");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			InputStream stream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buffer = new BufferedReader(reader);
			String line;
			while((line = buffer.readLine()) != null) {
				response.append(line);
			}
			buffer.close();
			connection.disconnect();
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return response.toString();
	}
	public static String searchMovieByTitle(String title) throws IOException{
		String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
				SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		
		
		    String jwtToken = Jwts.builder()
				.claim("name", "Santeri Suuronen")
				.claim("email", "santtus97@hotmail.com")
				.setSubject("da783fd")
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(51, ChronoUnit.MINUTES)))
				.signWith(hmacKey)
				.compact();
		
		try {
			title = URLEncoder.encode(title, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String requestUrl = SEARCH_URL
					.replaceAll("TITLE", title)
					.replaceAll("APIKEY", parseJwt(jwtToken).getBody().getSubject());
		return sendGetRequest(requestUrl);
	}
	public static String searchMovieByIMDB(String imdb) throws IOException {
		String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
				SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		
		
		    String jwtToken = Jwts.builder()
				.claim("name", "Santeri Suuronen")
				.claim("email", "santtus97@hotmail.com")
				.setSubject("da783fd")
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(51, ChronoUnit.MINUTES)))
				.signWith(hmacKey)
				.compact();
		//System.out.println(jwtToken);
		//System.out.println(parseJwt(jwtToken));
		
		String requestURL = SEARCH_BY_IMDB_URL
				    .replaceAll("IMDB", imdb)
					.replaceAll("APIKEY", parseJwt(jwtToken).getBody().getSubject());
		return sendGetRequest(requestURL);
	}

	public static Jws<Claims> parseJwt(String jwtString){
		String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
	    Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
	                                    SignatureAlgorithm.HS256.getJcaName());

	    Jws<Claims> jwt = Jwts.parserBuilder()
	            .setSigningKey(hmacKey)
	            .build()
	            .parseClaimsJws(jwtString);
	   
	    return jwt;
	}
	 

	public static void main(String[] args) throws IOException {
		  
		 
		// TODO Auto-generated method stub
		String jsonResponse = moviemain.searchMovieByTitle("batman");
		System.out.println(jsonResponse);
		jsonResponse = moviemain.searchMovieByIMDB("tt0372784");
		System.out.println(jsonResponse);
		
	
		
	}

}
