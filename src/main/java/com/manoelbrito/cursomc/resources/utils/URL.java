package com.manoelbrito.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import static java.util.stream.Collectors.toList;

public class URL {

	
	public static String decodeParam(String s) {
		
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodIntList(String s){
		String[] vet=s.split(",");
		List<Integer>list=new ArrayList<>();
		for(int i=0; i< vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
			
		}
		return list;
		//Alternativa com Lambda
		//return Arrays.asList(s.split(",")).stream().map(x->Integer.parseInt(x)).collect(toList());
	}
}
