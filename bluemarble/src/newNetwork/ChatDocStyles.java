package newNetwork;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ChatDocStyles {
	StyleContext sc;   
	Style defaultStyle, imageStyle, mainStyle, whisperStyle;
	
	public ChatDocStyles(StyleContext sc) {
		this.sc = sc;
		createDocumentStyles();
	}
	
	public void createDocumentStyles() {
		defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);  //defaultStyle		
		mainStyle = sc.addStyle("MainStyle", defaultStyle);   //mainStyle
		StyleConstants.setForeground(mainStyle, Color.black);
		StyleConstants.setLeftIndent(mainStyle, 16);
		StyleConstants.setRightIndent(mainStyle, 16);
		StyleConstants.setFirstLineIndent(mainStyle, 16);
		StyleConstants.setFontFamily(mainStyle, "±¼¸²");
		StyleConstants.setFontSize(mainStyle, 20);
		imageStyle = sc.addStyle("ImageStyle", null); //imageSytle
		whisperStyle = sc.addStyle("WhisperStyle", null);	//whisperStyle
		StyleConstants.setForeground(whisperStyle, Color.red);
	}
}
