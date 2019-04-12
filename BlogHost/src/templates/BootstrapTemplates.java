package templates;

import forms.Checkbox;
import forms.Input;
import html.CompoundElement;

public class BootstrapTemplates
{
	public static CompoundElement formGroup(String labelText, Class<? extends Input> clazz, String helpText, String id)
	{
		CompoundElement outerDiv = new CompoundElement("div");
		outerDiv.addClass("form-group");
		
		Input input;
		
		try
		{
			input = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			return null;
		}
		
		input.addClass("form-control");
		
		if(id != null)
		{
			input.setAttribute("id", id);
			
		}
		
		if(labelText != null)
		{
			input.setLabelText(labelText);
			outerDiv.addElement(input.getLabel());
		}
		
		outerDiv.addElement(input);
		
		if(helpText != null)
		{
			CompoundElement help = new CompoundElement("small", helpText);
			help.addClasses("form-text", "text-muted");
			outerDiv.addElement(help);
		}
		
		return outerDiv;
	}
	
	public static CompoundElement formGroupCheckbox(String labelText, String helpText, String id)
	{
		CompoundElement outerDiv = new CompoundElement("div");
		outerDiv.addClasses("form-group", "form-check");
		
		Checkbox box = new Checkbox();
		box.addClass("form-check-input");
		if(id != null)
		{
			box.setAttribute("id", id);
		}
		
		outerDiv.addElement(box);
		if(labelText != null)
		{
			box.setLabelText(labelText);
			box.getLabel().addClass("form-check-label");
			outerDiv.addElement(box.getLabel());
		}
		
		if(helpText != null)
		{
			CompoundElement help = new CompoundElement("small", helpText);
			help.addClasses("form-text", "text-muted");
			outerDiv.addElement(help);
		}
		
		return outerDiv;
	}
	
	public static CompoundElement formGroupTextarea(String labelText, String helpText, String id)
	{
		CompoundElement outerDiv = new CompoundElement("div");
		outerDiv.addClass("form-group");
		
		CompoundElement textarea = new CompoundElement("textarea");
		textarea.addClass("form-control");
		
		if(id != null)
		{
			textarea.setAttribute("id", id);
		}
		if(labelText != null)
		{
			CompoundElement label = new CompoundElement("label", labelText);
			label.setAttribute("for", textarea.getAttribute("id"));
			outerDiv.addElement(label);
		}
		
		outerDiv.addElement(textarea);
		
		if(helpText != null)
		{
			CompoundElement help = new CompoundElement("small", helpText);
			help.addClasses("form-text", "text-muted");
			outerDiv.addElement(help);
		}
		
		return outerDiv;
	}
}
