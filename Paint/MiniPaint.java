import javafx.application.Application;
import javafx.beans.value.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.shape.*; 
import javafx.scene.control.*;
import javafx.scene.layout.*; 
import javafx.geometry.*; 
import javafx.collections.*;
import javafx.scene.image.*;


import java.awt.Graphics;


//Ryan McVicker
public class MiniPaint extends Application { 

	//shape used for determining which shape the user wants to draw
	String shapeTool = "";
	double length, startX, startY, endX, endY; 

	ArrayList<Shape> shape = new ArrayList<Shape>();
	//used for determining which mode the user want to use 
	boolean Draw, Delete, Move;


	//blank slate shapes to be added to the canvas
	Line newLine = new Line();
	Rectangle newRect = new Rectangle();
	Circle newCircle = new Circle();
	Ellipse newEllipse = new Ellipse();
	
	//added ColorPicker instead of sliders in favor of simplicity - Ryan McVicker
	ColorPicker colorPicker = new ColorPicker();

	Canvas shapeCanvas = new Canvas(1200,800); 

	GraphicsContext graphc = shapeCanvas.getGraphicsContext2D(); 

    	Point2D dragAnchor;
	public void start(Stage stage) {

		Random r = new Random();
		BorderPane root = new BorderPane();
		Scene scene = new Scene( root, 1000, 850 ) ;
		root.setPadding(new Insets(10, 10, 10, 20));


		//========================================================  UI widgets  =============================

		//controls which strings are displayed on the choicebox 
		String shapes[] = {"Line","Ray","Ellipse","Rectangle","Rectangle fill", "Circle","Circle fill"};
		ChoiceBox<String> shapeChoiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(shapes));
		final ToggleGroup radioButtons = new ToggleGroup(); 

		Label toolLabel = new Label("Tools"); 
		Label currentColorLabel = new Label("Color"); 

		RadioButton tool1 = new RadioButton("Draw");
		RadioButton tool2 = new RadioButton("Move"); 
		RadioButton tool3 = new RadioButton("Delete");

		tool1.setToggleGroup(radioButtons);
		tool2.setToggleGroup(radioButtons);
		tool3.setToggleGroup(radioButtons);





		VBox sideMenu = new VBox(20);
		Label colorPickerLabel = new Label("Change Color");
		Label shapeLabel = new Label("Shape");

		sideMenu.getChildren().addAll(toolLabel,tool1,tool2,tool3,shapeLabel,shapeChoiceBox,colorPickerLabel, colorPicker);
		root.setCenter(shapeCanvas);

		VBox elements = new VBox(30);
		sideMenu.setStyle("-fx-padding: 10;-fx-border-style: solid inside;-fx-border-width: 2;-fx-border-insets: 5;-fx-border-radius: 5;");
		root.setLeft(sideMenu);
		
//===================================== EVENT HANDLERS ================================================

		shapeChoiceBox.getSelectionModel().selectedIndexProperty().addListener( (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

			//changes the type of shape based on what value is returned from new_val
			//  shapes] = {"Line","Rectangle","Ellipse","Ray","Rectangle fill", "Circle","Circle fill"};
			switch((int)new_val){
				case 0: shapeTool = "Line"; System.out.printf("Mode changed to : %s\n",shapeTool);break;
				case 1: shapeTool = "Ray"; System.out.printf("Mode changed to : %s\n",shapeTool);break;
				case 2: shapeTool = "Ellipse"; System.out.printf("Mode changed to : %s\n",shapeTool);break;
					// A Ray will create a new line that connects to the previous one 
				case 3: shapeTool = "Rectangle"; System.out.printf("Mode changed to : %s\n",shapeTool);break;
				case 4: shapeTool = "Rectangle fill"; System.out.printf("Mode changed to : %s\n",shapeTool);break; 
				case 5: shapeTool = "Circle"; System.out.printf("Mode changed to : %s\n",shapeTool);break; 
				case 6: shapeTool = "Circle fill"; System.out.printf("Mode changed to : %s\n",shapeTool);break; 
			}

		});

		// ================= determine which tool to use ===================================

		radioButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle currentTool, Toggle newTool) {
				RadioButton tool = (RadioButton)newTool.getToggleGroup().getSelectedToggle();
				//change the string used for determining which tool to use.
				switch(tool.getText()){
					case "Move": Move = true; Draw = false;Delete = false;break;
					case "Draw": Draw = true; Move = false; Delete = false; break;
					case "Delete": Delete = true; Move = false; Draw = false;break;
				}


			}
		});
		// ================ determine which shape to draw ==================================================


		shapeCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				//System.out.println(shapeTool);

				if(Draw){
					if(shapeTool == "Paint"){
						graphc.setFill(colorPicker.getValue());

					}
					else if (shapeTool == "Line"){
						graphc.beginPath();
						//set up variables for the line 

						graphc.setStroke(colorPicker.getValue());


						startX = mouseEvent.getX();
						startY = mouseEvent.getY();

						newLine.setStartX(startX); 
						newLine.setStartY(startY);

						graphc.moveTo(startX, startY);
					}
					else if(shapeTool == "Rectangle" || shapeTool == "Rectangle fill"){ 
						startX = mouseEvent.getSceneX();
						startY = mouseEvent.getSceneY(); 

						graphc.setStroke(colorPicker.getValue());

						newRect.setX(mouseEvent.getX());
						newRect.setY(mouseEvent.getY());

					}
					else if(shapeTool == "Ellipse"){
						//System.out.printf("Width of ellipse : ", newEllipse.width
						startX = mouseEvent.getX();
						startY = mouseEvent.getY(); 
						//moves the graphics context to the location of cursor
						graphc.moveTo(mouseEvent.getX(), mouseEvent.getY());

						graphc.setStroke(colorPicker.getValue());

						newEllipse.setCenterX(mouseEvent.getX());
						newEllipse.setCenterY(mouseEvent.getY());

					}
					else if (shapeTool == "Circle" || shapeTool == "Circle fill"){
						graphc.setStroke(colorPicker.getValue());
						graphc.moveTo(mouseEvent.getX(), mouseEvent.getY());

						newCircle.setCenterX(mouseEvent.getX());
						newCircle.setCenterY(mouseEvent.getY());

					}
				}else if (Move){
					
				}

			}
		});
		

		shapeCanvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {



				if(shapeTool == "Ray fill"){
					graphc.strokeLine(newLine.getStartX(), newLine.getStartY(), newLine.getEndX(), newLine.getEndY());
					graphc.fill();
				}

				else if(shapeTool == "Line"){

					double endX = mouseEvent.getX();
					double endY = mouseEvent.getY();
					graphc.setStroke(colorPicker.getValue());
					graphc.strokeLine(startX, startY, endX,endY);

				}
				else if(shapeTool == "Rectangle"){

					newRect.setWidth(Math.abs(mouseEvent.getX()-newRect.getX()));
					newRect.setHeight(Math.abs(mouseEvent.getY()-newRect.getY()));

					if(newRect.getX() > mouseEvent.getX()){
						//newRect.setX(mouseEvent.getX());
						newRect.setTranslateX(mouseEvent.getX());
					}

					if(newRect.getY() > mouseEvent.getY()){
						newRect.setY(mouseEvent.getY());
						newRect.setTranslateY(mouseEvent.getY());
					}

					graphc.strokeRect(newRect.getX(),newRect.getY(),newRect.getWidth(),newRect.getHeight());
					shape.add(newRect);







				}
				else if (shapeTool == "Ellipse"){


					newEllipse.setRadiusX(Math.abs(endX - startX));
					newEllipse.setRadiusY(Math.abs(endY - startY));

					//						newEllipse.setTranslateY(mouseEvent.getY());

					System.out.printf("Data:\n newEllipse.getCenterX(): %s\nnewEllipse.getCenterY(): %s\nnewEllipse.getRadiusX(): %s\nnewEllipse.getRadiusY(): %s\n",newEllipse.getCenterX(),newEllipse.getCenterY(),newEllipse.getRadiusX(),newEllipse.getRadiusY());

					graphc.strokeOval(newEllipse.getCenterX(),newEllipse.getCenterY(),newEllipse.getRadiusX(),newEllipse.getRadiusY());

					shape.add(newEllipse);





				}


				else if(shapeTool=="Rectangle fill"){

					newRect.setWidth(Math.abs(mouseEvent.getX()-newRect.getX()));
					newRect.setHeight(Math.abs(mouseEvent.getY()-newRect.getY()));

					if(newRect.getX() > mouseEvent.getX()){
						//newRect.setX(mouseEvent.getX());
						newRect.setTranslateX(mouseEvent.getX());
					}

					if(newRect.getY() > mouseEvent.getY()){
						newRect.setY(mouseEvent.getY());
						newRect.setTranslateY(mouseEvent.getY());
					}

					graphc.setFill(colorPicker.getValue());
					graphc.fillRect(newRect.getX(),newRect.getY(),newRect.getWidth(),newRect.getHeight());
					shape.add(newRect);

				}



				else if(shapeTool=="Circle"){

					newCircle.setRadius(Math.sqrt(Math.pow(newCircle.getCenterX()-mouseEvent.getX(),2) + Math.pow(newCircle.getCenterY()-mouseEvent.getY(),2)));
					graphc.strokeOval(newCircle.getCenterX(),newCircle.getCenterY(),newCircle.getRadius(),newCircle.getRadius());
					shape.add(newCircle);
				}
				else if (shapeTool == "Circle fill"){
					newCircle.setRadius(Math.sqrt(Math.pow(newCircle.getCenterX()-mouseEvent.getX(),2) + Math.pow(newCircle.getCenterY()-mouseEvent.getY(),2)));
					graphc.setFill(colorPicker.getValue());
					graphc.fillOval(newCircle.getCenterX(),newCircle.getCenterY(),newCircle.getRadius(),newCircle.getRadius());
					shape.add(newCircle);

				}

			}
		});
	
		//============================================================================================

		stage.setScene(scene);
		stage.show();
	}
	private void reDrawScreen() {
		shapeCanvas.getGraphicsContext2D().clearRect(0, 0, shapeCanvas.getWidth(), shapeCanvas.getHeight());
		for(Shape p : shape) {

			double diameter = 23;
			shapeCanvas.getGraphicsContext2D().strokeOval( p.getLayoutX() - (p.getLayoutX() / 2), p.getLayoutY() - (p.getLayoutY() / 2), p.getLayoutY(), p.getLayoutY());

		}
	}
    

}

class ShapeCollection{
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	private ArrayList<Circle> circles = new ArrayList<Circle>();

	public ShapeCollection(){
		lines = new ArrayList<Line>();
		rectangles = new ArrayList<Rectangle>();
		circles = new ArrayList<Circle>();
	}

	public void addShape(Shape shape){
		
	}
	public ArrayList<Shape> getAllShapes(){
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		for (Line line : lines){
			shapes.add(line);
		}
		for (Rectangle rect : rectangles ){
			shapes.add(rect);
		}
		for (Circle circ : circles){
			shapes.add(circ);
		}
		return shapes;
	}
}
