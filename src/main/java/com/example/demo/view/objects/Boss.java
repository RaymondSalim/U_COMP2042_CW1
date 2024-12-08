package com.example.demo.view.objects;

import com.example.demo.view.entities.ActiveActorDestructible;
import com.example.demo.view.entities.FighterPlane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the boss enemy plane in the game.
 * <p>
 * The boss has unique abilities, including a shield and a complex movement pattern.
 * </p>
 */
public class Boss extends FighterPlane {
    private static final String IMAGE_NAME = "bossplane.png";
    private static final String SHIELD_IMAGE_PATH = "/com/example/demo/images/shield.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double BOSS_FIRE_RATE = .04;
    private static final double BOSS_SHIELD_PROBABILITY = .002;
    private static final int IMAGE_HEIGHT = 300;
    private static final int VERTICAL_VELOCITY = 80;
    private static final int MAX_HEALTH = 100;
    private static final int HEALTH = MAX_HEALTH;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int Y_POSITION_UPPER_BOUND = -100;
    private static final int Y_POSITION_LOWER_BOUND = 475;
    private static final int MAX_FRAMES_WITH_SHIELD = 500;

    private final List<Integer> movePattern;
    private final ImageView shieldImage;
    private boolean isShielded;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;
    private int framesWithShieldActivated;

    /**
     * Constructs a {@code Boss} instance with its visuals, shield, and movement patterns.
     */
    public Boss() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);

        // Initialize shield image
        shieldImage = new ImageView(new Image(getClass().getResource(SHIELD_IMAGE_PATH).toExternalForm()));
        shieldImage.setFitWidth(IMAGE_HEIGHT + 50); // Slightly larger than the boss
        shieldImage.setFitHeight(IMAGE_HEIGHT + 50);
        shieldImage.setPreserveRatio(true);
        shieldImage.setVisible(false); // Initially hidden

        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        framesWithShieldActivated = 0;
        isShielded = false;
        initializeMovePattern();
    }

    @Override
    public void updatePosition(double deltaTime) {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove() * deltaTime);
        double currentPosition = getLayoutY() + getTranslateY();
        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY);
        }

        // Update shield position
        updateShieldPosition();
    }

    @Override
    public void updateActor(double deltaTime) {
        updatePosition(deltaTime);
        updateShield();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
    }

    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
        }
    }

    /**
     * Initializes the movement pattern for the boss.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(ZERO);
        }
        Collections.shuffle(movePattern);
    }

    /**
     * Updates the shield's state and visual display.
     */
    private void updateShield() {
        if (isShielded) framesWithShieldActivated++;
        else if (shieldShouldBeActivated()) activateShield();
        if (shieldExhausted()) deactivateShield();
    }

    /**
     * Calculates the next move direction for the boss based on its movement pattern.
     *
     * @return the next move direction as an integer value.
     */
    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
    }

    /**
     * Gets the maximum health of the boss.
     *
     * @return the maximum health value.
     */
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    /**
     * Determines whether the boss should fire a projectile in the current frame.
     *
     * @return {@code true} if the boss should fire; {@code false} otherwise.
     */
    private boolean bossFiresInCurrentFrame() {
        return Math.random() < BOSS_FIRE_RATE;
    }

    /**
     * Calculates the initial y-coordinate for the boss's projectiles.
     *
     * @return the y-coordinate where the projectile should spawn.
     */
    private double getProjectileInitialPosition() {
        return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
    }

    /**
     * Determines whether the boss's shield should be activated based on a probability.
     *
     * @return {@code true} if the shield should be activated; {@code false} otherwise.
     */
    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    /**
     * Checks if the shield has been active for its maximum allowed duration.
     *
     * @return {@code true} if the shield duration is exhausted; {@code false} otherwise.
     */
    private boolean shieldExhausted() {
        return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
    }

    /**
     * Activates the boss's shield.
     */
    private void activateShield() {
        isShielded = true;
        shieldImage.setVisible(true);
    }

    /**
     * Deactivates the boss's shield.
     */
    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
        shieldImage.setVisible(false);
        framesWithShieldActivated = 0;
    }

    /**
     * Updates the shield's position to match the boss.
     */
    private void updateShieldPosition() {
        shieldImage.setLayoutX(getLayoutX() + getTranslateX() - 50);
        shieldImage.setLayoutY(getLayoutY() + getTranslateY() - 25);
    }

    /**
     * Gets the shield's visual representation.
     *
     * @return the {@link ImageView} of the shield.
     */
    public ImageView getShieldImage() {
        return shieldImage;
    }
}
