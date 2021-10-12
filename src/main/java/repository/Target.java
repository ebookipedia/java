package repository;

import static com.softalks.ebookipedia.Output.print;
import static java.lang.System.out;

import java.io.IOException;

import org.kohsuke.github.GHContentBuilder;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import com.softalks.github.Repository;

public class Target {

	private Target() {
		// SONAR mandatory
	}

	/**
	 * @param args An array with one only item having the value of the
	 *             GITHUB_REPOSITORY variable
	 */
	public static void main(String[] args) {
		out.print("processing starting ...");
		Repository source = new Repository(args[0]);
		String owner = source.owner;
		Repository target = new Repository(owner, owner + ".github.io");
		print("target", target);
		GitHub account;
		try {
			out.println("connecting to Github ...");
			account = GitHubBuilder.fromEnvironment().build();
			out.println("connected");
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		GHOrganization organization;
		try {
			organization = account.getOrganization(owner);
		} catch (IOException e) {
			organization = null;
		}
		try {
			if (organization == null) {
				out.println("Not an organization. Getting account's repository " + target.name + " ...");
				account.getRepository(target.toString());	
			} else {
				out.println("Getting organization's repository " + target.name + " ...");
				organization.getRepository(target.toString());	
			}
			out.print("found!");
		} catch (IOException e) {
			out.println("Not found. Getting repository " + target.name + " ...");
			GHCreateRepositoryBuilder newRepository = getBuilder(account, organization, target.name);
			create(newRepository);
			out.print("created!");
		}
		out.print("processing ended");
	}
	
	private static GHCreateRepositoryBuilder getBuilder(GitHub account, GHOrganization organization, String name) {
		if (organization == null) {
			return account.createRepository(name);
		} else {
			return organization.createRepository(name);
		}
		
	}
	
	private static GHRepository create(GHCreateRepositoryBuilder builder) {
		GHRepository ghTarget;
		try {
			ghTarget = builder.create();
			GHContentBuilder content = ghTarget.createContent();
			content.message("test");
			content.path("hello.txt");
			content.content("Hello, World!".getBytes());
			content.commit();
			return ghTarget;
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
}