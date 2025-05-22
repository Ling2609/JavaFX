package com.groupfx.JavaFXApp;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;


/**
 * Restrict the input type/format, Constructors need enter <b>REGX</b> for validation 
 * IF not Success Return NULL for Input Field 
 * */
public class Restriction_Text implements UnaryOperator<TextFormatter.Change> {
	
	private String regx;
	
	public Restriction_Text(String regx) 
	{
		this.regx=regx;
	}
	
		@Override
		public TextFormatter.Change apply(TextFormatter.Change change)
		{
			String text= change.getControlNewText();
			if(text.isEmpty() || text.matches(regx)) 
			{
				return change;
			}
			return null;
		}
	
}
