package templates;

import java.util.Base64;
import java.util.List;

import forms.Checkbox;
import forms.File;
import forms.Input;
import html.CompoundElement;
import html.Element;

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
		
		if(clazz.equals(File.class))
		{
			input.addClass("form-control-file");
		}
		else
		{
			input.addClass("form-control");
		}
		
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
	
	public static CompoundElement card(String title, String description, Object picture, String link, String linkText)
	{
		CompoundElement card = new CompoundElement("div");
		card.addClasses("card");
		
		Element image = new Element("img");
		image.addClass("card-img-top");
		if(picture == null)
		{
			// Default store picture
			image.setAttribute("src", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/No_image_available_600_x_450.svg/600px-No_image_available_600_x_450.svg.png");
		}
		else if(picture instanceof String)
		{
			image.setAttribute("src", (String) picture);
		}
		else if(picture instanceof byte[])
		{
			image.setAttribute("src", "data:image/jpg;base64," + Base64.getEncoder().encodeToString((byte[]) picture));
		}
		
		CompoundElement cardBody = new CompoundElement("div");
		cardBody.addClass("card-body");
		
		if(title != null)
		{
			CompoundElement cardTitle = new CompoundElement("h5", title);
			cardTitle.addClass("card-title");
			cardBody.addElement(cardTitle);
		}
		
		if(description != null)
		{
			CompoundElement cardDescription = new CompoundElement("p", description);
			cardDescription.addClass("card-text");
			cardBody.addElement(cardDescription);
		}
		
		if(link != null)
		{
			CompoundElement cardLink = new CompoundElement("a", linkText != null ? linkText : "");
			cardLink.addClasses("btn", "btn-primary");
			cardLink.setAttribute("href", link);
			cardBody.addElement(cardLink);
		}
		
		card.addElement(image);
		card.addElement(cardBody);
		return card;
	}
	
	public static CompoundElement scrollableModal(String title, String id, List<Element> bodyElements, List<Element> footerElements)
	{
		CompoundElement modal = new CompoundElement("div");
		modal.addClasses("modal", "fade");
		modal.setAttribute("id", id);
		modal.setAttribute("tabindex", "-1");
		modal.setAttribute("role", "dialog");
		modal.setAttribute("aria-hidden", "true");
		
		CompoundElement scrollable = new CompoundElement("div");
		scrollable.addClasses("modal-dialog", "modal-dialog-scrollable");
		scrollable.setAttribute("role", "document");
		
		CompoundElement content = new CompoundElement("div");
		content.addClass("modal-content");
		
		CompoundElement header = new CompoundElement("div");
		header.addClass("modal-header");
		
		CompoundElement titleElement = new CompoundElement("h5", title);
		titleElement.addClass("modal-title");
		
		CompoundElement closeButton = new CompoundElement("button");
		closeButton.setAttribute("type", "button");
		closeButton.addClass("close");
		closeButton.setAttribute("data-dismiss", "modal");
		closeButton.setAttribute("aria-label", "Close");
		
		CompoundElement closeIcon = new CompoundElement("span", "&times;");
		closeIcon.setAttribute("aria-hidden", "true");
		closeButton.addElement(closeIcon);
		
		header.addElement(titleElement);
		header.addElement(closeButton);
		
		CompoundElement body = new CompoundElement("div");
		body.addClass("modal-body");
		for(Element e : bodyElements)
		{
			body.addElement(e);
		}
		
		CompoundElement footer = new CompoundElement("div");
		footer.addClass("modal-footer");
		
		CompoundElement footerCloseButton = new CompoundElement("button", "Close");
		footerCloseButton.addClasses("btn", "btn-secondary");
		footerCloseButton.setAttribute("data-dismiss", "modal");
		
		footer.addElement(footerCloseButton);
		for(Element e : footerElements)
		{
			footer.addElement(e);
		}
		
		content.addElement(header);
		content.addElement(body);
		content.addElement(footer);
		scrollable.addElement(content);
		modal.addElement(scrollable);
		
		return modal;
	}
}
