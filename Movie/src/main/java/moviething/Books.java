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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Books {
	private static final String SEARCH_BY_ISBN = "http://openlibrary.org/api/books?bibkeys=ISBN";
	public static String sendGetBook(String requestUrl) throws IOException {
		StringBuffer response = new StringBuffer();		
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = 
					(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("ACCEPT","*/*");
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
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	public Books() {
		
	}
	public static String searchBookByISBN(String ISBN) throws IOException {
	
		String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
				SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		
		
		    String jwtToken = Jwts.builder()
				.setSubject(ISBN)
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(51, ChronoUnit.MINUTES)))
				.signWith(hmacKey)
				.compact();
			String requestURL = SEARCH_BY_ISBN
				    .replaceAll("ISBN", parseJwt(jwtToken).getBody().getSubject());
	return sendGetBook(requestURL);
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
	String jsonResponse = Books.searchBookByISBN("0201558025");
	System.out.println(jsonResponse);
	}

}
