package de.verschwiegener.atero.github;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentUpdateResponse;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import de.verschwiegener.atero.util.files.config.Config;
import de.verschwiegener.atero.util.files.config.handler.XMLHelper;

public class GitHubUtils {
    
    private GitHub github;
    private GHRepository repo;
    
    public void auth() throws IOException {
	String token = new String(Base64.getDecoder().decode("Z2hwX0lKUDI4U3VCNUtQVm1ZeENuNVpsQnU2WmtQZlF6ZjNQOXNBUQ=="));
	System.out.println("Token: " + token);
	//github = new GitHubBuilder().withOAuthToken("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").build();
	//github = new GitHubBuilder().withOAuthToken(token).build();
	github = GitHub.connectUsingOAuth(token);
	repo = github.getUser("Verschwiegener").getRepository("AteroConfigs");
    }
    
    public void createRequest(Config config) {
	//updateFile(config);
	//try {
	//try {
	    //GHTreeBuilder tree =  repo.createTree();
	    //GHTree tree2 = tree.create();
	    //repo.createContent("", "", "");
	    //GHContent content = repo.getFileContent("Test.config");
	//} catch (IOException e) {
	   // e.printStackTrace();
	//}
	//    //System.out.println("Repo: " + repo.getFullName());
	    //repo.createContent().branch("main").message("Message").content("Test").path("main").commit();
	    //repo.createCommit().message(config.getName()).tree("main").create();
	   // repo.createIssue(config.getName()).body(XMLHelper.createString(config)).create();
	    //repo.createPullRequest(config.getName(), "main", "main", XMLHelper.createString(config));
	    //repo.createContent( XMLHelper.createString(config), "Test", "main");
	//} catch (IOException e) {
	    //e.printStackTrace();
	//}
    }
    public void updateFile(Config config) {
	try {
	    //GHContent content = repo.getFileContent(config.getName() + ".config");
	    //System.out.println("Content: " + content.getDownloadUrl());
	    //content.update("Content", "Update", "main");
	    //repo.createCommit().message("Test").author("Name", "Email@gmail.com", new Date()).tree(content.getSha()).create();
	    //Create Issue works
	    //repo.createIssue("test").body("test").create();
	    String name = "Test.config";
	    GHContentUpdateResponse response = repo.createContent(name, name, name, "main");
	    Thread.sleep(100);
	    repo.createContent().content("Content").path("Test.config").branch("main").message("Message").sha(response.getContent().getSha()).commit();
	    repo.createPullRequest("createPullRequest", "main", "main", "## test");
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}