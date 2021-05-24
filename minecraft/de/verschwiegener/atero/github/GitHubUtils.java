package de.verschwiegener.atero.github;

import java.io.IOException;
import java.util.Base64;

//import org.kohsuke.github.GHRepository;
//import org.kohsuke.github.GitHub;
//import org.kohsuke.github.GitHubBuilder;

import de.verschwiegener.atero.util.files.config.Config;
import de.verschwiegener.atero.util.files.config.handler.XMLHelper;

public class GitHubUtils {
    
  //  private GitHub github;
  //  private GHRepository repo;
    
    public void auth() throws IOException {
	String token = new String(Base64.getDecoder().decode("Z2hwX0lKUDI4U3VCNUtQVm1ZeENuNVpsQnU2WmtQZlF6ZjNQOXNBUQ=="));
	System.out.println("Token: " + token);
	//github = new GitHubBuilder().withOAuthToken("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").build();
	//github = new GitHubBuilder().withOAuthToken(token).build();
	//github = GitHub.connectUsingOAuth(token);
	//repo = github.getUser("Verschwiegener").getRepository("AteroConfigs");
    }
    
    public void createRequest(Config config) {
	//try {
	    //System.out.println("Repo: " + repo.getFullName());
	    //repo.createContent().branch("main").message("Message").content("Test").path("main").commit();
	    //repo.createCommit().message(config.getName()).tree("main").create();
	   // repo.createIssue(config.getName()).body(XMLHelper.createString(config)).create();
	    //repo.createPullRequest(config.getName(), "main", "main", XMLHelper.createString(config));
	    //repo.createContent( XMLHelper.createString(config), "Test", "main");
	//} catch (IOException e) {
	    //e.printStackTrace();
	//}
    }

}
