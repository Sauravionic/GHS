package io.ionic.GHS.Controllers;

import io.ionic.GHS.Models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

@Controller
public class HomeController {

    public String Error = "Success";
    public String Error2 = "Success";

    @GetMapping("/")
    public String search() {
        return "search";
    }

    @GetMapping("/form_search")
    public String form_search() {
        return "search";
    }
    @PostMapping("/form_search")
    public String handleForm(@RequestParam("username") String username, Model model) throws IOException, InterruptedException {

        username = username.replaceAll("\\s", "");
        //Forming the api URL
        String apiURL = "https://api.github.com/users/" + username;

        // Parsing the URL
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(apiURL)).
                build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //parsing the JSON data
        JSONObject apiData = new JSONObject(httpResponse.body());

        // Adding the data to the model
        try {
            String name = (String) apiData.get("name");
            String userName = (String)apiData.get("login");
            String avatarURL = (String)apiData.get("avatar_url");
            int followers = (Integer)apiData.get("followers");
            int following = (Integer)apiData.get("following");
            String bio = (String)apiData.get("bio");
            int repo = (Integer)apiData.get("public_repos");

            model.addAttribute("name", name);
            model.addAttribute("username", userName);
            model.addAttribute("avatarURL", avatarURL);
            model.addAttribute("followers", followers);
            model.addAttribute("following", following);
            model.addAttribute("bio", bio);
            model.addAttribute("repo", repo);

//            System.out.println(name + " " + avatarURL + " " + followers + " " + following + " " + following + " " + bio + " " + repo);
            Error = "success";
        }
        catch(Exception e) {
            Error = "Error";
        }

        if(Error.equals("Error")) {


            try {

                String userName = (String) apiData.get("login");
                int followers = (Integer)apiData.get("followers");
                int following = (Integer)apiData.get("following");
                int repo = (Integer)apiData.get("public_repos");

                model.addAttribute("username", userName);
                model.addAttribute("name", userName);
                model.addAttribute("avatarURL", "https://avatars.githubusercontent.com/u/64633535?v=4");
                model.addAttribute("followers", followers);
                model.addAttribute("following", following);
                model.addAttribute("bio", "Bio not Found");
                model.addAttribute("repo", repo);
//                System.out.println(userName);
                Error2 = "success";
            }
            catch(Exception e) {
                Error2 = "Error";
            }
        }

        if(Error2.equals("Error")) {

            model.addAttribute("username", "404");
            model.addAttribute("name", "Not Found");
            model.addAttribute("avatarURL", "https://img.icons8.com/material-outlined/48/000000/github.png");
            model.addAttribute("followers", "Not Found");
            model.addAttribute("following", "Not Found");
            model.addAttribute("bio", "Enter an existing or a valid username");
            model.addAttribute("repo", "Not Found");
            Error2 = "success";
        }
        return "search";
    }
}
