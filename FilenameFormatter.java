package ph.gov.bir.sales;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameFormatter {

	public String dateUpload;
	public String username;
	public String channel;
	public String transactionNumber;
	public String branchType;

	public FilenameFormatter() {
		username = "";
		channel = "";
		transactionNumber = "";
		branchType = "";
	}

	public FilenameFormatter(String dateUpload, String username, String channel, String transactionNumber,
			String branchType) {
		this();
		this.dateUpload = dateUpload;
		this.username = username;
		this.channel = channel;
		this.transactionNumber = transactionNumber;
		this.branchType = branchType;
	}
	
	private FilenameFormatter(String filename) {
		this();
		String format = "^(?<dateUpload>[0-9]{8}){1}.*$";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(filename);
		matcher.find();
		dateUpload = matcher.group("dateUpload");
		
		format = """
				\\_{1}(?<channel>[0-9]{3})\\_{1}\
				(?<transactionNumber>STN[0-9]{19})\\_{1}[0-9]{1,}\\_{1}\
				(?<branchType>[MS]B){1}.csv$""";
		pattern = Pattern.compile(format);
		matcher = pattern.matcher(filename);
		if(matcher.find()) {
			username = filename.substring(9, matcher.start());
			channel = matcher.group("channel");
			transactionNumber = matcher.group("transactionNumber");
			branchType = matcher.group("branchType");
		} else {
			//System.out.println("Invalid filename.");
		}
		
		
		
	}

	public static FilenameFormatter parseFilenameFormatter(String filename) {
		return new FilenameFormatter(filename);
	}

	public String getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(String dateUpload) {
		this.dateUpload = dateUpload;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

	@Override
	public String toString() {
		return String.format("""
				FilenameFormatter[dateUpload = %s, username = %s, \
				channel = %s, transactionNumber = %s, branchType = %s]""", dateUpload, username, channel, transactionNumber, branchType);
	}
}
