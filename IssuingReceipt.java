package ph.gov.bir.sales;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ph.gov.bir.sales.exception.InvalidIssuingReceiptException;
import ph.gov.bir.sales.exception.InvalidSalesReportException;

public class IssuingReceipt {
	private String prefix;
	private String receipt;

	public IssuingReceipt() {
		prefix = "";
		receipt = "";
	}

	public IssuingReceipt(String prefix, String receipt) {
		this.prefix = prefix;
		this.receipt = receipt;
	}
	
	public IssuingReceipt(String receipt) throws InvalidIssuingReceiptException {
		int errorCode = 0x0;
		ArrayList<String> listOfError = new ArrayList<String>();
		String errorFormat = "";
		if(!matcher("^[ORST]{1}", receipt).find()) {
			throw new InvalidIssuingReceiptException("Last OR contains invalid prefix.");
		}
		if(receipt.length() < 2) {
			throw new InvalidIssuingReceiptException("Last OR is invalid.");
		}
		if(matcher("[ ]{1}", receipt).find()) {//space
			errorCode = errorCode | 001;
			listOfError.add("contains at least one space");
		}
		if(matcher("[;]{1}", receipt).find()) {
			errorCode = errorCode | 002;
			listOfError.add("contains at least one semicolon");
		}
		if(errorCode == (errorCode & 007)) {
			errorFormat = "Last OR %s.";
			String mergeError = String.format(errorFormat, String.join(" and ", listOfError));
			listOfError.removeAll(listOfError);
			listOfError.add(mergeError);
		}
		if(errorCode > 0) {
			throw new InvalidIssuingReceiptException(String.join(" ", listOfError));
		}
		Matcher matcher = matcher("^(?<prefix>[ORST]{1})(?<receipt>.+?)$", receipt);
		matcher.find();
		this.prefix = matcher.group("prefix");
		this.receipt = matcher.group("receipt");
		
	}
	
	public static IssuingReceipt parseIssuingReceipt(String receipt) throws InvalidIssuingReceiptException{
		return new IssuingReceipt(receipt);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	
	private Matcher matcher(String regex, String receipt) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(receipt);
	}
	
	public String toString() {
		return String.format("""
				IssuingReceipt[prefix=%s, receipt=%s]""", prefix, receipt);
	}

}
