
package com.movies.action;

import com.opensymphony.xwork2.ActionSupport;
import java.net.URLEncoder; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class SearchDetailsAction extends ActionSupport{
    String SearchKeyword,SearchType;    
    boolean PresentInLocalDB;    
    List<MovieDetailPOJO> MovieDetailList ;
    MovieDetailPOJO obj ;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet results = null ;
    int actorid, movieid,record_counter=0;
    public void setRecord_counter(int record_counter) {
            this.record_counter = record_counter;
    }
    public int getRecord_counter() {
            return record_counter;
    }
    
    public boolean isPresentInLocalDB() {
        return PresentInLocalDB;
    }

    public void setPresentInLocalDB(boolean PresentInLocalDB) {
        this.PresentInLocalDB = PresentInLocalDB;
    }

    public List<MovieDetailPOJO> getMovieDetailList() {
        return MovieDetailList;
    }

    public void setMovieDetailList(List<MovieDetailPOJO> MovieDetailList) {
        this.MovieDetailList = MovieDetailList;
    }
    
    
    public String getSearchKeyword() {
        return SearchKeyword;
    }

    public void setSearchKeyword(String SearchKeyword) {
        this.SearchKeyword = SearchKeyword;
    }

    public String getSearchType() {
        return SearchType;
    }

    public void setSearchType(String SearchType) {
        this.SearchType = SearchType;
    }
    
    
     @Override
    public String execute () throws Exception {
        MovieDetailList = new ArrayList<>();  
        PresentInLocalDB = true;
        System.out.println(getSearchKeyword());
        if("Search as Actor Name".equals(getSearchType())){
            ActorSearch();
            return SUCCESS;
        }            
        int tagid = 0;
        try {
            System.out.println("Object newInstance 1");
            conn = Utils.ConnectDB();
            System.out.println(conn);
            ps = conn.prepareStatement("select tagid from tags where tagvalue = ?");
            ps.setString(1,SearchKeyword);
            results = ps.executeQuery(); 
            if(results.next() && PresentInLocalDB) {
                tagid = results.getInt("tagid");
                System.out.println(tagid);
                MovieDetailSetter(tagid);                                 
            }
            else {     
                PresentInLocalDB = false;
                int page = 1;
                Object totalpages=1;
                JSONObject jobj,tmp;                
                JSONArray jarray ;
                do {
                    String query ="https://api.themoviedb.org/3/search/movie?api_key=d756bfc1b32812fb7ba3657c38256fe&language=en-US"
                                   + "&query="+URLEncoder.encode(SearchKeyword, "UTF-8")+"&page="+page;                    
                    jobj = (JSONObject)Utils.MakeApiCall(query);
                    totalpages = jobj.get("total_pages");
                    jarray = (JSONArray) jobj.get("results");                    
                    for(int i =0;i<jarray.size();i++) {
                        obj = new MovieDetailPOJO();
                        tmp =  (JSONObject) jarray.get(i);
                        long id = (long) tmp.get("id");
                        String tmpQuery = "https://api.themoviedb.org/3/movie/"+id+"?api_key=d756bfc1b32812fb7ba3657c38256fe&language=en-US";
                        JSONObject jsonObject = (JSONObject)Utils.MakeApiCall(tmpQuery);                        
                        String imdb_id = (String)jsonObject.get("imdb_id");
                        if(imdb_id != null && !imdb_id.trim().isEmpty()) {
                            String queryString = "http://www.omdbapi.com/?i="+imdb_id+"&apikey=97eb5ce";                        
                            jsonObject = (JSONObject)Utils.MakeApiCall(queryString);
                            String resp = (String)jsonObject.get("Response");
                            System.out.println(resp);
                            if("True".equals(resp)){
                                InsertIntoMoviesDB(tagid, imdb_id, jsonObject);                            
                            }
                        }
                    }         
                    System.out.println(jarray.size());                    
                    page++;
                }while(page<=(long)totalpages);
                ps = conn.prepareStatement("select tagid from tags where tagvalue = ?");
                ps.setString(1,SearchKeyword);            
                results = ps.executeQuery(); 
                results.next();
                tagid = results.getInt("tagid");
                MovieDetailSetter(tagid);                
            }
        }
        
        catch(Exception e){
            e.printStackTrace();
        }     
        finally {
            results.close();
            ps.close();
            conn.close();
        }
        return SUCCESS;
    }    
    
    
    public void ActorSearch() throws SQLException {        
        int keyid=0;
        try {
            System.out.println("Object newInstance 2");            
            conn = Utils.ConnectDB();
            ps = conn.prepareStatement("select keyid from keywords where keyvalue = ?");
            ps.setString(1,SearchKeyword);            
            results = ps.executeQuery();             
            if(results.next() && PresentInLocalDB) {
                keyid = results.getInt("keyid");
                ActorDetailSetter(keyid);                             
            }
            else {
                PresentInLocalDB = false;
                int page = 1;
                long totalpages=1;
                JSONObject jobj,tmp = null;
                JSONArray jarray ;                
                do {
                    String query ="https://api.themoviedb.org/3/search/person?api_key=d756bfc1b32812fb7ba3657c38256fe&language=en-US"
                                   + "&query="+URLEncoder.encode(getSearchKeyword(), "UTF-8")+"&page="+page;                    
                    jobj = (JSONObject)Utils.MakeApiCall(query);
                    totalpages = (long)jobj.get("total_pages");
                    jarray = (JSONArray) jobj.get("results");                      
                    for(int i =0;i<jarray.size();i++) {
                        obj = new MovieDetailPOJO();
                        tmp =  (JSONObject) jarray.get(i);
                        long id = (long) tmp.get("id");
                        String tmpQuery = "https://api.themoviedb.org/3/person/"+id+"?api_key=d75fc1b32812fb7ba3657c38256fe&language=en-US";                        
                        JSONObject jsonObject = (JSONObject)Utils.MakeApiCall(tmpQuery);  
                        String imdb_id = (String)jsonObject.get("imdb_id");
                        if(imdb_id != null && !imdb_id.trim().isEmpty()) {  
                            InsertIntoActorsDB(keyid,imdb_id,jsonObject);
                        }
                    }         
                    System.out.println(jarray.size());
                    page++;
                }while(page<=(long)totalpages);     
                ps = conn.prepareStatement("select keyid from keywords where keyvalue = ?");
                ps.setString(1,SearchKeyword);            
                results = ps.executeQuery(); 
                results.next();
                keyid = results.getInt("keyid");
                ActorDetailSetter(keyid);
            }   
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            results.close();
            ps.close();
            conn.close();
        }
    }
    
    public void MovieDetailSetter(int tagid) {
        try {
            ps = conn.prepareStatement("select * from movies where imdbid in (select imdbid from movietag where tagid = ?)");
            ps.setInt(1,tagid);
            results = ps.executeQuery();
            while(results.next()) {
                obj = new MovieDetailPOJO();  
                obj.setImdbid((results.getString("imdbid")));
                obj.setTitle( results.getString("title"));
                if(Integer.valueOf(results.getString("rating" )) != (int)0.00)
                    obj.setRating(Integer.valueOf(results.getString("rating")));                
                else
                    obj.setRating((int)(0.00));                
                if(results.getString("releasedate" ) != null)
                    obj.setReleasedate(results.getDate("releasedate"));
                else
                    obj.setReleasedate(null);                
                obj.setAwards(results.getString("awards"));
                obj.setActors("");
                obj.setDirectors("");
                ps = conn.prepareStatement("select actors from movieactors where imdbid = ? ");
                ps.setString(1,results.getString("imdbid"));
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    obj.setActors(obj.getActors() + ',' + rs.getString("actors"));
                }
                ps = conn.prepareStatement("select directors from moviedirectors where imdbid = ? ");
                ps.setString(1,results.getString("imdbid"));
                rs = ps.executeQuery();
                while(rs.next()) {
                    obj.setDirectors(obj.getDirectors() + ',' + rs.getString("directors"));
                }            
                obj.setActors(obj.getActors().substring(1));
                obj.setDirectors(obj.getDirectors().substring(1));
                MovieDetailList.add(obj);
                rs.close();
                record_counter++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ActorDetailSetter(int keyid) {
        try {
            ps = conn.prepareStatement("select * from actors where imdbid in (select imdbid from actorkey where keyid = ?)");
            ps.setInt(1,keyid);
            results = ps.executeQuery();
            while(results.next()) {
                obj = new MovieDetailPOJO();  
                obj.setImdbid((results.getString("imdbid")));
                obj.setName( results.getString("name"));          
                if(results.getDate("birthday") != null)
                    obj.setBday((results.getDate("birthday")));
                else
                    obj.setBday(null);
                obj.setBirthplace(results.getString("birthplace"));
                if(results.getDate("deathday") != null)
                    obj.setDday(results.getDate("deathday"));
                else
                    obj.setDday(null);
                MovieDetailList.add(obj);
                record_counter++;
            }
        }   
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertIntoMoviesDB (int tagid, String imdb_id, JSONObject Jobj ) {
        try {
            ps = conn.prepareStatement("select * from tags where tagvalue = ?");
            ps.setString(1,SearchKeyword);
            results = ps.executeQuery();
            if(!results.next()) {                                
                ps = conn.prepareStatement("insert into tags(tagvalue) values(?)");
                ps.setString(1,SearchKeyword);
                ps.execute();
                ps=conn.prepareStatement("select tagid from tags where tagvalue = ?");
                ps.setString(1,SearchKeyword);
                results = ps.executeQuery();
                results.next();
                movieid= results.getInt("tagid");
            }
            ps = conn.prepareStatement("select imdbid from movies where imdbid = ?");
            ps.setString(1,imdb_id);
            results = ps.executeQuery();
            if(!results.next()) {
                
                ps = conn.prepareStatement("insert into movies values(?,?,?,?,?)");                       
                ps.setString(1,imdb_id);
                ps.setString(2,(String)Jobj.get("Title"));
                if (!"N/A".equals(Jobj.get("Released").toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = originalFormat.parse((String)Jobj.get("Released"));
                    String formattedDate = targetFormat.format(date);
                    ps.setDate(3,Date.valueOf(formattedDate)); 
                }
                else
                    ps.setDate(3, null);
                
                if(!"N/A".equals(Jobj.get("imdbRating").toString())) {
                    ps.setFloat(4,Float.parseFloat(Jobj.get("imdbRating").toString()));                                                
                }
                else
                    ps.setFloat(4, (float) 0.0);                                                
                ps.setString(5,(String)Jobj.get("Awards"));                        
                ps.execute();
                String[] Actors = Jobj.get("Actors").toString().split(",");                        
                for(int i=0; i < Actors.length; i++) {
                    ps = conn.prepareStatement("insert into movieactors values(?,?)");
                    ps.setString(1, imdb_id);
                    ps.setString(2, Actors[i].trim());
                    ps.execute();
                }
                String[] Directors = Jobj.get("Director").toString().split(",");                        
                for(int i=0; i < Directors.length; i++) {
                    ps = conn.prepareStatement("insert into moviedirectors values(?,?)");
                    ps.setString(1, imdb_id);
                    ps.setString(2, Directors[i].trim());
                    ps.execute();
                }
            }
            ps = conn.prepareStatement("select * from movietag where imdbid = ? and tagid = ?");
            ps.setString(1, imdb_id);
            ps.setInt(2,movieid);
            results = ps.executeQuery();
            if(!results.next()) {
                ps = conn.prepareStatement("insert into movietag values(?,?)");
                ps.setString(1, imdb_id);
                ps.setInt(2,movieid);
                ps.execute();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void InsertIntoActorsDB(int keyid , String imdb_id,JSONObject Jobj) {        
        try {
            ps = conn.prepareStatement("select * from keywords where keyvalue = ?");
            ps.setString(1,SearchKeyword);
            results = ps.executeQuery();
            if(!results.next()){
                ps = conn.prepareStatement("insert into keywords(keyvalue) values(?)");
                ps.setString(1,SearchKeyword);
                ps.execute();
                ps = conn.prepareStatement("select keyid from keywords where keyvalue = ?");
                ps.setString(1,SearchKeyword);
                results  = ps.executeQuery();
                results.next();
                actorid= results.getInt("keyid");
            }
            ps = conn.prepareStatement("select imdbid from actors where imdbid = ?");
            ps.setString(1,imdb_id);
            results = ps.executeQuery();
            if(!results.next()){
                ps = conn.prepareStatement("insert into actors values(?,?,?,?,?)");                       
                ps.setString(1,imdb_id);
                ps.setString(2,(String)Jobj.get("name"));     
                if((String) Jobj.get("birthday") != null ) {
                    Date birthday = Date.valueOf((String)Jobj.get("birthday"));
                    ps.setDate(3, birthday);
                }
                else
                    ps.setDate(3,null);  
                if((String)Jobj.get("place_of_birth") !=null)
                    ps.setString(4,(String)Jobj.get("place_of_birth")); 
                else
                    ps.setString(4,"NOT FOUND");
                if((String)Jobj.get("deathday") != null) {
                    Date deathday = Date.valueOf((String)Jobj.get("deathday"));
                    ps.setDate(5, deathday);
                }
                else
                    ps.setDate(5, null);
                ps.execute();
            }
            ps = conn.prepareStatement("select * from actorkey where imdbid = ? and keyid = ?");
            ps.setString(1,imdb_id);
            ps.setInt(2,actorid);
            results = ps.executeQuery();
            if(!results.next()){
                ps = conn.prepareStatement("insert into actorkey values(?,?)");
                ps.setString(1, imdb_id);
                ps.setInt(2,actorid);
                ps.execute();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
