package application;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class GameTimer extends AnimationTimer{
	private Menu menu;
	private Stage stage;

	private Map<String, String> houseOrders = new HashMap<>();
	// Define house locations
	private Map<String, Point> storeLocations = Map.of(
			"corn", new Point(55, 127),
			"tools", new Point(307, 15),
			"tomato", new Point(560, 15),
			"seeds", new Point(765, 15)
			);

	private Map<String, Point> houseLocations = Map.of(
			"House 1", new Point(176, 172),
			"House 2", new Point(745, 172),
			"House 3", new Point(176, 333),
			"House 4", new Point(745, 333),
			"House 5", new Point(176, 497),
			"House 6", new Point(745, 497)
			);


	private Map<String, Image> orderSprites = Map.of(
			"corn", new Image(getClass().getResource("corn.png").toExternalForm()),
			"tools", new Image(getClass().getResource("tools.png").toExternalForm()),
			"tomato", new Image(getClass().getResource("tomato.png").toExternalForm()),
			"seeds", new Image(getClass().getResource("seeds.png").toExternalForm())
			);

	private List<String> storeKeys = Arrays.asList("corn", "tools", "tomato", "seeds");

	private final Set<KeyCode> pressedKeys = new HashSet<>();
	private Map<String, Long> orderCreationTimes = new HashMap<>();

	private List<Rectangle> houseBounds;
	private List<Rectangle> storeBounds;
	
	private List<Rectangle> Borders;

	private Rectangle kart1;
	private Rectangle kart2;
	
	//
	private GraphicsContext gc;

	// timers
	private long lastOrderTime = 0;  // To store the last time orders were generated
	private static final long ORDER_INTERVAL = 35000;  // 60,000 ms = 1 minute 

	private final long GAME_DURATION = 300000000000L; // 5 minutes
	//private final long GAME_DURATION =3000000000L; //for testing 

	private final long MINI_GAME_DURATION = 30000000000L; // 8 seconds
	private final long ORDER_DURATION = 30000000000L; // 30 seconds

	private long startTime;
	private boolean gameOver = false;

	private Vehicle vehicle1;
	private Vehicle vehicle2;

	public static boolean canMoveVehicle1 = true;
	public static boolean canMoveVehicle2 = true;
	
	private boolean naturalDisasterRoll = true;
	private int naturalDisasterTrig = 0;
	public static boolean naturalDisasterOcc = false;
	private int vehicleBumpFixer = 1;
	
	public boolean miniGameSuccess;

	private String Kart1keyHolder = "";
	private String Kart2keyHolder = "";

	public boolean checker;
	public int happinessLevel = 100;
	private Scene scene;
	private String userInput = "";
	private String randomText = ""; // Stores the random text to be typed

	public int score = 0;

	private boolean isMiniGameActive = false;
	private boolean miniGameStarted = false;  // Track if the mini-game is already triggered
	private Timeline miniGameTimeline;

	Map<Integer, String> houseNames = new HashMap<>();
	
	Image gameOverlay = new Image(getClass().getResource("gameproperoverlay.png").toExternalForm());
	
	Image thunderstorm1 = new Image(getClass().getResource("rain1.png").toExternalForm());
	Image thunderstorm2 = new Image(getClass().getResource("rain2.png").toExternalForm());
	Image rain_alert = new Image(getClass().getResource("rain_alert.png").toExternalForm());
	Image fog = new Image(getClass().getResource("fog.png").toExternalForm());
	Image blackout_alert = new Image(getClass().getResource("blackout_alert.png").toExternalForm());
	Image blackout = new Image(getClass().getResource("blackout.png").toExternalForm());

	
	Rectangle sample1 = new Rectangle(104.9, 93.2, 333.6, 564.9);

	public GameTimer(GraphicsContext gc, Scene scene, Stage stage) {
		this.gc = gc;
		this.scene = scene;
		this.stage = stage;
		this.menu = new Menu();

		this.vehicle1 = new Vehicle(435, 100);
		this.vehicle2 = new Vehicle(500, 100);
		
		// load player 1's selected cow
		if (Menu.P1_cow == 1) {
			this.vehicle1.loadImage(getClass().getResource("cow_up.png").toExternalForm());
		}
		else if (Menu.P1_cow == 2) {
			this.vehicle1.loadImage(getClass().getResource("cowbnw_up.png").toExternalForm());
		}
		else if (Menu.P1_cow == 3) {
			this.vehicle1.loadImage(getClass().getResource("cowpink_up.png").toExternalForm());
		}
		
		// load player 2's selected cow
		if (Menu.P2_cow == 1) {
			this.vehicle2.loadImage(getClass().getResource("cow_up.png").toExternalForm());
		}
		else if (Menu.P2_cow == 2) {
			this.vehicle2.loadImage(getClass().getResource("cowbnw_up.png").toExternalForm());
		}
		else if (Menu.P2_cow == 3) {
			this.vehicle2.loadImage(getClass().getResource("cowpink_up.png").toExternalForm());
		}

		this.handleKeyPressEvent();

		kart1 = new Rectangle(vehicle1.getX(), vehicle1.getY(), 20, 25);
		kart2 = new Rectangle(vehicle2.getX(), vehicle2.getY(), 20, 25);

		houseBounds = Arrays.asList(
				new Rectangle(386.2, 253.6, 53.4, 47.1), 
				new Rectangle(560.5, 253.6, 53.4, 47.1),
				new Rectangle(386.2, 415.8, 53.4, 47.1), 
				new Rectangle(560.5, 415.8, 53.4, 47.1),  
				new Rectangle(386.2, 577.9, 53.4, 47.1),  
				new Rectangle(560.5, 577.9, 53.4, 47.1)  
				);

		houseNames.put(0, "House 1");
		houseNames.put(1, "House 2");
		houseNames.put(2, "House 3");
		houseNames.put(3, "House 4");
		houseNames.put(4, "House 5");
		houseNames.put(5, "House 6");

		storeBounds = Arrays.asList(
				new Rectangle(127.6, 171.8, 53.4, 47.1),  //STORE 1
				new Rectangle(332.8 ,171.8, 53.4, 47.1), // STORE 2
				new Rectangle(582.3, 171.8, 53.4, 47.1), // STORE 3
				new Rectangle(785.7, 171.8, 53.4, 47.1) // STORE 4
				);
		
		Borders = Arrays.asList(
				new Rectangle(109.9, 93.2, 330.6, 564.9)
				);

		this.startTime = System.nanoTime();


	}

	//debugging method
	private void drawhouseBounds(GraphicsContext gc) {
		gc.setStroke(Color.RED); // Outline color

		// Iterate through houseBounds and draw each rectangle
		for (int i = 0; i < houseBounds.size(); i++) {
			Rectangle rect = houseBounds.get(i);
			gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}

		for (int i = 0; i < storeBounds.size(); i++) {
			Rectangle rect = storeBounds.get(i);
			gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			
		}

	}


	private void handleKeyPressEvent() {
		scene.setOnKeyPressed(event -> {
			KeyCode code = event.getCode();

			// handle vehicle movement
			moveVehicle(code);

			// check for 'L' key press to start the mini-game
			if (code == KeyCode.L && !miniGameStarted && 
					(checkCollisionStore2(kart1, storeBounds) || checkCollisionStore2(kart2, storeBounds))) {
				triggerMiniGame();
			}

			if(code == KeyCode.ENTER) {
				checkMiniGameResult();
			}

			if(code == KeyCode.U) {
				pressedKeys.add(KeyCode.U);
			}

			if (isMiniGameActive) {
				processUserInput(code);
			}
		});

		// Handle key releases
		scene.setOnKeyReleased(event -> {
			KeyCode code = event.getCode();
			pressedKeys.remove(KeyCode.U);

			stopVehicle(code);  
		});
	}



	private boolean checkCollisionStore2(Rectangle kart, List<Rectangle> storeBounds) {
		kart1 = new Rectangle(vehicle1.getX(), vehicle1.getY(), 20, 25);
		kart2 = new Rectangle(vehicle2.getX(), vehicle2.getY(), 20, 25);	

		for (Rectangle store : storeBounds) {
			if (kart.getBoundsInLocal().intersects(store.getBoundsInLocal())){  
				return true;  
			}
		}

		return false;
	}

	public boolean checkCollisionStore(Rectangle kart, Rectangle store) {
		kart1 = new Rectangle(vehicle1.getX(), vehicle1.getY(), 20, 25);
		kart2 = new Rectangle(vehicle2.getX(), vehicle2.getY(), 20, 25);

		return kart.getBoundsInLocal().intersects(store.getBoundsInLocal());
	}

	public boolean checkCollisionHome(Rectangle kart, Rectangle house) {
//		kart1 = new Rectangle(vehicle1.getX(), vehicle1.getY(), 20, 25);
//		kart2 = new Rectangle(vehicle2.getX(), vehicle2.getY(), 20, 25);

		return kart.getBoundsInLocal().intersects(house.getBoundsInLocal());
	}


	private void generateHouseOrders(long now) {
		if (now - lastOrderTime >= ORDER_INTERVAL * 1_000_000) {  
			Random random = new Random();

			for (String house : houseLocations.keySet()) {
				if (!houseOrders.containsKey(house)) {  // only generate an order if no current order exists
					long currentTime = System.nanoTime();
					orderCreationTimes.put(house, currentTime);

					String order = storeKeys.get(random.nextInt(storeKeys.size()));
					houseOrders.put(house, order);

					Point location = houseLocations.get(house);
					Image image = orderSprites.get(order);

					if (image != null) {
						gc.drawImage(image, location.x, location.y, 58, 67);
						//  System.out.println("Order generated: " + house + " -> " + order + " at location " + location);
					} else {
						System.err.println("Sprite not found for order: " + order);
					}
				}
			}

			lastOrderTime = now;  // update the last order generation time
		}
	}


	public void completeOrder(String house) {
		if (houseOrders.containsKey(house)) {
			houseOrders.remove(house);
			orderCreationTimes.remove(house);
			System.out.println("Order completed for house: " + house);
		}
	}


	private void checkOverdueOrders(long now) {
		//Returns a Set of map entries (Map.Entry<K, V>), which are key-value pairs.
		for (Iterator<Map.Entry<String, Long>> it = orderCreationTimes.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Long> entry = it.next(); 
			String house = entry.getKey();
			long creationTime = entry.getValue();

			// skip orders that have already been completed (removed from houseOrders)
			if (!houseOrders.containsKey(house)) {
				continue; 
			}

			//If the elapsed time is greater than or equal to 30 seconds, the order is considered overdue.
			if (now - creationTime >= 30 * 1_000_000_000L) {
				System.out.println("Order overdue at house: " + house);

				//Decrease happiness level
				//Math.max(0, ...): Ensures that the happiness level does not go below 0.
				happinessLevel = Math.max(0, happinessLevel - 5);
				System.out.println("Happiness level decreased to: " + happinessLevel);

				// Remove the overdue order
				it.remove();
				houseOrders.remove(house);
			}
		}
	}


	private String loadGoods(Rectangle kart, List<Rectangle> storeBounds, 
			List<String> storeKeys, String kartKeyHolder) {

		String targetText = generateRandomString(4);      // The target text to match in the mini-game

		for (int i = 0; i < storeBounds.size(); i++) {
			Rectangle store = storeBounds.get(i);  // Get the current store rectangle
			String storeKey = storeKeys.get(i);    // Get the product for the current store
			Point storeLocation = storeLocations.get(storeKey);

			if (checkCollisionStore(kart, store)) {
				//	gc.fillText("Press 'L' to load " + kartKeyHolder, 100, 100);

				if (isMiniGameActive) {
					gc.clearRect(100, 120, 300, 50);  // Clear previous input (if necessary)
					Image minigameImage = new Image(getClass().getResource("minigame.png").toExternalForm());

					// Draw the mini-game image at the store's location
					if (storeLocation != null) {
						gc.drawImage(minigameImage, storeLocation.x, storeLocation.y, 101, 56);

						int textOffsetX = 16; // Horizontal offset for text
						int textOffsetY = 25; // Vertical offset for text

						// Render randomText near the store dynamically
						gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
						gc.setFill(Color.web("#8f625c"));
						gc.fillText(randomText, storeLocation.x + textOffsetX, storeLocation.y + textOffsetY);

						// Render userInput near the store dynamically
						gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
						gc.setFill(Color.web("#8f625c"));
						gc.fillText(userInput, storeLocation.x + textOffsetX + 30, storeLocation.y + textOffsetY + 23);
					}


					// Check if user input matches the target text when Enter is pressed
					if (pressedKeys.contains(KeyCode.ENTER)) {
						if (userInput.equals(targetText)) {
							System.out.println("Mini-game success!");
							miniGameSuccess = true;
						} else {
							System.out.println("Try again.");
							miniGameSuccess = false;
						}

						// Reset the mini-game and user input
						userInput = "";
						miniGameStarted = false;  
					}
				}



				if (miniGameSuccess) {
					kartKeyHolder = updateKartStatus(kart, storeKey, kartKeyHolder);  // Update kart status
					System.out.println("Product loaded from store: " + storeKey);
					return kartKeyHolder;  // Return updated kartKeyHolder
				}

			}


		}


		// If no successful load, return the original kartKeyHolder
		return kartKeyHolder;
	}



	private boolean areRectanglesEqual(Rectangle r1, Rectangle r2) {
		return r1.getX() == r2.getX() && r1.getY() == r2.getY()
				&& r1.getWidth() == r2.getWidth() && r1.getHeight() == r2.getHeight();
	}


	private String updateKartStatus(Rectangle kart, String product, String kartKeyHolder) {
		if (areRectanglesEqual(kart, kart1)) {
			kartKeyHolder = product;  // Kart1 is holding the product
			System.out.println("Kart 1 loaded with " + product);
		} else if (areRectanglesEqual(kart, kart2)) {
			kartKeyHolder = product;  // Kart2 is holding the product
			System.out.println("Kart 2 loaded with " + product);
		} else {
			System.out.println("Kart not recognized.");
		}
		return kartKeyHolder;
	}



	public static String generateRandomString(int length) {
		String characters = "BCEFGHIJKMNOPQRTVXYZ";
		StringBuilder result = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			result.append(characters.charAt(index));
		}

		return result.toString();
	}


	private void triggerMiniGame() {
		isMiniGameActive = true;
		randomText = generateRandomString(4); // Generate text to type
		userInput = ""; // Reset input
		System.out.println("Mini-game started! Type this: " + randomText);
//		gc.clearRect(200, 150, 400, 50); // Clear previous text

		// Start the mini-game timer
		miniGameTimeline = new Timeline(new KeyFrame(Duration.seconds(8), e -> {
			endMiniGame();
		}));
		miniGameTimeline.setCycleCount(1);
		miniGameTimeline.play();
	}


	private void processUserInput(KeyCode code) {
		// check if the key is a letter or digit, but not 'L'
		if ((code.isLetterKey() && code != KeyCode.L)) {
			userInput += code.getName();  // add key to input
		} else if (code == KeyCode.BACK_SPACE && userInput.length() > 0) {
			userInput = userInput.substring(0, userInput.length() - 1);  // Remove last character
		}
	}


	private void checkMiniGameResult() {
		if (userInput.equals(randomText)) {
			miniGameSuccess = true;
			System.out.println("Mini-game success!");
		} else {
			miniGameSuccess = false;
			System.out.println("Mini-game failed!");
		}
		endMiniGame();
	}

	private void endMiniGame() {
		isMiniGameActive = false;
		miniGameTimeline.stop();
		System.out.println("Mini-game ended. Game resumes.");
	}


	private void unloadingMechanism(Rectangle kart, String kartKeyHolder) {
		String collidedHouse = null;  // Variable to store the house that collided

		// Iterate through the houseBounds list to check for collisions
		for (int i = 0; i < houseBounds.size(); i++) {
			Rectangle house = houseBounds.get(i);
			if (checkCollisionHome(kart, house)) {  // Check if the kart collides with this house
				collidedHouse = houseNames.get(i);  // Get the house name using the index
				break;  // Exit the loop once a collision is found
			}
		}

		if (collidedHouse != null) {  // If there's a collision with a house
			// Check if the order matches for the collided house
			if (isOrderMatched(kartKeyHolder, collidedHouse, houseOrders)) {
			//	System.out.println("Order matched for kart: " + kartKeyHolder + " at house: " + collidedHouse);

				// Check if 'U' key is pressed for unloading
				if (pressedKeys.contains(KeyCode.U)){
					//System.out.println("U key pressed for kart: " + kartKeyHolder);

					// Check which kart is holding the product and unload accordingly
					if (areRectanglesEqual(kart, kart1)) {
						System.out.println("Kart1 unloading goods: " + kartKeyHolder);
						Kart1keyHolder = unloadGoods(kartKeyHolder);  // Unload goods from Kart1
						removeHouseOrder(collidedHouse);  // Remove the house order after unloading
						completeOrder(collidedHouse);  // Update the order status to "Completed"
					} else if (areRectanglesEqual(kart, kart2)) {
						System.out.println("Kart2 unloading goods: " + kartKeyHolder);
						Kart2keyHolder = unloadGoods(kartKeyHolder);  // Unload goods from Kart2
						removeHouseOrder(collidedHouse);  // Remove the house order after unloading
						completeOrder(collidedHouse);  // Update the order status to "Completed"

					}
				}
			}
		}
	}



	private String unloadGoods(String kartKeyHolder) {
		score += 10;  // Increment the score
		System.out.println("Current score: " + score);
		// Reset the kart's goods by directly clearing the kart's key holder
		if (kartKeyHolder.equals(Kart1keyHolder)) {
			Kart1keyHolder = "";  // Clear goods from Kart1
		} else if (kartKeyHolder.equals(Kart2keyHolder)) {
			Kart2keyHolder = "";  // Clear goods from Kart2
		}

		return "";  // Returning empty string for consistency, but it’s no longer needed to update the caller
	}

	private void removeHouseOrder(String kartKeyHolder) {
		// Remove the house order associated with the kartKeyHolder
		houseOrders.remove(kartKeyHolder);
	}


	private boolean isOrderMatched(String kartKeyHolder, String house, Map<String, String> houseOrders) {
		if (houseOrders.containsKey(house)) {
			String order = houseOrders.get(house);  // Get the order for the specific house
			return order.equals(kartKeyHolder);     // Check if the kart's product matches the house's order
		}
		return false;  // Return false if the house has no order
	}


	private void moveVehicle(KeyCode k) {
		
		if (isMiniGameActive) {
			return; // Exit the method to stop movement
		}
		
		// updating images depending on direction of the cow (PLAYER 1)
		if(k==KeyCode.UP) {
			if (Menu.P1_cow == 1) {
				this.vehicle1.loadImage(getClass().getResource("cow_up.png").toExternalForm());
			}
			else if (Menu.P1_cow == 2) {
				this.vehicle1.loadImage(getClass().getResource("cowbnw_up.png").toExternalForm());
			}
			else if (Menu.P1_cow == 3) {
				this.vehicle1.loadImage(getClass().getResource("cowpink_up.png").toExternalForm());
			}
			this.vehicle1.setDirection(0);
			this.vehicle1.setDY(-3);                 
		}

		if(k==KeyCode.LEFT) {
			if (Menu.P1_cow == 1) {
				this.vehicle1.loadImage(getClass().getResource("cow_left.png").toExternalForm());
			}
			else if (Menu.P1_cow == 2) {
				this.vehicle1.loadImage(getClass().getResource("cowbnw_left.png").toExternalForm());
			}
			else if (Menu.P1_cow == 3) {
				this.vehicle1.loadImage(getClass().getResource("cowpink_left.png").toExternalForm());
			}
			this.vehicle1.setDirection(3);
			this.vehicle1.setDX(-3);
		}

		if(k==KeyCode.DOWN) {
			if (Menu.P1_cow == 1) {
				this.vehicle1.loadImage(getClass().getResource("cow_down.png").toExternalForm());
			}
			else if (Menu.P1_cow == 2) {
				this.vehicle1.loadImage(getClass().getResource("cowbnw_down.png").toExternalForm());
			}
			else if (Menu.P1_cow == 3) {
				this.vehicle1.loadImage(getClass().getResource("cowpink_down.png").toExternalForm());
			}
			this.vehicle1.setDirection(2);
			this.vehicle1.setDY(3);
		}
		
		if(k==KeyCode.RIGHT) {
			if (Menu.P1_cow == 1) {
				this.vehicle1.loadImage(getClass().getResource("cow_right.png").toExternalForm());
			}
			else if (Menu.P1_cow == 2) {
				this.vehicle1.loadImage(getClass().getResource("cowbnw_right.png").toExternalForm());
			}
			else if (Menu.P1_cow == 3) {
				this.vehicle1.loadImage(getClass().getResource("cowpink_right.png").toExternalForm());
			}
			this.vehicle1.setDirection(1);
			this.vehicle1.setDX(3);
		}		
		// updating images depending on direction of the cow (PLAYER 2)
		if(k==KeyCode.W) {
			if (Menu.P2_cow == 1) {
				this.vehicle2.loadImage(getClass().getResource("cow_up.png").toExternalForm());
			}
			else if (Menu.P2_cow == 2) {
				this.vehicle2.loadImage(getClass().getResource("cowbnw_up.png").toExternalForm());
			}
			else if (Menu.P2_cow == 3){
				this.vehicle2.loadImage(getClass().getResource("cowpink_up.png").toExternalForm());
			}
			this.vehicle2.setDirection(0);
			this.vehicle2.setDY(-3);                 
		}

		if(k==KeyCode.A) {
			if (Menu.P2_cow == 1) {
				this.vehicle2.loadImage(getClass().getResource("cow_left.png").toExternalForm());
			}
			else if (Menu.P2_cow == 2) {
				this.vehicle2.loadImage(getClass().getResource("cowbnw_left.png").toExternalForm());
			}
			else if (Menu.P2_cow == 3) {
				this.vehicle2.loadImage(getClass().getResource("cowpink_left.png").toExternalForm());
			}
			this.vehicle2.setDirection(3);
			this.vehicle2.setDX(-3);
		}

		if(k==KeyCode.S) {
			if (Menu.P2_cow == 1) {
				this.vehicle2.loadImage(getClass().getResource("cow_down.png").toExternalForm());
			}
			else if (Menu.P2_cow == 2) {
				this.vehicle2.loadImage(getClass().getResource("cowbnw_down.png").toExternalForm());
			}
			else if (Menu.P2_cow == 3) {
				this.vehicle2.loadImage(getClass().getResource("cowpink_down.png").toExternalForm());
			}
			this.vehicle2.setDirection(2);
			this.vehicle2.setDY(3);
		}
		
		if(k==KeyCode.D) {
			if (Menu.P2_cow == 1) {
				this.vehicle2.loadImage(getClass().getResource("cow_right.png").toExternalForm());
			}
			else if (Menu.P2_cow == 2) {
				this.vehicle2.loadImage(getClass().getResource("cowbnw_right.png").toExternalForm());
			}
			else if ((Menu.P2_cow == 3)){
				this.vehicle2.loadImage(getClass().getResource("cowpink_right.png").toExternalForm());
			}
			this.vehicle2.setDirection(1);
			this.vehicle2.setDX(3);
		}	
		
//		System.out.println(k +" key pressed.");
   	}

	private void stopVehicle(KeyCode k){
		this.vehicle1.setDX(0);
		this.vehicle1.setDY(0);
		this.vehicle2.setDX(0);
		this.vehicle2.setDY(0);
	}
	

	private void endGame() {
	//	System.out.println("pumasok hehP");
		gameOver = true; 
		menu.opengameOverScene(stage,score,happinessLevel);
		// display the scores (calls the scoring function)
	}
	
	// gets random number on whether to trigger a natural disaster or not
	private int triggerNaturalDisaster() {		
		Random rand = new Random();
		
		naturalDisasterTrig = rand.nextInt(2);
		System.out.println(naturalDisasterTrig);
		return naturalDisasterTrig;
	}
	
	private void limitVehicleMovement(Vehicle vehicle) {
		// limit vehicle movement
		
		// borders
		if (vehicle.x <= 45) {
			vehicle.x = vehicle.x + vehicleBumpFixer;
		}
		else if (vehicle.x >= GameStage.WINDOW_WIDTH - 110) {
			vehicle.x = vehicle.x - vehicleBumpFixer;
		}
		else if (vehicle.y <= 35) {
			vehicle.y = vehicle.y + vehicleBumpFixer;
		}
		else if (vehicle.y >= GameStage.WINDOW_HEIGHT - 110) {
			vehicle.y = vehicle.y - vehicleBumpFixer;
		}
		
		// left and right borders
		else if (vehicle.y >= 43 && vehicle.y <= 126 ||
				vehicle.y >= 184 && vehicle.y <= 217 ||
				vehicle.y >= 268 && vehicle.y <= 295 ||
				vehicle.y >= 341 && vehicle.y <= 373 ||
				vehicle.y >= 427 && vehicle.y <= 451 ||
				vehicle.y >= 504 && vehicle.y <= 535 ||
				vehicle.y >= 584 && vehicle.y <= 616) {
			
			if (vehicle.x >= 56 && vehicle.x <= 60) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 422 && vehicle.x >= 420) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else if (vehicle.x <= 440 && vehicle.x >= 438) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 488 && vehicle.x >= 486) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else if (vehicle.x <= 506 && vehicle.x >= 504) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 874 && vehicle.x >= 872) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else {
			//	System.out.println(vehicle.x);
			//	System.out.println(vehicle.y);
				vehicle.move();
			}
			
		}
		// left and right borders (house spaces)
		else if (vehicle.y >= 217 && vehicle.y <= 268 ||
				vehicle.y >= 373 && vehicle.y <= 427 ||
				vehicle.y >= 535 && vehicle.y <= 584) {
			if (vehicle.x >= 56 && vehicle.x <= 60) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 366 && vehicle.x >= 362) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else if (vehicle.x <= 440 && vehicle.x >= 436) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 488 && vehicle.x >= 484) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else if (vehicle.x <= 557 && vehicle.x >= 552) {
				vehicle.x = vehicle.x - vehicleBumpFixer;
			}
			else if (vehicle.x <= 874 && vehicle.x >= 870) {
				vehicle.x = vehicle.x + vehicleBumpFixer;
			}
			else {
			//	System.out.println(vehicle.x);
			//	System.out.println(vehicle.y);
				vehicle.move();
			}
		}
		// bottom borders
		else if (vehicle.y <= 633 && vehicle.y >= 623 ||
				vehicle.y <= 475 && vehicle.y >= 465 ||
				vehicle.y <= 312 && vehicle.y >= 302 ||
				vehicle.y <= 150 && vehicle.y >= 140) {
			if (vehicle.x >= 56 && vehicle.x <= 422 ||
					vehicle.x >= 513 && vehicle.x <= 874) {
				vehicle.y = vehicle.y + vehicleBumpFixer;
			}
			else {
			//	System.out.println(vehicle.x);
			//	System.out.println(vehicle.y);
				vehicle.move();
			}
		}
		// top borders (house spaces)
		else if (vehicle.y >= 39 && vehicle.y <= 49 ||
				vehicle.y >= 166 && vehicle.y <= 170 ||
				vehicle.y >= 328 && vehicle.y <= 332 ||
				vehicle.y >= 488 && vehicle.y <= 492) {
			if (vehicle.x >= 56 && vehicle.x <= 422 ||
					vehicle.x >= 513 && vehicle.x <= 874) {
				vehicle.y = vehicle.y - vehicleBumpFixer;
			}
			else {
			//	System.out.println(vehicle.x);
			//	System.out.println(vehicle.y);
				vehicle.move();
			}
		}
		else {
		//	System.out.println(vehicle.x);
		//	System.out.println(vehicle.y);
			vehicle.move();
		}
	}

	@Override
	public void handle(long now) {
		// Check if the game is over
		if (gameOver) {
			return; // Exit the method if the game is over
		}

		// Clear the graphics context for the new frame
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);

		// Generate house orders and draw house bounds
		generateHouseOrders(now);
		checkOverdueOrders(now);
		//drawhouseBounds(gc);


		Kart1keyHolder = loadGoods(kart1, storeBounds, storeKeys, Kart1keyHolder);
		Kart2keyHolder = loadGoods(kart2, storeBounds, storeKeys, Kart2keyHolder);

		unloadingMechanism(kart1, Kart1keyHolder);
		unloadingMechanism(kart2, Kart2keyHolder);

		limitVehicleMovement(vehicle1);
		limitVehicleMovement(vehicle2);
		
		// Render vehicles
 		this.vehicle1.render(this.gc);
		this.vehicle2.render(this.gc);
		
		// Draw orders for each house
		for (String house : houseLocations.keySet()) {
			Point location = houseLocations.get(house);
			String order = houseOrders.get(house);

			if (order != null) {
				Image orderImage = orderSprites.get(order);
				if (orderImage != null) {
					gc.drawImage(orderImage, location.x, location.y, 58, 67);
				}
			}
		}
		
		
		long elapsedTime = now - startTime;  
		long secondsElapsed = elapsedTime / 1_000_000_000L;  // Convert nanoseconds to seconds
		long remainingTime = GAME_DURATION / 1_000_000_000L - secondsElapsed;

	
		// natural disasters roll every minute; 50/50 chance of occurring
		if (remainingTime % 60 == 1) {
			naturalDisasterRoll = true;
		}

		if (remainingTime % 60 == 0 && remainingTime / 60 != 5 && (naturalDisasterRoll == true)) {
			naturalDisasterTrig = triggerNaturalDisaster();
			naturalDisasterRoll = false;
		}

		if (naturalDisasterTrig == 1 && (remainingTime % 60 >= 50 && remainingTime % 60 <= 59)) {
		    if ((remainingTime / 10) % 2 == 1) {
				this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		        gc.drawImage(blackout, 0, 0);
		        gc.drawImage(blackout_alert, 691.3, 8.3, 300, 52);
		 		this.vehicle1.render(this.gc);
				this.vehicle2.render(this.gc);

		         
		    }	
		}
		

		gc.drawImage(gameOverlay, 3, 3, 1000, 750);


		gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		gc.setFill(Color.web("#8f625c"));
		gc.fillText(Integer.toString(happinessLevel), 89, 47);

		gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		gc.setFill(Color.web("#8f625c"));
		gc.fillText(Integer.toString(score), 50, 96);
		
		if (remainingTime >= 0) {
			long remainingMinutes = remainingTime / 60;
			long remainingSeconds = remainingTime % 60;
			String formattedRemainingTime = String.format("%02d:%02d", remainingMinutes, remainingSeconds);
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			gc.setFill(Color.web("#8f625c"));
			gc.fillText(formattedRemainingTime, 515, 711);  
		} else {
			System.out.println("TIMES UP");
			endGame(); 
		}


		if (miniGameSuccess) {
			miniGameSuccess = false; // Reset for the next mini-game
		}
		
		if (happinessLevel <=0) {
			endGame(); 
		}
	}

}






