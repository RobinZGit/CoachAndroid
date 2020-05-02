package com.mycompany.myapp;

public class Result
{   
    public String sacc;
	public Integer iacc;
	//==
	public double acc;
	public String rest;
	
	public Result(double v, String r){
		this.acc = v;
		this.rest = r;
	}
	
	//
	public Result(String sv, String r){
		this.sacc = sv;
		this.rest = r;
	}
	
	public Result(Integer iv, String r){
		this.iacc = iv;
		this.rest = r;
	}
}
