package ph.gov.bir.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ph.gov.bir.sales.exception.InvalidIssuingReceiptException;
import ph.gov.bir.sales.exception.InvalidSalesReportException;

public class SalesReport {

	private String tin;
	private String branch;
	private int year;
	private int month;
	private String min;
	private IssuingReceipt receipt;
	private double vatSales;
	private double vatZero;
	private double vatExempt;
	private double otherSales;

	public SalesReport() {
		tin = "";
		branch = "";
		year = 0;
		month = 0;
		min = "";
		receipt = new IssuingReceipt();
		vatSales = 0.0;
		vatZero = 0.0;
		vatExempt = 0.0;
		otherSales = 0.0;
	}

	public SalesReport(String tin, String branch, int year, int month, String min, IssuingReceipt receipt,
			double vatSales, double vatZero, double vatExempt, double otherSales) {
		this();
		this.tin = tin;
		this.branch = branch;
		this.year = year;
		this.month = month;
		this.min = min;
		this.receipt = receipt;
		this.vatSales = vatSales;
		this.vatZero = vatZero;
		this.vatExempt = vatExempt;
		this.otherSales = otherSales;
	}

	public SalesReport(String sales) throws InvalidSalesReportException {
		this();
		String data[] = new String[10];
		String tin = "";
		String branch = "";
		int year = 0;
		int month = 0;
		String min = "";
		IssuingReceipt receipt = null;
		double vatSales = 0.0;
		double vatZero = 0.0;
		double vatExempt = 0.0;
		double otherSales = 0.0;

		int numberOfCommas = 0;
		int errorCode = 0;
		String errorFormat = "";
		ArrayList<String> listOfError = new ArrayList<String>();

		Matcher matcher = matcher("[,]{1}", sales);

		while (matcher.find()) {
			numberOfCommas++;
		}
		if (numberOfCommas > 9) {
			listOfError.add("more than 9 commas");
		} else if (numberOfCommas < 9) {
			listOfError.add("insufficient data");
		}
		
		matcher = matcher("[\"]{1}", sales);
		if (matcher.find()) {
			listOfError.add("at least one quotation mark");
		}
		
		if (listOfError.size() > 0) {
			errorFormat = "The row contains %s.";
			throw new InvalidSalesReportException(String.format(errorFormat, String.join(" and ", listOfError)));
		}
		
		matcher = matcher("[,]{9,}", sales);
		if (matcher.find()) {
			throw new InvalidSalesReportException("The row is invalid.");
		}
		
		

		/*
		 * matcher = matcher("[,]{1}", sales); while(matcher.find()) {
		 * System.out.println(matcher.end()); } matcher.reset(); while(matcher.find()) {
		 * System.out.println(matcher.group()); }
		 */

		matcher = matcher(",?([^,]*)", sales);
		for (int i = 0; i < data.length && matcher.find(); i++) {
			data[i] = matcher.group().replace(",", "");
		}

		// TIN
		try {
			Integer.parseInt(data[0]);
			if (data[0].length() != 9) {
				throw new NumberFormatException();
			}
			tin = data[0];
		} catch (NumberFormatException e) {
			if (matcher("[-]{1}", data[0]).find()) {
				listOfError.add("TIN contains at least one dash.");
			} else {
				listOfError.add("Invalid TIN.");
			}
		}

		// Branch
		try {
			Integer.parseInt(data[1]);
			if (data[1].length() != 5 && data[1].length() !=3) {
				throw new NumberFormatException();
			}
			branch = data[1];
		} catch (NumberFormatException e) {
			listOfError.add("Invalid branch code.");
		}

		// Year
		try {
			year = Integer.parseInt(data[3]);
			if (year < 1) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			listOfError.add("Invalid year.");
		}

		// Month
		try {
			month = Integer.parseInt(data[2]);
			if (month > 12 || month < 1) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			listOfError.add("Invalid month.");
		}

		// MIN

		try { // Receipt
			receipt = IssuingReceipt.parseIssuingReceipt(data[5]);
		} catch (InvalidIssuingReceiptException e) {
			listOfError.add(e.getMessage());
		}

		// VAT Sales
		try {
			vatSales = Double.parseDouble(data[6]);
		} catch (NumberFormatException e) {
			listOfError.add("Invalid VAT Sales format.");
		}

		// VAT Zero
		try {
			vatZero = Double.parseDouble(data[7]);
		} catch (NumberFormatException e) {
			listOfError.add("Invalid VAT Zero format.");
		}

		// VAT Exempt
		try {
			vatExempt = Double.parseDouble(data[8]);
		} catch (NumberFormatException e) {
			listOfError.add("Invalid VAT Exempt format.");
		}

		// Other Sales
		try {
			otherSales = Double.parseDouble(data[9]);
		} catch (NumberFormatException e) {
			listOfError.add("Invalid Other Sales format.");
		}
		
		if (listOfError.size() > 0) {
			throw new InvalidSalesReportException(String.join(" ", listOfError));
		}

		this.tin = tin;
		this.branch = branch;
		this.year = year;
		this.month = month;
		this.min = sales.split("[,]{1}")[4];
		this.receipt = receipt;
		this.vatSales = vatSales;
		this.vatZero = vatZero;
		this.vatExempt = vatExempt;
		this.otherSales = otherSales;

	}

	public static SalesReport parseSalesReport(String sales) throws InvalidSalesReportException {
		return new SalesReport(sales);
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public IssuingReceipt getReceipt() {
		return receipt;
	}

	public void setReceipt(IssuingReceipt receipt) {
		this.receipt = receipt;
	}

	public double getVatSales() {
		return vatSales;
	}

	public void setVatSales(double vatSales) {
		this.vatSales = vatSales;
	}

	public double getVatZero() {
		return vatZero;
	}

	public void setVatZero(double vatZero) {
		this.vatZero = vatZero;
	}

	public double getVatExempt() {
		return vatExempt;
	}

	public void setVatExempt(double vatExempt) {
		this.vatExempt = vatExempt;
	}

	public double getOtherSales() {
		return otherSales;
	}

	public void setOtherSales(double otherSales) {
		this.otherSales = otherSales;
	}

	@Override
	public String toString() {
		return String.format("""
				SalesReport[tin = %s, branch = %s, year = %d, month = %d, \
				min = %s, receipt = %s, vatSales = %.2f, vatZero = %.2f, vatExempt = %.2f, \
				otherSales = %.2f]""", tin, branch, year, month, min, receipt, vatSales, vatZero, vatExempt,
				otherSales);
	}

	private Matcher matcher(String regex, String receipt) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(receipt);
	}

}
