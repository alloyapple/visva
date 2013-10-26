package vn.com.shoppie.database;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class adbObject extends Object implements Serializable{
	public String CLASS_UNIQUE=this.getClass().getSimpleName();
	/**
	 * fields _id in database's table
	 * */
	public int _id;
	
	/**
	 * hold strings as fields in database.
	 * */
	protected String[] values;
	
	/**
	 * initial object of database
	 * @see setData(int,String[])
	 * @param index: _id
	 * @param values: fields of table
	 * */
	public adbObject(int index,String... values){
		this._id=index;
		this.values=values;
		this.CLASS_UNIQUE=this.getClass().getSimpleName();
	}
	/**
	 * set _id and fields of table
	 * */
	public void setData(int index,String... values){
		this._id=index;
		this.values=values;
	}
	
	public abstract String[] getValues();
}
