package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.ListModel;

public class ListController  {
	@FXML
	private Label label;
	@FXML
	private ImageView img;

    @FXML
    private Label labelnum;

	
	
	public void setData(ListModel list) {
        
		label.setText(list.getName());
        Image image = new Image(list.getImg(),160,160,false,true);
        img.setImage(image);
        System.out.println(list.getId());
        if(list.getId().equals("Special"))
        	labelnum.setText("Click");
        else {
        	labelnum.setText(list.getMem_num()+" membres");
            System.out.println(list.getMem_num());
        }
        
    }



	public Label getLabel() {
		return label;
	}



	public void setLabel(Label label) {
		this.label = label;
	}



	public ImageView getImg() {
		return img;
	}



	public void setImg(ImageView img) {
		this.img = img;
	}



	public Label getLabelnum() {
		return labelnum;
	}



	public void setLabelnum(Label labelnum) {
		this.labelnum = labelnum;
	}
	
	
	
	

}
