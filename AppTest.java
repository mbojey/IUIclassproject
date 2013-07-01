package foo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AppTest {
	public static double working = .5;
	public static double notworking = .5;
	private WebDriver driver;
	private String lasthead = "";
	String nonwork = "nhl network movie gma score myspace profile true hero browser record chapter updates claymore ultimate helped experts organisms organs"
			+ "episode official rover trailers college scores rights health global students myspace fighting hero worlds virtual flash ag ft"
			+ "movies mmorpg episode monkey food amp youtube discover articles celebrities hockey aircraft shatoparvic past xbox playstation registration virtual party"
			+ "naruto battle teen eating game nsa british tsn nba crashes blog living antibully friends universe dc games killed food"
			+ "online news game free people video tv play world canada dog amp manga official games sports latest watch funny esports sports sport facebook";
	String work = "encyclopedia videos primes quot saving algebra programing twitter paul saint solutions sfn elusive prime huge continually guardian editor time fi news jeff topic books qva bayes theorem qed dr bolstad book algeria prime updated robots robot unveils apple make nptel integral theory tfanet overview cosmo learning integrals magazines hampshire consumer mathematics bits shows graphs video donn journal results records events olympic computer news math programming theory operating introduction free video science system number latest lecture tutorial technology systems twitter conjecture media ios learn encyclopedia bayes theorem qed google tom wireless wi latest giant warman matt games http wifi calculator";

	public AppTest() {
		driver = new ChromeDriver();
	}

	public AppTest(String url) {
		driver = new ChromeDriver();
		driver.get(url);
	}

	public static double getworking() {
		return working;
	}

	public static double getnotworking() {
		return notworking;
	}

	public void showPageSourceAndWait() {
		driver.get("https://youtube.com");
		String page = getPageHead();
		System.out.println(page);
		try {
			Thread.currentThread();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getPageHead() {
		try {
			String page = driver.getPageSource();
			String descriptiontext = "";
			int start = page.indexOf("<title>");
			int end = page.indexOf("</title>");
			int description = page.indexOf("\"description\"");
			if (description != -1) {
				int enddescription = page.indexOf(">", description);
				descriptiontext = page.substring(description + 12,
						enddescription);
			}
			if (start > -1) {
				page = page.substring(start + 7, end);
				page = page + descriptiontext;
			} else
				page = descriptiontext;
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
			return "Nothing";
		}
	}

	public void changePage(String url) {
		driver.get(url);
	}

	public void cleanup() {
		driver.close();
		driver.quit();
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.setErr(null);
		int workcount = 0, noncount = 0;
		AppTest ex = new AppTest();
		File sites = new File("workurls.txt");
		Scanner scan = new Scanner(sites);
		while (scan.hasNext()) {
			ex.changePage(scan.nextLine());
			ex.lasthead = ex.getPageHead();
			workcount = ex.work();
			noncount = ex.nonwork();
			double[] previous = { working, notworking };
			ex.getprobabilities(noncount, workcount, previous);
			//System.out.println(workcount + " " + noncount);
			System.out.println(working + "," + notworking);
			noncount = 0;
			workcount = 0;
		}
		scan.close();
		ex.cleanup();
		System.out.println("end of main");
	}

	@SuppressWarnings("static-access")
	public void getprobabilities(int nonworking, int working, double[] previous) {
		Bayes_net test = new Bayes_net();
		double[][] workCPT = {
				{ .99, .94, .89, .84, .79, .74, .69, .64, .59, .54, .49, .44,
						.39, .34, .29, .24, .19, .14, .09, .04 },
				{ .04, .09, .14, .19, .24, .29, .34, .39, .44, .49, .54, .59,
						.64, .69, .74, .79, .84, .89, .94, .99 } };
		double[][] nonworkCPT = {
				{ .01, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6,
						.65, .7, .75, .8, .85, .9, .95 },
				{ .95, .9, .85, .8, .75, .7, .65, .6, .55, .5, .45, .4, .35,
						.3, .25, .2, .15, .1, .05, .01 } };
		// double[][] timeCPT = {{.95,.05},{.2,.8}};
		test.add_vertex("Working", 2);
		test.add_vertex("Work Matches", 20);
		test.add_vertex("Non Matches", 20);
		// test.add_vertex("Working+1", 2);
		// test.add_vertex("Work Matches+1", 20);
		// test.add_vertex("Non Matches+1", 20);
		test.add_edge("Working", 2, "Work Matches", 20, workCPT);
		test.add_edge("Working", 2, "Non Matches", 20, nonworkCPT);
		// test.add_edge("Working+1", 2, "Work Matches+1", 20, workCPT);
		// test.add_edge("Working+1", 2, "Non Matches+1", 20, nonworkCPT);
		// test.add_edge("Working+1", 2, "Working", 2, timeCPT);
		JoinTree sample = test.convertToJoinTree();
		sample.makeConsistent();
		Vertex<String> isWorking = new Vertex<String>("Working", 2);
		Vertex<String> Working = new Vertex<String>("Work Matches", 20);
		Vertex<String> NotWorking = new Vertex<String>("Non Matches", 20);
		if (working < 20)
			sample.enter_evidence(Working, working);
		else
			sample.enter_evidence(Working, 20);
		if (nonworking < 20)
			sample.enter_evidence(NotWorking, nonworking);
		else
			sample.enter_evidence(NotWorking, 20);

		// sample.Roll_over(new Vertex<String>("Working",2), new
		// Vertex<String>("Working+1",2));
		double[] result = sample.find_probability(isWorking);
		this.working = result[1];
		this.notworking = result[0];

	}

	@SuppressWarnings("resource")
	public int nonwork() {
		Scanner nwscan;
		int noncount = 0;
		Scanner current;
		nwscan = new Scanner(nonwork);
		while (nwscan.hasNext()) {
			current = new Scanner(lasthead);
			String compare = nwscan.next();
			while (current.hasNext()) {
				String currentword = current.next();
				if (currentword.endsWith("\""))
					currentword = currentword.substring(0,
							currentword.length() - 1);
				if (currentword.endsWith(","))
					currentword = currentword.substring(0,
							currentword.length() - 1);
				if (currentword.contains("content"))
					currentword = "";
				// System.out.println(currentword+" "+ compare);
				if (compare.equalsIgnoreCase(currentword))
					noncount++;
			}
		}
		return noncount;

	}

	@SuppressWarnings("resource")
	public int work() {
		int workcount = 0;
		Scanner wscan;
		wscan = new Scanner(work);
		Scanner current;
		while (wscan.hasNext()) {
			current = new Scanner(lasthead);
			String compare = wscan.next();
			while (current.hasNext()) {
				String currentword = current.next();
				if (currentword.endsWith("\""))
					currentword = currentword.substring(0,
							currentword.length() - 1);
				if (currentword.endsWith(","))
					currentword = currentword.substring(0,
							currentword.length() - 1);
				if (currentword.contains("content"))
					currentword = "";
				// System.out.println(currentword+" "+ compare);
				if (compare.equalsIgnoreCase(currentword))
					workcount++;
			}
		}
		return workcount;

	}

	/**
	 * Executes a script on an element
	 * 
	 * @note Really should only be used when the web driver is sucking at
	 *       exposing functionality natively
	 * @param script
	 *            The script to execute
	 * @param element
	 *            The target of the script, referenced as arguments[0]
	 */
	public void trigger(String script, WebElement element) {
		((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Executes a script
	 * 
	 * @note Really should only be used when the web driver is sucking at
	 *       exposing functionality natively
	 * @param script
	 *            The script to execute
	 */
	public Object trigger(String script) {
		return ((JavascriptExecutor) driver).executeScript(script);
	}

	/**
	 * Opens a new tab for the given URL
	 * 
	 * @param url
	 *            The URL to
	 * @throws JavaScriptException
	 *             If unable to open tab
	 */
	public void openTab(String url) {
		String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
		Object element = trigger(String.format(script, url));
		if (element instanceof WebElement) {
			WebElement anchor = (WebElement) element;
			anchor.click();
			trigger("var a=arguments[0];a.parentNode.removeChild(a);", anchor);
		} else {
			throw new JavaScriptException(element, "Unable to open tab", 1);
		}
	}
}
