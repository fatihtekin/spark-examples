package com.cloudera.sparkwordcount;

import java.io.Serializable;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

public class Cars implements Serializable{

	private static final long serialVersionUID = 293543551538260429L;

	private String year;
	private String make;
	private String model;
	private String comment;
	private Integer quantity;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Cars(String year, String make, String model, String comment,
			Integer quantity) {
		super();
		this.year = year;
		this.make = make;
		this.model = model;
		this.comment = comment;
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cars [year=").append(year).append(", make=")
				.append(make).append(", model=").append(model)
				.append(", comment=").append(comment).append(", quantity=")
				.append(quantity).append("]");
		return builder.toString();
	}
	
	BSONObject convertToBson(){
		
        BSONObject bson = new BasicBSONObject();
        bson.put("year", year);
        bson.put("make", make);
        bson.put("model", model);
        bson.put("comment", comment);
        bson.put("quantity", quantity);
		return bson;
	}
	
	
}
