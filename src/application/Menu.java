package application;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Menu {

	public static final int WINDOW_HEIGHT = 750;
	public static final int WINDOW_WIDTH = 1000;

	// chosen cow skins; default = 1 (brown)
	protected static int P1_cow = 1;
	protected static int P2_cow = 1;

	private Stage stage;
	private Scene mainMenuScene, instructScene, developersScene, selectionScene,gameOverScene;

	private Button lastClickedButtonP1 = null;
	private Button lastClickedButtonP2 = null;
	private static MediaPlayer mediaPlayer;
	
	
	// the audio is loaded once during the static initialization
	static {
	    try {
	        Media soundMedia = new Media(GameStage.class.getResource("bgmusic.mp3").toExternalForm());
	        mediaPlayer = new MediaPlayer(soundMedia);
	        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	    } catch (Exception e) {
	        System.out.println("Error loading background music: " + e.getMessage());
	    }
	}

	//constructor, creates the scenes
	public Menu() {
		this.mainMenuScene = createMMScene();
		this.instructScene = createInstructionScene();
		this.developersScene = createDevelopersScene();
		this.selectionScene =  createSelectionScene() ;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setScene(mainMenuScene);
		//this.stage.setScene(mainMenuScene);
		this.stage.setTitle("Dairy Dash");
		this.stage.show();

		//ensure that the player cannot resize the window as to avoid issues
		stage.setResizable(false);
	}

	//	public void setgameOverStage(Stage stage) { //called when a gameOver is recognized in gametimer
	//		this.stage = stage;
	//		this.stage.setScene(gameOverScene);
	//		//this.stage.setScene(mainMenuScene);
	//		this.stage.show();
	//		
	//		//ensure that the player cannot resize the window as to avoid issues
	//		stage.setResizable(false);
	//	}

	private Scene createMMScene() {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("bg.png").toExternalForm());
		Image aboutbtnImg = new Image(getClass().getResource("about.png").toExternalForm());
		Image newGameImg = new Image(getClass().getResource("newGame.png").toExternalForm());
		Image devsImg = new Image(getClass().getResource("devs.png").toExternalForm());
		Image titleImg = new Image(getClass().getResource("DDTitle.png").toExternalForm());
		Image cloudsImg = new Image(getClass().getResource("clouds.png").toExternalForm());

		ImageView backgroundImageView = new ImageView(backgroundImage);
		ImageView titleImageView = new ImageView(titleImg);
		ImageView aboutBtn = new ImageView(aboutbtnImg);
		ImageView newGameBtn = new ImageView(newGameImg);
		ImageView devsBtn = new ImageView(devsImg);
		ImageView cloudsImageView = new ImageView(cloudsImg);

		//Music
		
	    if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
	        mediaPlayer.play();
	    }

		//Animation for Title 
		titleImageView.setFitWidth(350);
		titleImageView.setPreserveRatio(true);
		titleImageView.setTranslateY(4);
		titleImageView.setTranslateX(((WINDOW_WIDTH - 350) / 2) + 5);
		ScaleTransition scaleTransition = new ScaleTransition();
		scaleTransition.setNode(titleImageView);         
		scaleTransition.setDuration(Duration.seconds(2)); 
		scaleTransition.setFromX(1);               
		scaleTransition.setFromY(1);               
		scaleTransition.setToX(1.2);               
		scaleTransition.setToY(1.2);                
		scaleTransition.setCycleCount(ScaleTransition.INDEFINITE); 
		scaleTransition.setAutoReverse(true);     
		scaleTransition.play();


		//Animation for Clouds
		TranslateTransition translateTransition = new TranslateTransition(); 
		cloudsImageView.setFitWidth(1200);
		cloudsImageView.setTranslateX(-50);
		cloudsImageView.setPreserveRatio(true);
		translateTransition.setDuration(Duration.seconds(5)); 
		translateTransition.setNode(cloudsImageView);      
		translateTransition.setByX(40); 
		translateTransition.setCycleCount(50); 
		translateTransition.setAutoReverse(true); 
		translateTransition.play(); 

		//Button About
		Button about = new Button();
		about.setGraphic(aboutBtn);
		about.getStyleClass().add("btn");
		about.setTranslateY(300);
		about.setTranslateX(getButtonPos(aboutbtnImg)); //make this into a function 
		about.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructions(stage);
			}
		});


		//Button for newGame
		Button newGame = new Button();
		newGame.getStyleClass().add("btn");
		newGame.setGraphic(newGameBtn);
		newGame.setTranslateY(420);
		newGame.setTranslateX(getButtonPos(newGameImg)); 
		newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openSelection(stage);
				//				GameStage theGameStage = new GameStage();
				//				theGameStage.setStage(stage);
			}
		});


		//Button for newGame
		Button developers = new Button();
		developers.setTranslateY(540);
		developers.getStyleClass().add("btn");
		developers.setGraphic(devsBtn);
		developers.setTranslateX(getButtonPos(devsImg)); //make this into a function 
		developers.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openDevelopers(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,cloudsImageView,about,newGame,developers,titleImageView);
		return scene;
	}

	private Scene createInstructionScene() {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("InstructionsImg.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image next = new Image(getClass().getResource("NextBtn.png").toExternalForm());
		ImageView nextBtnImg = new ImageView(next);
		nextBtnImg.setFitHeight(76.5);
		nextBtnImg.setPreserveRatio(true);

		Button nextBtn = new Button();
		nextBtn.setGraphic(nextBtnImg);
		nextBtn.getStyleClass().add("btn");
		nextBtn.setTranslateX(550);
		nextBtn.setTranslateY(503.8);
		nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene2(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,nextBtn);
		return scene;
	}

	private Scene createDevelopersScene() {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("DevsImg.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(40);
		backBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(30);
		backBtn.setTranslateY(30);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openMainMenu(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn);
		return scene;
	}

	//	protected Scene creategameOverScene() {
	//		Group root = new Group(); // create a root
	//		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
	//		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	//		
	//		Image backgroundImage = new Image(getClass().getResource("gameOver.png").toExternalForm());
	//		ImageView backgroundImageView = new ImageView(backgroundImage);
	//		Image mainMenu = new Image(getClass().getResource("mainMenuBtn.png").toExternalForm());
	//		ImageView mainMenuImg = new ImageView(mainMenu);
	//		mainMenuImg.setFitWidth(226.4);
	//		mainMenuImg.setPreserveRatio(true);
	//		
	//		Button mainMenuBtn = new Button();
	//		mainMenuBtn.setGraphic(mainMenuImg);
	//		mainMenuBtn.getStyleClass().add("btn");
	//		mainMenuBtn.setTranslateX(395.7);
	//		mainMenuBtn.setTranslateY(580.2);
	//		mainMenuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
	//			public void handle(MouseEvent arg0) {
	//				openMainMenu(stage);
	//			}
	//		});
	//		
	//		root.getChildren().addAll(backgroundImageView,mainMenuBtn);
	//		return scene;
	//	}

	protected void opengameOverScene(Stage stage, int score, int happinessLevel) {
		this.stage = stage;
		//Font pixelFont = Font.loadFont("pixleFont.ttf", 100);
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("gameOver.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image mainMenu = new Image(getClass().getResource("mainMenuBtn.png").toExternalForm());
		ImageView mainMenuImg = new ImageView(mainMenu);
		mainMenuImg.setFitWidth(226.4);
		mainMenuImg.setPreserveRatio(true);

		Text scoreTxt = new Text(Integer.toString(score));
		Text happinessLevelTxt = new Text(Integer.toString(happinessLevel));
		happinessLevelTxt.setFont(Font.loadFont(getClass().getResource("pixelFont.ttf").toExternalForm(), 28));
		happinessLevelTxt.setFill(Color.web("#aa7959")); 
		scoreTxt.setFont(Font.loadFont(getClass().getResource("pixelFont.ttf").toExternalForm(), 28));
		scoreTxt.setFill(Color.web("#aa7959")); 
		double scoreTextWidth = scoreTxt.getLayoutBounds().getWidth(); //gets bounds of the text to get the width
		double happinessLevelTxtWidth = happinessLevelTxt.getLayoutBounds().getWidth(); //gets bounds of the text to get the width
		scoreTxt.setTranslateX(510 - scoreTextWidth / 2); 
		scoreTxt.setTranslateY(370.1);   
		happinessLevelTxt.setTranslateX(510 - happinessLevelTxtWidth / 2); 
		happinessLevelTxt.setTranslateY(470.8);


		Button mainMenuBtn = new Button();
		mainMenuBtn.setGraphic(mainMenuImg);
		mainMenuBtn.getStyleClass().add("btn");
		mainMenuBtn.setTranslateX(395.7);
		mainMenuBtn.setTranslateY(580.2);
		mainMenuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openMainMenu(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,scoreTxt,happinessLevelTxt,mainMenuBtn);
		this.stage.setScene(scene);
		//this.stage.setScene(mainMenuScene);
		this.stage.show();

		//ensure that the player cannot resize the window as to avoid issues
		stage.setResizable(false);
	}

	private Scene createSelectionScene() {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("selection_bg.png").toExternalForm());
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());

		Image p1c1 = new Image(getClass().getResource("unselectedp1c1.png").toExternalForm());
		Image p1c2 = new Image(getClass().getResource("unselectedp1c2.png").toExternalForm());
		Image p1c3 = new Image(getClass().getResource("unselectedp1c3.png").toExternalForm());
		Image p1c1Selected = new Image(getClass().getResource("selectedp1c1.png").toExternalForm());
		Image p1c2Selected = new Image(getClass().getResource("selectedp1c2.png").toExternalForm());
		Image p1c3Selected = new Image(getClass().getResource("selectedp1c3.png").toExternalForm());

		Image p2c1 = new Image(getClass().getResource("unselectedp2c1.png").toExternalForm());
		Image p2c2 = new Image(getClass().getResource("unselectedp2c2.png").toExternalForm());
		Image p2c3 = new Image(getClass().getResource("unselectedp2c3.png").toExternalForm());
		Image p2c1Selected = new Image(getClass().getResource("selectedp2c1.png").toExternalForm());
		Image p2c2Selected = new Image(getClass().getResource("selectedp2c2.png").toExternalForm());
		Image p2c3Selected = new Image(getClass().getResource("selectedp2c3.png").toExternalForm());

		ImageView backgroundImageView = new ImageView(backgroundImage);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(40);
		backBtnImg.setPreserveRatio(true);

		Button p1c1Btn = new Button();
		Button p1c2Btn = new Button();
		Button p1c3Btn = new Button();

		Button p2c1Btn = new Button();
		Button p2c2Btn = new Button();
		Button p2c3Btn = new Button();

		p1c1Btn.setGraphic(new ImageView(p1c1));
		p1c2Btn.setGraphic(new ImageView(p1c2));
		p1c3Btn.setGraphic(new ImageView(p1c3));

		p2c1Btn.setGraphic(new ImageView(p2c1));
		p2c2Btn.setGraphic(new ImageView(p2c2));
		p2c3Btn.setGraphic(new ImageView(p2c3));


		p1c1Btn.getStyleClass().add("btn");
		p1c2Btn.getStyleClass().add("btn");
		p1c3Btn.getStyleClass().add("btn");

		p2c1Btn.getStyleClass().add("btn");
		p2c2Btn.getStyleClass().add("btn");
		p2c3Btn.getStyleClass().add("btn");

		p1c1Btn.setTranslateX(107.6);
		p1c1Btn.setTranslateY(225.7);
		p1c2Btn.setTranslateX(112.6);
		p1c2Btn.setTranslateY(365.1);
		p1c3Btn.setTranslateX(112.6);
		p1c3Btn.setTranslateY(505.5);

		p2c1Btn.setTranslateX(633.1);
		p2c1Btn.setTranslateY(225.7);
		p2c2Btn.setTranslateX(633.1);
		p2c2Btn.setTranslateY(363.5);
		p2c3Btn.setTranslateX(633.1);
		p2c3Btn.setTranslateY(502.3);

		// Change Images
		p1c1Btn.setOnAction(e -> {
			if (lastClickedButtonP1 != p1c1Btn) {

				// updating current selected
				p1c1Btn.setGraphic(new ImageView(p1c1Selected));

				// updating previously selected to unselected
				if (lastClickedButtonP1 == p1c2Btn) {
					resetButtonImage(lastClickedButtonP1, p1c2);
				}
				else if (lastClickedButtonP1 == p1c3Btn) {
					resetButtonImage(lastClickedButtonP1, p1c3);
				}

				// updating last clicked button and chosen cow skin
				lastClickedButtonP1 = p1c1Btn;
				P1_cow = 1;
			}
		});

		p1c2Btn.setOnAction(e -> {
			if (lastClickedButtonP1 != p1c2Btn) {

				p1c2Btn.setGraphic(new ImageView(p1c2Selected));

				if (lastClickedButtonP1 == p1c1Btn) {
					resetButtonImage(lastClickedButtonP1, p1c1);
				}
				else if (lastClickedButtonP1 == p1c3Btn) {
					resetButtonImage(lastClickedButtonP1, p1c3);
				}

				lastClickedButtonP1 = p1c2Btn; 
				P1_cow = 2;
			}
		});

		p1c3Btn.setOnAction(e -> {
			if (lastClickedButtonP1 != p1c3Btn) {

				p1c3Btn.setGraphic(new ImageView(p1c3Selected));

				if (lastClickedButtonP1 == p1c1Btn) {
					resetButtonImage(lastClickedButtonP1, p1c1);
				}
				else if (lastClickedButtonP1 == p1c2Btn) {
					resetButtonImage(lastClickedButtonP1, p1c2);
				}

				lastClickedButtonP1 = p1c3Btn; 
				P1_cow = 3;
			}
		});

		p2c1Btn.setOnAction(e -> {
			if (lastClickedButtonP2 != p2c1Btn) {

				p2c1Btn.setGraphic(new ImageView(p2c1Selected));

				if (lastClickedButtonP2 == p2c2Btn) {
					resetButtonImage(lastClickedButtonP2, p2c2);
				}
				else if (lastClickedButtonP2 == p2c3Btn) {
					resetButtonImage(lastClickedButtonP2, p2c3);
				}

				lastClickedButtonP2 = p2c1Btn;
				P2_cow = 1;

			}
		});

		p2c2Btn.setOnAction(e -> {
			if (lastClickedButtonP2 != p2c2Btn) {

				p2c2Btn.setGraphic(new ImageView(p2c2Selected));

				if (lastClickedButtonP2 == p2c1Btn) {
					resetButtonImage(lastClickedButtonP2, p2c1);
				}
				else if (lastClickedButtonP2 == p2c3Btn) {
					resetButtonImage(lastClickedButtonP2, p2c3);
				}

				lastClickedButtonP2 = p2c2Btn;
				P2_cow = 2;
			}
		});

		p2c3Btn.setOnAction(e -> {
			if (lastClickedButtonP2 != p2c3Btn) {

				p2c3Btn.setGraphic(new ImageView(p2c3Selected));

				if (lastClickedButtonP2 == p2c1Btn) {
					resetButtonImage(lastClickedButtonP2, p2c1);
				}
				else if (lastClickedButtonP2 == p2c2Btn) {
					resetButtonImage(lastClickedButtonP2, p2c2);
				}

				lastClickedButtonP2 = p2c3Btn;
				P2_cow = 3;
			}
		});

		// Back Button
		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(30);
		backBtn.setTranslateY(30);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openMainMenu(stage);
			}
		});

		// Start Game Button
		Image gameStartImg = new Image(getClass().getResource("gameStart.png").toExternalForm());
		ImageView gameStartBtn = new ImageView(gameStartImg);
		Button startBtn = new Button();
		startBtn.getStyleClass().add("btn");
		startBtn.setGraphic(gameStartBtn);
		startBtn.setTranslateY(670);
		startBtn.setTranslateX(740);
		startBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				GameStage theGameStage = new GameStage(stage);
				theGameStage.setStage(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,p1c1Btn,p1c2Btn,p1c3Btn,p2c1Btn,p2c2Btn,p2c3Btn,startBtn);
		return scene;
	}


	private void openMainMenu(Stage stage) {
		this.stage.setScene(mainMenuScene);
	}

	private void openInstructions(Stage stage) {
		this.stage.setScene(instructScene);
	}
	//	protected void openGameOver(Stage stage) {
	//		this.stage.setScene(instructScene);
	//	}
	private void openInstructionScene2(Stage stage) {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("INSTRUCTIONS_2.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		Image next = new Image(getClass().getResource("NextBtn.png").toExternalForm());
		ImageView nextBtnImg = new ImageView(next);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(76.5);
		backBtnImg.setPreserveRatio(true);
		nextBtnImg.setFitHeight(76.5);
		nextBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(193.7);
		backBtn.setTranslateY(503.8);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructions(stage);
			}
		});

		Button nextBtn = new Button();
		nextBtn.setGraphic(nextBtnImg);
		nextBtn.getStyleClass().add("btn");
		nextBtn.setTranslateX(550);
		nextBtn.setTranslateY(503.8);
		nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene3(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,nextBtn);
		this.stage.setScene(scene);
	}

	private void openInstructionScene3(Stage stage) {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("INSTRUCTIONS_3.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		Image next = new Image(getClass().getResource("NextBtn.png").toExternalForm());
		ImageView nextBtnImg = new ImageView(next);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(76.5);
		backBtnImg.setPreserveRatio(true);
		nextBtnImg.setFitHeight(76.5);
		nextBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(193.7);
		backBtn.setTranslateY(503.8);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene2(stage);
			}
		});

		Button nextBtn = new Button();
		nextBtn.setGraphic(nextBtnImg);
		nextBtn.getStyleClass().add("btn");
		nextBtn.setTranslateX(550);
		nextBtn.setTranslateY(503.8);
		nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene4(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,nextBtn);
		this.stage.setScene(scene);
	}

	private void openInstructionScene4(Stage stage) {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("INSTRUCTIONS_4.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		Image next = new Image(getClass().getResource("NextBtn.png").toExternalForm());
		ImageView nextBtnImg = new ImageView(next);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(76.5);
		backBtnImg.setPreserveRatio(true);
		nextBtnImg.setFitHeight(76.5);
		nextBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(193.7);
		backBtn.setTranslateY(503.8);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene3(stage);
			}
		});

		Button nextBtn = new Button();
		nextBtn.setGraphic(nextBtnImg);
		nextBtn.getStyleClass().add("btn");
		nextBtn.setTranslateX(550);
		nextBtn.setTranslateY(503.8);
		nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene5(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,nextBtn);
		this.stage.setScene(scene);
	}

	private void openInstructionScene5(Stage stage) {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("INSTRUCTIONS_5.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		Image next = new Image(getClass().getResource("NextBtn.png").toExternalForm());
		ImageView nextBtnImg = new ImageView(next);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(76.5);
		backBtnImg.setPreserveRatio(true);
		nextBtnImg.setFitHeight(76.5);
		nextBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(193.7);
		backBtn.setTranslateY(503.8);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene4(stage);
			}
		});

		Button nextBtn = new Button();
		nextBtn.setGraphic(nextBtnImg);
		nextBtn.getStyleClass().add("btn");
		nextBtn.setTranslateX(550);
		nextBtn.setTranslateY(503.8);
		nextBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene6(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,nextBtn);
		this.stage.setScene(scene);
	}

	private void openInstructionScene6(Stage stage) {
		Group root = new Group(); // create a root
		Scene scene = new Scene(root, WINDOW_WIDTH,WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		Image backgroundImage = new Image(getClass().getResource("INSTRUCTIONS_6.png").toExternalForm());
		ImageView backgroundImageView = new ImageView(backgroundImage);
		Image backImg = new Image(getClass().getResource("BackBtn.png").toExternalForm());
		Image mm = new Image(getClass().getResource("mainMenuBtn.png").toExternalForm());
		ImageView mainMenuBtnImg = new ImageView(mm);
		ImageView backBtnImg = new ImageView(backImg);
		backBtnImg.setFitHeight(76.5);
		backBtnImg.setPreserveRatio(true);
		mainMenuBtnImg.setFitHeight(76.5);
		mainMenuBtnImg.setPreserveRatio(true);

		Button backBtn = new Button();
		backBtn.setGraphic(backBtnImg);
		backBtn.getStyleClass().add("btn");
		backBtn.setTranslateX(193.7);
		backBtn.setTranslateY(503.8);
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openInstructionScene5(stage);
			}
		});

		Button mainMenuBtn = new Button();
		mainMenuBtn.setGraphic(mainMenuBtnImg);
		mainMenuBtn.getStyleClass().add("btn");
		mainMenuBtn.setTranslateX(540);
		mainMenuBtn.setTranslateY(503.8);
		mainMenuBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				openMainMenu(stage);
			}
		});

		root.getChildren().addAll(backgroundImageView,backBtn,mainMenuBtn);
		this.stage.setScene(scene);
	}

	private void openDevelopers(Stage stage) {
		this.stage.setScene(developersScene);
	}

	private void openSelection(Stage stage) {
		this.stage.setScene(selectionScene);
	}

	public double getButtonPos(Image img) { //Function ensures that the button is in the center of the window
		double Width = img.getWidth();
		double finalPos = WINDOW_WIDTH/2 - Width/2;

		return finalPos;
	}

	private void resetButtonImage(Button button, Image img) {
		if (button == lastClickedButtonP1 || button == lastClickedButtonP2) {
			// Get the original image for the button and set it
			button.setGraphic(new ImageView(img));
		}
	}

}
