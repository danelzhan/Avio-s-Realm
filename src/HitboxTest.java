import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HitboxTest {
	
	Hitbox player = new Hitbox(0, 0, 50, 50);
	
	@Test
	void testTryUp() {
		player.setX(0);
		player.setY(60);
		assertFalse(player.tryMoveUp(0, 0, 60, 0));
		
		player.setX(0);
		player.setY(120);
		assertTrue(player.tryMoveUp(0, 0, 60, 0));
	}
	
	@Test
	void testTryDown() {
		player.setX(0);
		player.setY(-40);
		assertFalse(player.tryMoveDown(0, 0, 60, 0));
		
		player.setX(0);
		player.setY(-110);
		assertTrue(player.tryMoveDown(0, 0, 60, 0));
	}
	
	@Test
	void testTryRight() {
		player.setX(-40);
		player.setY(0);
		assertFalse(player.tryMoveRight(0, 0, 60, 0));
		
		player.setX(-100);
		player.setY(0);
		assertTrue(player.tryMoveRight(0, 0, 60, 0));
	}
	
	@Test
	void testTryLeft() {
		player.setX(50);
		player.setY(0);
		assertFalse(player.tryMoveLeft(0, 0, 60, 0));
		
		player.setX(120);
		player.setY(0);
		assertTrue(player.tryMoveLeft(0, 0, 60, 0));
	}
	
}
